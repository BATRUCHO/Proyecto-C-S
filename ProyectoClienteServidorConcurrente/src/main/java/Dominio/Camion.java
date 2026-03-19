package Dominio;
import java.time.LocalDateTime;

public class Camion extends Vehiculo {

    public Camion(int idVehiculo, String placa, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, TipoVehiculo tipo) {
        super(idVehiculo, placa, tipo);
        this.estado = estado;
        this.activo = activo;
        this.ultimoReporte = ultimoReporte;
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 2000.0; // Capacidad máxima para un camión
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 70.0; // Velocidad promedio para un camión
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
