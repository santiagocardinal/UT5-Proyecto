package ucu.edu.aed.tda.clasificacion.datos;

/**
 * Lote de cobros a revisar.
 *
 * @param claves              hash de (tarjeta, monto, minuto) de cada cobro
 * @param registrosSobrantes  respuesta esperada: cuántos cobros sobran
 *                            (n menos la cantidad de claves distintas)
 */
public record LotePagos(int[] claves, int registrosSobrantes) {
}
