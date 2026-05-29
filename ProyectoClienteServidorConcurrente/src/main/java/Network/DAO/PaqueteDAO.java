package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        rs.getBigDecimal("peso"),
        rs.getTimestamp("fecha_creacion")
    );
}
   
    public boolean crearPaquete(Paquete paquete) {      
        String sql = "INSERT INTO paquetes (descripcion, remitente, destinatario, direccion_entrega, peso, id_estado, fecha_creacion, id_vehiculo) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, paquete.getDescripcion());
            ps.setString(2, paquete.getRemitente());
            ps.setString(3, paquete.getDestinatario());
            ps.setString(4, paquete.getDireccion_entrega());
            
            // Manejo del Peso (BigDecimal)
            ps.setBigDecimal(5, paquete.getPeso()); 
            
            // Manejo de Estados y Vehículos (Int)
            ps.setInt(6, paquete.getId_estado());
            
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error al crear paquete en DB: " + e.getMessage());
            return false;
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
                + "direccion_entrega=?, peso=?, id_estado=?"
                + "WHERE id_paquete = ?";
        
        try (Connection conexion =  ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)){

                ps.setString(1, paquete.getDescripcion());
                ps.setString(2, paquete.getRemitente());
                ps.setString(3, paquete.getDestinatario());
                ps.setString(4, paquete.getDireccion_entrega());
                ps.setBigDecimal(5, paquete.getPeso());
                ps.setInt(6, paquete.getId_estado());

                // El ultimo parametro a actualizar es el ID único
                ps.setInt(7, paquete.getId_paquete());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Error al actualizar paquete: " + e.getMessage());
                return false;

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

}
