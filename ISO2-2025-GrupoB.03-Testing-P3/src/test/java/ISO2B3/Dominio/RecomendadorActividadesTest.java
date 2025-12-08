package ISO2B3.Dominio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class RecomendadorActividadesTest {

    // ======================= TESTS UNITARIOS (CPtest_n) ======================

    // CPtest_1 -> No puede realizar actividades por motivos de salud
    @Test
    public void CPtest_1_salud_no_puede_realizar() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                0,      // temperatura
                0,      // humedad
                false,  // hayPrecipitaciones
                false,  // estaNublado
                false,  // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                0,      // aforoActual
                0       // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "No puede realizar ninguna actividad por motivos de salud.",
                r.recomendar()
        );
    }

    // CPtest_2 -> Clima extremo: la mejor opción es quedarse en casa
    @Test
    public void CPtest_2_clima_extremo_quedarse_en_casa() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                -5,     // temperatura
                10,     // humedad
                true,   // hayPrecipitaciones
                true,   // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                10,     // aforoActual
                50      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "La mejor opción es quedarse en casa.",
                r.recomendar()
        );
    }

    // CPtest_3 -> Esquí con aforo completo
    @Test
    public void CPtest_3_esqui_aforo_completo() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                -8,     // temperatura
                5,      // humedad
                false,  // hayPrecipitaciones
                true,   // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                50,     // aforoActual
                50      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "Aforo completo para actividades de esquí.",
                r.recomendar()
        );
    }

    // CPtest_4 -> Senderismo/escalada con aforo completo
    @Test
    public void CPtest_4_senderismo_aforo_completo() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                5,      // temperatura
                50,     // humedad
                false,  // hayPrecipitaciones
                false,  // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                40,     // aforoActual
                40      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "Aforo completo en las zonas de senderismo/escalada.",
                r.recomendar()
        );
    }

    // CPtest_5 -> No hay recomendaciones (lluvia, humedad alta, etc.)
    @Test
    public void CPtest_5_sin_recomendaciones_lluvia() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                8,      // temperatura
                60,     // humedad
                true,   // hayPrecipitaciones
                true,   // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                10,     // aforoActual
                40      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "No hay recomendaciones disponibles para las condiciones proporcionadas.",
                r.recomendar()
        );
    }

    // CPtest_6 -> Actividades de primavera/verano/otoño
    @Test
    public void CPtest_6_actividades_primavera_verano_otono() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                20,     // temperatura
                40,     // humedad
                false,  // hayPrecipitaciones
                false,  // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                10,     // aforoActual
                50      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "Puede realizar actividades de primavera/verano/otoño.",
                r.recomendar()
        );
    }

    // CPtest_7 -> No hay recomendaciones por humedad alta
    @Test
    public void CPtest_7_sin_recomendaciones_humedad_alta() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                22,     // temperatura
                75,     // humedad
                false,  // hayPrecipitaciones
                false,  // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                10,     // aforoActual
                50      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "No hay recomendaciones disponibles para las condiciones proporcionadas.",
                r.recomendar()
        );
    }

    // CPtest_8 -> Actividades culturales o gastronómicas
    @Test
    public void CPtest_8_culturales_gastronomicas() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                30,     // temperatura
                40,     // humedad
                false,  // hayPrecipitaciones
                true,   // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                20,     // aforoActual
                50      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "Actividad recomendada: Actividades culturales o gastronómicas.",
                r.recomendar()
        );
    }

    // CPtest_9 -> Playa/piscina con aforo disponible
    @Test
    public void CPtest_9_playa_con_aforo() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                36,     // temperatura
                55,     // humedad
                false,  // hayPrecipitaciones
                false,  // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                10,     // aforoActual
                50      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "Actividad recomendada: Ir a la playa o piscina.",
                r.recomendar()
        );
    }

    // CPtest_10 -> Playa/piscina con aforo máximo
    @Test
    public void CPtest_10_playa_aforo_maximo() throws Exception {
        RecomendadorActividades r = new RecomendadorActividades(
                35,     // temperatura
                50,     // humedad
                false,  // hayPrecipitaciones
                true,   // estaNublado
                true,   // enPlenasFacultades
                false,  // tuvoEnfermedadReciente
                50,     // aforoActual
                50      // aforoMaximo
        );
        org.junit.Assert.assertEquals(
                "La piscina/playa está en aforo máximo.",
                r.recomendar()
        );
    }

    // CPtest_11 -> la excepción guarda correctamente el mensaje
    @Test
    public void CPtest_11_constructorConMensaje_guardaElMensaje() {
        String msg = "Mensaje de prueba";
        ISO2B3.Dominio.ActividadInvalidaException ex =
                new ISO2B3.Dominio.ActividadInvalidaException(msg);

        org.junit.Assert.assertNotNull("La excepción no debe ser nula", ex);
        org.junit.Assert.assertEquals("El mensaje de la excepción no coincide",
                msg, ex.getMessage());
    }

    // CPtest_12 -> se puede lanzar ActividadInvalidaException explícitamente
    @Test(expected = ISO2B3.Dominio.ActividadInvalidaException.class)
    public void CPtest_12_sePuedeLanzarActividadInvalidaException()
            throws ISO2B3.Dominio.ActividadInvalidaException {
        throw new ISO2B3.Dominio.ActividadInvalidaException("Error lanzado a propósito");
    }

    // CPtest_13 -> constructor: aforo negativo lanza excepción correcta
    // CPtest_13 -> constructor: aforo negativo lanza excepción correcta
    @Test
    public void CPtest_13_constructor_aforoNegativo_lanza() {
        try {
            new RecomendadorActividades(
                    20, 50, false, false,
                    true, false, -1, 10
            );
            org.junit.Assert.fail("Se esperaba ActividadInvalidaException por aforo negativo");
        } catch (ActividadInvalidaException e) {
            org.junit.Assert.assertEquals("Los aforos no pueden ser negativos.", e.getMessage());
        } catch (Exception e) {
            org.junit.Assert.fail("Excepción inesperada: " + e);
        }
    }

    // CPtest_14 -> constructor: humedad fuera de rango lanza excepción correcta
    @Test
    public void CPtest_14_constructor_humedadFuera_lanza() {
        try {
            new RecomendadorActividades(
                    20, 150, false, false,
                    true, false, 0, 10
            );
            org.junit.Assert.fail("Se esperaba ActividadInvalidaException por humedad inválida");
        } catch (ActividadInvalidaException e) {
            org.junit.Assert.assertEquals("La humedad debe estar entre 0 y 100.", e.getMessage());
        }
    }

    // ======================= TEST BASADO EN TXT (CP.txt) =====================

    @Test
    public void probarTodosLosCasosDelTxt() throws IOException {
        // CP.txt debe estar en src/test/resources
        InputStream in = getClass().getClassLoader().getResourceAsStream("CP.txt");
        assertNotNull(
                "No se ha encontrado el fichero de casos: CP.txt (debe estar en src/test/resources)",
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
                if (linea.startsWith("ID;")) {
                    continue; // cabecera TXT (separada por ;)
                }

                total++;
                ejecutarCaso(linea);
            }
        }

        assertTrue(
                "No se ha leído ningún caso de prueba del TXT",
                total > 0
        );
    }

    /**
     * Formato TXT (tipo CSV):
     * ID;METODO;temperatura;humedad;hayPrec;estaNub;enPlenas;tuvoEnf;
     * aforoAct;aforoMax;TIPORESULTADO;MENSAJE_ESPERADO
     */
    private void ejecutarCaso(String linea) {
        String[] partes = linea.split(";", -1); // -1 para conservar último campo vacío
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
        // Creamos el recomendador y LO USAMOS para evitar warnings del IDE
        ISO2B3.Dominio.RecomendadorActividades r =
                new ISO2B3.Dominio.RecomendadorActividades(
                        temperatura, humedad, hayPrec, estaNub,
                        enPlenas, tuvoEnf, aforoAct, aforoMax
                );

        assertNotNull("El recomendador no debe ser nulo", r);

        // Si se esperaba excepción pero no se lanzó → fallo
        if (!"OK".equalsIgnoreCase(tipoResultado)) {
            fail("Caso " + id + ": se esperaba excepción (" + tipoResultado
                    + ") pero el constructor no lanzó nada.");
        }

    } catch (ISO2B3.Dominio.ActividadInvalidaException e) {

        String msg = normalizar(e.getMessage());

        // Excepción esperada: aforo negativo
        if ("EXC_AFORO_NEG".equalsIgnoreCase(tipoResultado)) {

            assertEquals(
                    "Caso " + id + ": mensaje de excepción incorrecto",
                    "Los aforos no pueden ser negativos.",
                    msg
            );
            return;
        }

        // Excepción esperada: humedad incorrecta
        else if ("EXC_HUMEDAD".equalsIgnoreCase(tipoResultado)) {

            // La implementación puede comprobar antes el aforo → permitimos ambos mensajes
            boolean humedadValida =
                    "La humedad debe estar entre 0 y 100.".equals(msg);
            boolean aforoValido =
                    "Los aforos no pueden ser negativos.".equals(msg);

            if (!humedadValida && !aforoValido) {
                fail("Caso " + id + ": mensaje de excepción incorrecto para EXC_HUMEDAD. Obtenido: " + msg);
            }
            return;
        }

        // Si la excepción NO era esperada
        else {
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
            ISO2B3.Dominio.RecomendadorActividades r =
                    new ISO2B3.Dominio.RecomendadorActividades(
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

                // Igual que en el constructor: permitimos ambas posibilidades
                if (!"La humedad debe estar entre 0 y 100.".equals(msg)
                        && !"Los aforos no pueden ser negativos.".equals(msg)) {
                    fail("Caso " + id + ": mensaje de excepción incorrecto (EXC_HUMEDAD): " + msg);
                }

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