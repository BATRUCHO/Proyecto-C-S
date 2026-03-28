package Conexion;

/**
 *
 * @author Asus Vivobook
 */
import Dominio.Vehiculo;

public interface VehiculoDAO {
    boolean guardarVehiculo(Vehiculo vehiculo);
    Vehiculo buscarVehiculoPorId(int idVehiculo);
    Vehiculo buscarVehiculoPorPlaca(String placa);
}
