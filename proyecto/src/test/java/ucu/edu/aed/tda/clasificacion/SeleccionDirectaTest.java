package ucu.edu.aed.tda.clasificacion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

// Tests de selección directa (clasificacionDirecta).
public class SeleccionDirectaTest {

    private TClasificadorImpl clasificador;

    @Before
    public void setUp() {
        clasificador = new TClasificadorImpl();
    }

    // ===================== ORDENA BIEN

    @Test
    public void testSeleccionDirecta() {
        int[] datos = {5, 8, 2, 15, 4, 12, 3};
        int[] esperado = {2, 3, 4, 5, 8, 12, 15};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresRepetidos() {
        int[] datos = {0, 5, 5, 0, 10, 10, 1};
        int[] esperado = {0, 0, 1, 5, 5, 10, 10};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testArregloVacio() {
        int[] datos = {};

        clasificador.clasificacionDirecta(datos);

        assertEquals(0, datos.length);
    }

    @Test
    public void testUnElemento() {
        int[] datos = {8};
        int[] esperado = {8};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testDosElementosDesordenados() {
        int[] datos = {2, 1};
        int[] esperado = {1, 2};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};
        int[] esperado = {5, 5, 5, 5, 5};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresNegativos() {
        int[] datos = {-5, 8, -2, 0, 4};
        int[] esperado = {-5, -2, 0, 4, 8};

        clasificador.clasificacionDirecta(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testContraArraysSort() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int[] esperado = datos.clone();

        Arrays.sort(esperado);
        clasificador.clasificacionDirecta(datos);

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

            clasificador.clasificacionDirecta(datos);

            assertTrue(Arrays.equals(esperado, datos));
        }
    }

    // ===================== CONTADORES

    // Selección compara SIEMPRE n·(n−1)/2 veces (con n = 5 son 10), pero intercambia poco:
    // en orden inverso solo hace 2 intercambios (5 con 1 y 4 con 2; el 3 ya queda en su lugar).
    @Test
    public void testContadoresOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};

        clasificador.reiniciarContadores();
        clasificador.clasificacionDirecta(datos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(2, clasificador.movimientos);
    }

    // Ya ordenado: las mismas 10 comparaciones y ningún intercambio,
    // gracias al if (indiceDelMenor != i) que agregamos
    @Test
    public void testContadoresYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};

        clasificador.reiniciarContadores();
        clasificador.clasificacionDirecta(datos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(0, clasificador.movimientos);
    }

    @Test
    public void testContadoresTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};

        clasificador.reiniciarContadores();
        clasificador.clasificacionDirecta(datos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(0, clasificador.movimientos);
    }

    // Con 100 elementos al azar: 100·99/2 = 4950 comparaciones y como mucho 99 intercambios
    // (uno por pasada). Es el que menos mueve de todos.
    @Test
    public void testComparacionesFijasYPocosIntercambios() {
        Random random = new Random(2026);
        int[] datos = new int[100];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = random.nextInt(1000);
        }

        clasificador.reiniciarContadores();
        clasificador.clasificacionDirecta(datos);

        assertEquals(4950, clasificador.comparaciones);
        assertTrue(clasificador.movimientos <= 99);
    }
}