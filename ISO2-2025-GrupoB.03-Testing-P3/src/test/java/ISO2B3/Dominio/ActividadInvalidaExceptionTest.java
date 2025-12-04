package ISO2B3.Dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ActividadInvalidaExceptionTest {

    @Test
    public void constructorConMensaje_guardaElMensaje() {
        String msg = "Mensaje de prueba";
        ActividadInvalidaException ex = new ActividadInvalidaException(msg);

        assertNotNull("La excepción no debe ser nula", ex);
        assertEquals("El mensaje de la excepción no coincide",
                     msg, ex.getMessage());
    }

    @Test(expected = ActividadInvalidaException.class)
    public void sePuedeLanzarActividadInvalidaException() throws ActividadInvalidaException {
        throw new ActividadInvalidaException("Error lanzado a propósito");
    }
}