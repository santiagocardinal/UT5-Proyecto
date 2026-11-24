package ucu.edu.aed.tda.clasificacion.ucu.edu.aed.tda.clasificacion;

import junit.framework.TestCase;
import ucu.edu.aed.tda.clasificacion.TClasificadorImpl;

import java.util.Arrays;

public class HeapSortTest extends TestCase {

    private TClasificadorImpl clasificador;

    @Override
    protected void setUp() {
        clasificador = new TClasificadorImpl();
    }

    public void testHeapSortCompleto() {
        int[] datos = {5, 8, 2, 15, 4, 12, 3};
        int[] esperado = {2, 3, 4, 5, 8, 12, 15};

        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testHeapSortValoresRepetidos() {
        int[] datos = {0, 5, 5, 0, 10, 10, 1};
        int[] esperado = {0, 0, 1, 5, 5, 10, 10};

        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testHeapSortArregloVacio() {
        int[] datos = {};

        clasificador.heapsort(datos);

        assertEquals(0, datos.length);
    }

    public void testHeapSortUnElemento() {
        int[] datos = {8};
        int[] esperado = {8};

        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testHeapSortTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};
        int[] esperado = {5, 5, 5, 5, 5};

        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testHeapSortYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testHeapSortOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testHeapSortValoresNegativos() {
        int[] datos = {-5, 8, -2, 0, 4};
        int[] esperado = {-5, -2, 0, 4, 8};

        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testHeapSortContraArraysSort() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int[] esperado = datos.clone();

        Arrays.sort(esperado);
        clasificador.heapsort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    public void testTopK() {
        int[] datos = {5, 8, 2, 15, 4, 12, 3};
        int[] esperado = {15, 12, 8};

        int[] resultado = clasificador.topKHeapSort(datos, 3);

        assertTrue(Arrays.equals(esperado, resultado));
    }

    public void testTopKValoresRepetidos() {
        int[] datos = {0, 5, 5, 0, 10, 10, 1};
        int[] esperado = {10, 10, 5, 5};

        int[] resultado = clasificador.topKHeapSort(datos, 4);

        assertTrue(Arrays.equals(esperado, resultado));
    }

    public void testTopKUno() {
        int[] datos = {3, 7, 2, 9, 1};
        int[] esperado = {9};

        int[] resultado = clasificador.topKHeapSort(datos, 1);

        assertTrue(Arrays.equals(esperado, resultado));
    }

    public void testTopKCero() {
        int[] datos = {3, 7, 2, 9, 1};

        int[] resultado = clasificador.topKHeapSort(datos, 0);

        assertEquals(0, resultado.length);
    }

    public void testTopKIgualAN() {
        int[] datos = {3, 7, 2, 9, 1};
        int[] esperado = {9, 7, 3, 2, 1};

        int[] resultado = clasificador.topKHeapSort(datos, 5);

        assertTrue(Arrays.equals(esperado, resultado));
    }

    public void testTopKTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};
        int[] esperado = {5, 5, 5};

        int[] resultado = clasificador.topKHeapSort(datos, 3);

        assertTrue(Arrays.equals(esperado, resultado));
    }

    public void testTopKArregloVacio() {
        int[] datos = {};

        int[] resultado = clasificador.topKHeapSort(datos, 0);

        assertEquals(0, resultado.length);
    }

    public void testTopKNegativo() {
        int[] datos = {1, 2, 3};

        try {
            clasificador.topKHeapSort(datos, -1);
            fail("Debia lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Valor de k invalido", e.getMessage());
        }
    }

    public void testTopKMayorAN() {
        int[] datos = {1, 2, 3};

        try {
            clasificador.topKHeapSort(datos, 4);
            fail("Debia lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Valor de k invalido", e.getMessage());
        }
    }

    public void testTopKContraArraysSort() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int k = 5;

        int[] ordenado = datos.clone();
        Arrays.sort(ordenado);

        int[] esperado = new int[k];

        for (int i = 0; i < k; i++) {
            esperado[i] = ordenado[ordenado.length - 1 - i];
        }

        int[] resultado = clasificador.topKHeapSort(datos, k);

        assertTrue(Arrays.equals(esperado, resultado));
    }

    public void testTopKContraHeapSortCompleto() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int[] ordenado = datos.clone();
        int k = 5;

        clasificador.heapsort(ordenado);

        int[] esperado = new int[k];

        for (int i = 0; i < k; i++) {
            esperado[i] = ordenado[ordenado.length - 1 - i];
        }

        int[] resultado = clasificador.topKHeapSort(datos, k);

        assertTrue(Arrays.equals(esperado, resultado));
    }
}