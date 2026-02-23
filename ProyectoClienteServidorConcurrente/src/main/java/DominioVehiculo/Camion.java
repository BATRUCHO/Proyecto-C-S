package Proyectos.Curso_Cliente_Servidor;
import java.time.LocalDateTime;

public class Camion extends Vehiculo {

    public Camion(int idVehiculo, String placa, TipoVehiculo tipovehiculo, LocalDateTime fechacreacion, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, String ubicacionActual) {
        super(idVehiculo, tipovehiculo, placa, fechacreacion, estado, activo, ultimoReporte, ubicacionActual);
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 2000.0; // Capacidad máxima para un camión
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 70.0; // Velocidad promedio para un camión
    }
    
}
