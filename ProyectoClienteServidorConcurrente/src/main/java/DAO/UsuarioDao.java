package DAO;

import java.util.List;

import Conexion.Usuario;
import Dominio.Usuario;
import Dominio.Rol;


/**
 *
 * @author Asus Vivobook
 */
public interface UsuarioDAO {
   
    boolean guardarUsuario(Usuario usuario);

    Usuario buscarUsuarioPorId(int idUsuario);

    Usuario buscarUsuarioPorCorreo(String correo);

    Usuario eliminUsuario(int idUsuario);

    List<Usuario> listarUsuarios();
+
}
