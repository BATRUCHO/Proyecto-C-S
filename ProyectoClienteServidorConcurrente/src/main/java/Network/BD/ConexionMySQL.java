package Network.BD;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {
    private static final String URL = ConfigLoader.getProperty("db.url");
    private static final String USER = ConfigLoader.getProperty("db.user");
    private static final String PASS = ConfigLoader.getProperty("db.password");

    public static Connection getConexion() {
        Connection cn = null;
        try {
            Class.forName(ConfigLoader.getProperty("db.driver"));
            cn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        return cn;
    }
}
