package ISO2B3.Dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class ActividadInvalidaExceptionTest {

    @Test
    public void constructorConMensaje_guardaElMensaje() {
        String msg = "Mensaje de prueba";
        ISO2B3.Dominio.ActividadInvalidaException ex = new ISO2B3.Dominio.ActividadInvalidaException(msg);

        assertNotNull("La excepción no debe ser nula", ex);
        assertEquals("El mensaje de la excepción no coincide",
                     msg, ex.getMessage());
    }

    @Test(expected = ISO2B3.Dominio.ActividadInvalidaException.class)
    public void sePuedeLanzarActividadInvalidaException() throws ISO2B3.Dominio.ActividadInvalidaException {
        throw new ISO2B3.Dominio.ActividadInvalidaException("Error lanzado a propósito");
    }
}