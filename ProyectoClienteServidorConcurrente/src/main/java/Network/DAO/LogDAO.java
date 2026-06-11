package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.Excepciones.LogSistema;
import Network.BD.ConexionMySQL;


public class LogDAO {

    private LogSistema mapearLogs(ResultSet rs) throws SQLException {
        return new LogSistema(
            rs.getInt("id_log"),          
            rs.getInt("id_usuario"),      
            rs.getString("accion"),      
            rs.getString("detalles"),    
            rs.getTimestamp("fecha_hora"),
            rs.getString("nombre_completo"),
            rs.getString("rol_usuario")
        );
    }

   public void registrarEvento(int idUsuario, String accion, String detalle) {
    String sql = "INSERT INTO logs_sistema (id_usuario, accion, detalles, fecha_hora) VALUES (?, ?, ?, NOW())";
    
        try (Connection cn = Network.BD.ConexionMySQL.getConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, accion);
            ps.setString(3, detalle);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error persistiendo en DB: " + e.getMessage());
        }
    }

    public List<LogSistema> listarEventosSistema() {

        List<LogSistema> listaLogs = new ArrayList<>();
        String sql = "SELECT * FROM vista_auditoria_completa ORDER BY fecha_hora DESC";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql);  
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaLogs.add(mapearLogs(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar logs del sistema: " + e.getMessage());
        }
        return listaLogs;
    }

}
