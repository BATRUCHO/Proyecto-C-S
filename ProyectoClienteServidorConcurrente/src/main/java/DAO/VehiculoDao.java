package DAO;

/**
 *
 * @author Asus Vivobook
 */

import java.util.List;

import Dominio.EstadoVehiculo;
import Dominio.Vehiculo; 


public interface VehiculoDAO {

    boolean guardarVehiculo(Vehiculo vehiculo);
    
    Vehiculo buscarVehiculoPorId(int idVehiculo);

    Vehiculo buscarVehiculoPorPlaca(String placa);
    
    void actualizarEstado(int id, EstadoVehiculo estado);
    
    List<Vehiculo> listarVehiculosActivos();
    
}
