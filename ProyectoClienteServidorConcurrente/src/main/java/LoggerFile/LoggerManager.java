package LoggerFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Network.DAO.LogDAO; 

public class LoggerManager {

    private static final String FILE_PATH = "Sistema.log";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final LogDAO logDAO = new LogDAO();

    public static void log(int idUsuario, String accion, String detalle){
        String fecha = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String mensajeFormateado = String.format("[%s] [USER: %d] %s: %s", fecha, idUsuario, accion, detalle);

        System.out.println(mensajeFormateado);

        escribirEnArchivo(mensajeFormateado);

        logDAO.registrarEvento(idUsuario,accion,detalle);
        
    }

    private static void escribirEnArchivo(String mensaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(mensaje);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error crítico: No se pudo escribir en el archivo log: " + e.getMessage());
        }
    }




}
