package Dominio;

import static org.junit.Assert.*;
import org.junit.Test;

public class RecomendadorActividadesTest {

    //Constructor: aforos negativos
    @Test(expected = ActividadInvalidaException.class)
    public void testAforoMaximoNegativoLanzaExcepcion() throws ActividadInvalidaException {
        new RecomendadorActividades(20, 50, false, false, true, false, -1, 0);
    }

    @Test(expected = ActividadInvalidaException.class)
    public void testAforoActualNegativoLanzaExcepcion() throws ActividadInvalidaException {
        new RecomendadorActividades(20, 50, false, false, true, false, 10, -1);
    }

    //Constructor: humedad inválida
    @Test(expected = ActividadInvalidaException.class)
    public void testHumedadMenorQueCeroLanzaExcepcion() throws ActividadInvalidaException {
        new RecomendadorActividades(20, -1, false, false, true, false, 10, 5);
    }

    @Test(expected = ActividadInvalidaException.class)
    public void testHumedadMayorQueCienLanzaExcepcion() throws ActividadInvalidaException {
        new RecomendadorActividades(20, 101, false, false, true, false, 10, 5);
    }

    //Salud bloqueada
    @Test
    public void testSaludNoEnPlenasFacultades() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            20, 50, false, false, false, false, 10, 5
        );
        assertEquals("No puede realizar ninguna actividad por motivos de salud.", rec.recomendar());
    }

    @Test
    public void testTuvoEnfermedadReciente() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            20, 50, false, false, true, true, 10, 5
        );
        assertEquals("No puede realizar ninguna actividad por motivos de salud.", rec.recomendar());
    }

    //Clima extremo: quedarse en casa
    @Test
    public void testClimaExtremoQuedarseEnCasa() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            -5, 10, true, true, true, false, 10, 5
        );
        assertEquals("La mejor opción es quedarse en casa.", rec.recomendar());
    }

    //Esquí
    @Test
    public void testEsquiRecomendado() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            -5, 10, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Esquí.", rec.recomendar());
    }

    @Test
    public void testEsquiAforoCompleto() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            -5, 10, false, true, true, false, 10, 10
        );
        assertEquals("Aforo completo para actividades de esquí.", rec.recomendar());
    }

    //Senderismo/Escalada
    @Test
    public void testSenderismoEscaladaRecomendadoLimiteInferior() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            0, 50, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Senderismo o Escalada.", rec.recomendar());
    }

    @Test
    public void testSenderismoEscaladaRecomendadoLimiteSuperior() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            15, 50, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Senderismo o Escalada.", rec.recomendar());
    }

    @Test
    public void testSenderismoEscaladaAforoCompleto() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            10, 50, false, true, true, false, 10, 10
        );
        assertEquals("Aforo completo en las zonas de senderismo/escalada.", rec.recomendar());
    }

    //Actividades generales primavera/verano/otoño
    @Test
    public void testActividadesGeneralesBuenasCondiciones() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            20, 60, false, false, true, false, 10, 5
        );
        assertEquals("Puede realizar actividades de primavera/verano/otoño.", rec.recomendar());
    }

    @Test
    public void testActividadesGeneralesNoSiNublado() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            20, 60, false, true, true, false, 10, 5
        );
        // No cumple la condición de actividades generales y no entra en ninguna otra
        assertEquals("No hay recomendaciones disponibles para las condiciones proporcionadas.", rec.recomendar());
    }

    @Test
    public void testActividadesGeneralesNoSiHumedadAlta() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            20, 80, false, false, true, false, 10, 5
        );
        assertEquals("No hay recomendaciones disponibles para las condiciones proporcionadas.", rec.recomendar());
    }

    //Culturales y gastronómicas
    @Test
    public void testCulturalesYGastronomicas() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            30, 70, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Actividades culturales o gastronómicas.", rec.recomendar());
    }

    @Test
    public void testCulturalesYGastronomicasBordeInferior() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            25, 40, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Actividades culturales o gastronómicas.", rec.recomendar());
    }

    @Test
    public void testCulturalesYGastronomicasBordeSuperior() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            35, 40, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Actividades culturales o gastronómicas.", rec.recomendar());
    }

    //Playa/piscina
    @Test
    public void testPlayaPiscinaRecomendada() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            32, 50, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Ir a la playa o piscina.", rec.recomendar());
    }

    @Test
    public void testPlayaPiscinaAforoCompleto() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            32, 50, false, true, true, false, 10, 10
        );
        assertEquals("La piscina/playa está en aforo máximo.", rec.recomendar());
    }

    @Test
    public void testPlayaPiscinaTemperaturaJustoMayorQue30() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            31, 50, false, true, true, false, 10, 5
        );
        assertEquals("Actividad recomendada: Ir a la playa o piscina.", rec.recomendar());
    }

    //Caso sin recomendación
    @Test
    public void testSinRecomendaciones() throws ActividadInvalidaException {
        //Por ejemplo, temperatura moderada pero con precipitaciones
        RecomendadorActividades rec = new RecomendadorActividades(
            20, 70, true, true, true, false, 10, 5
        );
        assertEquals("No hay recomendaciones disponibles para las condiciones proporcionadas.", rec.recomendar());
    }
}