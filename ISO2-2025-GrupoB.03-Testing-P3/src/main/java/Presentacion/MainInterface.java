package Presentacion;

import java.util.InputMismatchException;
import java.util.Scanner;

import Dominio.RecomendadorActividades;
import Dominio.ActividadInvalidaException;

public class MainInterface {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("===== Sistema de Recomendación de Actividades =====");

        try {
            System.out.print("Temperatura actual: ");
            double temp = sc.nextDouble();

            System.out.print("Humedad relativa (%): ");
            int hum = sc.nextInt();

            System.out.print("¿Hay precipitaciones? (true/false): ");
            boolean prec = sc.nextBoolean();

            System.out.print("¿Está nublado? (true/false): ");
            boolean nublado = sc.nextBoolean();

            System.out.print("¿Está en plenas facultades físicas? (true/false): ");
            boolean sano = sc.nextBoolean();

            System.out.print("¿Ha tenido enfermedades infecciosas en las dos últimas semanas? (true/false): ");
            boolean enfermo = sc.nextBoolean();

            System.out.print("Aforo máximo permitido: ");
            int afMax = sc.nextInt();

            System.out.print("Aforo actual: ");
            int afAct = sc.nextInt();

            RecomendadorActividades rec = new RecomendadorActividades(
                temp, hum, prec, nublado, sano, enfermo, afMax, afAct
            );

            System.out.println("\nRESULTADO:");
            System.out.println(rec.recomendar());

        } catch (ActividadInvalidaException e) {
            System.out.println("Error en los datos: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: introdujiste texto donde debía ir un número.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
