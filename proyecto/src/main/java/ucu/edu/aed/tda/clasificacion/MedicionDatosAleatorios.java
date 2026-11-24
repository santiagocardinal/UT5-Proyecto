package ucu.edu.aed.tda.clasificacion;

import ucu.edu.aed.tda.clasificacion.datos.GeneradorPagos;
import ucu.edu.aed.tda.clasificacion.datos.Medidor;
import java.util.Arrays;

public class MedicionDatosAleatorios {

    static final int TAMANIO_CONSIGNA = 50_000_000;
    static final long SEMILLA = 2026;
    static final int[] VALORES_K = {10, 100, 1_000, 10_000, 100_000};
    static final int REPETICIONES = 3;
    static final int CALENTAMIENTOS = 1;

    public static void main(String[] args) {

        int[] datos = GeneradorPagos.generarLote(
                TAMANIO_CONSIGNA, 0, SEMILLA
        ).claves();

        System.out.println("===== PARTE D12 =====");
        System.out.println("Cantidad de elementos: " + datos.length);
        System.out.println("Distribucion: claves distintas al azar");
        System.out.println("Semilla: " + SEMILLA);

        medirOrdenamientos(datos);
        verificarTopKHeapSort(datos);
        medirTopKHeapSort(datos);
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

    static void medirTopKHeapSort(int[] datos) {

        TClasificadorImpl clasificador = new TClasificadorImpl();

        System.out.println("\n===== TOP-K HEAPSORT PARCIAL =====");

        for (int k : VALORES_K) {

            long[] tiempos = Medidor.medirNanos(
                    copia -> clasificador.topKHeapSort(copia, k),
                    datos,
                    REPETICIONES,
                    CALENTAMIENTOS,
                    false
            );

            double medianaMs = Medidor.mediana(tiempos) / 1_000_000.0;

            System.out.println("k = " + k + " -> " + medianaMs + " ms");
        }
    }

    static void verificarTopKHeapSort(int[] datos) {

        TClasificadorImpl clasificador = new TClasificadorImpl();

        System.out.println("\n===== VERIFICACION TOP-K HEAPSORT =====");

        int[] ordenado = datos.clone();
        Arrays.sort(ordenado);

        for (int k : VALORES_K) {

            int[] esperado = new int[k];

            for (int i = 0; i < k; i++) {
                esperado[i] = ordenado[ordenado.length - 1 - i];
            }

            int[] resultado = clasificador.topKHeapSort(datos.clone(), k);

            if (!Arrays.equals(esperado, resultado)) {
                throw new IllegalStateException(
                        "Resultado incorrecto para k = " + k
                );
            }

            System.out.println("Verificacion k = " + k + ": OK");
        }
    }
}