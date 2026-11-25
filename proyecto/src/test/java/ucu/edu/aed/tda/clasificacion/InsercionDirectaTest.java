package ucu.edu.aed.tda.clasificacion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

public class InsercionDirectaTest {

    private TClasificadorImpl clasificador;

    @Before
    public void setUp() {
        clasificador = new TClasificadorImpl();
    }

    // ===================== ORDENA BIEN

    @Test
    public void testInsercionDirecta() {
        int[] datos = {5, 8, 2, 15, 4, 12, 3};
        int[] esperado = {2, 3, 4, 5, 8, 12, 15};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresRepetidos() {
        int[] datos = {0, 5, 5, 0, 10, 10, 1};
        int[] esperado = {0, 0, 1, 5, 5, 10, 10};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testArregloVacio() {
        int[] datos = {};

        clasificador.insercionDirecta(datos);

        assertEquals(0, datos.length);
    }

    @Test
    public void testUnElemento() {
        int[] datos = {8};
        int[] esperado = {8};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testDosElementosDesordenados() {
        int[] datos = {2, 1};
        int[] esperado = {1, 2};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};
        int[] esperado = {5, 5, 5, 5, 5};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    // Este es el caso que fallaba con el código de clase (j > 0): el menor está en la primera posición
    // y nunca se comparaba con datos[0].
    @Test
    public void testElMenorVaAlPrincipio() {
        int[] datos = {3, 2, 1};
        int[] esperado = {1, 2, 3};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresNegativos() {
        int[] datos = {-5, 8, -2, 0, 4};
        int[] esperado = {-5, -2, 0, 4, 8};

        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testContraArraysSort() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int[] esperado = datos.clone();

        Arrays.sort(esperado);
        clasificador.insercionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    // Muchos arreglos al azar (con muchos repetidos, como las ventas) contra Arrays.sort
    @Test
    public void testMuchosArreglosAlAzar() {
        Random random = new Random(2026);
        for (int prueba = 0; prueba < 500; prueba++) {
            int[] datos = new int[random.nextInt(200)];
            for (int i = 0; i < datos.length; i++) {
                datos[i] = random.nextInt(10);
            }
            int[] esperado = datos.clone();
            Arrays.sort(esperado);

            clasificador.insercionDirecta(datos);

            assertTrue(Arrays.equals(esperado, datos));
        }
    }

    // ===================== CONTADORES

    // Ya ordenado: compara cada elemento con el anterior una sola vez y no mueve nada.
    // n = 5 -> 4 comparaciones, 0 movimientos.
    @Test
    public void testContadoresYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};

        clasificador.reiniciarContadores();
        clasificador.insercionDirecta(datos);

        assertEquals(4, clasificador.comparaciones);
        assertEquals(0, clasificador.movimientos);
    }

    // Orden inverso es el peor caso: cada elemento se compara y se desplaza con todos los anteriores.
    // n = 5 -> 1 + 2 + 3 + 4 = 10 comparaciones y 10 movimientos.
    @Test
    public void testContadoresOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};

        clasificador.reiniciarContadores();
        clasificador.insercionDirecta(datos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(10, clasificador.movimientos);
    }

    // Todos iguales: no se mueve nada, porque 5 < 5 es falso.
    @Test
    public void testContadoresTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};

        clasificador.reiniciarContadores();
        clasificador.insercionDirecta(datos);

        assertEquals(4, clasificador.comparaciones);
        assertEquals(0, clasificador.movimientos);
    }

    @Test
    public void testReiniciarContadores() {
        int[] datos = {5, 4, 3, 2, 1};
        clasificador.insercionDirecta(datos);

        clasificador.reiniciarContadores();

        assertEquals(0, clasificador.comparaciones);
        assertEquals(0, clasificador.movimientos);
    }
}