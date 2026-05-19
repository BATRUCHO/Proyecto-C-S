package Network.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.EstadoPaquete;
import Dominio.Incidencias;
import Dominio.Paquete;
import Network.BD.ConexionMySQL;


public class PaqueteDAO  {

  private Paquete mapearPaquete(ResultSet rs) throws SQLException {
    return new Paquete(
        rs.getInt("id_paquete"),
        rs.getString("descripcion"),
        rs.getString("remitente"),
        rs.getString("destinatario"),
        rs.getString("direccion_entrega"),
        rs.getDouble("peso"),
        rs.getInt("id_estado"),
        rs.getDate("fecha_creacion"),
        rs.getInt("id_vehiculo")
    );
}
   
    public boolean crearPaquete(Paquete paquete) {      
        String sql = "INSERT INTO paquetes (descripcion, remitente, destinatario, direccion_entrega, peso, id_estado, fecha_creacion, id_vehiculo) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, paquete.getDescripcion());
            ps.setString(2, paquete.getRemitente());
            ps.setString(3, paquete.getDestinatario());
            ps.setString(4, paquete.getDireccion_entrega());
            
            // Manejo del Peso (Double)
            ps.setDouble(5, paquete.getPeso()); 
            
            // Manejo de Estados y Vehículos (Int)
            ps.setInt(6, paquete.getId_estado());
            ps.setInt(7, paquete.getId_vehiculo());
            
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error al crear paquete en DB: " + e.getMessage());
            return false;
        }
    }

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

    
    public void actualizarEstadoPaquete(int idPaquete, EstadoPaquete nuevoEstado) {
        String sql = "UPDATE paquetes SET id_estado = ? WHERE id_paquete = ?";
        
            try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
                
                // Aquí extraemos el ID numérico del Enum usando el getter que creaste
                ps.setInt(1, nuevoEstado.getId()); 
                ps.setInt(2, idPaquete);
                
                int filasAfectadas = ps.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("Estado del paquete " + idPaquete + " actualizado a: " + nuevoEstado.name());
                }
            
            } catch (SQLException e) {
                System.err.println("Error al actualizar estado del paquete: " + e.getMessage());
            }
    }

    public boolean eliminarPaquete(int idPaquete) {
        String sql = "DELETE FROM paquetes WHERE id_paquete = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPaquete);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar paquete: " + e.getMessage());
            return false;
        }
    }

    public boolean EditarPaquete (Paquete paquete){
        // Usamos UPDATE y filtramos por el ID único
        String sql = "UPDATE paquetes SET descripcion=?, remitente=?, destinatario=?, "
                + "direccion_entrega=?, peso=?, id_estado=?, id_vehiculo=? "
                + "WHERE id_paquete = ?";
        
        try (Connection conexion =  ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)){

                ps.setString(1, paquete.getDescripcion());
                ps.setString(2, paquete.getRemitente());
                ps.setString(3, paquete.getDestinatario());
                ps.setString(4, paquete.getDireccion_entrega());
                ps.setDouble(5, paquete.getPeso());
                ps.setInt(6, paquete.getId_estado());
                ps.setInt(7, paquete.getId_vehiculo());

                // El ultimo parametro a actualizar es el ID único
                ps.setInt(8, paquete.getId_paquete());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Error al actualizar paquete: " + e.getMessage());
                return false;

            }
    }

  
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

   
    public List<Paquete> listarPaquetes() {
        List<Paquete> listaPaquetes = new ArrayList<>();
        String sql = "SELECT * FROM paquetes ";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql);  
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            Paquete p = mapearPaquete(rs);
            listaPaquetes.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar paquetes: " + e.getMessage());
        }
        return listaPaquetes;

        }

    public List<Paquete> listarPaquetesPorConductor(int idConductor) {
        List<Paquete> lista = new ArrayList<>();
        // Consultamos los paquetes que están en la tabla de asignaciones para ese conductor
        String sql = "SELECT p.* FROM paquetes p " +
                    "INNER JOIN asignaciones a ON p.id_paquete = a.id_paquete " +
                    "WHERE a.id_conductor = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setInt(1, idConductor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPaquete(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar paquetes por conductor: " + e.getMessage());
        }
        return lista;
    }

}
