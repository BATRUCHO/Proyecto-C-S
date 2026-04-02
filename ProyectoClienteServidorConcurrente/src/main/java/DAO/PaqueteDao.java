package DAO;


import Dominio.Paquete;
import Dominio.EstadoPaquete;
import java.util.List;

public interface PaqueteDAO {
    boolean guardarPaquete(Paquete paquete);
    Paquete buscarPaquetePorId(int idPaquete);
    Paquete buscarPaquetePorContenido(String contenido);
    List<Paquete> listarPaquetes();
    void actualizarEstado(int id, EstadoPaquete estado);
    void asignarVehiculo(int idPaquete, int idVehiculo);
}
