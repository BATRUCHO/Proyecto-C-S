package Proyectos.Curso_Cliente_Servidor;
import java.time.LocalDateTime;


public abstract class Vehiculo {
    protected final int idVehiculo;
    protected final String placa;
    protected final LocalDateTime fechacreacion;
    protected boolean activo;
    protected LocalDateTime ultimoReporte;
    protected String ubicacionActual;
    protected final TipoVehiculo tipovehiculo;
    protected final EstadoVehiculo estado;

    protected Vehiculo(int idVehiculo,TipoVehiculo tipovehiculo, String placa, LocalDateTime fechacreacion, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, String ubicacionActual) {
        this.idVehiculo = idVehiculo;
        this.tipovehiculo = tipovehiculo;
        this.placa = placa;
        this.fechacreacion = fechacreacion;
        this.estado = estado;
        this.ubicacionActual = ubicacionActual;
        this.activo = activo;
        this.ultimoReporte = ultimoReporte;
    }

     //getters//
    public int getIdVehiculo() {
        return idVehiculo;
    }
    public String getPlaca() {
        return placa;
    }
    public LocalDateTime getFechacreacion() {
        return fechacreacion;
    }
    public EstadoVehiculo getEstadoVehiculo() {
        return estado;
    }
    public boolean getActivo() {
        return activo;
    }
    public LocalDateTime getUltimoReporte() {
        return ultimoReporte;                                   
    }
    public String getUbicacionActual() {
        return ubicacionActual;
    }
    public TipoVehiculo getTipovehiculo() {
        return tipovehiculo;
    }

    //Metodos //

    public abstract double getCapacidadMaximaKG();

    public abstract double getVelocidadPromedioKmH();
    
    public boolean estaDisponible() {
        return this.estado == EstadoVehiculo.DISPONIBLE;     
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
