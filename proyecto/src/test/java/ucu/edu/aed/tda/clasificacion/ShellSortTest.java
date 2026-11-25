package ucu.edu.aed.tda.clasificacion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

// Tests de shellsort.
// Está en el mismo paquete que TClasificadorImpl porque shell no es público.
public class ShellSortTest {

    private TClasificadorImpl clasificador;

    @Before
    public void setUp() {
        clasificador = new TClasificadorImpl();
    }

    // Ordena con la secuencia de incrementos que usamos en las mediciones: n/2, n/4, ..., 1
    private void ordenar(int[] datos) {
        clasificador.shell(datos, TipoClasificador.incrementosPorDefecto(datos.length));
    }

    // ===================== ORDENA BIEN

    @Test
    public void testShellsort() {
        int[] datos = {5, 8, 2, 15, 4, 12, 3};
        int[] esperado = {2, 3, 4, 5, 8, 12, 15};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresRepetidos() {
        int[] datos = {0, 5, 5, 0, 10, 10, 1};
        int[] esperado = {0, 0, 1, 5, 5, 10, 10};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testArregloVacio() {
        int[] datos = {};

        ordenar(datos);

        assertEquals(0, datos.length);
    }

    @Test
    public void testUnElemento() {
        int[] datos = {8};
        int[] esperado = {8};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testTodosIguales() {
        int[] datos = {5, 5, 5, 5, 5};
        int[] esperado = {5, 5, 5, 5, 5};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5};
        int[] esperado = {1, 2, 3, 4, 5};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testOrdenInverso() {
        int[] datos = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] esperado = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testValoresNegativos() {
        int[] datos = {-5, 8, -2, 0, 4};
        int[] esperado = {-5, -2, 0, 4, 8};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    // El de clase usaba j > 0, así que nunca comparaba con datos[0]:
    // si el menor no estaba en la posición 0, podía quedar mal.
    @Test
    public void testElMenorVaAlPrincipio() {
        int[] datos = {3, 2, 1};
        int[] esperado = {1, 2, 3};

        ordenar(datos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    @Test
    public void testContraArraysSort() {
        int[] datos = {0, 8, 3, 15, 0, 12, 3, 20, 1, 5};
        int[] esperado = datos.clone();

        Arrays.sort(esperado);
        ordenar(datos);

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

            ordenar(datos);

            assertTrue(Arrays.equals(esperado, datos));
        }
    }

    // ===================== INCREMENTOS

    // Ordena bien con otra secuencia de incrementos, siempre que termine en 1
    @Test
    public void testOtrosIncrementos() {
        int[] datos = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] esperado = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Integer[] incrementos = {4, 2, 1};

        clasificador.shell(datos, incrementos);

        assertTrue(Arrays.equals(esperado, datos));
    }

    // Los incrementos se recorren del primero al último. La última pasada tiene que ser con salto 1:
    // el código de clase los recorría al revés y la última pasada era con el salto más grande.
    @Test
    public void testIncrementosPorDefectoTerminanEnUno() {
        Integer[] incrementos = TipoClasificador.incrementosPorDefecto(10);

        assertEquals(Integer.valueOf(1), incrementos[incrementos.length - 1]);
    }

    // ===================== CONTADORES

    // Con un solo incremento {1}, shellsort es igual a inserción directa:
    // en orden inverso con n = 5 hace 10 comparaciones y 10 movimientos.
    @Test
    public void testContadoresConIncrementoUno() {
        int[] datos = {5, 4, 3, 2, 1};
        Integer[] incrementos = {1};

        clasificador.reiniciarContadores();
        clasificador.shell(datos, incrementos);

        assertEquals(10, clasificador.comparaciones);
        assertEquals(10, clasificador.movimientos);
    }

    // Ya ordenado no mueve nada
    @Test
    public void testContadoresYaOrdenado() {
        int[] datos = {1, 2, 3, 4, 5, 6, 7, 8};

        clasificador.reiniciarContadores();
        ordenar(datos);

        assertEquals(0, clasificador.movimientos);
    }

    // Con los incrementos n/2, ..., 1 mueve menos que inserción directa en el peor caso
    @Test
    public void testMueveMenosQueInsercion() {
        int[] datos = new int[100];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = 100 - i;                    // orden inverso: el peor caso de inserción
        }

        clasificador.reiniciarContadores();
        clasificador.insercionDirecta(datos.clone());
        long movimientosInsercion = clasificador.movimientos;

        clasificador.reiniciarContadores();
        ordenar(datos.clone());
        long movimientosShell = clasificador.movimientos;

        assertTrue(movimientosShell < movimientosInsercion);
    }
}