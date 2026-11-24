package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Utilitario para medir algoritmos de ordenamiento de forma comparable.
 *
 * Cada corrida trabaja sobre una COPIA del arreglo original, así todos los
 * algoritmos reciben exactamente la misma entrada. Las corridas de
 * calentamiento no se cuentan: sirven para que la JVM compile el código
 * (JIT) antes de medir.
 */
public final class Medidor {

    private Medidor() {
    }

    /**
     * Ejecuta el algoritmo varias veces sobre copias de {@code original} y
     * devuelve el tiempo de cada corrida medida, en nanosegundos.
     *
     * Ejemplo: {@code Medidor.medirNanos(c::heapsort, datos, 5, 2)}
     */
    public static long[] medirNanos(Consumer<int[]> algoritmo, int[] original,
                                    int repeticiones, int calentamientos) {
        return medirNanos(algoritmo, original, repeticiones, calentamientos, true);
    }

    /**
     * Igual que el anterior, pero permite desactivar la verificación de que el
     * arreglo quede ordenado. Úsenlo para algoritmos que NO ordenan todo el
     * arreglo (por ejemplo, obtener solo los k mayores) y verifiquen el
     * resultado por su cuenta.
     */
    public static long[] medirNanos(Consumer<int[]> algoritmo, int[] original,
                                    int repeticiones, int calentamientos, boolean verificarOrden) {
        if (repeticiones < 1 || calentamientos < 0) {
            throw new IllegalArgumentException("Se necesita al menos 1 repetición y calentamientos >= 0");
        }
        for (int r = 0; r < calentamientos; r++) {
            algoritmo.accept(original.clone());
        }
        long[] tiempos = new long[repeticiones];
        for (int r = 0; r < repeticiones; r++) {
            int[] copia = original.clone();
            long inicio = System.nanoTime();
            algoritmo.accept(copia);
            tiempos[r] = System.nanoTime() - inicio;
            if (verificarOrden && r == 0 && !mismoContenidoOrdenado(original, copia)) {
                throw new IllegalStateException("El algoritmo no ordenó correctamente los datos");
            }
        }
        return tiempos;
    }

    /** Mediana de un conjunto de tiempos (más robusta que el promedio). */
    public static long mediana(long[] tiempos) {
        if (tiempos.length == 0) {
            throw new IllegalArgumentException("No hay tiempos para calcular la mediana");
        }
        long[] t = tiempos.clone();
        Arrays.sort(t);
        int m = t.length / 2;
        return t.length % 2 == 1 ? t[m] : (t[m - 1] + t[m]) / 2;
    }

    /** Conveniencia: mediana en milisegundos con decimales. */
    public static double medianaMs(Consumer<int[]> algoritmo, int[] original,
                                   int repeticiones, int calentamientos) {
        return mediana(medirNanos(algoritmo, original, repeticiones, calentamientos)) / 1_000_000.0;
    }

    /** true si el arreglo está en orden no decreciente. */
    public static boolean estaOrdenado(int[] datos) {
        for (int i = 1; i < datos.length; i++) {
            if (datos[i - 1] > datos[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * true si {@code resultado} está ordenado y tiene exactamente los mismos
     * elementos que {@code original} (no se perdió ni se duplicó nada).
     */
    public static boolean mismoContenidoOrdenado(int[] original, int[] resultado) {
        if (original.length != resultado.length || !estaOrdenado(resultado)) {
            return false;
        }
        int[] esperado = original.clone();
        Arrays.sort(esperado);
        return Arrays.equals(esperado, resultado);
    }
}
