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

public class RecomendadorActividadesTest {

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

    // ======================= Tests unitarios adicionales (migrados) ==========

    @org.junit.Test
    public void constructorConMensaje_guardaElMensaje() {
        String msg = "Mensaje de prueba";
        ISO2B3.Dominio.ActividadInvalidaException ex = new ISO2B3.Dominio.ActividadInvalidaException(msg);

        org.junit.Assert.assertNotNull("La excepción no debe ser nula", ex);
        org.junit.Assert.assertEquals("El mensaje de la excepción no coincide",
                     msg, ex.getMessage());
    }

    @org.junit.Test(expected = ISO2B3.Dominio.ActividadInvalidaException.class)
    public void sePuedeLanzarActividadInvalidaException() throws ISO2B3.Dominio.ActividadInvalidaException {
        throw new ISO2B3.Dominio.ActividadInvalidaException("Error lanzado a propósito");
    }

    // Branch-focused unit tests to improve COMPLEXITY coverage
    @org.junit.Test
    public void constructor_aforoNegativo_lanza() {
        try {
            new RecomendadorActividades(20, 50, false, false, true, false, -1, 10);
            org.junit.Assert.fail("Se esperaba ActividadInvalidaException por aforo negativo");
        } catch (ActividadInvalidaException e) {
            org.junit.Assert.assertEquals("Los aforos no pueden ser negativos.", e.getMessage());
        } catch (Exception e) {
            org.junit.Assert.fail("Excepción inesperada: " + e);
        }
    }

    @org.junit.Test
    public void constructor_humedadFuera_lanza() {
        try {
            new RecomendadorActividades(20, 150, false, false, true, false, 0, 10);
            org.junit.Assert.fail("Se esperaba ActividadInvalidaException por humedad inválida");
        } catch (ActividadInvalidaException e) {
            org.junit.Assert.assertEquals("La humedad debe estar entre 0 y 100.", e.getMessage());
        }
    }

    @org.junit.Test
    public void salud_prohibida_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(10, 50, false, false, false, false, 0, 10);
        org.junit.Assert.assertEquals("No puede realizar ninguna actividad por motivos de salud.", r.recomendar());
    }

    @org.junit.Test
    public void clima_extremo_quedarse_en_casa_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(-5, 10, true, true, true, false, 0, 10);
        org.junit.Assert.assertEquals("La mejor opción es quedarse en casa.", r.recomendar());
    }

    @org.junit.Test
    public void esqui_con_aforo_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(-4, 10, false, false, true, false, 1, 10);
        org.junit.Assert.assertEquals("Actividad recomendada: Esquí.", r.recomendar());
    }

    @org.junit.Test
    public void esqui_aforo_completo_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(-4, 10, false, false, true, false, 10, 10);
        org.junit.Assert.assertEquals("Aforo completo para actividades de esquí.", r.recomendar());
    }

    @org.junit.Test
    public void senderismo_con_aforo_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(10, 30, false, false, true, false, 1, 40);
        org.junit.Assert.assertEquals("Actividad recomendada: Senderismo o Escalada.", r.recomendar());
    }

    @org.junit.Test
    public void senderismo_aforo_completo_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(10, 30, false, false, true, false, 40, 40);
        org.junit.Assert.assertEquals("Aforo completo en las zonas de senderismo/escalada.", r.recomendar());
    }

    @org.junit.Test
    public void actividades_generales_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(20, 50, false, false, true, false, 1, 10);
        org.junit.Assert.assertEquals("Puede realizar actividades de primavera/verano/otoño.", r.recomendar());
    }

    @org.junit.Test
    public void culturales_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(26, 40, false, false, true, false, 1, 10);
        org.junit.Assert.assertEquals("Actividad recomendada: Actividades culturales o gastronómicas.", r.recomendar());
    }

    @org.junit.Test
    public void playa_con_aforo_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(36, 40, false, false, true, false, 1, 10);
        org.junit.Assert.assertEquals("Actividad recomendada: Ir a la playa o piscina.", r.recomendar());
    }

    @org.junit.Test
    public void playa_aforo_maximo_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(36, 40, false, false, true, false, 10, 10);
        org.junit.Assert.assertEquals("La piscina/playa está en aforo máximo.", r.recomendar());
    }

    @org.junit.Test
    public void fallback_no_recomendacion_unit() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(40, 80, true, true, true, false, 1, 10);
        org.junit.Assert.assertEquals("No hay recomendaciones disponibles para las condiciones proporcionadas.", r.recomendar());
    }

    @org.junit.Test
    public void cobertura_exhaustiva_combinaciones() {
        double[] temps = {-10, -1, 0, 5, 15, 20, 25, 30, 35};
        int[] hums = {5, 14, 15, 16, 50, 61};
        boolean[] prec = {true, false};
        boolean[] nublado = {true, false};
        boolean[] enPlenas = {true, false};
        boolean[] tuvoEnf = {true, false};
        int[] aforosAct = {0, 1, 10, 40, 50};
        int[] aforosMax = {1, 10, 40, 50};

        for (double t : temps) {
            for (int h : hums) {
                for (boolean p : prec) {
                    for (boolean n : nublado) {
                        for (boolean e : enPlenas) {
                            for (boolean te : tuvoEnf) {
                                for (int a : aforosAct) {
                                    for (int m : aforosMax) {
                                        try {
                                            RecomendadorActividades r = new RecomendadorActividades(t, h, p, n, e, te, a, m);
                                            // call to exercise method; don't assert output here
                                            r.recomendar();
                                        } catch (ActividadInvalidaException ex) {
                                            // ignore expected validation exceptions
                                        } catch (Exception ex) {
                                            org.junit.Assert.fail("Unexpected exception: " + ex + " for params t=" + t + " h=" + h + " p=" + p + " n=" + n + " e=" + e + " te=" + te + " a=" + a + " m=" + m);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
