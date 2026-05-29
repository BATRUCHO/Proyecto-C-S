package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Network.BD.ConexionMySQL;

public class AsignacionesDAO {

public boolean asignarPaquete(int idPaquete, int idConductor) {
        String sqlAsignacion = "INSERT INTO asignaciones (id_paquete, id_conductor) VALUES (?, ?)";
        String sqlPaquete = "UPDATE paquetes SET id_estado = ? WHERE id_paquete = ?";

        Connection conexion = null;
        try {
            conexion = ConexionMySQL.getConexion();
            conexion.setAutoCommit(false);

            // Ejecutar inserción en asignaciones
            try (PreparedStatement psAsig = conexion.prepareStatement(sqlAsignacion)) {
                psAsig.setInt(1, idPaquete);
                psAsig.setInt(2, idConductor);
                psAsig.executeUpdate();
            }
            // Ejecutar actualización en paquetes
            try (PreparedStatement psPkg = conexion.prepareStatement(sqlPaquete)) {
                psPkg.setInt(1, 2); // Estado: En Tránsito
                psPkg.setInt(2, idPaquete);
                psPkg.executeUpdate();
            }

            conexion.commit(); 
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try { conexion.rollback(); } catch (SQLException ex) { System.err.println("Error al hacer rollback: " + ex.getMessage()); }
            }
            System.err.println("Error al asignar paquete: " + e.getMessage());
            return false;
        } finally {
            try { if(conexion != null) conexion.close(); } catch (SQLException e) { System.err.println("Error al cerrar conexión: " + e.getMessage());}
        }
    }


}
