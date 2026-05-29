package Dominio.Excepciones;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogSistema implements Serializable {
    private int id_log;
    private int id_usuario;
    private String accion;
    private String detalles;
    private Timestamp fecha_hora;


    public LogSistema(int id_log,int id_usuario, String accion,String detalles,Timestamp fecha_hora) {
        this.id_log = id_log;
        this.id_usuario = id_usuario;
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

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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
