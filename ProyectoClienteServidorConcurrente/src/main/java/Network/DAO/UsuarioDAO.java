
package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.Usuarios;
import Network.BD.ConexionMySQL;

public class UsuarioDAO  {

private Usuarios mapearUsuario(ResultSet rs) throws SQLException {
    return new Usuarios(
        rs.getInt("id_usuario"),
        rs.getString("nombre"),
        rs.getString("apellido"),
        rs.getDate("fechaNacimiento"),
        rs.getString("dni"),
        rs.getString("email"),
        rs.getString("telefono"),
        rs.getString("password"),
        rs.getInt("id_rol")
    );
}

    public boolean registrarUsuario(Usuarios usuario) {

    String sql = "INSERT INTO usuarios (nombre, apellido, fechaNacimiento, dni, email, telefono, password, id_rol) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection con = ConexionMySQL.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, usuario.getNombre());
        ps.setString(2, usuario.getApellido());
        ps.setDate(3,usuario.getFechaNacimiento());
        ps.setString(4, usuario.getDni());
        ps.setString(5, usuario.getEmail());
        ps.setString(6, usuario.getTelefono());
        ps.setString(7, usuario.getPassword());
        ps.setInt(8, usuario.getIdRol());
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al registrar usuario: " + e.getMessage());
        return false;
    }
    
    }

    public Usuarios eliminarUsuario(int idUsuario) {
            String sql = "UPDATE usuarios SET activo = 0 WHERE id_usuario = ?";

            try (Connection conexion = ConexionMySQL.getConexion();
                    PreparedStatement ps = conexion.prepareStatement(sql)) {

                    ps.setInt(1, idUsuario);
                    int filasAfectadas = ps.executeUpdate();
                    
                    if (filasAfectadas > 0) {
                        return buscarUsuarioPorId(idUsuario);
                    }

            } catch (SQLException e) {
                System.err.println("Error al eliminar usuario: " + e.getMessage());
            }
            return null;
        }
   
    public List<Usuarios> listarUsuarios() {
        List<Usuarios> listaUsuarios= new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE ";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql);  
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            Usuarios u = mapearUsuario(rs);
            listaUsuarios.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return listaUsuarios;
    }


    public Usuarios validarLogin(String email, String password) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? ";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
        }
        return null;
    }


        // ------------------SQL Secundarios------------------- //

    public Usuarios buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }
    
    
    public Usuarios buscarUsuarioPorCorreo(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs); 
                }            
            } 
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por correo: " + e.getMessage());
        }
        return null;
    }        

}
