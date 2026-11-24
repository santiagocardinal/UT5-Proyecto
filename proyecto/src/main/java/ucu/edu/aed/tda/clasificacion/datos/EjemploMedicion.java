package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Arrays;

/**
 * Ejemplo de uso de un generador y del Medidor.
 *
 * Se mide Arrays.sort de Java como referencia. Para medir sus algoritmos,
 * escriban su propio Main en el paquete ucu.edu.aed.tda.clasificacion y pasen
 * referencias a métodos, por ejemplo {@code clasificador::insercionDirecta}.
 */
public class EjemploMedicion {
    public static void main(String[] args) {
        long semilla = 2026;
        for (int retraso : new int[]{100, 1_000, 10_000, 600_000}) {
            int[] datos = GeneradorTransacciones.generarTimestamps(10_000_000, retraso, semilla);
            double ms = Medidor.medianaMs(Arrays::sort, datos, 5, 2);
            System.out.printf("retraso=%7d ms  desplazamiento máx=%6d  Arrays.sort=%.2f ms%n",
                    retraso, GeneradorTransacciones.desplazamientoMaximo(datos), ms);
        }
    }
}
