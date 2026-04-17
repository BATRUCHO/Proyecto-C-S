
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import Dominio.Rol;
import Dominio.Usuario;



public class UsuarioDAOImpl implements UsuarioDAO {

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        String dni = rs.getString("DNI");
        String nombre = rs.getString("NOMBRE");
        String apellido = rs.getString("APELLIDO");
        String email = rs.getString("CORREO");
        String telefono = rs.getString("TELEFONO");
        String clave = rs.getString("CLAVE");
        LocalDate fechaNac = rs.getDate("FECHA_NAC").toLocalDate();
        
        // Obtenemos el Rol (Asumiendo que tienes un Enum o DAO para Rol)
        ///Rol rol = Rol.valueOf(rs.getString("ROL")); // pendiente
    
        return new Usuario(dni, fechaNac, nombre, apellido, email, telefono, clave, rol);
    }

    @Override
    public boolean guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (DNI, NOMBRE, APELLIDO, CORREO, TELEFONO, CLAVE, FECHA_NAC, ROL) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {        
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getPassword());
            ps.setDate(7, java.sql.Date.valueOf(usuario.getFechaNacimiento()));
            ///ps.setString(8, usuario.getRol().name()); // pendiente

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }
   
    @Override
    public Usuario buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM USUARIO WHERE ID_USUARIO = ?";

    try (Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs); 
                }            
            } 
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public Usuario buscarUsuarioPorCorreo(String email) {
        String sql = "SELECT * FROM USUARIO WHERE CORREO = ?";

    try (Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs); 
                }            
            } 
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por email:" + e.getMessage());
        }
        return null;
    }        

    @Override
    public Usuario eliminarUsuario(int idUsuario) {
        String sql = "UPDATE USUARIO SET ACTIVO = 0 WHERE ID_USUARIO = ?";

        try (Connection conexion = ConexionDB.getInstance().getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

                ps.setInt(1, idUsuario);
                int filasAfectadas = ps.executeUpdate();
                
                if (filasAfectadas > 0) {
                    return buscarUsuarioPorId(idUsuario);
                }

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> listaUsuarios= new ArrayList<>();
        String sql = "SELECT * FROM USUARIO WHERE ACTIVO = 1";

        try (Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);  
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            Usuario u = mapearUsuario(rs);
            listaUsuarios.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Usuarios: " + e.getMessage());
        }
        return listaUsuarios;
    
        }
    }


