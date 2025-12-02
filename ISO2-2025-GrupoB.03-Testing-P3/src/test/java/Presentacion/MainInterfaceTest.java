package Presentacion;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainInterfaceTest {

    private PrintStream originalOut;
    private java.io.InputStream originalIn;

    @Before
    public void setUp() {
        originalOut = System.out;
        originalIn = System.in;
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private String ejecutarMainConEntrada(String input) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setIn(in);
            System.setOut(new PrintStream(out));

            MainInterface.main(new String[0]);

            return out.toString();
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @Test
    public void testFlujoCorrecto() {
        String input =
            "20\n" +
            "50\n" +
            "false\n" +
            "false\n" +
            "true\n" +
            "false\n" +
            "10\n" +
            "5\n";

        String salida = ejecutarMainConEntrada(input);
        assertTrue(salida.contains("RESULTADO:"));
        assertTrue(salida.contains("Puede realizar actividades de primavera/verano/otoño."));
    }

    @Test
    public void testErrorHumedadInvalida() {
        String input =
            "20\n" +
            "150\n" +
            "false\n" +
            "false\n" +
            "true\n" +
            "false\n" +
            "10\n" +
            "5\n";

        String salida = ejecutarMainConEntrada(input);
        assertTrue(salida.contains("Error en los datos:"));
    }

    @Test
    public void testErrorInputMismatch() {
        String input = "veinte\n";

        String salida = ejecutarMainConEntrada(input);
        assertTrue(salida.contains("Error: introdujiste texto donde debía ir un número."));
    }
}
