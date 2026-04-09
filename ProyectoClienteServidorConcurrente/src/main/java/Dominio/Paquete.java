package Dominio;


import java.io.Serializable;
import java.time.LocalDateTime;


public class Paquete implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int idPaquete;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;
    
    private Provincia provinciaOrigen; 
    private Provincia provinciaDestino; 
    private Integer idVehiculoAsignado; 

    private double pesoKg;
    private EstadoPaquete estado;
    private String contenido;

    public Paquete(int idPaquete, Provincia origen, Provincia destino, double pesoKg, String contenido) {
        this.idPaquete = idPaquete;
        this.provinciaOrigen = origen;
        this.provinciaDestino = destino;
        this.pesoKg = pesoKg;
        this.contenido = contenido;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPaquete.PENDIENTE;
        this.idVehiculoAsignado = null;
        this.fechaEntrega = null;
    }
    //Sobrecarga de constructores

    public Paquete(int idPaquete, LocalDateTime fechaCreacion, Provincia provinciaOrigen, Provincia provinciaDestino, double pesoKg, EstadoPaquete estado, String contenido, Integer idVehiculoAsignado, LocalDateTime fechaEntrega) {
      this.idPaquete = idPaquete;
      this.fechaCreacion = fechaCreacion;
      this.provinciaOrigen = provinciaOrigen;
      this.provinciaDestino = provinciaDestino;
      this.pesoKg = pesoKg;
      this.estado = estado;
      this.contenido = contenido;
      this.idVehiculoAsignado = idVehiculoAsignado;
      this.fechaEntrega = fechaEntrega;

    }

    public Paquete(String id, String destino) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public Provincia getProvinciaOrigen() {
        return provinciaOrigen;
    }

    public Provincia getProvinciaDestino() {
        return provinciaDestino;
    }

    public Integer getIdVehiculoAsignado() {
        return idVehiculoAsignado;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public EstadoPaquete getEstado() {
        return estado;
    }

    public String getContenido() {
        return contenido;
    }


    public void asignarVehiculo(int idVehiculo) { //good
        if (!estado.puedeCambiarA(EstadoPaquete.ASIGNADO)) {
            throw new IllegalStateException("Transición de estado no permitida");
        }
        this.idVehiculoAsignado = idVehiculo;
        this.estado = EstadoPaquete.ASIGNADO;
    }

    public void cambiarEstado(EstadoPaquete nuevoEstado) { //good
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }if (this.estado == EstadoPaquete.ENTREGADO) {
            throw new IllegalStateException("El paquete ya ha sido entregado y no se puede cambiar su estado");
        }if (nuevoEstado == EstadoPaquete.EN_TRANSITO && idVehiculoAsignado == null) {
            throw new IllegalStateException("No se puede cambiar el estado a EN_TRANSITO sin un vehículo asignado");
        }if (nuevoEstado == EstadoPaquete.ENTREGADO && fechaEntrega == null) {
            throw new IllegalStateException("No se puede cambiar el estado a ENTREGADO sin una fecha de entrega registrada");
        } 
        this.estado = nuevoEstado;
    }

    public void fechaEntrega(LocalDateTime fechaEntrega) {
        if (fechaEntrega == null) {
            throw new IllegalArgumentException("La fecha de entrega no puede ser nula");
        }
        if (idVehiculoAsignado == null) {
            throw new IllegalStateException("No se puede registrar la entrega sin un vehículo asignado");
        }if (estado != EstadoPaquete.EN_TRANSITO) {
            throw new IllegalStateException("El paquete no está en tránsito y no se puede registrar la entrega");
        } 
        this.fechaEntrega = fechaEntrega;
        cambiarEstado(EstadoPaquete.ENTREGADO);       
    }
    public boolean estadoPendiente() {
        return this.estado == EstadoPaquete.PENDIENTE;
    }

    public boolean estadoAsignado() {
        return this.estado == EstadoPaquete.ASIGNADO;
    }

    public boolean estadoEnTransito() {
        return this.estado == EstadoPaquete.EN_TRANSITO;
    }

    public boolean estadoEntregado() {
        return this.estado == EstadoPaquete.ENTREGADO;
    }

}
    
 