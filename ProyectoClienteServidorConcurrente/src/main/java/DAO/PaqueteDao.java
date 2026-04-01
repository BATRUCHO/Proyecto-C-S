package DAO;


import Dominio.Paquete;

public interface PaqueteDAO {

boolean guardarPaquete(Paquete paquete);
Paquete buscarPaquetePorId(int idPaquete);
Paquete buscarPaquetePorContenido(String contenido);
}
