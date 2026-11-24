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
