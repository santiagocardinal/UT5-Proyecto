package ucu.edu.aed.tda.clasificacion;

public abstract class TClasificador {

    /**
     * Ordena el vector por inserción directa
     */
    abstract void insercionDirecta(int[] datos);

    /**
     * Ordena el vector utilizando shellsort con un vector de incrementos datos
     */
    abstract void shell(int[] datos, Integer[] incrementos);

    /**
     * Ordena el vector utilizando la técnica de Burbuja
     */
    abstract void burbuja(int[] datos);


    public void quicksort(int[] datos) {
        quicksort(datos, 0, datos.length - 1);
    }

    /**
     * método recursivo de quicksort
     */
    private void quicksort(int[] datos, int i, int j) {
        if (i <= j) {
            int indicePivote = obtenerPivote(datos, i, j);
            // debe ser un rango válido
            if (indicePivote >= i && indicePivote < j) {
                int pivote = datos[indicePivote];
                int k = particion(datos, i, j, pivote);
                quicksort(datos, i, k - 1);
                quicksort(datos, k, j);
            }
        }
    }

    /**
     * método auxiliar y utilizado en quicksort
     * devuelve un valor del vector ubicado entre i <= x < j
     */
    protected abstract int obtenerPivote(int[] datos, int i, int j);

    /**
     * método auxiliar y utilizado en quicksort
     */
    protected abstract int particion(int[] datos, int i, int j, int pivote);

    /**
     * Ordena el vector utilizando clasificación directa
     */
    public abstract void clasificacionDirecta(int[] datos);

    /**
     * Ordena el vector utilizando heapsort
     */
    public abstract void heapsort(int[] datos);

    /**
     * método auxiliar y utilizado en heapsort
     */
    protected abstract void desplazaElemento(int[] datos, int primero, int ultimo);

    /**
     * Ordena el vector utilizando cuenta por distribución (clasificación por enumeración o cuenta).
     * Admite claves repetidas; el rango de claves va del mínimo al máximo del vector
     */
    public abstract void cuentaPorDistribucion(int[] datos);

    /**
     * Ordena el vector utilizando binsort (clasificación por urnas), con una urna por cada
     * valor de clave entre el mínimo y el máximo del vector. Debe admitir claves repetidas
     */
    public abstract void binsort(int[] datos);

    /**
     * Ordena el vector utilizando bucketsort: distribuye los elementos en cantidadUrnas urnas
     * por intervalos de valores, ordena cada urna (por ejemplo, por inserción) y las concatena
     */
    public abstract void bucketsort(int[] datos, int cantidadUrnas);

    /**
     * Ordena el vector utilizando radix sort (clasificación por residuos) en base 10
     */
    public void radixsort(int[] datos) {
        radixsort(datos, 10);
    }

    /**
     * Ordena el vector utilizando radix sort (clasificación por residuos) en la base dada.
     * Precondición: todos los valores del vector son mayores o iguales a 0, y base >= 2
     */
    public abstract void radixsort(int[] datos, int base);

    /**
     * método auxiliar y utilizado en radixsort
     * realiza una pasada ESTABLE de distribución según el dígito (valor / exponente) % base
     */
    protected abstract void distribuirPorDigito(int[] datos, int exponente, int base);

    /**
     * método de ayuda y utilizado en cuentaPorDistribucion, binsort, bucketsort y radixsort
     * devuelve {mínimo, máximo} de un vector no vacío
     */
    protected int[] obtenerRango(int[] datos) {
        int minimo = datos[0];
        int maximo = datos[0];
        for (int valor : datos) {
            if (valor < minimo) {
                minimo = valor;
            } else if (valor > maximo) {
                maximo = valor;
            }
        }
        return new int[]{minimo, maximo};
    }

    /**
     * método de ayuda para intercambiar valores en un vector dados sus índices
     */
    protected void intercambiar(int[] datos, int i, int j) {
        int aux = datos[i];
        datos[i] = datos[j];
        datos[j] = aux;
    }

    /**
     * calcula el tiempo de ejecución de una tarea en milisegundos
     */
    public long medirTiempoDeEjecución(Runnable tarea) {
        long start = System.currentTimeMillis();

        tarea.run();

        return (System.currentTimeMillis() - start);
    }


}
