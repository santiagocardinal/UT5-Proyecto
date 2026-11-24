package ucu.edu.aed.tda.clasificacion;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorPagos;
import java.util.Arrays;

public class MedicionDatosAleatorios {

    static final int TAMANIO_CONSIGNA = 50_000_000;
    static final long SEMILLA = 2026;
    static final int[] VALORES_K = Main.VALORES_K;

    public static void main(String[] args) {

        int[] datos = GeneradorPagos.generarLote(
                TAMANIO_CONSIGNA, 0, SEMILLA
        ).claves();

        System.out.println("===== PARTE D12 =====");
        System.out.println("Cantidad de elementos: " + datos.length);
        System.out.println("Distribucion: claves distintas al azar");
        System.out.println("Semilla: " + SEMILLA);

        medirOrdenamientos(datos);
        verificarTopK(datos);
        Main.medirTopK(datos);
    }

    static void medirOrdenamientos(int[] datos) {

        System.out.println("\n===== ORDENAMIENTOS COMPLETOS =====");

        Main.medirAlgoritmo(
                "Insercion directa",
                Main.clasificador::insercionDirecta,
                true, datos
        );

        Main.medirAlgoritmo(
                "Shellsort",
                Main::ordenarConShell,
                false, datos
        );

        Main.medirAlgoritmo(
                "Burbuja",
                Main.clasificador::burbuja,
                true, datos
        );

        Main.medirAlgoritmo(
                "Quicksort",
                Main.clasificador::quicksort,
                false, datos
        );

        Main.medirAlgoritmo(
                "Seleccion directa",
                Main.clasificador::clasificacionDirecta,
                true, datos
        );

        Main.medirAlgoritmo(
                "Heapsort",
                Main.clasificador::heapsort,
                false, datos
        );
    }

    static void verificarTopK(int[] datos) {

        TClasificadorImpl clasificador = new TClasificadorImpl();

        System.out.println("\n===== VERIFICACION TOP-K =====");

        int[] ordenado = datos.clone();
        Arrays.sort(ordenado);

        for (int k : VALORES_K) {

            int[] esperado = new int[k];

            for (int i = 0; i < k; i++) {
                esperado[i] = ordenado[ordenado.length - 1 - i];
            }

            int[] resultadoHeapSort = clasificador.topKHeapSort(
                    datos.clone(), k
            );

            int[] resultadoHeapMinimo = clasificador.topKHeapMinimo(
                    datos.clone(), k
            );

            if (!Arrays.equals(esperado, resultadoHeapSort)) {
                throw new IllegalStateException(
                        "Heapsort parcial incorrecto para k = " + k
                );
            }

            if (!Arrays.equals(esperado, resultadoHeapMinimo)) {
                throw new IllegalStateException(
                        "Heap minimo incorrecto para k = " + k
                );
            }

            System.out.println(
                    "k = " + k + ": Heapsort parcial OK, Heap minimo OK"
            );
        }
    }
}