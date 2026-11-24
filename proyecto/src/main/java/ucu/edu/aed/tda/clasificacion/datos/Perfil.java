package ucu.edu.aed.tda.clasificacion.datos;

/** Perfiles de entrada para la consigna 6. */
public enum Perfil {
    /** Permutación aleatoria. */
    ALEATORIO,
    /** Ya ordenado de menor a mayor. */
    ASCENDENTE,
    /** Ordenado de mayor a menor. */
    DESCENDENTE,
    /** Sube hasta la mitad y después baja. */
    TUBO_DE_ORGANO,
    /** Solo 10 valores distintos, muy repetidos. */
    POCOS_DISTINTOS,
    /** Ordenado salvo el último 1 %, que está al azar (padrones agregados al final). */
    ORDENADO_CON_COLA
}
