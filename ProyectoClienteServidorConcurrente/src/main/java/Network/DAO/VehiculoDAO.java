package Network.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.EstadoVehiculo;
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
        rs.getString("estado")
    );

}
    
    public boolean registrarVehiculo(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, id_tipoVehiculo, estado) VALUES (?, ?, ?, ?, ?)";
            
        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getId_tipo_vehiculo());
            ps.setString(5, vehiculo.getEstado());
                    
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean editarVehiculo(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculos SET placa = ?, marca = ?, modelo = ?, id_tipoVehiculo = ?, estado = ? WHERE id_vehiculo = ?";
        
        try(Connection c = ConexionMySQL.getConexion();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getId_tipo_vehiculo());
            ps.setString(5, vehiculo.getEstado());
            ps.setInt(6, vehiculo.getId_vehiculo());
            
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
            e.printStackTrace();
        }
        return listaVehiculos;
        
        }












        public void actualizarEstadoVehiculo(int id, EstadoVehiculo estado) {
            String sql = "UPDATE vehiculos SET estado = ? WHERE id_vehiculo = ?";
            try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
                
                ps.setString(1, estado.name()); // O estado.getTexto() si lo implementaste
                ps.setInt(2, id);
                ps.executeUpdate();
                
            } catch (SQLException e) {      
                e.printStackTrace();
            }
        }



        public void registrarUbicacion(int id_vehiculo, double latitud, double longitud) {
            
            String sql = "INSERT INTO ubicaciones_vehiculos (id_vehiculo, latitud, longitud) VALUES (?, ?, ?)";
            
            try (Connection con = ConexionMySQL.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id_vehiculo);
                ps.setDouble(2, latitud);
                ps.setDouble(3, longitud);
                
                ps.executeUpdate();
            }catch(SQLException e) {
                e.printStackTrace();
            }    
        }

}




