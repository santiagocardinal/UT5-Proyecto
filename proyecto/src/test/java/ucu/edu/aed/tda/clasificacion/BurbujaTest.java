package ucu.edu.aed.tda.clasificacion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

// Tests de burbuja.
// Está en el mismo paquete que TClasificadorImpl porque burbuja no es público.
public class BurbujaTest {

    private TClasificadorImpl clasificador;

    @Before
    public void setUp() {
        clasificador = new TClasificadorImpl();
    }

    // ===================== ORDENA BIEN

    @Test
    public void testBurbuja() {
        int[] datos = {5, 8, 2, 15, 4, 12, 3};
        int[] esperado = {2, 3, 4, 5, 8, 12, 15};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresRepetidos() {
        int[] datos = {0, 5, 5, 0, 10, 10, 1};
        int[] esperado = {0, 0, 1, 5, 5, 10, 10};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testArregloVacio() {
        int[] datos = {};

        clasificador.burbuja(datos);

        assertEquals(0, datos.length);
    }

    @Test
    public void testUnElemento() {
        int[] datos = {8};
        int[] esperado = {8};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    // Con el código de clase (i < n - 2 y j > i + 1) un arreglo de 2 elementos no se ordenaba:
    // con n = 2 el for de afuera no hacía ninguna pasada.
    @Test
    public void testDosElementosDesordenados() {
        int[] datos = {2, 1};
        int[] esperado = {1, 2};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    // Con el código de clase los dos primeros nunca se comparaban entre sí (j > i + 1)
    @Test
    public void testLosDosPrimerosDesordenados() {
        int[] datos = {2, 1, 3, 4, 5};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};
        int[] esperado = {5, 5, 5, 5, 5};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresNegativos() {
        int[] datos = {-5, 8, -2, 0, 4};
        int[] esperado = {-5, -2, 0, 4, 8};

        clasificador.burbuja(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testContraArraysSort() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int[] esperado = datos.clone();

        Arrays.sort(esperado);
        clasificador.burbuja(datos);

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

            clasificador.burbuja(datos);

            assertTrue(Arrays.equals(esperado, datos));
        }
    }

    // ===================== CONTADORES

    // Burbuja compara SIEMPRE todos los pares: n·(n−1)/2, sin importar cómo vengan los datos.
    // Con n = 5 son 10 comparaciones. Orden inverso es el peor caso: los 10 pares se intercambian.
    @Test
    public void testContadoresOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};

        clasificador.reiniciarContadores();
        clasificador.burbuja(datos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(10, clasificador.movimientos);
    }

    // Ya ordenado: las mismas 10 comparaciones, pero ningún intercambio
    @Test
    public void testContadoresYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};

        clasificador.reiniciarContadores();
        clasificador.burbuja(datos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(0, clasificador.movimientos);
    }

    // Todos iguales: 5 < 5 es falso, así que tampoco intercambia nada
    @Test
    public void testContadoresTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};

        clasificador.reiniciarContadores();
        clasificador.burbuja(datos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(0, clasificador.movimientos);
    }

    // Las comparaciones no dependen de los datos: con 100 elementos al azar son 100·99/2 = 4950
    @Test
    public void testComparacionesNoDependenDeLosDatos() {
        Random random = new Random(2026);
        int[] datos = new int[100];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = random.nextInt(1000);
        }

        clasificador.reiniciarContadores();
        clasificador.burbuja(datos);

        assertEquals(4950, clasificador.comparaciones);
    }
}