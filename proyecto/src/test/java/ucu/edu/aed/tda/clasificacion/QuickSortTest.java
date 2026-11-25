package ucu.edu.aed.tda.clasificacion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

// Tests de quicksort, de nuestro obtenerPivote y de nuestra particion.
// Está en el mismo paquete que TClasificadorImpl porque obtenerPivote y particion son protected.
public class QuickSortTest {

    private TClasificadorImpl clasificador;

    @Before
    public void setUp() {
        clasificador = new TClasificadorImpl();
    }

    // ===================== ORDENA BIEN

    @Test
    public void testQuicksort() {
        int[] datos = {5, 8, 2, 15, 4, 12, 3};
        int[] esperado = {2, 3, 4, 5, 8, 12, 15};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresRepetidos() {
        int[] datos = {0, 5, 5, 0, 10, 10, 1};
        int[] esperado = {0, 0, 1, 5, 5, 10, 10};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testArregloVacio() {
        int[] datos = {};

        clasificador.quicksort(datos);

        assertEquals(0, datos.length);
    }

    @Test
    public void testUnElemento() {
        int[] datos = {8};
        int[] esperado = {8};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};
        int[] esperado = {5, 5, 5, 5, 5};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testOrdenInverso() {
        int[] datos = {5, 4, 3, 2, 1};
        int[] esperado = {1, 2, 3, 4, 5};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresNegativos() {
        int[] datos = {-5, 8, -2, 0, 4};
        int[] esperado = {-5, -2, 0, 4, 8};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    // El caso de la consigna: con la mediana de tres se rompía; con nuestro pivote ordena
    @Test
    public void testCasoDeLaConsigna() {
        int[] datos = {1, 1, 1, 2};
        int[] esperado = {1, 1, 1, 2};

        clasificador.quicksort(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testContraArraysSort() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int[] esperado = datos.clone();

        Arrays.sort(esperado);
        clasificador.quicksort(datos);

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

            clasificador.quicksort(datos);

            assertTrue(Arrays.equals(esperado, datos));
        }
    }

    // ===================== OBTENER PIVOTE

    // Los dos primeros distintos son 3 y 7: devuelve la posición del mayor (el 7, posición 1)
    @Test
    public void testPivoteSegundoMayor() {
        int[] datos = {3, 7, 1, 9};

        int x = clasificador.obtenerPivote(datos, 0, 3);

        assertEquals(1, x);
    }

    // Los dos primeros distintos son 7 y 3: el mayor es el primero (posición 0)
    @Test
    public void testPivotePrimeroMayor() {
        int[] datos = {7, 3, 1, 9};

        int x = clasificador.obtenerPivote(datos, 0, 3);

        assertEquals(0, x);
    }

    // Saltea los iguales del principio: los dos primeros distintos son 4 y 6
    @Test
    public void testPivoteSalteaIguales() {
        int[] datos = {4, 4, 4, 6, 1};

        int x = clasificador.obtenerPivote(datos, 0, 4);

        assertEquals(3, x);
    }

    // Todos iguales: no hay pivote, devuelve -1
    @Test
    public void testPivoteTodosIguales() {
        int[] datos = {5, 5, 5, 5};

        int x = clasificador.obtenerPivote(datos, 0, 3);

        assertEquals(-1, x);
    }

    // El contrato: el valor del pivote es estrictamente mayor que el mínimo del rango,
    // o devuelve -1 si todos son iguales.
    // (Puede devolver j solo si todos los anteriores son iguales y el último es mayor: ese rango
    //  ya está ordenado y el quicksort del proyecto lo deja como está, porque pide x < j.)
    @Test
    public void testPivoteMayorQueElMinimo() {
        Random random = new Random(2026);
        for (int prueba = 0; prueba < 1000; prueba++) {
            int[] datos = new int[2 + random.nextInt(50)];
            for (int i = 0; i < datos.length; i++) {
                datos[i] = random.nextInt(5);
            }
            int i = 0;
            int j = datos.length - 1;
            int minimo = Arrays.stream(datos).min().getAsInt();

            int x = clasificador.obtenerPivote(datos, i, j);

            if (x == -1) {
                for (int v : datos) {
                    assertEquals(datos[0], v);          // -1 solo si son todos iguales
                }
            } else {
                assertTrue(x >= i && x <= j);
                assertTrue(datos[x] > minimo);          // la condición de la pregunta 1
            }
        }
    }

    // ===================== PARTICIÓN

    // Después de particionar: a la izquierda de k quedan los < pivote y desde k los >= pivote,
    // y las dos partes son más chicas que el rango (así la recursión avanza)
    @Test
    public void testParticion() {
        Random random = new Random(2026);
        for (int prueba = 0; prueba < 1000; prueba++) {
            int[] datos = new int[2 + random.nextInt(50)];
            for (int i = 0; i < datos.length; i++) {
                datos[i] = random.nextInt(5);
            }
            int i = 0;
            int j = datos.length - 1;
            int x = clasificador.obtenerPivote(datos, i, j);
            if (x == -1) {
                continue;                               // todos iguales: no se particiona
            }
            int pivote = datos[x];

            int k = clasificador.particion(datos, i, j, pivote);

            for (int p = i; p < k; p++) {
                assertTrue(datos[p] < pivote);
            }
            for (int p = k; p <= j; p++) {
                assertTrue(datos[p] >= pivote);
            }
            assertTrue(k > i);                          // la parte izquierda no queda vacía
            assertTrue(k <= j);                         // la parte derecha no queda vacía
        }
    }

    // ===================== CONTADORES

    // Ordenar todos iguales no intercambia nada (obtenerPivote devuelve -1 de entrada)
    @Test
    public void testContadoresTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};

        clasificador.reiniciarContadores();
        clasificador.quicksort(datos);

        assertEquals(0, clasificador.movimientos);
        assertEquals(8, clasificador.comparaciones);   // 4 elementos x 2 comparaciones (no es mayor, no es menor)
    }
}