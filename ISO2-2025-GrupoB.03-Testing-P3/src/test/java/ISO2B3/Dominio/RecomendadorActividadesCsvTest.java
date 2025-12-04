package ISO2B3.Dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import ISO2B3.Dominio.RecomendadorActividades;
import ISO2B3.Dominio.ActividadInvalidaException;

public class RecomendadorActividadesCsvTest {

    @Test
    public void probarTodosLosCasosDelCsv() throws IOException {
        // CP.csv debe estar en src/test/resources
        InputStream in = getClass().getClassLoader().getResourceAsStream("CP.csv");
        assertNotNull(
                "No se ha encontrado el fichero de casos: CP.csv (debe estar en src/test/resources)",
                in
        );

        int total = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue; // líneas vacías
                }
                if (linea.startsWith("#")) {
                    continue; // comentarios
                }
                if (linea.startsWith("ID,")) {
                    continue; // cabecera CSV
                }

                total++;
                ejecutarCaso(linea);
            }
        }

        assertTrue(
                "No se ha leído ningún caso de prueba del CSV",
                total > 0
        );
    }

    /**
     * Formato CSV:
     * ID,METODO,temperatura,humedad,hayPrec,estaNub,enPlenas,tuvoEnf,
     * aforoAct,aforoMax,TIPORESULTADO,MENSAJE_ESPERADO
     */
    private void ejecutarCaso(String linea) {
        String[] partes = linea.split(",", -1); // -1 para conservar último campo vacío
        assertTrue("Línea con menos de 11 campos: " + linea, partes.length >= 11);

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

            if ("A".equalsIgnoreCase(metodo)) {
                probarConstructor(
                        id, temperatura, humedad, hayPrec, estaNub,
                        enPlenas, tuvoEnf, aforoAct, aforoMax, tipoResultado
                );
            } else if ("B".equalsIgnoreCase(metodo)) {
                probarRecomendar(
                        id, temperatura, humedad, hayPrec, estaNub,
                        enPlenas, tuvoEnf, aforoAct, aforoMax, tipoResultado, mensajeEsperado
                );
            } else {
                fail("Caso " + id + ": método desconocido '" + metodo + "'");
            }

        } catch (NumberFormatException e) {
            fail("Error parseando numéricos en el caso " + id + ": " + e.getMessage());
        }
    }

    // ======================= MÉTODO A: constructor ============================
    private void probarConstructor(
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
                new ISO2B3.Dominio.RecomendadorActividades(
                    temperatura, humedad, hayPrec, estaNub,
                    enPlenas, tuvoEnf, aforoAct, aforoMax
                );

            if (!"OK".equalsIgnoreCase(tipoResultado)) {
                fail("Caso " + id + ": se esperaba excepción (" + tipoResultado
                        + ") pero el constructor no lanzó nada.");
            }

        } catch (ISO2B3.Dominio.ActividadInvalidaException e) {
            String msg = normalizar(e.getMessage());

            if ("EXC_AFORO_NEG".equalsIgnoreCase(tipoResultado)) {
                assertEquals(
                        "Caso " + id + ": mensaje de excepción incorrecto",
                        "Los aforos no pueden ser negativos.",
                        msg
                );
            } else if ("EXC_HUMEDAD".equalsIgnoreCase(tipoResultado)) {
                assertEquals(
                        "Caso " + id + ": mensaje de excepción incorrecto",
                        "La humedad debe estar entre 0 y 100.",
                        msg
                );
            } else {
                fail("Caso " + id + ": excepción no esperada \"" + e.getMessage()
                        + "\" (se esperaba tipoResultado=" + tipoResultado + ")");
            }
        }
    }

    // ======================= MÉTODO B: recomendar() ==========================
    private void probarRecomendar(
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
                ISO2B3.Dominio.RecomendadorActividades r = new ISO2B3.Dominio.RecomendadorActividades(
                    temperatura, humedad, hayPrec, estaNub,
                    enPlenas, tuvoEnf, aforoAct, aforoMax
                );

            if (!"OK".equalsIgnoreCase(tipoResultado)) {
                fail("Caso " + id + ": se esperaba excepción (" + tipoResultado
                        + ") pero el constructor no lanzó nada.");
            }

            String obtenido = r.recomendar();
            String esperadoNorm = normalizar(mensajeEsperado);
            String obtenidoNorm = normalizar(obtenido);

            assertEquals(
                    "Caso " + id + ": mensaje devuelto por recomendar() incorrecto",
                    esperadoNorm,
                    obtenidoNorm
            );

        } catch (ISO2B3.Dominio.ActividadInvalidaException e) {
            String msg = normalizar(e.getMessage());

            if ("EXC_AFORO_NEG".equalsIgnoreCase(tipoResultado)) {
                assertEquals(
                        "Caso " + id + ": mensaje de excepción incorrecto",
                        "Los aforos no pueden ser negativos.",
                        msg
                );
            } else if ("EXC_HUMEDAD".equalsIgnoreCase(tipoResultado)) {
                assertEquals(
                        "Caso " + id + ": mensaje de excepción incorrecto",
                        "La humedad debe estar entre 0 y 100.",
                        msg
                );
            } else if ("OK".equalsIgnoreCase(tipoResultado)) {
                fail("Caso " + id
                        + ": se esperaba ejecución correcta, pero se lanzó excepción: \""
                        + e.getMessage() + "\"");
            } else {
                fail("Caso " + id + ": excepción inesperada \"" + e.getMessage()
                        + "\" (tipoResultado=" + tipoResultado + ")");
            }
        }
    }

    // ======================= Utilidad ========================================
    private String normalizar(String s) {
        if (s == null) return "";
        return s.trim();
    }
}
