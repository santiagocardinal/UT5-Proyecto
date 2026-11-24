package ucu.edu.aed.tda.clasificacion;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorVentas;
import ucu.edu.aed.tda.clasificacion.datos.Medidor;
import ucu.edu.aed.tda.clasificacion.datos.GeneradorPagos;

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

        // 2) PARTE A: explorar los datos (ceros, máximo, distintos, histograma)
        //explorar(ventas);

        
        // 3) PARTE A3: ¿influyen los repetidos?
        //    Contamos las operaciones con 10.000 ventas (muchos repetidos)
        //    y con 10.000 claves al azar (sin repetidos) para comparar.
        //int[] conRepetidos = Arrays.copyOf(ventas, 10_000);
        //int[] sinRepetidos = GeneradorPagos.generarLote(10_000, 0, 2026).claves();
        //contarOperaciones(conRepetidos, "CON repetidos (ventas)");
        //contarOperaciones(sinRepetidos, "SIN repetidos (al azar)");


        // 4) Prueba rápida: los seis algoritmos con 10.000 productos
        // medirTodos(ventas, 10_000);

        // 5) Todos contra todos: los seis algoritmos con tamaños que se duplican.
        //    El último parámetro dice si el algoritmo es cuadrático (O(n²)) o no (O(n log n)),
        //    lo usamos para estimar.
        /*medirAlgoritmo("Inserción directa", clasificador::insercionDirecta, true, ventas);
        medirAlgoritmo("Shellsort", Main::ordenarConShell, false, ventas);
        medirAlgoritmo("Burbuja", clasificador::burbuja, true, ventas);
        medirAlgoritmo("Quicksort", clasificador::quicksort, false, ventas);
        medirAlgoritmo("Selección directa", clasificador::clasificacionDirecta, true, ventas);
        medirAlgoritmo("Heapsort", clasificador::heapsort, false, ventas);
        */
       // 6) PARTE C8: los k mayores sin ordenar todo
        verificarTopK(ventas);
        medirTopK(ventas);
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

    // Explora los datos: ceros, máximo, valores distintos e histograma
    static void explorar(int[] ventas) {
        System.out.println("===== PARTE A: EXPLORACIÓN =====");
        System.out.println("Cantidad de productos: " + ventas.length);

        int ceros = contarCeros(ventas);
        System.out.println("Productos que vendieron 0: " + ceros);
        System.out.println("Porcentaje de ceros: " + (ceros * 100.0 / ventas.length) + " %");

        int maximo = buscarMaximo(ventas);
        System.out.println("Máximo vendido: " + maximo);

        int distintos = contarDistintos(ventas, maximo);
        System.out.println("Valores distintos: " + distintos);

        mostrarHistograma(ventas);
    
    }


    // Cuenta cuántos productos no vendieron nada
    static int contarCeros(int[] ventas) {
        int ceros = 0;
        for (int i = 0; i < ventas.length; i++) {
            if (ventas[i] == 0) {
                ceros++;
            }
        }
        return ceros;
    }

    // Busca el producto que más vendió
    static int buscarMaximo(int[] ventas) {
        int maximo = ventas[0];
        for (int i = 1; i < ventas.length; i++) {
            if (ventas[i] > maximo) {
                maximo = ventas[i];
            }
        }
        return maximo;
    }

    // Cuenta cuántos valores distintos hay.
    // Usamos un arreglo de boolean donde la posición v dice si ya vimos el valor v.
    // Se puede porque el máximo no es tan grande (1.000.000).
    static int contarDistintos(int[] ventas, int maximo) {
        boolean[] yaVisto = new boolean[maximo + 1];
        int distintos = 0;
        for (int i = 0; i < ventas.length; i++) {
            int v = ventas[i];
            if (!yaVisto[v]) {
                yaVisto[v] = true;
                distintos++;
            }
        }
        return distintos;
    }
    // Histograma por rangos. Usamos rangos que crecen de a 10 porque casi todos
    // venden 0 o 1 y muy pocos venden mucho (si los rangos fueran iguales no se ve nada).
    static void mostrarHistograma(int[] ventas) {
        int vendieron0 = 0;
        int vendieron1 = 0;
        int de2a9 = 0;
        int de10a99 = 0;
        int de100a999 = 0;
        int de1000a9999 = 0;
        int de10000omas = 0;

        for (int i = 0; i < ventas.length; i++) {
            int v = ventas[i];
            if (v == 0) {
                vendieron0++;
            } else if (v == 1) {
                vendieron1++;
            } else if (v <= 9) {
                de2a9++;
            } else if (v <= 99) {
                de10a99++;
            } else if (v <= 999) {
                de100a999++;
            } else if (v <= 9999) {
                de1000a9999++;
            } else {
                de10000omas++;
            }
        }

        System.out.println("Histograma:");
        imprimirFila("0", vendieron0, ventas.length);
        imprimirFila("1", vendieron1, ventas.length);
        imprimirFila("2 a 9", de2a9, ventas.length);
        imprimirFila("10 a 99", de10a99, ventas.length);
        imprimirFila("100 a 999", de100a999, ventas.length);
        imprimirFila("1000 a 9999", de1000a9999, ventas.length);
        imprimirFila("10000 o más", de10000omas, ventas.length);
    }
    
    // Ordena una copia de los datos con cada algoritmo y muestra
    // cuántas comparaciones y movimientos hizo (los contadores de nuestro clasificador)
    static void contarOperaciones(int[] datos, String titulo) {
        System.out.println("\n" + titulo + ":");
        int[] copia;

        copia = datos.clone();
        clasificador.reiniciarContadores();
        clasificador.insercionDirecta(copia);
        System.out.println("  Inserción directa: comparaciones = " + clasificador.comparaciones
                + ", movimientos = " + clasificador.movimientos);

        copia = datos.clone();
        clasificador.reiniciarContadores();
        ordenarConShell(copia);
        System.out.println("  Shellsort:         comparaciones = " + clasificador.comparaciones
                + ", movimientos = " + clasificador.movimientos);

        copia = datos.clone();
        clasificador.reiniciarContadores();
        clasificador.burbuja(copia);
        System.out.println("  Burbuja:           comparaciones = " + clasificador.comparaciones
                + ", movimientos = " + clasificador.movimientos);

        copia = datos.clone();
        clasificador.reiniciarContadores();
        clasificador.quicksort(copia);
        System.out.println("  Quicksort:         comparaciones = " + clasificador.comparaciones
                + ", movimientos = " + clasificador.movimientos);

        copia = datos.clone();
        clasificador.reiniciarContadores();
        clasificador.clasificacionDirecta(copia);
        System.out.println("  Selección directa: comparaciones = " + clasificador.comparaciones
                + ", movimientos = " + clasificador.movimientos);

        copia = datos.clone();
        clasificador.reiniciarContadores();
        clasificador.heapsort(copia);
        System.out.println("  Heapsort:          comparaciones = " + clasificador.comparaciones
                + ", movimientos = " + clasificador.movimientos);
    }

    // Imprime una fila del histograma: el rango, la cantidad, el porcentaje
    // y una barra de # (cada # es 1 % de los productos)
    static void imprimirFila(String rango, int cantidad, int total) {
        double porcentaje = cantidad * 100.0 / total;
        String barra = "#".repeat((int) Math.round(porcentaje));
        System.out.printf("  %-12s -> %10d  (%6.3f %%)  %s%n", rango, cantidad, porcentaje, barra);
    }
    
    // ===================================================================== PARTE C
    // Los valores de k que pide la consigna
    static final int[] VALORES_K = {10, 100, 1_000, 10_000, 100_000};

    // Verifica que los dos métodos den lo mismo que los últimos k del arreglo ordenado.
    static void verificarTopK(int[] ventas) {
        System.out.println("\n===== PARTE C: VERIFICACIÓN =====");

        // ordenamos una copia de todo el arreglo (con nuestro quicksort, que es el más rápido)
        int[] ordenado = ventas.clone();
        clasificador.quicksort(ordenado);

        for (int k : VALORES_K) {
            // los k mayores son los últimos k del ordenado, pero los queremos del mayor al menor
            int[] esperado = new int[k];
            for (int i = 0; i < k; i++) {
                esperado[i] = ordenado[ordenado.length - 1 - i];
            }

            int[] conHeapSort = clasificador.topKHeapSort(ventas.clone(), k);  // clone: topKHeapSort desordena
            int[] conHeapMinimo = clasificador.topKHeapMinimo(ventas, k);

            boolean okHeapSort = Arrays.equals(esperado, conHeapSort);
            boolean okHeapMinimo = Arrays.equals(esperado, conHeapMinimo);
            System.out.println("  k = " + k + ":  heapsort parcial " + (okHeapSort ? "OK" : "FALLA")
                    + ",  heap de mínimos " + (okHeapMinimo ? "OK" : "FALLA")
                    + "   (el k-ésimo mayor vendió " + esperado[k - 1] + ")");
        }
    }

     static void medirTopK(int[] ventas) {
        System.out.println("\n===== PARTE C8: TIEMPOS (mediana de 3 corridas, en ms) =====");
        for (int k : VALORES_K) {
            long[] tiempos = Medidor.medirNanos(datos -> clasificador.topKHeapSort(datos, k), ventas, 3, 1, false);
            double msHeapSort = Medidor.mediana(tiempos) / 1_000_000.0;

            tiempos = Medidor.medirNanos(datos -> clasificador.topKHeapMinimo(datos, k), ventas, 3, 1, false);
            double msHeapMinimo = Medidor.mediana(tiempos) / 1_000_000.0;

            System.out.println("  k = " + k + ":  heapsort parcial = " + msHeapSort
                    + " ms,  heap de mínimos = " + msHeapMinimo + " ms");
        }
    }
}
