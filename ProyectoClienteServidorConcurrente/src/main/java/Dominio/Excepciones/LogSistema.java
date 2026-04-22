package Dominio.Excepciones;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogSistema implements Serializable {
    private int idLog;
    private int idUsuario;
    private String accion;
    private Timestamp fechaHora;


    public LogSistema(int idLog, int idUsuario, String accion, Timestamp fechaHora) {
        this.idLog = idLog;
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.fechaHora = fechaHora;
    }

    public LogSistema(int i, int idUsuarioActual, String string, String string2, Object object) {
        //TODO Auto-generated constructor stub
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

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }


}
