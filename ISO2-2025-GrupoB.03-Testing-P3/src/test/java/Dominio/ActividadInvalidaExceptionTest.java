package Dominio;

import static org.junit.Assert.*;
import org.junit.Test;

public class ActividadInvalidaExceptionTest {

    @Test
    public void testMensajeSePropaga() {
        String msg = "Mensaje de error personalizado";
        ActividadInvalidaException ex = new ActividadInvalidaException(msg);
        assertEquals(msg, ex.getMessage());
    }

    @Test
    public void testEsSubclaseDeException() {
        ActividadInvalidaException ex = new ActividadInvalidaException("error");
        assertTrue(ex instanceof Exception);
    }
}
