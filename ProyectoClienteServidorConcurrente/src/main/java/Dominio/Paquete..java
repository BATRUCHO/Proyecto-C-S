package Proyectos.Curso_Cliente_Servidor;

import java.time.LocalDateTime;
import java.util.Objects;

 public class Paquete {
 
    private final int idPaquete;
    private final LocalDateTime fechaCreacion;

    private String direccionOrigen;
    private String direccionDestino;
    private double pesoKg;
    private double costoEnvio;
    private EstadoPaquete estado;
    private Integer idVehiculoAsignado; // Puede ser null si no está asignado
    private LocalDateTime fechaEntrega; // Puede ser null si no ha sido entregado
    private String contenido;
    

    public Paquete(int idPaquete, String direccionOrigen, String direccionDestino, double pesoKg, double costoEnvio, String contenido) {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPaquete.PENDIENTE;
        this.idVehiculoAsignado = null;
        this.fechaEntrega = null;

        // Validaciones de campos obligatorios //
        this.direccionOrigen = Objects.requireNonNull(direccionOrigen, "La dirección de origen no puede ser nula");
        this.direccionDestino = Objects.requireNonNull(direccionDestino, "La dirección de destino no puede ser nula");
        this.contenido = Objects.requireNonNull(contenido, "El contenido del paquete no puede ser nulo");

        // Validaciones de peso y costo de envío y ID del paquete //
        if (pesoKg <= 0) {
            throw new IllegalArgumentException("El peso del paquete debe ser mayor que cero");
        } else {
            this.pesoKg = pesoKg;   
        }       
        if (costoEnvio < 0) {
            throw new IllegalArgumentException("El costo de envío no puede ser negativo");
        } else {
            this.costoEnvio = costoEnvio;
        }
        if (idPaquete <= 0) {
            throw new IllegalArgumentException("El ID del paquete debe ser un número positivo");
        }else {
            this.idPaquete = idPaquete;
        }
    }

    // getters // 

    public int getIdPaquete() {
        return idPaquete;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public String getDireccionOrigen() {
        return direccionOrigen;
    }
    public String getDireccionDestino() {
        return direccionDestino;
    }
    public double getPesoKg() {
        return pesoKg;
    }
    public double getCostoEnvio() {
        return costoEnvio;
    }
    public EstadoPaquete getEstado() {
        return estado;
    }
    public Integer getIdVehiculoAsignado() {
        return idVehiculoAsignado;
    }
    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }
    public String getContenido() {
        return contenido;
    }

    //metodos//

    public void cambiarEstado(EstadoPaquete nuevoEstado) {
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

    public void asignarVehiculo(int idVehiculo) {
        if (this.estado != EstadoPaquete.PENDIENTE) {
            throw new IllegalStateException("El paquete no está en estado PENDIENTE y no se le puede asignar un vehículo");
        }
        if (this.idVehiculoAsignado != null) {
            throw new IllegalStateException("El paquete ya tiene un vehículo asignado");
        }
        if (idVehiculo <= 0 ) {
            throw new IllegalArgumentException("El ID del vehículo no es válido");
        }
        this.idVehiculoAsignado = idVehiculo;
        cambiarEstado(EstadoPaquete.ASIGNADO);  
    }

    public void registrarEntrega(LocalDateTime fechaEntrega) {
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
    
 