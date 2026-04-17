package DAO;


import java.util.List;

import Dominio.EstadoPaquete;
import Dominio.Paquete;

public interface PaqueteDAO {

    boolean guardarPaquete(Paquete paquete);

    Paquete buscarPaquetePorId(int idPaquete);

    Paquete buscarPaquetePorContenido(String contenido);
    
    void actualizarEstado(int id, EstadoPaquete estado);

    void asignarVehiculo(int idPaquete, int idVehiculo);
    
    List<Paquete> listarPaquetes();
}
