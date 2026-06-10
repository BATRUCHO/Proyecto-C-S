package Dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


public class Paquete implements Serializable {

    private int id_paquete;
    private String descripcion;
    private String remitente;
    private String destinatario;
    private String direccion_entrega;
    private BigDecimal peso;
    private int id_estado;
    private Timestamp fecha_creacion;
    
    public Paquete(int id_paquete, String descripcion, String remitente, String destinatario, String direccion_entrega, BigDecimal peso, 
        Timestamp fecha_creacion) {

        this.id_paquete = id_paquete;
        this.descripcion = descripcion;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.direccion_entrega = direccion_entrega;
        this.peso = peso;
        this.fecha_creacion = fecha_creacion;

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
    

    public BigDecimal getPeso() {
        return peso;
    }



    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }


    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }


    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }


    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

  
}
    
 