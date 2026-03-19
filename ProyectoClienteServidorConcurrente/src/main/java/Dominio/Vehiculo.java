package Dominio;

import java.io.Serializable;
import java.time.LocalDateTime;


public abstract class Vehiculo implements Serializable {

    protected final int idVehiculo;
    protected final String placa;
    protected EstadoVehiculo estado; // Quitar 'final' para poder cambiarlo
    protected boolean activo;
    protected LocalDateTime ultimoReporte;
    protected final TipoVehiculo tipo;
   
    protected Vehiculo(int idVehiculo, String placa, TipoVehiculo tipo) {
        this.idVehiculo = idVehiculo;
        this.placa = placa;
        this.tipo = tipo;
        this.estado = EstadoVehiculo.DISPONIBLE;
        this.activo = true;
        this.ultimoReporte = LocalDateTime.now();
    }
   
 // Getters
    
    public int getIdVehiculo() {
        return idVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getUltimoReporte() {
        return ultimoReporte;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }


    //Metodos //

    public abstract double getCapacidadMaximaKG();

    public abstract double getVelocidadPromedioKmH();
    
    public boolean estaDisponible() {
        return this.estado == EstadoVehiculo.DISPONIBLE;     
    }

    public boolean realizarEntrega(EstadoVehiculo nuevoEstadoVehiculo) {
        if (!estaDisponible()) {
            return false;
        }
        this.estado = nuevoEstadoVehiculo;
        this.ultimoReporte = LocalDateTime.now();
        return true;
    }

    public boolean puedeTransportar(double pesoCargaKg) { //pendiente definir el tipo de carga double pesoCargaKg)
        if (!this.estaDisponible()) {
            return false;
        }
        if (!this.activo) {
            return false;
        }
        return pesoCargaKg <= getCapacidadMaximaKG();
    }
      
}
