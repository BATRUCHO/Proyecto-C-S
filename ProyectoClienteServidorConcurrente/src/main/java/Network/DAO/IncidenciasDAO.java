package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Dominio.Incidencias;
import Network.BD.ConexionMySQL;

public class IncidenciasDAO {


 /* 
       public void registrarIncidencias(Incidencias in) {
        String sql = "INSERT INTO incidencias (id_paquete, id_conductor, descripcion) VALUES (?, ?, ?)";
            try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, in.getIdPaquete());
                ps.setInt(2, in.getIdConductor());
                ps.setString(3, in.getDescripcion());
                ps.executeUpdate();
        }   catch (SQLException e) {
            System.err.println("Error al registrar incidencia: " + e.getMessage());
        }
    }

    */

}
