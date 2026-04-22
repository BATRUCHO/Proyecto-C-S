package Network.BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    // Datos de tu MySQL local
    private static final String BD = "quick_delivery_db";
    private static final String URL = "jdbc:mysql://localhost:3306/" + BD + "?serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "BbA2025@@1025105";
                

    public static Connection getConexion() {
        Connection cn = null;
        try {
            // Intentamos cargar el driver que agregaste a Referenced Libraries
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ Conexión exitosa a la base de datos: " + BD);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el conector JAR. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Error de SQL: " + e.getMessage());
        }
        return cn;
    }

}
