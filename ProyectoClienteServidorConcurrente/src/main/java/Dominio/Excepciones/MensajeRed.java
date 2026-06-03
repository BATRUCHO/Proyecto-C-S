package Dominio.Excepciones;

import java.io.Serializable;

public class MensajeRed implements Serializable {
    private String accion;
    private Object payload;
    private boolean estadoExito;
    private String mensajeRespuesta;
    private int idUsuarioSender;

    public MensajeRed(String accion, Object payload, boolean estadoExito, String mensajeRespuesta, int idUsuarioSender) {
        this.accion = accion;
        this.payload = payload;
        this.estadoExito = estadoExito;
        this.mensajeRespuesta = mensajeRespuesta;
        this.idUsuarioSender = idUsuarioSender;

    }

    // SobreCarga de constructor
    public MensajeRed(String accion, Object payload, boolean estadoExito, String mensajeRespuesta) {
        this.accion = accion;
        this.payload = payload;
        this.estadoExito = estadoExito;
        this.mensajeRespuesta = mensajeRespuesta;
        this.idUsuarioSender = 0;
    }


    // Getters y setters
    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public boolean isEstadoExito() {
        return estadoExito;
    }

    public void setEstadoExito(boolean estadoExito) {
        this.estadoExito = estadoExito;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

    public int getIdUsuarioSender() {
        return idUsuarioSender;
    }

    public void setIdUsuarioSender(int idUsuarioSender) {
        this.idUsuarioSender = idUsuarioSender;
    }

}
