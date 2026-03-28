package modelo;

public class Paquete {
    private String id;
    private String destino;

    public Paquete() { }

    public Paquete(String id, String destino) {
        this.id = id;
        this.destino = destino;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "id='" + id + '\'' +
                ", destino='" + destino + '\'' +
                '}';
    }
}