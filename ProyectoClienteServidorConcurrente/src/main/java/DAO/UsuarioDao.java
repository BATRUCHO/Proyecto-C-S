package DAO;

import java.util.List;

import Dominio.Usuario;




public interface UsuarioDAO {
   
    boolean guardarUsuario(Usuario usuario);

    Usuario buscarUsuarioPorId(int idUsuario);

    Usuario buscarUsuarioPorCorreo(String correo);

    Usuario eliminarUsuario(int idUsuario);

    List<Usuario> listarUsuarios();

}
