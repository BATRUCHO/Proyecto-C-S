package Network.DAO;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    import Dominio.Excepciones.LogSistema;
    import Network.BD.ConexionMySQL;


public class LogDAO {

   public void registrarEvento(int idUsuario, String accion, String detalle) {
    String sql = "INSERT INTO logs_sistema (id_usuario, accion, detalle, fecha_hora) VALUES (?, ?, ?, NOW())";
    // Usamos el ConfigLoader que ya arreglamos para la conexión segura
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
}
