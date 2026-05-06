
package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.Conductores;
import Dominio.Usuario;
import Network.BD.ConexionMySQL;

public class UsuarioDAO  {

private Usuario mapearUsuario(ResultSet rs) throws SQLException {
    return new Usuario(
        rs.getInt("id_usuario"),
        rs.getString("dni"),
        rs.getDate("fechaNacimiento"),
        rs.getString("nombre"),
        rs.getString("apellido"),
        rs.getString("email"),
        rs.getString("telefono"),
        rs.getString("password"),
        rs.getInt("id_rol")
    );
}

    public boolean registrarUsuario(Usuario usuario) {

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

    public Usuario eliminarUsuario(int idUsuario) {
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
   
    public List<Usuario> listarUsuarios() {
        List<Usuario> listaUsuarios= new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE ";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql);  
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            Usuario u = mapearUsuario(rs);
            listaUsuarios.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return listaUsuarios;
        }


    public Usuario validarLogin(String email, String password) {
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

    public Usuario buscarUsuarioPorId(int idUsuario) {
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
    
    
    public Usuario buscarUsuarioPorCorreo(String email) {
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


    public Conductores obtenerConductorPorUsuario(int id_usuario) {
    String sql = "SELECT id_conductor, nombre, cedula, id_usuario, id_vehiculo_asignado FROM conductores WHERE id_usuario = ?";

    try (Connection conexion = ConexionMySQL.getConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setInt(1, id_usuario);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                
                return new Conductores(
                    rs.getInt("id_conductor"),
                    rs.getString("nombre"),
                    rs.getString("cedula"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_vehiculo_asignado")
                );
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener conductor por usuario: " + e.getMessage());
    }
    return null;
    }
}
