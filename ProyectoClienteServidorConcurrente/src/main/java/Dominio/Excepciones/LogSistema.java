package Dominio.Excepciones;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogSistema implements Serializable {
    private int idLog;
    private int idUsuario;
    private String accion;
    private String detalle;
    private Timestamp fechaHora;


    public LogSistema(int idLog, int idUsuario, String accion,String detalle,Timestamp fechaHora) {
        this.idLog = idLog;
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.detalle = detalle;
        this.fechaHora = fechaHora;
    }


    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    public String getDetalle() {
        return detalle;
    }
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }


}
