
package ucu.edu.aed.tda.clasificacion; 
 
public class TClasificadorImpl extends TClasificador { 
 
    // ===== CONTADORES =====
    public long comparaciones = 0;
    public long movimientos = 0;

    public void reiniciarContadores() {
        comparaciones = 0;
        movimientos = 0;
    }

    @Override
    protected void intercambiar(int[] datos, int i, int j) {
        movimientos++;
        super.intercambiar(datos, i, j);
    }

    // menorQue(a, b): hace la comparación a < b y además suma 1 al contador de comparaciones.
    // Lo usamos en todos los algoritmos en vez de poner "<" directo, así no nos olvidamos
    // de contar ninguna. Para preguntar a >= b usamos !menorQue(a, b).
    private boolean menorQue(int a, int b) {
        comparaciones++;
        return a < b;
    }


    // ==========================================INSERCIÓN DIRECTA

    /*@Override 
    ESTE ES EL ORIGINAL DADO EN CLASE PERO TUVIMOS QUE CAMBIALO PARA QUE CUENTE LOS MOVIMIENTOS Y COMPARACIONES
    void insercionDirecta(int[] datos) { 
        for (int i = 1; i < datos.length; i++) { 
            int aux = datos[i]; 
            int j = i - 1; 
            while (j > 0 && aux < datos[j]) { 
                datos[j + 1] = datos[j]; 
                j = j - 1; 
            } 
            datos[j + 1] = aux; 
        } 
    }*/
    
    //SE LE AGREGA LO DE LAS COMPARACIONES Y MOVIMIENTOS
    // El de clase usa posiciones de 1 a n (j > 0). En Java empieza en 0: j >= 0.
    // Además se agregan los contadores de comparaciones y movimientos.

    @Override
    void insercionDirecta(int[] datos) {
        for (int i = 1; i < datos.length; i++) {
            int aux = datos[i];
            int j = i - 1;
            while (j >= 0 && menorQue(aux, datos[j])) {   
                datos[j + 1] = datos[j];
                movimientos++;                            // desplazamiento
                j = j - 1;
            }
            datos[j + 1] = aux;
        }
    }


    // =======================================================SHELLSORT

    /*@Override 
    void shell(int[] datos, Integer[] incrementos) { 
       
        for (int k = incrementos.length - 1; k >= 0; k--) { 
            int h = incrementos[k]; 
            for (int i = h; i < datos.length; i++) { 
                int aux = datos[i]; 
                int j = i - h; 
                while (j > 0 && aux < datos[j]) { 
                    datos[j + h] = datos[j]; 
                    j = j - h; 
                } 
                datos[j + h] = aux; 
            } 
        } 
    } */
   
    @Override
    void shell(int[] datos, Integer[] incrementos) {
        for (int k = 0; k < incrementos.length; k++) {   // incrementosPorDefecto devuelve n/2, n/4, ..., 1 (de mayor a menor):
                                                         // se recorre del primero al último para que la última pasada sea con salto 1       
            int h = incrementos[k];
            for (int i = h; i < datos.length; i++) {
                int aux = datos[i];
                int j = i - h;
                while (j >= 0 && menorQue(aux, datos[j])) {  
                    datos[j + h] = datos[j];
                    movimientos++;                            // desplazamiento
                    j = j - h;
                }
                datos[j + h] = aux;
            }
        }
    }
 
    // ================================================================BURBUJA
    /*@Override
    void burbuja(int[] datos) { 
        int n = datos.length; 
        for (int i = 0; i < n - 2; i++) { 
            for (int j = n - 1; j > i + 1; j--) { 
                if (datos[j] < datos[j - 1]) { 
                    intercambiar(datos, j, j - 1); 
                } 
            } 
        }
    }*/
    // El de clase usa posiciones de 1 a n. En Java empieza en 0: i < n - 1 y j > i.
    // Además se agregan los contadores.

    @Override
    void burbuja(int[] datos) {
        int n = datos.length;
        for (int i = 0; i < n - 1; i++) {               
            for (int j = n - 1; j > i; j--) {           
                if (menorQue(datos[j], datos[j - 1])) {
                    intercambiar(datos, j, j - 1);
                }
            }
        }
    }

 
    // ================================================================QUICKSORT
 
    /*@Override 
    protected int obtenerPivote(int[] datos, int i, int j) { 
        int primero = datos[i]; 
        for (int k = i + 1; k <= j; k++) { 
            if (datos[k] > primero) { 
                return k; 
            } else if (datos[k] < primero) { 
                return i; 
            } 
        } 
        return -1; 
    } 
 
    @Override 
    protected int particion(int[] datos, int i, int j, int pivote) { 
        int L = i; 
        int R = j; 
        do { 
            intercambiar(datos, L, R);
            while (datos[L] < pivote) { 
                L = L + 1; 
            }
            while (datos[R] >= pivote) {
                R = R - 1; 
            } 
        } while (L <= R); 
        return L; 
    }*/

   // Pivote: el mayor de los dos primeros elementos distintos (siempre > mínimo del rango)
    @Override
    protected int obtenerPivote(int[] datos, int i, int j) {
        int primero = datos[i];
        for (int k = i + 1; k <= j; k++) {
            if (menorQue(primero, datos[k])) {
                return k;
            } else if (menorQue(datos[k], primero)) {
                return i;
            }
        }
        return -1;
    }

    // Deja a la izquierda los < pivote y a la derecha los >= pivote; devuelve L
    @Override
    protected int particion(int[] datos, int i, int j, int pivote) {
        int L = i;
        int R = j;
        do {
            intercambiar(datos, L, R);
            while (menorQue(datos[L], pivote)) {        // datos[L] < pivote
                L = L + 1;
            }
            while (!menorQue(datos[R], pivote)) {       // datos[R] >= pivote
                R = R - 1;
            }
        } while (L <= R);
        return L;
    } 

 
    // ================================================================SELECCIÓN DIRECTA
    /*@Override 
    public void clasificacionDirecta(int[] datos) { // recibe el vector a ordenar
        int n = datos.length; // n: cantidad de elementos
        for (int i = 0; i <= n - 2; i++) { // "Para i = 1 hasta n-1": en Java de 0 hasta n-2
            int indiceDelMenor = i; // "indiceDelMenor <- i": supone que el menor está en la posición i
            int menorValor = datos[i]; // "menorValor <- V[i]": guarda ese valor como el menor provisorio
            for (int j = i + 1; j <= n - 1; j++) { // "Para j = i+1 hasta n": recorre el resto del vector
                if (datos[j] < menorValor) { // "Si V[j] < menorValor": encontró un valor más chico
                    indiceDelMenor = j; // "indiceDelMenor <- j" (en la diapositiva dice "indiceDelMejor", es un error de tipeo)
                    menorValor = datos[j]; // "menorValor <- V[j]": actualiza el menor valor encontrado
                } 
            } 
            intercambiar(datos, i, indiceDelMenor); // "intercambiar(V, i, indiceDelMenor)": lleva el menor a la posición i
        } 
    }*/
    @Override
    public void clasificacionDirecta(int[] datos) {
        int n = datos.length;
        for (int i = 0; i <= n - 2; i++) {
            int indiceDelMenor = i;
            int menorValor = datos[i];
            for (int j = i + 1; j <= n - 1; j++) {
                if (menorQue(datos[j], menorValor)) {
                    indiceDelMenor = j;
                    menorValor = datos[j];
                }
            }
            if (indiceDelMenor != i) {                  // no intercambia un elemento consigo mismo
                intercambiar(datos, i, indiceDelMenor);
            }
        }
    }
 
    
    // ================================================================HEAPSORT
    @Override
    public void heapsort(int[] datos) {
        int n = datos.length;
        for (int i = n / 2; i >= 1; i--) {
            desplazaElemento(datos, i, n);
        }
        for (int i = n; i >= 2; i--) {
            intercambiar(datos, 1 - 1, i - 1);
            desplazaElemento(datos, 1, i - 1);
        }
    }

    @Override
    protected void desplazaElemento(int[] datos, int primero, int ultimo) {
        int actual = primero;
        while (actual <= ultimo / 2) {
            if (ultimo == 2 * actual) {
                if (menorQue(datos[actual - 1], datos[2 * actual - 1])) {
                    intercambiar(datos, actual - 1, 2 * actual - 1);
                }
                actual = ultimo;
            } else {
                int mayor = mayorHijo(datos, 2 * actual, 2 * actual + 1);
                if (menorQue(datos[actual - 1], datos[mayor - 1])) {
                    intercambiar(datos, actual - 1, mayor - 1);
                    actual = mayor;
                } else {
                    actual = ultimo;
                }
            }
        }
    }

    private int mayorHijo(int[] datos, int hijoIzq, int hijoDer) {
        if (!menorQue(datos[hijoIzq - 1], datos[hijoDer - 1])) {   // izq >= der
            return hijoIzq;
        }
        return hijoDer;
    }

    @Override public void cuentaPorDistribucion(int[] datos) { throw new UnsupportedOperationException("No pedido"); }
    @Override public void binsort(int[] datos) { throw new UnsupportedOperationException("No pedido"); }
    @Override public void bucketsort(int[] datos, int cantidadUrnas) { throw new UnsupportedOperationException("No pedido"); }
    @Override public void radixsort(int[] datos, int base) { throw new UnsupportedOperationException("No pedido"); }
    @Override protected void distribuirPorDigito(int[] datos, int exponente, int base) { throw new UnsupportedOperationException("No pedido"); }


    // nos va a servir
    public boolean estaOrdenado(int[] datos) {
        for (int i = 0; i < datos.length - 1; i++) {
            if (datos[i] > datos[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
    
 
