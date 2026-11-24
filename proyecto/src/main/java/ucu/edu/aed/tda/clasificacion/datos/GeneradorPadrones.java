package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Arrays;
import java.util.Random;

/**
 * Consigna 6 · Servicio público de ordenamiento.
 *
 * Genera listas de números de padrón (enteros a partir de 1.000.000) con
 * distintos perfiles. El perfil "armado contra tu pivote" no está acá: lo
 * construye cada equipo, porque depende de su implementación de
 * obtenerPivote.
 */
public final class GeneradorPadrones {

    private static final int BASE = 1_000_000;

    private GeneradorPadrones() {
    }

    public static int[] generar(Perfil perfil, int n, long semilla) {
        Random rnd = new Random(semilla);
        int[] d = new int[n];
        switch (perfil) {
            case ALEATORIO -> {
                for (int i = 0; i < n; i++) {
                    d[i] = BASE + i;
                }
                mezclar(d, 0, n, rnd);
            }
            case ASCENDENTE -> {
                for (int i = 0; i < n; i++) {
                    d[i] = BASE + i;
                }
            }
            case DESCENDENTE -> {
                for (int i = 0; i < n; i++) {
                    d[i] = BASE + n - 1 - i;
                }
            }
            case TUBO_DE_ORGANO -> {
                int mitad = n / 2;
                for (int i = 0; i < n; i++) {
                    d[i] = BASE + (i < mitad ? 2 * i : 2 * (n - 1 - i) + 1);
                }
            }
            case POCOS_DISTINTOS -> {
                for (int i = 0; i < n; i++) {
                    d[i] = BASE + rnd.nextInt(10);
                }
            }
            case ORDENADO_CON_COLA -> {
                int cola = Math.max(1, n / 100);
                for (int i = 0; i < n; i++) {
                    d[i] = BASE + i;
                }
                // se traen a la cola padrones elegidos al azar de todo el rango
                for (int i = n - cola; i < n; i++) {
                    int j = rnd.nextInt(i + 1);
                    int aux = d[i];
                    d[i] = d[j];
                    d[j] = aux;
                }
                // se reordena el prefijo para que solo la cola quede desordenada
                Arrays.sort(d, 0, n - cola);
            }
        }
        return d;
    }

    /** Mezcla de Fisher-Yates del rango [desde, hasta). */
    static void mezclar(int[] d, int desde, int hasta, Random rnd) {
        for (int i = hasta - 1; i > desde; i--) {
            int j = desde + rnd.nextInt(i - desde + 1);
            int aux = d[i];
            d[i] = d[j];
            d[j] = aux;
        }
    }
}
