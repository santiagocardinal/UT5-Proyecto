package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Random;

/**
 * Consigna 4 · Reorganización de un depósito automatizado.
 *
 * La posición i del arreglo es el lugar i de la estantería; el valor es el
 * vencimiento del pallet que está ahí, en días a partir de hoy (0 a 1094).
 * Los pallets están ubicados al azar.
 */
public final class GeneradorDeposito {

    public static final int HORIZONTE_DIAS = 1095;

    private GeneradorDeposito() {
    }

    public static int[] generarVencimientos(int n, long semilla) {
        Random rnd = new Random(semilla);
        int[] vencimientos = new int[n];
        for (int i = 0; i < n; i++) {
            vencimientos[i] = rnd.nextInt(HORIZONTE_DIAS);
        }
        return vencimientos;
    }
}
