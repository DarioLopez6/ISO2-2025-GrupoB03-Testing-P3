package Dominio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RecomendadorActividadesTester {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java RecomendadorActividadesTester <ruta_fichero_txt>");
            System.exit(1);
        }

        String rutaFichero = args[0];

        try {
            ejecutarPruebasDesdeFichero(rutaFichero);
        } catch (IOException e) {
            System.err.println("Error leyendo el fichero de casos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lee el fichero de texto línea a línea y ejecuta cada caso de prueba.
     */
    public static void ejecutarPruebasDesdeFichero(String rutaFichero) throws IOException {
        int total = 0;
        int ok = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Saltar líneas vacías y comentarios
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                total++;
                boolean resultado = ejecutarCaso(linea);
                if (resultado) {
                    ok++;
                }
            }
        }

        System.out.println("=================================");
        System.out.println("Resumen general: " + ok + " / " + total + " casos OK");
        System.out.println("=================================");
    }

    /**
     * Ejecuta un caso de prueba a partir de una línea del .txt
     * Formato:
     * ID;METODO;temp;hum;hayPrec;estaNub;enPlenas;tuvoEnf;aforoAct;aforoMax;TIPORESULTADO;MENSAJE_ESPERADO
     */
    private static boolean ejecutarCaso(String linea) {
        String[] partes = linea.split(";", -1); // -1 para conservar campos vacíos

        if (partes.length < 11) {
            System.out.println("[ERROR FORMATO] Línea con menos de 11 campos: " + linea);
            return false;
        }

        String id = partes[0].trim();
        String metodo = partes[1].trim();

        try {
            double temperatura = Double.parseDouble(partes[2].trim());
            double humedad = Double.parseDouble(partes[3].trim());
            boolean hayPrec = Boolean.parseBoolean(partes[4].trim());
            boolean estaNub = Boolean.parseBoolean(partes[5].trim());
            boolean enPlenas = Boolean.parseBoolean(partes[6].trim());
            boolean tuvoEnf = Boolean.parseBoolean(partes[7].trim());
            int aforoAct = Integer.parseInt(partes[8].trim());
            int aforoMax = Integer.parseInt(partes[9].trim());
            String tipoResultado = partes[10].trim();
            String mensajeEsperado = (partes.length >= 12) ? partes[11].trim() : "";

            System.out.println("---- Ejecutando " + id + " (método " + metodo + ") ----");

            switch (metodo) {
                case "A":
                    return probarConstructor(
                            id, temperatura, humedad, hayPrec, estaNub,
                            enPlenas, tuvoEnf, aforoAct, aforoMax, tipoResultado
                    );

                case "B":
                    return probarRecomendar(
                            id, temperatura, humedad, hayPrec, estaNub,
                            enPlenas, tuvoEnf, aforoAct, aforoMax, tipoResultado, mensajeEsperado
                    );

                default:
                    System.out.println("[ERROR] Método desconocido en el caso " + id + ": " + metodo);
                    return false;
            }

        } catch (NumberFormatException e) {
            System.out.println("[ERROR PARSING] Caso " + id + ": " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    //   MÉTODO A: pruebas del constructor
    // =========================================================
    private static boolean probarConstructor(
            String id,
            double temperatura,
            double humedad,
            boolean hayPrec,
            boolean estaNub,
            boolean enPlenas,
            boolean tuvoEnf,
            int aforoAct,
            int aforoMax,
            String tipoResultado
    ) {
        try {
            RecomendadorActividades r = new RecomendadorActividades(temperatura, humedad, hayPrec, estaNub,enPlenas, tuvoEnf, aforoAct, aforoMax);

            // Si no salta excepción:
            if ("OK".equalsIgnoreCase(tipoResultado)) {
                System.out.println("[OK] " + id + ": objeto creado correctamente (sin excepción).");
                return true;
            } else {
                System.out.println("[FALLO] " + id + ": se esperaba excepción (" + tipoResultado
                        + ") pero el constructor no lanzó nada.");
                return false;
            }

        } catch (ActividadInvalidaException e) {
            String msg = normalizar(e.getMessage());

            if ("EXC_AFORO_NEG".equalsIgnoreCase(tipoResultado)
                    && "Los aforos no pueden ser negativos.".equals(msg)) {
                System.out.println("[OK] " + id + ": excepción de aforo negativo correcta.");
                return true;
            }

            if ("EXC_HUMEDAD".equalsIgnoreCase(tipoResultado)
                    && "La humedad debe estar entre 0 y 100.".equals(msg)) {
                System.out.println("[OK] " + id + ": excepción de humedad correcta.");
                return true;
            }

            System.out.println("[FALLO] " + id + ": excepción inesperada: \"" + e.getMessage()
                    + "\" (tipo esperado: " + tipoResultado + ")");
            return false;
        }
    }

    // =========================================================
    //   MÉTODO B: pruebas de recomendar()
    // =========================================================
    private static boolean probarRecomendar(
            String id,
            double temperatura,
            double humedad,
            boolean hayPrec,
            boolean estaNub,
            boolean enPlenas,
            boolean tuvoEnf,
            int aforoAct,
            int aforoMax,
            String tipoResultado,
            String mensajeEsperado
    ) {
        try {
            RecomendadorActividades r = new RecomendadorActividades(
                    temperatura, humedad, hayPrec, estaNub,
                    enPlenas, tuvoEnf, aforoAct, aforoMax
            );

            // Si esperábamos excepción pero no ha saltado:
            if (!"OK".equalsIgnoreCase(tipoResultado)) {
                System.out.println("[FALLO] " + id + ": se esperaba excepción (" + tipoResultado
                        + ") pero el constructor no lanzó nada.");
                return false;
            }

            String obtenido = r.recomendar();
            String esperadoNorm = normalizar(mensajeEsperado);
            String obtenidoNorm = normalizar(obtenido);

            if (esperadoNorm.equals(obtenidoNorm)) {
                System.out.println("[OK] " + id + ": mensaje de recomendar() correcto.");
                return true;
            } else {
                System.out.println("[FALLO] " + id + ": mensaje distinto.");
                System.out.println("   Esperado: \"" + mensajeEsperado + "\"");
                System.out.println("   Obtenido: \"" + obtenido + "\"");
                return false;
            }

        } catch (ActividadInvalidaException e) {
            String msg = normalizar(e.getMessage());

            // Solo llegamos aquí si el constructor lanza excepción
            if ("EXC_AFORO_NEG".equalsIgnoreCase(tipoResultado)
                    && "Los aforos no pueden ser negativos.".equals(msg)) {
                System.out.println("[OK] " + id + ": excepción de aforo negativo correcta.");
                return true;
            }

            if ("EXC_HUMEDAD".equalsIgnoreCase(tipoResultado)
                    && "La humedad debe estar entre 0 y 100.".equals(msg)) {
                System.out.println("[OK] " + id + ": excepción de humedad correcta.");
                return true;
            }

            if ("OK".equalsIgnoreCase(tipoResultado)) {
                System.out.println("[FALLO] " + id + ": se esperaba ejecución correcta, pero se lanzó excepción: \""
                        + e.getMessage() + "\"");
            } else {
                System.out.println("[FALLO] " + id + ": excepción inesperada: \"" + e.getMessage()
                        + "\" (tipo esperado: " + tipoResultado + ")");
            }
            return false;
        }
    }

    // =========================================================
    //   Utilidad para normalizar textos
    // =========================================================
    private static String normalizar(String s) {
        if (s == null) return "";
        return s.trim();
    }
}