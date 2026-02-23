package Proyectos.Curso_Cliente_Servidor;
import java.time.LocalDateTime; 

public class Carro  extends Vehiculo {

    public Carro(int idVehiculo, String placa, TipoVehiculo tipovehiculo, LocalDateTime fechacreacion, EstadoVehiculo estado, boolean activo, LocalDateTime ultimoReporte, String ubicacionActual) {
        super(idVehiculo, tipovehiculo, placa, fechacreacion, estado, activo, ultimoReporte, ubicacionActual);
    }

    @Override
    public double getCapacidadMaximaKG() {
        return 200.0; // Capacidad máxima para un carro
    }

    @Override
    public double getVelocidadPromedioKmH() {
        return 70.0; // Velocidad promedio para un carro
    }
    
}
