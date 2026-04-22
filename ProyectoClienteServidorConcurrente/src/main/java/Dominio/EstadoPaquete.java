package Dominio;

public enum EstadoPaquete {
    EN_BODEGA(1),
    EN_TRANSITO(2),
    ENTREGADO(3),
    CON_INCIDENCIA(4);

    private final int id;

    EstadoPaquete(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}