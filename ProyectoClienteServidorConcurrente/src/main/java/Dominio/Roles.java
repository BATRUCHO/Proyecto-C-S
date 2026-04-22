package Dominio;


public enum Roles   {

    CLIENTE(1),
    REPARTIDORES(2),
    ADMINISTRADOR(3);

    private final int id;

    Roles(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
