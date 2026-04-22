package Dominio;


import java.io.Serializable;
import java.sql.Date;


public class Paquete implements Serializable {

    private int id_paquete;
    private String descripcion;
    private String remitente;
    private String destinatario;
    private String direccion_entrega;
    private int id_estado;
    private Date fecha_creacion;
    private int id_vehiculo;

    
    public Paquete(int id_paquete, String descripcion, String remitente, String destinatario, String direccion_entrega,
            int id_estado, Date fecha_creacion, int id_vehiculo) {
        this.id_paquete = id_paquete;
        this.descripcion = descripcion;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.direccion_entrega = direccion_entrega;
        this.id_estado = id_estado;
        this.fecha_creacion = fecha_creacion;
        this.id_vehiculo = id_vehiculo;

    }

    public int getId_paquete() {
        return id_paquete;
    }


    public void setId_paquete(int id_paquete) {
        this.id_paquete = id_paquete;
    }


    public String getDescripcion() {
        return descripcion;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getRemitente() {
        return remitente;
    }


    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }


    public String getDestinatario() {
        return destinatario;
    }


    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }


    public String getDireccion_entrega() {
        return direccion_entrega;
    }


    public void setDireccion_entrega(String direccion_entrega) {
        this.direccion_entrega = direccion_entrega;
    }


    public int getId_estado() {
        return id_estado;
    }


    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }


    public Date getFecha_creacion() {
        return fecha_creacion;
    }


    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;

    }
}
    
 