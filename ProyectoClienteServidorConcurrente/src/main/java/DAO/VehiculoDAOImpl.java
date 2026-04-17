package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Dominio.Bicicleta;
import Dominio.Camion;
import Dominio.Carro;
import Dominio.EstadoVehiculo;
import Dominio.Moto;
import Dominio.TipoVehiculo;
import Dominio.Vehiculo;


public class VehiculoDAOImpl implements VehiculoDAO {


private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
    int id = rs.getInt("ID_VEHICULO");
    String placa = rs.getString("PLACA");
    String tipoStr = rs.getString("TIPO_VEHICULO");
    
    EstadoVehiculo estado = EstadoVehiculo.valueOf(rs.getString("ESTADO_VEHICULO"));
    TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(tipoStr);
    boolean activo = rs.getBoolean("ACTIVO");
    LocalDateTime reporte = rs.getTimestamp("ULTIMO_REPORTE").toLocalDateTime();


    return switch (tipoStr) {
        
        case "MOTO" -> new Moto(id, placa, estado,activo, reporte,tipoVehiculo);
        case "CAMION" -> new Camion(id, placa, estado, activo, reporte,tipoVehiculo);
        case "CARRO" -> new Carro(id, placa, estado, activo, reporte,tipoVehiculo);
        case "BICICLETA" -> new Bicicleta(id, placa, estado, activo, reporte,tipoVehiculo);
        default -> throw new IllegalArgumentException("Tipo de vehículo desconocido: " + tipoStr);
    };
}
    
@Override
    public boolean guardarVehiculo(Vehiculo vehiculo) {
    String sql = "INSERT INTO VEHICULO (ID_VEHICULO, PLACA, TIPO_VEHICULO, ESTADO_VEHICULO, ACTIVO, ULTIMO_REPORTE) VALUES (?, ?, ?, ?, ?, ?)";
        
    try (Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
        
        ps.setInt(1, vehiculo.getIdVehiculo());
        ps.setString(2, vehiculo.getPlaca());
        ps.setString(3, vehiculo.getTipo().name());
        ps.setString(4, vehiculo.getEstado().name());
        ps.setBoolean(5, vehiculo.isActivo());
        ps.setTimestamp(6, Timestamp.valueOf(vehiculo.getUltimoReporte()));

        int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Vehiculo guardado correctamente.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar vehiculo: " + e.getMessage());
        }

        return false;
    }

@Override
public Vehiculo buscarVehiculoPorId(int idVehiculo) {
String sql = "SELECT * FROM VEHICULO WHERE ID_VEHICULO = ?";

try (Connection conexion = ConexionDB.getInstance().getConnection();
        PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setInt(1, idVehiculo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapearVehiculo(rs); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return null;
}
    
@Override
public Vehiculo buscarVehiculoPorPlaca(String placa) {
String sql = "SELECT * FROM VEHICULO WHERE PLACA = ?";

try (Connection conexion = ConexionDB.getInstance().getConnection();
        PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setString(1, placa);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapearVehiculo(rs); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return null;
}

@Override
public void actualizarEstado(int id, EstadoVehiculo estado) {
    String sql = "UPDATE VEHICULO SET ESTADO_VEHICULO = ?, ULTIMO_REPORTE = ? WHERE ID_VEHICULO = ?";
    try (Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del vehículo: " + e.getMessage());
        }
    }
@Override
public List<Vehiculo> listarVehiculosActivos() {

    List<Vehiculo> listaVehiculos= new ArrayList<>();
    String sql = "SELECT * FROM VEHICULO WHERE ACTIVO = 1";

    try (Connection conexion = ConexionDB.getInstance().getConnection();
        PreparedStatement ps = conexion.prepareStatement(sql);  
        ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
           Vehiculo v = mapearVehiculo(rs);
           listaVehiculos.add(v);
        }
    } catch (SQLException e) {
        System.err.println("Error al listar vehículos: " + e.getMessage());
    }
    return listaVehiculos;
    
    }
}
