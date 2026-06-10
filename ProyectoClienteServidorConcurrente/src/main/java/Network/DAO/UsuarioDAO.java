package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.Usuarios;
import Network.BD.ConexionMySQL;

public class UsuarioDAO {

    // Método auxiliar para mapear de manera uniforme
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
            rs.getTimestamp("fecha_creacion"), 
            rs.getBoolean("activo"),
            rs.getInt("id_rol")    
        );
    }

    // 1. REGISTRAR USUARIO 
    public boolean registrarUsuario(Usuarios usuario) {
        String sql = "INSERT INTO usuarios (nombre, apellido, fechaNacimiento, dni, email, telefono, password, activo, id_rol) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setDate(3, usuario.getFechaNacimiento());
            ps.setString(4, usuario.getDni());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getTelefono());
            ps.setString(7, usuario.getPassword());
            ps.setBoolean(8, usuario.isActivo());
            ps.setInt(9, usuario.getIdRol());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // 2. ALTERAR_ESTADO_USUARIO 
    public boolean alternarEstadoUsuario(int idUsuario) {
        // Dejamos el valor 0 fijo para asegurar que la acción siempre inactive al usuario
        String sql = "UPDATE usuarios SET activo = (1-activo) WHERE id_usuario = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en alternar estado de usuario: " + e.getMessage());
            return false;
        }
    }
   
    // 3. LISTAR USUARIOS 
    public List<Usuarios> listarUsuarios() {
        List<Usuarios> listaUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conexion = ConexionMySQL.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);  
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaUsuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return listaUsuarios;
    }

    // 4. VALIDAR LOGIN 
    public Usuarios validarLogin(String email, String password) {
        // Garantiza seguridad: solo autentica si coincide credenciales Y el usuario está activo
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? AND activo = 1";

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

    // 5. EDITAR USUARIO 

    public boolean editarUsuario(Usuarios usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, fechaNacimiento = ?, dni = ?, email = ?, telefono = ?, password = ?, id_rol = ? WHERE id_usuario = ?";

        try(Connection c = ConexionMySQL.getConexion();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setDate(3, usuario.getFechaNacimiento());
            ps.setString(4, usuario.getDni());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getTelefono());
            ps.setString(7, usuario.getPassword());
            ps.setInt(8, usuario.getIdRol());
            ps.setInt(9, usuario.getId_usuario());

            return ps.executeUpdate() > 0;

        }catch(SQLException e){
            System.err.println("Error al editar usuario: " + e.getMessage());
            return false;
        }
    }
}
