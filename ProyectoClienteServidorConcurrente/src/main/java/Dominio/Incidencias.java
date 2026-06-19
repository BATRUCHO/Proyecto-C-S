package Dominio;

import java.io.Serializable;
import java.sql.Timestamp;

public class Incidencias implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id_incidencia;
    private String descripcion;
    private Timestamp fecha_hora;

    public Incidencias(int id_incidencia, String descripcion, Timestamp fecha_hora){
        this.id_incidencia = id_incidencia;
        this.descripcion = descripcion;
        this.fecha_hora = fecha_hora;
    }

    public int getId_incidencia() {
        return id_incidencia;
    }

    public void setId_incidencia(int id_incidencia) {
        this.id_incidencia = id_incidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Timestamp fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
}
