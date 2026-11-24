package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Random;

/**
 * Consigna 2 · Ranking "Top K" de un e-commerce.
 *
 * Genera las unidades vendidas en la última hora por cada producto del
 * catálogo. La distribución es de cola larga (Pareto): la mayoría de los
 * productos vende 0 o 1 unidad y unos pocos venden miles o más
 * (el máximo está acotado a 1.000.000). El producto i es la
 * posición i del arreglo.
 */
public final class GeneradorVentas {

    private static final double ALFA = 1.1;
    private static final double SIN_VENTAS = 0.40;
    private static final int TOPE = 1_000_000;

    private GeneradorVentas() {
    }

    public static int[] generarVentas(int n, long semilla) {
        Random rnd = new Random(semilla);
        int[] ventas = new int[n];
        for (int i = 0; i < n; i++) {
            if (rnd.nextDouble() < SIN_VENTAS) {
                ventas[i] = 0;
            } else {
                double u = 1.0 - rnd.nextDouble(); // (0, 1]
                double x = Math.floor(Math.pow(u, -1.0 / ALFA));
                ventas[i] = (int) Math.min(TOPE, x);
            }
        }
        return ventas;
    }
}
