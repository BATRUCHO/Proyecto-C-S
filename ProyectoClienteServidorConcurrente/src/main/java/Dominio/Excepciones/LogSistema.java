package Dominio.Excepciones;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogSistema implements Serializable {
    private int id_log;
    private String accion;
    private String detalles;
    private Timestamp fecha_hora;


    public LogSistema(int id_log, String accion,String detalles,Timestamp fecha_hora) {
        this.id_log = id_log;
        this.accion = accion;
        this.detalles = detalles;
        this.fecha_hora = fecha_hora;
    }

    public int getId_log() {
        return id_log;
    }


    public void setId_log(int id_log) {
        this.id_log = id_log;
    }


    public String getAccion() {
        return accion;
    }


    public void setAccion(String accion) {
        this.accion = accion;
    }


    public String getDetalles() {
        return detalles;
    }


    public void setDetalle(String detalles) {
        this.detalles = detalles;
    }


    public Timestamp getFecha_hora() {
        return fecha_hora;
    }


    public void setFecha_hora(Timestamp fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

}
