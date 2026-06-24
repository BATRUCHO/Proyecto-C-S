package PaqueteCliente.Vista.AdminVista.Log;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Dominio.Excepciones.LogSistema;

import java.awt.*;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Utilidades.CSVExporter;
import java.util.List;

public class PanelLog extends JPanel {

    private JTable tblLogs;
    private JButton btnActualizarLogs, btnExportarCSV;

    private final AdminControlador adminControl;

    private List<LogSistema> listaLogs;
    private DefaultTableModel modeloLogs;

    public PanelLog(Frame parentFrame, AdminControlador controlador) {

        this.adminControl = controlador;

        setLayout(new BorderLayout());
        initComponents();
        configurarEventos();
        refrescarTablaLogs();

    }

    private void initComponents() {

        String[] columnas = {"ID", "Usuario", "Nombre Completo", "Rol", "Accion", "Descripcion", "Fecha"};
        modeloLogs = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblLogs = new JTable(modeloLogs);
        add(new JScrollPane(tblLogs), BorderLayout.CENTER);
        JPanel panelBotones = new JPanel();

        btnActualizarLogs = new JButton("Actualizar Logs");
        btnExportarCSV = new JButton("Descargar Archivo");

        panelBotones.add(btnActualizarLogs);
        panelBotones.add(btnExportarCSV);

        add(panelBotones, BorderLayout.SOUTH);

    }

    private void configurarEventos() {

        btnActualizarLogs.addActionListener(e -> {
            refrescarTablaLogs();
        });

        btnExportarCSV.addActionListener(e -> {

            // 1. Validamos que tengamos datos en la lista, por lo que el sistema tenga
            // datos para descargar de la lista Logs
            if (listaLogs == null || listaLogs.isEmpty()) {
                this.listaLogs = adminControl.listarEventosSistema();
            }
            // 2. Definimos las opciones que verá el usuario
            Object[] opciones = { "Reporte de Auditoría de Logs", "Reporte de Paquetes", "Reporte de Vehiculos",
                    "Reporte de Usuarios" };

            // 3. Desplegamos el menú de selección estructurado
            Object seleccion = JOptionPane.showInputDialog(
                    this, // 1. Component (Componente padre)
                    "Seleccione el reporte que desea exportar a Excel:", // 2. Object (Mensaje interno)
                    "Extractor de Reportes Corporativos", // 3. String (Título de la ventana)
                    JOptionPane.QUESTION_MESSAGE, // 4. int (Tipo de mensaje/ícono)
                    null, // 5. Icon (Icono personalizado, null usa defecto)
                    opciones, // 6. Object[] (El arreglo de opciones para el ComboBox)
                    opciones[0] // 7. Object (La opción seleccionada por defecto)

            );

            // 4. Control defensivo por si el usuario presiona "Cancelar" o cierra la
            // ventana
            if (seleccion == null) {
                return;
            }

            // 5. Control defencibo por si el usuario presiona "Cancelar" o cierra la
            // ventana
            String opcionElegida = seleccion.toString();

            switch (opcionElegida) {
                case "Reporte de Auditoría de Logs" -> {
                    CSVExporter.exportarLogs(this, this.listaLogs);

                }
                case "Reporte de Paquetes" -> {
                    JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Aviso",
                            JOptionPane.INFORMATION_MESSAGE);

                }
                case "Reporte de Vehiculos" -> {
                    JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Aviso",
                            JOptionPane.INFORMATION_MESSAGE);

                }
                case "Reporte de Usuarios" -> {
                    JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Aviso",
                            JOptionPane.INFORMATION_MESSAGE);

                }
                default -> {
                    JOptionPane.showMessageDialog(this, "Opción no válida seleccionada." + opcionElegida, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });

    }

    private void refrescarTablaLogs() {

        // 1. UX: Cambiamos el cursor de toda la ventana al de "Cargando"
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // 2. UX: Limpiamos la tabla y ponemos un mensaje temporal directo en la celda
        modeloLogs.setRowCount(0);
        // Agregamos una fila vacía con el texto en la columna del medio (Ej:
        // Descripción)
        modeloLogs.addRow(new Object[] { "", "⏳ Cargando datos desde el servidor...", "", "", "", "", "", "" });

        SwingWorker<List<LogSistema>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<LogSistema> doInBackground() throws Exception {
                return adminControl.listarEventosSistema();
            }

            @Override
            protected void done() {
                try {
                    listaLogs = get();

                    modeloLogs.setRowCount(0);
                    if (listaLogs != null && !listaLogs.isEmpty()) {
                        for (LogSistema l : listaLogs) {
                            Object[] fila = {
                                    l.getId_log(),
                                    l.getId_usuario(),
                                    l.getNombre_completo(),
                                    l.getRol_usuario(),
                                    l.getAccion(),
                                    l.getDetalles(),
                                    l.getFecha_hora()
                            };
                            modeloLogs.addRow(fila);
                        }
                    } else {
                        modeloLogs.addRow(
                                new Object[] { "", "No hay logs registrados actualmente.", "", "", "", "", "" });
                    }
                } catch (Exception ex) {
                    System.err.println("Error asincronico al cargar logs: " + ex.getMessage());
                    modeloLogs.setColumnCount(0);
                    modeloLogs
                            .addRow(new Object[] { "", "❌ Error de conexión con el servidor.", "", "", "", "", "" });
                } finally {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        worker.execute();
    }

}
