package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Random;

/**
 * Consigna 3 · Detección de cobros duplicados.
 *
 * Cada cobro se resume en un hash de 32 bits de (tarjeta, monto, minuto).
 * Se generan claves distintas y luego se "plantan" copias de algunas de
 * ellas. El lote informa la respuesta esperada para que el equipo valide su
 * solución.
 */
public final class GeneradorPagos {

    private GeneradorPagos() {
    }

    /**
     * @param n                    cantidad total de cobros
     * @param proporcionDuplicados fracción de cobros que son copias (0.005 = 0,5 %)
     */
    public static LotePagos generarLote(int n, double proporcionDuplicados, long semilla) {
        if (proporcionDuplicados < 0 || proporcionDuplicados >= 1) {
            throw new IllegalArgumentException("proporcionDuplicados debe estar en [0, 1)");
        }
        Random rnd = new Random(semilla);
        int copias = (int) Math.round(n * proporcionDuplicados);
        int unicos = n - copias;
        int sal = rnd.nextInt();

        int[] claves = new int[n];
        for (int i = 0; i < unicos; i++) {
            claves[i] = mezclar(i ^ sal); // biyectiva: garantiza claves distintas
        }
        for (int i = unicos; i < n; i++) {
            claves[i] = claves[rnd.nextInt(unicos)];
        }
        // mezcla de Fisher-Yates para que las copias queden dispersas
        GeneradorPadrones.mezclar(claves, 0, n, rnd);
        return new LotePagos(claves, copias);
    }

    /** Finalizador de MurmurHash3: una biyección sobre los enteros de 32 bits. */
    private static int mezclar(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }
}
