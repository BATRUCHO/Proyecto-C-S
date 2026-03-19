package Dominio;
import java.time.LocalDateTime;

public class Bicicleta extends Vehiculo {

    public Bicicleta(int idVehiculo, String placa, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, TipoVehiculo tipo) {
        super(idVehiculo, placa, tipo);
        this.estado = estado;
        this.activo = activo;
        this.ultimoReporte = ultimoReporte;
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 10.0; // Capacidad máxima para una bicicleta
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 10.0; // Velocidad promedio para una bicicleta
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
