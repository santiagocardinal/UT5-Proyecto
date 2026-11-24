# El contrato del pivote

El quicksort del proyecto base ya está escrito y no se modifica. Llama a `obtenerPivote(datos, i, j)` y después a `particion(datos, i, j, pivote)`, y sigue con `quicksort(i, k - 1)` y `quicksort(k, j)`.

`obtenerPivote` devuelve un índice `x` con `i <= x < j`, o `-1` si todos los elementos del rango son iguales. Pero eso no alcanza para que el quicksort termine: lo que se necesita depende de cómo escriban `particion`.

> Supongamos que `particion` deja a la izquierda los elementos menores que el pivote y a la derecha los mayores o iguales, como en el libro.
>
> En ese caso, si el pivote es el mínimo del rango, la parte izquierda queda vacía. Entonces `quicksort(k, j)` recibe el mismo rango otra vez y la recursión no avanza nunca.

---

## Respuestas

### 1. Con su `particion`, ¿qué condición tiene que cumplir el valor del pivote para que las dos partes sean siempre más chicas que el rango original?

El valor del pivote tiene que ser **estrictamente mayor que el mínimo del rango**.

- A la izquierda queda por lo menos el mínimo (que es menor que el pivote), así que la parte derecha nunca es el rango entero.
- A la derecha queda por lo menos el propio pivote (que es mayor o igual a sí mismo), así que la parte izquierda tampoco es el rango entero.
- Además, como siempre hay un elemento menor que el pivote, el `while` que mueve `R` siempre encuentra dónde frenar y no se sale del rango.

Así las dos partes quedan más chicas que el rango original y la recursión avanza.

### 2. La estrategia «el mayor de los dos primeros elementos distintos», ¿cumple esa condición?

**Sí.** Si hay dos valores distintos, el mayor de los dos es más grande que el otro, y por lo tanto es más grande que el mínimo del rango. Si todos los elementos son iguales devuelve -1 y el quicksort no sigue con ese rango, que ya está ordenado.

Es la estrategia que usa nuestro `obtenerPivote`, y en la prueba ordena `[1, 1, 1, 2]` sin problemas.

### 3. ¿Y «la mediana entre el primero, el del medio y el último»? Prueben con `[1, 1, 1, 2]`.

**No cumple.** Con `i = 0` y `j = 3`:

| Elemento | Posición | Valor |
|---|---|---|
| Primero | 0 | 1 |
| Del medio | (0 + 3) / 2 = 1 | 1 |
| Último | 3 | 2 |

La mediana es **1**, que es el mínimo del rango.

Con nuestra `particion`, ningún elemento es menor que el pivote, entonces el `while` de `R` no encuentra dónde parar y se sale del arreglo: en la prueba da **`ArrayIndexOutOfBoundsException: Index -1`**.

Si la partición no se saliera del rango, la parte izquierda quedaría vacía y `quicksort(k, j)` recibiría el mismo rango una y otra vez, hasta terminar en `StackOverflowError`. En los dos casos el problema es el mismo: el pivote es el mínimo.

### 4. Si alguna no la cumple, ¿cómo la arreglan?

Arreglamos la mediana de tres así (clase `PivoteMedianaArreglada`):

- Si los tres valores son iguales, la mediana no sirve. Usamos «el mayor de los dos primeros distintos», o -1 si todo el rango es igual.
- Si la mediana es igual al menor de los tres, usamos el **mayor** de los tres, que es estrictamente mayor que el mínimo.
- `obtenerPivote` tiene que devolver un índice menor que `j`. Si el pivote elegido es el último, lo intercambiamos con el del medio y devolvemos la posición del medio.

Con eso el pivote siempre es mayor que el mínimo del rango. En la prueba, la versión arreglada ordena `[1, 1, 1, 2]` correctamente.

---

### Código de la prueba

```java
package ucu.edu.aed.tda.clasificacion;

import java.util.Arrays;

// =====================================================================
// DEMO DEL CONTRATO DEL PIVOTE
// Probamos el quicksort con tres formas de elegir el pivote sobre [1, 1, 1, 2]:
//   1) la nuestra: el mayor de los dos primeros distintos  -> tiene que ordenar
//   2) la mediana de tres (primero, medio, último)        -> se rompe
//   3) la mediana de tres arreglada                       -> tiene que ordenar
// Para cambiar solo el pivote hacemos subclases de TClasificadorImpl que
// sobreescriben obtenerPivote (particion y quicksort quedan iguales).
// =====================================================================
public class DemoPivote {

    public static void main(String[] args) {
        probar("Mayor de los dos primeros distintos", new TClasificadorImpl());
        probar("Mediana de tres", new PivoteMedianaDeTres());
        probar("Mediana de tres ARREGLADA", new PivoteMedianaArreglada());
    }

    static void probar(String nombre, TClasificadorImpl clasificador) {
        int[] datos = {1, 1, 1, 2};
        System.out.println(nombre + ":");
        try {
            clasificador.quicksort(datos);
            System.out.println("  terminó, resultado: " + Arrays.toString(datos));
        } catch (StackOverflowError | ArrayIndexOutOfBoundsException e) {
            // el pivote es el mínimo del rango: la partición o la recursión se rompen
            System.out.println("  SE ROMPE: " + e);
        }
    }
}

// Mediana entre el primero, el del medio y el último (sin arreglar)
class PivoteMedianaDeTres extends TClasificadorImpl {
    @Override
    protected int obtenerPivote(int[] datos, int i, int j) {
        return medianaDeTres(datos, i, j);
    }

    // Devuelve la posición (i, medio o j) del valor que queda en el medio de los tres
    static int medianaDeTres(int[] datos, int i, int j) {
        int medio = (i + j) / 2;
        int a = datos[i];
        int b = datos[medio];
        int c = datos[j];
        if ((a <= b && b <= c) || (c <= b && b <= a)) return medio;
        if ((b <= a && a <= c) || (c <= a && a <= b)) return i;
        return j;
    }
}

// Mediana de tres arreglada para que el pivote sea siempre mayor que el mínimo
class PivoteMedianaArreglada extends TClasificadorImpl {
    @Override
    protected int obtenerPivote(int[] datos, int i, int j) {
        int medio = (i + j) / 2;
        int menor = Math.min(datos[i], Math.min(datos[medio], datos[j]));
        int mayor = Math.max(datos[i], Math.max(datos[medio], datos[j]));

        // si los tres son iguales, la mediana no sirve: usamos nuestra estrategia
        // (el mayor de los dos primeros distintos, o -1 si son todos iguales)
        if (menor == mayor) {
            return super.obtenerPivote(datos, i, j);
        }

        int x = PivoteMedianaDeTres.medianaDeTres(datos, i, j);
        if (datos[x] == menor) {
            // la mediana es el mínimo: usamos el mayor de los tres
            if (datos[i] == mayor) x = i;
            else if (datos[medio] == mayor) x = medio;
            else x = j;
        }
        // el quicksort solo acepta índices menores que j:
        // si el pivote quedó en j, lo pasamos a la posición del medio
        if (x == j) {
            intercambiar(datos, medio, j);
            x = medio;
        }
        return x;
    }
}
```

### Salida en la terminal

```
Mayor de los dos primeros distintos:
  terminó, resultado: [1, 1, 1, 2]
Mediana de tres:
  SE ROMPE: java.lang.ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 4
Mediana de tres ARREGLADA:
  terminó, resultado: [1, 1, 1, 2]
```

## 3. Memoria
 
> Los volúmenes de datos son grandes: hasta 50.000.000 de elementos en un arreglo. Ejecuten siempre con más memoria y más pila que las que da Java por defecto: `java -Xmx4g -Xss512m …`
>
> En IntelliJ, eso va en Run → Edit Configurations → VM options. Si su máquina tiene poca memoria, avisen: se puede acordar un tamaño menor.
>
> Si el quicksort recursa muy profundo, Java tira `StackOverflowError`. Agrandar la pila puede ayudar, pero tienen que explicar por qué pasó. Un desborde de pila también es un resultado.
 
- `-Xmx4g`: hasta 4 GB de memoria. Un arreglo de 50 millones de `int` ocupa unos 200 MB, y el Medidor hace copias.
- `-Xss512m`: más pila, que es lo que gasta cada llamada recursiva del quicksort.

Usamos **VS Code** en vez de IntelliJ. Ahí las VM options van en `.vscode/launch.json`, en el campo `vmArgs`:
 
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Main",
            "request": "launch",
            "mainClass": "ucu.edu.aed.tda.clasificacion.Main",
            "vmArgs": "-Xmx4g -Xss512m"
        },
        {
            "type": "java",
            "name": "DemoPivote",
            "request": "launch",
            "mainClass": "ucu.edu.aed.tda.clasificacion.DemoPivote",
            "vmArgs": "-Xmx4g -Xss512m"
        }
    ]
}
```
 
Para comprobar que se aplicó, el `Main` imprime la memoria máxima al empezar:
 
```
Memoria máxima: 4096 MB
Midiendo con n = 10000
  Inserción directa: 10.508 ms
  Shellsort:         0.763 ms
  Burbuja:           84.9905 ms
  Quicksort:         0.3448 ms
  Selección directa: 25.4597 ms
  Heapsort:          0.8343 ms

```
### ¿Por qué podría desbordarse la pila?

Con los datos de la consigna, no se desborda porque cada llamada recursiva elimina al menos un valor distinto, limitando la profundidad a 6.440.

En cambio, con datos ordenados, el pivote genera particiones muy desequilibradas, de un solo elemento. La recursión puede alcanzar profundidad `n`, provocando un `StackOverflowError` y un rendimiento de **O(n²)**.

Aumentar la pila con `-Xss512m` permite terminar, pero no soluciona el problema del pivote.

---

## 4. Cómo medir
 
> 1. **Siempre con `Medidor`.** Cada algoritmo recibe una copia de los mismos datos. Hay corridas de calentamiento, para que la JVM compile el código, y se informa la mediana de al menos 3 corridas.
> 2. **Varios tamaños.** Midan una serie que se duplica (por ejemplo 10.000, 20.000, 40.000, … hasta el tamaño de la consigna). Un solo tamaño no muestra cómo crece el costo.
> 3. **Regla de corte.** Si una corrida tarda más de 60 segundos, no sigan agrandando ese algoritmo. Anoten «no terminó» y estimen el tiempo para el tamaño completo a partir de los tamaños que sí midieron. Expliquen cómo hicieron la estimación.
> 4. **Todos contra todos.** Comparen los seis algoritmos, al menos en los tamaños en que terminan. Burbuja y heapsort son obligatorios en todas las consignas: burbuja es la línea base y heapsort es el «seguro» contra el que tiene que competir su elección.
> 5. **Verificar.** `Medidor` comprueba que el arreglo quede ordenado y con los mismos elementos. Si un algoritmo es rápido pero ordena mal, no cuenta. Si lo que miden no ordena todo el arreglo (por ejemplo, si solo busca los k mayores), usen `Medidor.medirNanos(algoritmo, datos, repeticiones, calentamientos, false)` y verifiquen el resultado por su cuenta.
 
| Punto | Cómo lo cumplimos |
|---|---|
| 1. Siempre con Medidor | Usamos `Medidor.medianaMs(algoritmo, datos, 3, calentamientos)`, con copias de los mismos datos, 2 calentamientos (1 desde 5.000.000) y la mediana de 3 corridas. |
| 2. Varios tamaños | `n` comienza en 10.000 y se duplica hasta 50.000.000. |
| 3. Regla de corte | Estimamos el tiempo antes de ejecutar cada tamaño para evitar esperas excesivas. |
| 4. Todos contra todos | El `main` mide los seis algoritmos, incluidos burbuja y heapsort. |
| 5. Verificar | `medianaMs` comprueba que el arreglo quede ordenado y conserve los mismos elementos. Para top-k vamos a usar  `medirNanos(..., false)`, porque no ordena todo el arreglo. |

### Regla de corte y estimación

El Medidor realiza 5 corridas por tamaño (2 calentamientos y 3 mediciones; desde 5.000.000, 4 corridas con 1 calentamiento).

- **Algoritmos cuadráticos** (inserción, burbuja y selección): al duplicar `n`, el tiempo se multiplica aproximadamente por 4.
- **Algoritmos O(n log n)** (shellsort, quicksort y heapsort): al duplicar `n`, el tiempo aumenta un poco más del doble (usamos un factor de 2,1).

Si la estimación para el siguiente tamaño supera los 60 segundos, no lo ejecutamos y registramos **«no terminó»**.

Para estimar el tiempo de los 50.000.000 elementos, usamos el último tamaño medido `n` y su tiempo `t(n)`:

- **Cuadráticos:** `t(N) = t(n) · (N / n)²`
- **O(n log n):** `t(N) = t(n) · (N / n) · (log N / log n)`

### Código (`medirAlgoritmo`, en `Main.java`)
 
```java

    // Mide cada algoritmo con tamaños que se duplican desde 10.000 hasta 50.000.000.
    // El Medidor trabaja con copias de los mismos datos, realiza calentamientos
    // y devuelve la mediana de 3 mediciones, verificando que el arreglo quede ordenado.
    //
    // Regla de corte: estimamos el tiempo antes de ejecutar cada tamaño.
    // Al duplicar n, los algoritmos cuadráticos tardan unas 4 veces más
    // y los O(n log n), un poco más del doble.
    // Si la estimación supera los 60 segundos, no ejecutamos ese tamaño:
    // anotamos "no terminó" y estimamos el tiempo para 50.000.000
    // usando el último tamaño que sí medimos.

    static void medirAlgoritmo(String nombre, Consumer<int[]> algoritmo, boolean cuadratico, int[] ventas) {
        System.out.println("\n" + nombre + ":");
 
        int ultimoN = 0;          // último tamaño que medimos
        double ultimoMs = 0;      // lo que tardó ese tamaño
 
        int n = 10_000;
        while (true) {
            if (n > ventas.length) {
                n = ventas.length;          // el último paso es el tamaño completo
            }
 
            // ¿Cuánto tardaría este tamaño? (si ya medimos alguno antes)
            if (ultimoN > 0) {
                double factor = cuadratico ? 4 : 2.1;
                double prevision = ultimoMs * factor * ((double) n / ultimoN / 2);
                if (prevision > CORTE_MS) {
                    System.out.println("  n = " + n + "  -> no terminó (se estima que pasa los 60 s)");
                    break;
                }
            }
 
            int[] datos = Arrays.copyOf(ventas, n);        // los primeros n productos
            int calentamientos = (n >= 5_000_000) ? 1 : 2;  // con n grande uno alcanza
            double ms = Medidor.medianaMs(algoritmo, datos, 3, calentamientos);
            System.out.println("  n = " + n + "  -> " + ms + " ms");
 
            ultimoN = n;
            ultimoMs = ms;
 
            if (n == ventas.length) {
                return;                     // llegamos al tamaño completo, no hay que estimar
            }
            n = n * 2;
        }
 
        // Estimación para el tamaño completo a partir del último tamaño medido:
        //   cuadrático:  t(N) = t(n) * (N/n)²
        //   n log n:     t(N) = t(n) * (N/n) * (log N / log n)
        double veces = (double) ventas.length / ultimoN;
        double estimadoMs;
        if (cuadratico) {
            estimadoMs = ultimoMs * veces * veces;
        } else {
            estimadoMs = ultimoMs * veces * (Math.log(ventas.length) / Math.log(ultimoN));
        }
        System.out.println("  ESTIMADO para n = " + ventas.length + ": " + Math.round(estimadoMs) + " ms"
                + " (" + Math.round(estimadoMs / 3_600_000.0 * 10) / 10.0 + " horas)");
    }
```
 
### Salida en la terminal
 
```
Inserción directa:
  n = 10000  -> 14.7045 ms
  n = 20000  -> 50.0166 ms
  n = 40000  -> 204.3931 ms
  n = 80000  -> 877.8277 ms
  n = 160000  -> 3421.751 ms
  n = 320000  -> 8890.2925 ms
  n = 640000  -> 50270.9324 ms
  n = 1280000  -> no terminó (se estima que pasa los 60 s)
  ESTIMADO para n = 50000000: 306829421 ms (85.2 horas)

Shellsort:
  n = 10000  -> 0.3705 ms
  n = 20000  -> 0.7596 ms
  n = 40000  -> 1.6303 ms
  n = 80000  -> 3.1189 ms
  n = 160000  -> 8.6657 ms
  n = 320000  -> 14.1278 ms
  n = 640000  -> 29.536 ms
  n = 1280000  -> 67.8065 ms
  n = 2560000  -> 137.0171 ms
  n = 5120000  -> 324.9217 ms
  n = 10240000  -> 663.6807 ms
  n = 20480000  -> 1384.7356 ms
  n = 40960000  -> 3108.102 ms
  n = 50000000  -> 3104.7718 ms

Burbuja:
  n = 10000  -> 53.0659 ms
  n = 20000  -> 461.3091 ms
  n = 40000  -> 2008.5789 ms
  n = 80000  -> 9309.5839 ms
  n = 160000  -> 32157.607 ms
  n = 320000  -> no terminó (se estima que pasa los 60 s)
  ESTIMADO para n = 50000000: 3140391309 ms (872.3 horas)

Quicksort:
  n = 10000  -> 0.4615 ms
  n = 20000  -> 0.6658 ms
  n = 40000  -> 0.3964 ms
  n = 80000  -> 0.9622 ms
  n = 160000  -> 1.836 ms
  n = 320000  -> 3.619 ms
  n = 640000  -> 7.4941 ms
  n = 1280000  -> 13.5985 ms
  n = 2560000  -> 29.2386 ms
  n = 5120000  -> 59.0402 ms
  n = 10240000  -> 118.7542 ms
  n = 20480000  -> 229.1904 ms
  n = 40960000  -> 472.4521 ms
  n = 50000000  -> 576.8153 ms

Selección directa:
  n = 10000  -> 22.3443 ms
  n = 20000  -> 90.2411 ms
  n = 40000  -> 343.7087 ms
  n = 80000  -> 1172.5239 ms
  n = 160000  -> 4506.9043 ms
  n = 320000  -> 17032.4119 ms
  n = 640000  -> no terminó (se estima que pasa los 60 s)
  ESTIMADO para n = 50000000: 415830369 ms (115.5 horas)

Heapsort:
  n = 10000  -> 0.3468 ms
  n = 20000  -> 0.7384 ms
  n = 40000  -> 1.9615 ms
  n = 80000  -> 2.9856 ms
  n = 160000  -> 7.9826 ms
  n = 320000  -> 12.8152 ms
  n = 640000  -> 23.3828 ms
  n = 1280000  -> 48.9651 ms
  n = 2560000  -> 108.0004 ms
  n = 5120000  -> 251.3774 ms
  n = 10240000  -> 500.8123 ms
  n = 20480000  -> 1083.2924 ms
  n = 40960000  -> 2093.534 ms
  n = 50000000  -> 2559.4211 ms
```
### Resumen con 50.000.000 productos
 
| Algoritmo | Tiempo para 50.000.000 | ¿Medido o estimado? |
|---|---|---|
| Quicksort | 0,58 segundos | medido |
| Heapsort | 2,56 segundos | medido |
| Shellsort | 3,10 segundos | medido |
| Inserción directa | 3,6 días | estimado |
| Selección directa | 4,8 días | estimado |
| Burbuja | 36 días | estimado |
 
**Cómo pasamos de milisegundos a segundos y días:**
- Segundos: milisegundos ÷ 1.000. Ejemplo: 576,8 ms ÷ 1.000 = 0,58 s.
- Horas: el programa ya las imprime (milisegundos ÷ 3.600.000).
- Días: horas ÷ 24. Ejemplo: burbuja 872,3 horas ÷ 24 = 36 días.

### Conclusión
 
Con 50 millones de productos, **quicksort ordena todo en poco más de medio segundo**. Heapsort y shellsort también terminan, en unos 3 segundos. Los otros tres algoritmos (inserción, selección y burbuja) **tardarían días**: no sirven para este volumen de datos.
 
La diferencia está en cómo crece el tiempo cuando hay más datos. Si duplicamos la cantidad de productos, quicksort, heapsort y shellsort tardan más o menos el doble. Inserción, selección y burbuja tardan **cuatro veces más**. Con pocos datos casi no se nota, pero con 50 millones la diferencia pasa de segundos a días.
 
**Quicksort es el más rápido: unas 4 veces más que heapsort**, que es el algoritmo «seguro» contra el que tiene que competir.
 