package ucu.edu.aed.tda.clasificacion.datos;

import java.util.Random;

/**
 * Consigna 5 · Percentiles en una estación meteorológica.
 *
 * Genera lecturas de temperatura, en décimas de grado (183 = 18,3 °C), en el
 * orden en que se tomaron, repartidas en uno o más días. Cada día sigue un
 * ciclo (mínimo de madrugada, máximo a media tarde), con ruido del sensor y
 * un frente frío que entra en algún momento del día. Hay muchos valores
 * repetidos: unas pocas centenas de valores distintos.
 */
public final class GeneradorLecturas {

    private GeneradorLecturas() {
    }

    /** Lecturas de un solo día. */
    public static int[] generarLecturas(int n, long semilla) {
        return generarLecturas(n, 1, semilla);
    }

    /** n lecturas repartidas en partes iguales entre {@code dias} días. */
    public static int[] generarLecturas(int n, int dias, long semilla) {
        Random rnd = new Random(semilla);
        double[] frente = new double[dias]; // hora (fracción del día) del frente frío de cada día
        for (int d = 0; d < dias; d++) {
            frente[d] = 0.3 + 0.5 * rnd.nextDouble();
        }
        int[] lecturas = new int[n];
        for (int i = 0; i < n; i++) {
            double tiempo = (double) i * dias / n;
            int dia = (int) tiempo;
            double t = tiempo - dia;
            double ciclo = 150 + 70 * Math.sin(2 * Math.PI * (t - 0.375));
            double caida = t > frente[dia] ? -40 : 0;
            double ruido = 6 * rnd.nextGaussian();
            lecturas[i] = (int) Math.round(ciclo + caida + ruido);
        }
        return lecturas;
    }
}
