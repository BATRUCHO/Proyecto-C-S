package Conexion;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        Connection con = ConexionDB.getInstance().getConnection();

        if (con != null) {
            System.out.println("La conexion con la base de datos fue exitosa.");
        } else {
            System.out.println("No se pudo conectar con la base de datos.");
        }

    }
}
