package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Arrays;
import java.util.Random;

/**
 * Consigna 1 · Consolidación de transacciones bancarias.
 *
 * Las transacciones ocurren a lo largo de un día. Cada mensaje viaja por la
 * red con una demora aleatoria entre 0 y {@code retrasoMaxMs} milisegundos, y
 * la casa central las guarda en el orden en que llegan. Cuanto mayor es la
 * demora máxima, más desordenado queda el arreglo respecto del orden
 * cronológico.
 *
 * El timestamp registrado tiene resolución de segundos: hay muchos empates
 * (con 10.000.000 de transacciones, unas 116 por segundo), lo que permite
 * observar la estabilidad.
 */
public final class GeneradorTransacciones {

    public static final int SEGUNDOS_POR_DIA = 86_400;
    public static final int MS_POR_DIA = SEGUNDOS_POR_DIA * 1000;

    private GeneradorTransacciones() {
    }

    public static Transaccion[] generarTransacciones(int n, int sucursales,
                                                     int retrasoMaxMs, long semilla) {
        if (retrasoMaxMs < 0) {
            throw new IllegalArgumentException("retrasoMaxMs debe ser >= 0");
        }
        Random rnd = new Random(semilla);
        int[] ocurrenciaMs = new int[n];
        for (int i = 0; i < n; i++) {
            ocurrenciaMs[i] = rnd.nextInt(MS_POR_DIA);
        }
        Arrays.sort(ocurrenciaMs);

        // llegada = ocurrencia + demora; se ordena por llegada (desempate por índice)
        long[] llegada = new long[n];
        for (int i = 0; i < n; i++) {
            long t = ocurrenciaMs[i] + (long) rnd.nextInt(retrasoMaxMs + 1);
            llegada[i] = (t << 32) | i;
        }
        Arrays.sort(llegada);

        Transaccion[] resultado = new Transaccion[n];
        for (int pos = 0; pos < n; pos++) {
            int original = (int) (llegada[pos] & 0xFFFFFFFFL);
            int sucursal = rnd.nextInt(sucursales);
            long monto = (rnd.nextInt(2_000_000) - 500_000L);
            resultado[pos] = new Transaccion(pos, sucursal, ocurrenciaMs[original] / 1000, monto);
        }
        return resultado;
    }

    /** Solo las claves (timestamps), en orden de llegada, para usar con TClasificador. */
    public static int[] generarTimestamps(int n, int retrasoMaxMs, long semilla) {
        return timestamps(generarTransacciones(n, 500, retrasoMaxMs, semilla));
    }

    public static int[] timestamps(Transaccion[] transacciones) {
        int[] t = new int[transacciones.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = transacciones[i].timestamp();
        }
        return t;
    }

    /**
     * Máximo desplazamiento: cuántas posiciones, como mucho, está un elemento
     * lejos de su lugar en el arreglo ordenado (considerando ordenamiento estable).
     */
    public static int desplazamientoMaximo(int[] datos) {
        int n = datos.length;
        long[] conPos = new long[n];
        for (int i = 0; i < n; i++) {
            conPos[i] = ((long) datos[i] << 32) | i;
        }
        Arrays.sort(conPos);
        int max = 0;
        for (int destino = 0; destino < n; destino++) {
            int origen = (int) (conPos[destino] & 0xFFFFFFFFL);
            max = Math.max(max, Math.abs(destino - origen));
        }
        return max;
    }
}
