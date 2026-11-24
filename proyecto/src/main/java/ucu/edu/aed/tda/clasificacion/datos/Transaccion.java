package ucu.edu.aed.tda.clasificacion.datos;

/**
 * Movimiento bancario tal como llega a la casa central.
 *
 * @param id        orden de llegada (0 = la primera que llegó)
 * @param sucursal  sucursal que la originó
 * @param timestamp segundos desde la medianoche en que ocurrió
 * @param montoCentesimos monto en centésimos (puede ser negativo)
 */
public record Transaccion(int id, int sucursal, int timestamp, long montoCentesimos) {
}
