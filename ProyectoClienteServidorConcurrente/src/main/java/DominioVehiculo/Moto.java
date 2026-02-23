package Proyectos.Curso_Cliente_Servidor;
import java.time.LocalDateTime;

public class Moto extends Vehiculo {

    public Moto(int idVehiculo, String placa, TipoVehiculo tipovehiculo, LocalDateTime fechacreacion, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, String ubicacionActual) {
        super(idVehiculo, tipovehiculo, placa, fechacreacion, estado, activo, ultimoReporte, ubicacionActual);
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 25.0; // Capacidad máxima para una moto
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 75.0; // Velocidad promedio para una moto
    }
}