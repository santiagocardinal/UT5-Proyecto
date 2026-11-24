package ucu.edu.aed.tda.clasificacion;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorVentas;
import ucu.edu.aed.tda.clasificacion.datos.Medidor;

import java.util.Arrays;
import java.util.function.Consumer;

/* =====================================================================
 MAIN ORIGINAL (el que vino con el proyecto). Lo dejamos comentado.
 =====================================================================
 public class Main {
     public static void main(String[] args) {
         GeneradorDatosGenericos gdg = new GeneradorDatosGenericos();
         int[] vectorAleatorio = gdg.generarDatosAleatorios();
         int[] vectorAscendente = gdg.generarDatosAscendentes();
         int[] vectorDescendente = gdg.generarDatosDescendentes();

         TClasificador clasificador = null; // TODO instanciar con implementación

         clasificador.insercionDirecta(vectorAleatorio);
         clasificador.insercionDirecta(vectorAscendente);
         clasificador.insercionDirecta(vectorDescendente);

         for (int value : vectorAleatorio) {
             System.out.print(value + " ");
         }
         System.out.println();

         for (int k : vectorAscendente) {
             System.out.print(k + " ");
         }
         System.out.println();
         for (int j : vectorDescendente) {
             System.out.print(j + " ");
         }

     }
 }*/

// =====================================================================
// Ejecutar con VM options: -Xmx4g -Xss512m
// =====================================================================
public class Main {

    static TClasificadorImpl clasificador = new TClasificadorImpl();
    static final int TAMANIO_CONSIGNA = 50_000_000;
    static final double CORTE_MS = 60_000;   // regla de corte: 60 segundos

    public static void main(String[] args) {
        // Para comprobar que tomó -Xmx4g (tiene que decir ~4000 MB). Después se puede sacar.
        System.out.println("Memoria máxima: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + " MB");

        // 1) Generamos los datos con el generador --> 50 millones de productos, semilla 2026
        int[] ventas = GeneradorVentas.generarVentas(TAMANIO_CONSIGNA, 2026);

        // 2) Prueba rápida: los seis algoritmos con 10.000 productos
        medirTodos(ventas, 10_000);

        // 3) Todos contra todos: los seis algoritmos con tamaños que se duplican.
        //    El último parámetro dice si el algoritmo es cuadrático (O(n²)) o no (O(n log n)),
        //    lo usamos para estimar.
        medirAlgoritmo("Inserción directa", clasificador::insercionDirecta, true, ventas);
        medirAlgoritmo("Shellsort", Main::ordenarConShell, false, ventas);
        medirAlgoritmo("Burbuja", clasificador::burbuja, true, ventas);
        medirAlgoritmo("Quicksort", clasificador::quicksort, false, ventas);
        medirAlgoritmo("Selección directa", clasificador::clasificacionDirecta, true, ventas);
        medirAlgoritmo("Heapsort", clasificador::heapsort, false, ventas);
    }
    

    // Mide los seis algoritmos con los primeros n productos.
    // Medidor.medianaMs(algoritmo, datos, repeticiones, calentamientos):
    static void medirTodos(int[] ventas, int n) {
        int[] datos = Arrays.copyOf(ventas, n);   // los primeros n productos
        System.out.println("Midiendo con n = " + n);

        double ms = Medidor.medianaMs(clasificador::insercionDirecta, datos, 3, 2);
        System.out.println("  Inserción directa: " + ms + " ms");

        ms = Medidor.medianaMs(Main::ordenarConShell, datos, 3, 2);
        System.out.println("  Shellsort:         " + ms + " ms");

        ms = Medidor.medianaMs(clasificador::burbuja, datos, 3, 2);
        System.out.println("  Burbuja:           " + ms + " ms");

        ms = Medidor.medianaMs(clasificador::quicksort, datos, 3, 2);
        System.out.println("  Quicksort:         " + ms + " ms");

        ms = Medidor.medianaMs(clasificador::clasificacionDirecta, datos, 3, 2);
        System.out.println("  Selección directa: " + ms + " ms");

        ms = Medidor.medianaMs(clasificador::heapsort, datos, 3, 2);
        System.out.println("  Heapsort:          " + ms + " ms");
    }

    static void medirAlgoritmo(String nombre, Consumer<int[]> algoritmo, boolean cuadratico, int[] ventas) {
        System.out.println("\n" + nombre + ":");

        int ultimoN = 0;          // último tamaño que medimos
        double ultimoMs = 0;      // lo que tardó ese tamaño

        int n = 10_000;
        while (true) {
            if (n > ventas.length) {
                n = ventas.length;          // el último paso es el tamaño completo
            }

            // ¿Cuánto tardaría este tamaño? (si ya medimos alguno antes)
            if (ultimoN > 0) {
                double factor = cuadratico ? 4 : 2.1;
                double prevision = ultimoMs * factor * ((double) n / ultimoN / 2);
                if (prevision > CORTE_MS) {
                    System.out.println("  n = " + n + "  -> no terminó (se estima que pasa los 60 s)");
                    break;
                }
            }

            int[] datos = Arrays.copyOf(ventas, n);        // los primeros n productos
            int calentamientos = (n >= 5_000_000) ? 1 : 2;  // con n grande uno alcanza
            double ms = Medidor.medianaMs(algoritmo, datos, 3, calentamientos);
            System.out.println("  n = " + n + "  -> " + ms + " ms");

            ultimoN = n;
            ultimoMs = ms;

            if (n == ventas.length) {
                return;                     // llegamos al tamaño completo, no hay que estimar
            }
            n = n * 2;
        }

        // Estimación para el tamaño completo a partir del último tamaño medido:
        //   cuadrático:  t(N) = t(n) * (N/n)²
        //   n log n:     t(N) = t(n) * (N/n) * (log N / log n)
        double veces = (double) ventas.length / ultimoN;
        double estimadoMs;
        if (cuadratico) {
            estimadoMs = ultimoMs * veces * veces;
        } else {
            estimadoMs = ultimoMs * veces * (Math.log(ventas.length) / Math.log(ultimoN));
        }
        System.out.println("  ESTIMADO para n = " + ventas.length + ": " + Math.round(estimadoMs) + " ms"
                + " (" + Math.round(estimadoMs / 3_600_000.0 * 10) / 10.0 + " horas)");
    }

    // shell necesita además los incrementos, así que lo envolvemos en un método que recibe solo el arreglo (que es lo que pide el Medidor)
    static void ordenarConShell(int[] datos) {
        clasificador.shell(datos, TipoClasificador.incrementosPorDefecto(datos.length));
    }
}