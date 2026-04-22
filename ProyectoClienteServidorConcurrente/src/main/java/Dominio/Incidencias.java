package Dominio;

import java.io.Serializable;
import java.sql.Timestamp;

public class Incidencias implements Serializable {

    private int idIncidencia;
    private int idPaquete;
    private int idConductor;
    private String descripcion;
    private Timestamp fechaHora;

public Incidencias(int idIncidencia, int idPaquete, int idConductor, String descripcion, Timestamp fechaHora){
    this.idIncidencia = idIncidencia;
    this.idPaquete = idPaquete;
    this.idConductor = idConductor;
    this.descripcion = descripcion;
    this.fechaHora = fechaHora;
}

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    public int getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(int idConductor) {
        this.idConductor = idConductor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

}
