package DAO;

import Dominio.EstadoVehiculo;
import Dominio.TipoVehiculo;
import Dominio.Vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class VehiculoDAOImpl implements VehiculoDAO {

@Override
public boolean guardarVehiculo(Vehiculo vehiculo) {
String sql = "INSERT INTO VEHICULO (PLACA, FECHACREACION, TIPO_VEHICULO, ESTADO_VEHICULO, ACTIVO, ULTIMO_REPORTE, UBICACION_ACTUAL) VALUES (?, ?, ?, ?, ?, ?, ?)";

 try {
 Connection conexion = ConexionDB.getInstance().getConnection();
 PreparedStatement ps = conexion.prepareStatement(sql);

ps.setString(1, vehiculo.getPlaca());
 ps.setTimestamp(2, Timestamp.valueOf(vehiculo.getFechacreacion()));
 ps.setString(3, vehiculo.getTipovehiculo().name());
 ps.setString(4, vehiculo.getEstadoVehiculo().name());
 ps.setBoolean(5, vehiculo.getActivo());
 ps.setTimestamp(6, Timestamp.valueOf(vehiculo.getUltimoReporte()));
 ps.setString(7, vehiculo.getUbicacionActual());

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

        try {
            Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idVehiculo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Vehiculo vehiculo = new Vehiculo(
                        rs.getInt("ID_VEHICULO"),
                        TipoVehiculo.valueOf(rs.getString("TIPO_VEHICULO")),
                        rs.getString("PLACA"),
                        rs.getTimestamp("FECHACREACION").toLocalDateTime(),
                        EstadoVehiculo.valueOf(rs.getString("ESTADO_VEHICULO")),
                        rs.getBoolean("ACTIVO"),
                        rs.getTimestamp("ULTIMO_REPORTE").toLocalDateTime(),
                        rs.getString("UBICACION_ACTUAL")
                ) {
                    @Override
                    public double getCapacidadMaximaKG() {
                        return 1000.0;
                    }

                    @Override
                    public double getVelocidadPromedioKmH() {
                        return 80.0;
                    }
                };

                return vehiculo;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar vehiculo por ID: " + e.getMessage());
        }

        return null;
    }

    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        String sql = "SELECT * FROM VEHICULO WHERE PLACA = ?";

        try {
            Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, placa);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Vehiculo vehiculo = new Vehiculo(
                        rs.getInt("ID_VEHICULO"),
                        TipoVehiculo.valueOf(rs.getString("TIPO_VEHICULO")),
                        rs.getString("PLACA"),
                        rs.getTimestamp("FECHACREACION").toLocalDateTime(),
                        EstadoVehiculo.valueOf(rs.getString("ESTADO_VEHICULO")),
                        rs.getBoolean("ACTIVO"),
                        rs.getTimestamp("ULTIMO_REPORTE").toLocalDateTime(),
                        rs.getString("UBICACION_ACTUAL")
                ) {
                    @Override
                    public double getCapacidadMaximaKG() {
                        return 1000.0;
                    }

                    @Override
                    public double getVelocidadPromedioKmH() {
                        return 80.0;
                    }
                };

                return vehiculo;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar vehiculo por placa: " + e.getMessage());
        }

        return null;
    }
}
