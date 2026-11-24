package ucu.edu.aed.tda.clasificacion.datos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lectura y escritura de los archivos de la carpeta {@code datos/} de cada
 * consigna.
 *
 * Formatos:
 * <ul>
 *   <li>Enteros: un número por línea, sin encabezado.</li>
 *   <li>Transacciones: CSV con encabezado {@code id,sucursal,timestamp,montoCentesimos}.</li>
 * </ul>
 *
 * Ejemplo: {@code int[] ventas = ArchivoDatos.leerEnteros(Path.of("datos/ventas.txt"));}
 */
public final class ArchivoDatos {

    private static final String ENCABEZADO_TRANSACCIONES = "id,sucursal,timestamp,montoCentesimos";

    private ArchivoDatos() {
    }

    /** Lee un archivo con un entero por línea. Ignora líneas vacías. */
    public static int[] leerEnteros(Path archivo) {
        try (BufferedReader in = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            int[] datos = new int[1 << 20];
            int n = 0;
            String linea;
            while ((linea = in.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                if (n == datos.length) {
                    datos = Arrays.copyOf(datos, datos.length * 2);
                }
                datos[n++] = Integer.parseInt(linea.trim());
            }
            return Arrays.copyOf(datos, n);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + archivo, e);
        }
    }

    /** Escribe un entero por línea. */
    public static void escribirEnteros(Path archivo, int[] datos) {
        try (BufferedWriter out = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            for (int x : datos) {
                out.write(Integer.toString(x));
                out.write('\n');
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir " + archivo, e);
        }
    }

    /** Lee el CSV de transacciones de la consigna 1, en orden de llegada. */
    public static Transaccion[] leerTransacciones(Path archivo) {
        try (BufferedReader in = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String encabezado = in.readLine();
            if (!ENCABEZADO_TRANSACCIONES.equals(encabezado)) {
                throw new IllegalArgumentException("Encabezado inesperado en " + archivo + ": " + encabezado);
            }
            List<Transaccion> lista = new ArrayList<>();
            String linea;
            while ((linea = in.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] c = linea.split(",");
                lista.add(new Transaccion(Integer.parseInt(c[0]), Integer.parseInt(c[1]),
                        Integer.parseInt(c[2]), Long.parseLong(c[3])));
            }
            return lista.toArray(new Transaccion[0]);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + archivo, e);
        }
    }

    /** Escribe el CSV de transacciones de la consigna 1. */
    public static void escribirTransacciones(Path archivo, Transaccion[] transacciones) {
        try (BufferedWriter out = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            out.write(ENCABEZADO_TRANSACCIONES);
            out.write('\n');
            for (Transaccion t : transacciones) {
                out.write(t.id() + "," + t.sucursal() + "," + t.timestamp() + "," + t.montoCentesimos());
                out.write('\n');
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir " + archivo, e);
        }
    }
}
