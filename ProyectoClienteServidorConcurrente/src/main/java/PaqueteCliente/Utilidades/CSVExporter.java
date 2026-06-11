package PaqueteCliente.Utilidades;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Dominio.Excepciones.LogSistema;

public class CSVExporter {

    public static void exportarLogs(Component parent, List<LogSistema> logs) {

        //1. Se valida que haya datos para exportar
        if (logs == null || logs.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay datos para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //2. Se instanciar el JFileChooser a archivo nativo de java para exportar el archivo
        JFileChooser fileChooser = new JFileChooser();

        //3. Se sugiere nombres de archivos por defecto
        fileChooser.setDialogTitle("Guardar Reporte de Auditoría");
        fileChooser.setSelectedFile(new File("Reporte_Auditoria.csv"));
        
        //4. Se selecciona la ubicacion de guardado del archivo
        int userSelection = fileChooser.showSaveDialog(parent);

        //5. Si el usuario dio al boton de guardar
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoGuardar = fileChooser.getSelectedFile();
            
            // Asegurar extensión .csv
            String path = archivoGuardar.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) {
                archivoGuardar = new File(path + ".csv");
            }
            //6. Escribir el archivo CSV con los datos
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoGuardar))) {
                // Cabecera
                writer.write("ID_Log;ID_Usuario;Nombre_Completo;Rol;Accion;Detalles;Fecha_Hora");
                writer.newLine();

                // Datos
                for (LogSistema log : logs) {

                    // Evaluamos analíticamente si el ID de usuario es 0 (Anónimo) para limpiar la celda
                    String idUsuarioStr = (log.getId_usuario() <= 0) ? "-" : String.valueOf(log.getId_usuario());

                    // Protejemos los campos generados por el Join a la vista
                    String empleado = (log.getNombre_completo() != null) ? log.getNombre_completo() : "Sistema / Anónimo";
                    String rol =(log.getRol_usuario() != null) ? log.getRol_usuario() : "Sistema / Anónimo";

                    // %d para enteros, %s para cadenas, %f para flotantes, etc.
                    String fila = String.format( 
                            "%d;%s;%s;%s;%s;%s;%s",
                            log.getId_log(),
                            idUsuarioStr,
                            empleado,
                            rol,
                            log.getAccion(),
                            log.getDetalles() != null ? log.getDetalles() : "Sin detalles especificos",
                            log.getFecha_hora() != null ? log.getFecha_hora() : "Sin fecha"
                    );
                    writer.write(fila);
                    writer.newLine();
                }

                JOptionPane.showMessageDialog(parent, "Archivo exportado exitosamente en:\n" + archivoGuardar.getName(), "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Error al exportar CSV: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
