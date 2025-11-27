package Dominio;

public class RecomendadorActividades {
    
    private double temperatura;
    private int humedad;
    private boolean hayPrecipitaciones;
    private boolean estaNublado;
    private boolean enPlenasFacultades;
    private boolean tuvoEnfermedadReciente;
    private int aforoMaximo;
    private int aforoActual;

    public RecomendadorActividades(double temperatura, int humedad, boolean hayPrecipitaciones,
            boolean estaNublado, boolean enPlenasFacultades, boolean tuvoEnfermedadReciente,
            int aforoMaximo, int aforoActual) throws ActividadInvalidaException {

        if (aforoMaximo < 0 || aforoActual < 0)
            throw new ActividadInvalidaException("Los aforos no pueden ser negativos.");
        if (humedad < 0 || humedad > 100)
            throw new ActividadInvalidaException("La humedad debe estar entre 0 y 100.");
        
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.hayPrecipitaciones = hayPrecipitaciones;
        this.estaNublado = estaNublado;
        this.enPlenasFacultades = enPlenasFacultades;
        this.tuvoEnfermedadReciente = tuvoEnfermedadReciente;
        this.aforoMaximo = aforoMaximo;
        this.aforoActual = aforoActual;
    }

    public String recomendar() {

        // Salud
        if (!enPlenasFacultades || tuvoEnfermedadReciente) {
            return "No puede realizar ninguna actividad por motivos de salud.";
        }

        // Clima extremo
        if (temperatura < 0 && humedad < 15 && hayPrecipitaciones) {
            return "La mejor opción es quedarse en casa.";
        }

        // Esquí
        if (temperatura < 0 && humedad < 15 && !hayPrecipitaciones) {
            if (aforoActual < aforoMaximo)
                return "Actividad recomendada: Esquí.";
            else
                return "Aforo completo para actividades de esquí.";
        }

        // Senderismo / Escalada
        if (temperatura >= 0 && temperatura <= 15 && !hayPrecipitaciones) {
            if (aforoActual < aforoMaximo)
                return "Actividad recomendada: Senderismo o Escalada.";
            else
                return "Aforo completo en las zonas de senderismo/escalada.";
        }

        // Actividades generales
        if (temperatura >= 15 && temperatura <= 25 && !hayPrecipitaciones 
                && !estaNublado && humedad <= 60) {
            return "Puede realizar actividades de primavera/verano/otoño.";
        }

        // Culturales y gastronómicas
        if (temperatura >= 25 && temperatura <= 35 && !hayPrecipitaciones) {
            return "Actividad recomendada: Actividades culturales o gastronómicas.";
        }

        // Playa / piscina
        if (temperatura > 30 && !hayPrecipitaciones) {
            if (aforoActual < aforoMaximo)
                return "Actividad recomendada: Ir a la playa o piscina.";
            else
                return "La piscina/playa está en aforo máximo.";
        }

        return "No hay recomendaciones disponibles para las condiciones proporcionadas.";
    }
}
