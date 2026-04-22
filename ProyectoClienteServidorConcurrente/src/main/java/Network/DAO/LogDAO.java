package Network.DAO;

   import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    import Dominio.Excepciones.LogSistema;
    import Network.BD.ConexionMySQL;


public class LogDAO {


    public boolean registrarAccion(LogSistema log) {
        String sql = "INSERT INTO logs_sistema (id_usuario, accion) VALUES (?, ?)";
        
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, log.getIdUsuario());
            ps.setString(2, log.getAccion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
