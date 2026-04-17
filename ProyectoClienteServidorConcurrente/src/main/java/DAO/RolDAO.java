package DAO;

import java.util.List;

import Dominio.Rol;

public interface RolDAO {

    boolean guardarRol(Rol rol);

    Rol buscarRolPorId(int idRol);

    Rol buscarRolPorNombre(String nombreRol);

    void actualizarDescripcion(int id, String nuevaDescripcion);
    
    List<Rol> listarRoles();


}
