package Dominio;

public enum TipoVehiculo {
    CAMIONETA(1),
    MOTOCICLETA(2),
    CAMION_PESADO(3);

    private final int id;

    TipoVehiculo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}