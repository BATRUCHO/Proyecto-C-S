package DAO;

import Conexion.Usuario;

/**
 *
 * @author Asus Vivobook
 */
public interface UsuarioDAO {
   
    boolean guardarUsuario(Usuario usuario);
    Usuario buscarUsuarioPorId(int idUsuario);
    Usuario buscarUsuarioPorCorreo(String correo);
}
