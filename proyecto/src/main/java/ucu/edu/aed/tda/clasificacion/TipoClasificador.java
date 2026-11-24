package ucu.edu.aed.tda.clasificacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmos de clasificación de TClasificador, agrupados por familia.
 * Permite recorrerlos en un loop, por ejemplo para medir tiempos:
 *
 * <pre>
 * for (TipoClasificador tipo : TipoClasificador.values()) {
 *     int[] copia = datos.clone();
 *     long ms = clasificador.medirTiempoDeEjecución(() -&gt; tipo.ordenar(clasificador, copia));
 *     System.out.println(tipo.getNombre() + ": " + ms + " ms");
 * }
 * </pre>
 */
public enum TipoClasificador {

    INSERCION_DIRECTA("Inserción directa", Familia.INSERCION),
    SHELLSORT("Shellsort", Familia.INSERCION),
    BURBUJA("Burbuja", Familia.INTERCAMBIO),
    QUICKSORT("Quicksort", Familia.INTERCAMBIO),
    SELECCION_DIRECTA("Selección directa", Familia.SELECCION),
    HEAPSORT("Heapsort", Familia.SELECCION),
    CUENTA_POR_DISTRIBUCION("Cuenta por distribución", Familia.ENUMERACION),
    BINSORT("Binsort", Familia.DISTRIBUCION),
    BUCKETSORT("Bucketsort", Familia.DISTRIBUCION),
    RADIXSORT("Radix sort", Familia.DISTRIBUCION);

    /** Familias de métodos de clasificación interna, según el material del curso */
    public enum Familia {
        INSERCION, INTERCAMBIO, SELECCION, ENUMERACION, DISTRIBUCION
    }

    private final String nombre;
    private final Familia familia;

    TipoClasificador(String nombre, Familia familia) {
        this.nombre = nombre;
        this.familia = familia;
    }

    public String getNombre() {
        return nombre;
    }

    public Familia getFamilia() {
        return familia;
    }

    /**
     * true si el algoritmo ordena comparando claves (cota inferior n log n);
     * false si es de distribución (puede ser lineal para ciertas clases de datos)
     */
    public boolean esComparativo() {
        return familia != Familia.ENUMERACION && familia != Familia.DISTRIBUCION;
    }

    /**
     * Ordena el vector con este algoritmo usando el clasificador dado.
     * Los algoritmos con parámetros usan valores por defecto:
     * shellsort con incrementos n/2, n/4, ..., 1; bucketsort con n urnas; radix sort en base 10
     */
    public void ordenar(TClasificador clasificador, int[] datos) {
        switch (this) {
            case INSERCION_DIRECTA -> clasificador.insercionDirecta(datos);
            case SHELLSORT -> clasificador.shell(datos, incrementosPorDefecto(datos.length));
            case BURBUJA -> clasificador.burbuja(datos);
            case QUICKSORT -> clasificador.quicksort(datos);
            case SELECCION_DIRECTA -> clasificador.clasificacionDirecta(datos);
            case HEAPSORT -> clasificador.heapsort(datos);
            case CUENTA_POR_DISTRIBUCION -> clasificador.cuentaPorDistribucion(datos);
            case BINSORT -> clasificador.binsort(datos);
            case BUCKETSORT -> clasificador.bucketsort(datos, Math.max(1, datos.length));
            case RADIXSORT -> clasificador.radixsort(datos);
        }
    }

    /**
     * Secuencia de incrementos original de Shell para un vector de largo n: n/2, n/4, ..., 1
     */
    public static Integer[] incrementosPorDefecto(int n) {
        List<Integer> incrementos = new ArrayList<>();
        for (int h = n / 2; h >= 1; h /= 2) {
            incrementos.add(h);
        }
        if (incrementos.isEmpty()) {
            incrementos.add(1);
        }
        return incrementos.toArray(new Integer[0]);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
