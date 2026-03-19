package Dominio;
import java.time.LocalDateTime; 

public class Carro  extends Vehiculo {

    public Carro(int idVehiculo, String placa, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, TipoVehiculo tipo) {
        super(idVehiculo, placa, tipo);
        this.estado = estado;
        this.activo = activo;
        this.ultimoReporte = ultimoReporte;
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 200.0; // Capacidad máxima para un carro
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 70.0; // Velocidad promedio para un carro
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
