package DAO;

/**
 *
 * @author Asus Vivobook
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Dominio.EstadoPaquete;
import Dominio.Paquete;
import Dominio.Provincia;


public class PaqueteDAOImpl implements PaqueteDAO {

  private Paquete mapearPaquete(ResultSet rs) throws SQLException {
    // 1. Datos obligatorios
    int id = rs.getInt("ID_PAQUETE");
    double pesoKg = rs.getDouble("PESO_KG"); // OJO: Verifica si en la DB es PESO_KG o PESO_KILOS
    String contenido = rs.getString("CONTENIDO");
    
    // 2. Enums y Fechas obligatorias
    Provincia origen = Provincia.valueOf(rs.getString("DIRECCION_ORIGEN"));
    Provincia destino = Provincia.valueOf(rs.getString("DIRECCION_DESTINO"));
    EstadoPaquete estado = EstadoPaquete.valueOf(rs.getString("ESTADO"));
    
    LocalDateTime fechaCreacion = rs.getTimestamp("FECHA_CREACION").toLocalDateTime();

    // 3. Manejo de Opcionales (Vehículo)
    Integer idVehiculo = null;
    int idVehiculoRaw = rs.getInt("ID_VEHICULO_ASIGNADO");
    if (!rs.wasNull()) {
        idVehiculo = idVehiculoRaw;
    }

    // 4. Manejo de Opcionales (Fecha Entrega) - Tu corrección aplicada
    LocalDateTime fechaEntrega = null;
    Timestamp tsEntrega = rs.getTimestamp("FECHA_ENTREGA");
    if (tsEntrega != null) {
        fechaEntrega = tsEntrega.toLocalDateTime();
    }

    // 5. Retorno usando el constructor de "reconstitución"
    return new Paquete(id, fechaCreacion, origen, destino, pesoKg, estado, contenido, idVehiculo, fechaEntrega);
}

    @Override
    public boolean guardarPaquete(Paquete paquete) {
        String sql = "INSERT INTO PAQUETE (ID_PAQUETE, FECHA_CREACION, DIRECCION_ORIGEN, DIRECCION_DESTINO, PESO_KG, ESTADO, CONTENIDO, ID_VEHICULO_ASIGNADO, FECHA_ENTREGA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, paquete.getIdPaquete());
            ps.setTimestamp(2, Timestamp.valueOf(paquete.getFechaCreacion()));
            ps.setString(3, paquete.getProvinciaOrigen().name());
            ps.setString(4, paquete.getProvinciaDestino().name());
            ps.setDouble(5, paquete.getPesoKg());
            ps.setString(6, paquete.getEstado().name());
            ps.setString(7, paquete.getContenido());

            if (paquete.getIdVehiculoAsignado() != null) {
                ps.setInt(8, paquete.getIdVehiculoAsignado());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            if (paquete.getFechaEntrega() != null) {
                ps.setTimestamp(9, Timestamp.valueOf(paquete.getFechaEntrega()));
            } else {
                ps.setNull(9, java.sql.Types.TIMESTAMP);
            }
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Paquete guardado correctamente.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar paquete: " + e.getMessage());
        }

        return false;
    }

    @Override
    public Paquete buscarPaquetePorId(int idPaquete) {
    String sql = "SELECT * FROM PAQUETE WHERE ID_PAQUETE = ?";

    try {Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPaquete);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPaquete(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;    
        
    }

    @Override
    public Paquete buscarPaquetePorContenido(String contenido) {
    String sql = "SELECT * FROM PAQUETE WHERE CONTENIDO = ?";

    try(Connection conexion = ConexionDB.getInstance().getConnection();
        PreparedStatement ps = conexion.prepareStatement(sql)){
        ps.setString(1, contenido);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapearPaquete(rs);
            }
        }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
        
    }

    @Override
    public List<Paquete> listarPaquetes() {
        List<Paquete> listaPaquetes = new ArrayList<>();
        String sql = "SELECT * FROM PAQUETE WHERE ACTIVO = 1";

        try (Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);  
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            Paquete p = mapearPaquete(rs);
            listaPaquetes.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Paquetes: " + e.getMessage());
        }
        return listaPaquetes;

        }

    @Override
    public void actualizarEstado(int id, EstadoPaquete estado) {
        String sql = "UPDATE PAQUETE SET ESTADO = ?, FECHA_ULTIMA_MODIFICACION = ? WHERE ID_PAQUETE = ?";
        try (Connection conexion = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
    }

    @Override
    public void asignarVehiculo(int idPaquete, int idVehiculo) {
        String sql = "UPDATE PAQUETE SET ID_VEHICULO_ASIGNADO = ?, ESTADO = ? WHERE ID_PAQUETE = ?";
        try (Connection conexion = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            ps.setString(2, EstadoPaquete.ASIGNADO.name());
            ps.setInt(3, idPaquete);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al asignar vehículo: " + e.getMessage());
        }
    }
}
