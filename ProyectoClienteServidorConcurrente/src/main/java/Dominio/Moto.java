package Dominio;
import java.time.LocalDateTime;

public class Moto extends Vehiculo {

     public Moto(int idVehiculo, String placa, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, TipoVehiculo tipo) {
        super(idVehiculo, placa, tipo);
        this.estado = estado;
        this.activo = activo;
        this.ultimoReporte = ultimoReporte;
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 25.0; // Capacidad máxima para una moto
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 75.0; // Velocidad promedio para una moto
    }

    @Override
    public boolean realizarEntrega(EstadoVehiculo nuevoEstadoVehiculo) {
        if (!estaDisponible()) {
            return false;
        }
        this.estado = nuevoEstadoVehiculo;
        this.ultimoReporte = LocalDateTime.now();
        return true;
    }
}