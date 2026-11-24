package ucu.edu.aed.tda.clasificacion;

import junit.framework.TestCase;
import ucu.edu.aed.tda.clasificacion.datos.GeneradorPagos;
import java.util.Arrays;

public class MedicionDatosAleatoriosTest extends TestCase {

    private TClasificadorImpl clasificador;

    @Override
    protected void setUp() {
        clasificador = new TClasificadorImpl();
    }

    public void testCantidadDatosGenerados() {
        int[] datos = GeneradorPagos.generarLote(
                10_000, 0, 2026
        ).claves();

        assertEquals(10_000, datos.length);
    }

    public void testClavesDistintas() {
        int[] datos = GeneradorPagos.generarLote(
                10_000, 0, 2026
        ).claves();

        Arrays.sort(datos);

        for (int i = 1; i < datos.length; i++) {
            assertTrue(datos[i] != datos[i - 1]);
        }
    }

    public void testSemillaReproducible() {
        int[] datos1 = GeneradorPagos.generarLote(
                10_000, 0, 2026
        ).claves();

        int[] datos2 = GeneradorPagos.generarLote(
                10_000, 0, 2026
        ).claves();

        assertTrue(Arrays.equals(datos1, datos2));
    }

    public void testHeapSortDatosAleatorios() {
        int[] datos = GeneradorPagos.generarLote(
                10_000, 0, 2026
        ).claves();

        int[] esperado = datos.clone();

        Arrays.sort(esperado);
        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testTopKDatosAleatorios() {
        int[] datos = GeneradorPagos.generarLote(
                200_000, 0, 2026
        ).claves();

        int[] ordenado = datos.clone();
        Arrays.sort(ordenado);

        for (int k : MedicionDatosAleatorios.VALORES_K) {

            int[] esperado = new int[k];

            for (int i = 0; i < k; i++) {
                esperado[i] = ordenado[ordenado.length - 1 - i];
            }

            int[] resultado = clasificador.topKHeapSort(
                    datos.clone(), k
            );

            assertTrue(
                    "Resultado incorrecto para k = " + k,
                    Arrays.equals(esperado, resultado)
            );
        }
    }
    public void testTopKHeapMinimoDatosAleatorios() {

        int[] datos = GeneradorPagos.generarLote(
                200_000, 0, 2026
        ).claves();

        int[] ordenado = datos.clone();
        Arrays.sort(ordenado);

        for (int k : MedicionDatosAleatorios.VALORES_K) {

            int[] esperado = new int[k];

            for (int i = 0; i < k; i++) {
                esperado[i] = ordenado[ordenado.length - 1 - i];
            }

            int[] resultado = clasificador.topKHeapMinimo(
                    datos.clone(), k
            );

            assertTrue(
                    "Resultado incorrecto para k = " + k,
                    Arrays.equals(esperado, resultado)
            );
        }
    }
}