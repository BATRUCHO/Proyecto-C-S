package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
private static ConexionDB instancia; 
    private Connection conexion;
    private final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    
    private final String USUARIO = "system";
    private final String CLAVE = "1234";

    private ConexionDB() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexion exitosa a Oracle.");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontro el driver JDBC de Oracle.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar con Oracle.");
            e.printStackTrace();
        }
    }

    public static ConexionDB getInstance() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConnection() {
        return conexion;
    }
}
