package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.Vehiculo;
import Network.BD.ConexionMySQL;

public class VehiculoDAO  {


private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {

   return new Vehiculo(
        rs.getInt("id_vehiculo"),
        rs.getString("placa"),
        rs.getString("marca"),
        rs.getString("modelo"),
        rs.getInt("id_tipoVehiculo"),
        rs.getInt("id_estado_vehiculo")
    );

}
    
    public boolean registrarVehiculo(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, id_tipoVehiculo, id_estado_vehiculo) VALUES (?, ?, ?, ?, ?)";
            
        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getId_tipoVehiculo());
            ps.setInt(5, vehiculo.getId_estado_vehiculo());
                    
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar vehiculo: " + e.getMessage());
            return false;
        }
    }


    public boolean editarVehiculo(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculos SET placa = ?, marca = ?, modelo = ?, id_tipoVehiculo = ?, estado = ?, id_tipo_vehiculo = ?, id_estado_vehiculo = ? WHERE id_vehiculo = ?";
        
        try(Connection c = ConexionMySQL.getConexion();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getId_tipoVehiculo());
            ps.setInt(5, vehiculo.getId_estado_vehiculo());
            
            return ps.executeUpdate() > 0;

            }catch(SQLException e){
                System.err.println("Error al editar vehiculo: " + e.getMessage());
                return false;
            }
    }

    public boolean eliminarVehiculo(int id) {
        String sql = "DELETE FROM vehiculos WHERE id_vehiculo = ?";

        try(Connection c = ConexionMySQL.getConexion();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        }catch(SQLException e){
            System.err.println("Error al eliminar vehiculo: " + e.getMessage());
            return false;
        }

    }

      
    public List<Vehiculo> listarVehiculosActivos() {
        List<Vehiculo> listaVehiculos= new ArrayList<>();

        String sql = "SELECT * FROM vehiculos ";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql);  
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            Vehiculo v = mapearVehiculo(rs);
            listaVehiculos.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar vehiculos: " + e.getMessage());
        }
        return listaVehiculos;
        
        }


}
