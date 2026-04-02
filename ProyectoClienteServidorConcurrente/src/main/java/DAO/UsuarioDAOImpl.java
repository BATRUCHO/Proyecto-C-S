
package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Dominio.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {

    
    public boolean guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (NOMBRE, CORREO, CLAVE, ROL) VALUES (?, ?, ?, ?)";
        // Usar try-with-resources para cerrar PreparedStatement automáticamente
        try (Connection conexion = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNombre());
            
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getClave());
            ps.setString(4, usuario.getRol());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Usuario guardado correctamente.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }

        return false;
    }

   
    public Usuario buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection conexion = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery(); // Idealmente también en try-with-resources

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setCorreo(rs.getString("CORREO"));
                usuario.setClave(rs.getString("CLAVE"));
                usuario.setRol(rs.getString("ROL"));
                return usuario;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por ID: " + e.getMessage());
        }

        return null;
    }

    
    public Usuario buscarUsuarioPorCorreo(String correo) {
        String sql = "SELECT * FROM USUARIO WHERE CORREO = ?";

        try {
            Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, correo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setCorreo(rs.getString("CORREO"));
                usuario.setClave(rs.getString("CLAVE"));
                usuario.setRol(rs.getString("ROL"));
                return usuario;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por correo: " + e.getMessage());
        }

        return null;
    }


    @Override
    public boolean guardarUsuario(Dominio.Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardarUsuario'");
    }


    @Override
    public Dominio.Usuario eliminUsuario(int idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminUsuario'");
    }


    @Override
    public List<Dominio.Usuario> listarUsuarios() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarUsuarios'");
    }
}
