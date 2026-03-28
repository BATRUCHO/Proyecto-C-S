package Conexion;

/**
 *
 * @author Asus Vivobook
 */
import Dominio.Paquete;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PaqueteDAOImpl implements PaqueteDAO {

    @Override
    public boolean guardarPaquete(Paquete paquete) {
        String sql = "INSERT INTO PAQUETE (ID_PAQUETE, FECHA_CREACION, DIRECCION_ORIGEN, DIRECCION_DESTINO, PESO_KG, COSTO_ENVIO, ESTADO, ID_VEHICULO_ASIGNADO, FECHA_ENTREGA, CONTENIDO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, paquete.getIdPaquete());
            ps.setTimestamp(2, Timestamp.valueOf(paquete.getFechaCreacion()));
            ps.setString(3, paquete.getDireccionOrigen());
            ps.setString(4, paquete.getDireccionDestino());
            ps.setDouble(5, paquete.getPesoKg());
            ps.setDouble(6, paquete.getCostoEnvio());
            ps.setString(7, paquete.getEstado().name());

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

            ps.setString(10, paquete.getContenido());

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

        try {
            Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPaquete);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Paquete paquete = new Paquete(
                        rs.getInt("ID_PAQUETE"),
                        rs.getString("DIRECCION_ORIGEN"),
                        rs.getString("DIRECCION_DESTINO"),
                        rs.getDouble("PESO_KG"),
                        rs.getDouble("COSTO_ENVIO"),
                        rs.getString("CONTENIDO")
                );

                return paquete;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar paquete por ID: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Paquete buscarPaquetePorContenido(String contenido) {
        String sql = "SELECT * FROM PAQUETE WHERE CONTENIDO = ?";

        try {
            Connection conexion = ConexionDB.getInstance().getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, contenido);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Paquete paquete = new Paquete(
                        rs.getInt("ID_PAQUETE"),
                        rs.getString("DIRECCION_ORIGEN"),
                        rs.getString("DIRECCION_DESTINO"),
                        rs.getDouble("PESO_KG"),
                        rs.getDouble("COSTO_ENVIO"),
                        rs.getString("CONTENIDO")
                );

                return paquete;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar paquete por contenido: " + e.getMessage());
        }

        return null;
    } 
}
