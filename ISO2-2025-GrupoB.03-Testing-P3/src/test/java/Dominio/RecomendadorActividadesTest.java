package Dominio;

import org.junit.Test;
import static org.junit.Assert.*;

public class RecomendadorActividadesTest {

    // Test para actividad de salud bloqueada
    @Test
    public void testSaludNoPermitida() throws ActividadInvalidaException {
        RecomendadorActividades rec = new RecomendadorActividades(
            20, 50, false, false, false, false, 10, 5
        );
        String resultado = rec.recomendar();
        assertEquals("No puede realizar ninguna actividad por motivos de salud.", resultado);
    }
}
