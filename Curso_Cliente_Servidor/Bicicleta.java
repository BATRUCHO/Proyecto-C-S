package Proyectos.Curso_Cliente_Servidor;
import java.time.LocalDateTime;

public class Bicicleta extends Vehiculo {

    public Bicicleta(int idVehiculo, String placa, TipoVehiculo tipovehiculo, LocalDateTime fechacreacion, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, String ubicacionActual) {
        super(idVehiculo, tipovehiculo, placa, fechacreacion, estado, activo, ultimoReporte, ubicacionActual);
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 10.0; // Capacidad máxima para una bicicleta
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 10.0; // Velocidad promedio para una bicicleta
    }
    
}
