[![Santiago Cardinal](https://img.shields.io/badge/GitHub-Santiago_Cardinal-8E412E?logo=github&logoColor=black)](https://github.com/santiagocardinal)
[![Sebastián Chapper](https://img.shields.io/badge/GitHub-Sebastián_Chapper-BA6F4D?logo=github&logoColor=black)](https://github.com/sebachapper)
[![Nicolás Leyton](https://img.shields.io/badge/GitHub-Nicolás_Leyton-E6CEBC?logo=github&logoColor=black)](https://github.com/Nicosley)
[![Hernan López](https://img.shields.io/badge/GitHub-Hernan_López-A2A182?logo=github&logoColor=black)](https://github.com/HL0pez)
[![Rodrigo Montiel](https://img.shields.io/badge/GitHub-Rodrigo_Montiel-687259?logo=github&logoColor=black)](https://github.com/rodrigomontiel-44)
[![Luján Uhalde](https://img.shields.io/badge/GitHub-Luján_Uhalde-F4ECE2?logo=github&logoColor=black)](https://github.com/Lujan448)

---
# Consigna 2 · Los más vendidos de la hora

> Algoritmos y Estructuras de Datos · Trabajo final de clasificación

## El escenario

Un marketplace en línea tiene 50.000.000 de publicaciones activas. En la portada hay un recuadro, «Lo más vendido ahora», que muestra las 100 publicaciones con más unidades vendidas en la última hora, del primero al centésimo.

El recuadro se recalcula cada minuto con los datos de la última hora, y el cálculo compite por el servidor con todo lo demás que hace el sitio. Además, marketing ya pidió variantes: los 10 más vendidos para la aplicación del celular y los 1.000 más vendidos para un informe interno.

Las ventas tienen cola larga: la mayoría de los productos no vendió nada o vendió una unidad, y unos pocos vendieron miles.

## Los datos

```java
// posición i = producto i; valor = unidades vendidas en la última hora
int[] ventas = GeneradorVentas.generarVentas(50_000_000, 2026);
```

En `datos/ventas.txt` están las 50.000.000 de ventas ya generadas, una por línea: la línea `i` corresponde a la publicación `i`. Se leen con `ArchivoDatos.leerEnteros(Path.of("datos/ventas.txt"))`.

Trabajen con k = 10, 100, 1.000, 10.000 y 100.000, donde k es la cantidad de productos del ranking.

## Parte A · Modelar

1. ¿Qué se ordena y por qué clave? ¿Qué se necesita como resultado: el arreglo entero ordenado o algo menos?
2. Exploren los datos: ¿cuántos productos vendieron 0 unidades? ¿Cuál es el máximo? ¿Cuántos valores distintos hay? Hagan un histograma.
3. ¿Influye que haya tantos valores repetidos en el comportamiento de cada algoritmo? ¿En cuáles sí y en cuáles no?

## Parte B · Predecir

4. Si se ordena el arreglo completo, ¿cuánto cuesta en función de n? Si solo se necesitan los k mayores, ¿cuánto debería costar como mínimo? ¿Se puede leer cada producto una sola vez?
5. Repasen cómo funciona cada uno de los seis algoritmos. ¿Alguno produce los elementos de a uno, del mayor al menor, de modo que se pueda frenar a la mitad?
6. Escriban su hipótesis: ¿qué conviene para k = 100? ¿Y para k = 100.000? ¿Esperan que la respuesta dependa de k?

## Parte C · Medir

7. Midan el tiempo de ordenar el arreglo completo con los algoritmos O(n log n). Para los cuadráticos, midan tamaños menores y extrapolen a 50.000.000.
8. Implementen al menos dos formas de obtener los k mayores sin ordenar todo. Al menos una tiene que reutilizar piezas de su implementación de `TClasificador`. Verifiquen que el resultado coincide con los últimos k del arreglo ordenado. Para medirlas, usen `Medidor.medirNanos(…, false)`, porque estos métodos no dejan todo el arreglo ordenado.
9. Midan cada forma para cada valor de k y armen un gráfico del tiempo en función de k, en escala logarítmica, que incluya como líneas horizontales los tiempos de ordenar todo.

## Parte D · Analizar

10. Escriban el orden de complejidad de cada método en función de n y de k. ¿Los tiempos medidos lo confirman?
11. ¿Para qué valor de k deja de convenir cada método y pasa a convenir ordenar todo? ¿Por qué?
12. Si las ventas no tuvieran cola larga, sino que cada producto vendiera una cantidad distinta al azar, ¿cambiarían los tiempos? Pruébenlo con `GeneradorPagos.generarLote(50_000_000, 0, 2026).claves()`, que genera claves distintas al azar.

## Parte E · Recomendar

13. El recuadro se recalcula cada minuto. ¿Qué método recomiendan para k = 100 y cuánto tiempo de servidor por día ahorra respecto de ordenar todo con heapsort?
14. ¿Y si las ventas llegaran de a una, en tiempo real, y el ranking tuviera que estar siempre actualizado? Describan, sin implementarlo, qué estructura mantendrían.

----------
## A1. ¿Qué se ordena y por qué clave? ¿Qué se necesita como resultado?

Se ordena un arreglo de 50.000.000 de enteros. La **posición** `i` es la publicación `i`,y el **valor** es cuántas unidades vendió en la última hora. La **clave** de ordenamiento son las unidades vendidas, porque queremos los productos que más vendieron.

**No hace falta el arreglo entero ordenado.** El recuadro muestra solo los 100 más vendidos (10 en el celular y 1.000 en el informe interno), o sea los **k mayores, ordenados del primero al último**. El orden de los otros 49.999.900 productos no nos importa.Por eso tiene sentido buscar formas de sacar solo los k mayores sin ordenar todo.

Un detalle: si ordenamos el arreglo de valores, perdemos qué producto era cada uno, porque el valor se mueve de posición. En el sitio real habría que guardar también el número de publicación junto con las ventas. Para medir los algoritmos alcanza con los valores.

### Restricciones que impone la situación

- **Memoria:** 50 millones de `int` ocupan unos 200 MB, y el Medidor trabaja con copias.
  Los seis algoritmos ordenan dentro del mismo arreglo, sin crear otro del mismo tamaño.
  Quicksort usa además la pila para la recursión; por eso corremos con `-Xmx4g -Xss512m`.

- **Estabilidad:** no hace falta. Dos productos con las mismas ventas valen lo mismo para el ranking, y da igual cuál quede primero. Si se quisiera un desempate fijo, se podría usar el número de publicación.
- **Peor caso:** el ranking se recalcula cada minuto y compite por el servidor con el resto del sitio, así que importa que el tiempo sea predecible y no solo bueno en promedio.
  Quicksort en el peor caso es O(n²) (por ejemplo, con datos ya ordenados y nuestro pivote).
  Heapsort es siempre O(n log n), por eso es el «seguro».
- **Costo de mover:** mover un `int` es barato, pero los algoritmos que mueven muchísimo igual pierden: inserción y burbuja hacen 18 millones de movimientos con solo 10.000 productos. En el sitio real se moverían pares (publicación, ventas), que siguen siendo baratos.

## A3. ¿Influye que haya tantos valores repetidos?

En los 50 millones de productos hay solo **6.440 valores distintos**, y el 72 % vendió 0 o 1: casi todo son repetidos. Para ver si eso cambia algo, ordenamos 10.000 elementos de dos formas y contamos las operaciones con los contadores de nuestro clasificador:

- **Con repetidos:** los primeros 10.000 productos de las ventas.
- **Sin repetidos:** 10.000 claves distintas al azar, de `GeneradorPagos.generarLote(10_000, 0, 2026).claves()`.

| Algoritmo | Comparaciones con repetidos | Comparaciones sin repetidos | Movimientos con repetidos | Movimientos sin repetidos |
|---|---|---|---|---|
| Inserción directa | 18.204.013 | 24.847.582 | 18.194.016 | 24.837.589 |
| Shellsort | 143.721 | 267.901 | 26.347 | 153.015 |
| Burbuja | 49.995.000 | 49.995.000 | 18.194.016 | 24.837.589 |
| Quicksort | 70.481 | 253.496 | 5.867 | 37.445 |
| Selección directa | 49.995.000 | 49.995.000 | 9.985 | 9.991 |
| Heapsort | 157.066 | 235.341 | 77.892 | 124.249 |

**¿En cuáles no influye?**
- **Burbuja y selección directa:** hacen exactamente las mismas comparaciones (49.995.000 = n·(n−1)/2) con repetidos o sin ellos, porque siempre comparan todos los pares que les tocan. Burbuja sí mueve menos, porque dos iguales no se intercambian, pero sigue comparando todo.

**¿En cuáles sí influye?**
- **Inserción directa:** un 27 % menos de operaciones. Un elemento deja de moverse cuando encuentra uno menor o igual, y con muchos iguales frena antes. Igual sigue siendo O(n²).
- **Heapsort:** un 33 % menos de comparaciones. Al bajar un elemento en el heap, si es igual al hijo se queda donde está. Sigue siendo O(n log n).
- **Shellsort:** casi la mitad de comparaciones y casi 6 veces menos movimientos.
- **Quicksort:** es el que más gana, con 3,6 veces menos comparaciones y 6 veces menos movimientos.
  Con nuestro pivote («el mayor de los dos primeros distintos»), cuando un rango tiene todos los valores iguales `obtenerPivote` devuelve −1 y ese rango no se sigue partiendo. Con tantos ceros y unos, hay rangos enormes que se resuelven de una sola vez.

**Por qué pasa:** cuando dos valores son iguales, `menorQue` da falso y el elemento no se mueve.
Eso le ahorra trabajo a todos los algoritmos menos a burbuja y selección, que comparan todos los pares igual.

>Los repetidos también son un riesgo para quicksort. Con un pivote mal elegido (la mediana de tres sin arreglar), el pivote puede ser el mínimo del rango y el quicksort no termina, como mostramos en el contrato del pivote.