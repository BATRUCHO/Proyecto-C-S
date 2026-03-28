package Dominio;

public enum Provincia {
    SAN_JOSE(0), 
    ALAJUELA(20),
    CARTAGO(25),
    HEREDIA(12),
    GUANACASTE(210),
    PUNTARENAS(95),
    LIMON(160);

    private final int distanciaKm;

    // Constructor del Enum
    Provincia(int distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public int getDistanciaKm() {
        return distanciaKm;
    }
}
