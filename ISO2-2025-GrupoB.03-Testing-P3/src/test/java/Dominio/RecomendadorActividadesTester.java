package Dominio;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class RecomendadorActividadesTester {

    // El fichero debe estar en src/test/resources/casosRecomendador.txt
    private static final String FICHERO_CASOS = "/casosRecomendador.txt";

    @Test
    public void todosLosCasosDelFicheroDebenPasar() throws IOException {
        InputStream is = getClass().getResourceAsStream(FICHERO_CASOS);
        assertNotNull("No se ha encontrado el fichero de casos: " + FICHERO_CASOS, is);

        int total = 0;
        int ok = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
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
                } else {
                    // Si un caso falla, fallamos el test
                    fail("Falló el caso de prueba " + total + " (línea: " + linea + "). Revisa la salida por consola.");
                }
            }
        }

        System.out.println("=================================");
        System.out.println("Resumen general: " + ok + " / " + total + " casos OK");
        System.out.println("=================================");

        assertTrue("No se ha ejecutado ningún caso de prueba.", total > 0);
        assertTrue("No todos los casos han pasado correctamente.", ok == total);
    }

    /**
     * Ejecuta un caso de prueba a partir de una línea del .txt
     * Formato:
     * ID;METODO;temp;hum;hayPrec;estaNub;enPlenas;tuvoEnf;aforoAct;aforoMax;TIPORESULTADO;MENSAJE_ESPERADO
     */
    private boolean ejecutarCaso(String linea) {
        String[] partes = linea.split(";", -1); // -1 para conservar campos vacíos

        if (partes.length < 11) {
            System.out.println("[ERROR FORMATO] Línea con menos de 11 campos: " + linea);
            return false;
        }

        String id = partes[0].trim();
        String metodo = partes[1].trim();

        try {
            double temperatura = Double.parseDouble(partes[2].trim());
            int humedad = Integer.parseInt(partes[3].trim());
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
    private boolean probarConstructor(
            String id,
            double temperatura,
            int humedad,
            boolean hayPrec,
            boolean estaNub,
            boolean enPlenas,
            boolean tuvoEnf,
            int aforoAct,
            int aforoMax,
            String tipoResultado
    ) {
        try {
            // Solo queremos comprobar que el constructor se ejecuta o lanza la excepción correcta
            new RecomendadorActividades(
                    temperatura, humedad, hayPrec, estaNub,
                    enPlenas, tuvoEnf, aforoAct, aforoMax
            );

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
    private boolean probarRecomendar(
            String id,
            double temperatura,
            int humedad,
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
    private String normalizar(String s) {
        if (s == null) return "";
        return s.trim();
    }
}