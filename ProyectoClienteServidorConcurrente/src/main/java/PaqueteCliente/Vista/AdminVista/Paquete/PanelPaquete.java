package PaqueteCliente.Vista.AdminVista.Paquete;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import Dominio.EstadoPaquete;
import Dominio.Paquete;
import Dominio.Usuarios;

import java.awt.*;
import java.util.List;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Utilidades.MetodosBusquedaId;

public class PanelPaquete extends JPanel {

    // Swing component
    private JTable tblPaquetes;
    private JButton btnActualizarPaquetes, btnRegistrarPaquete, btnAsignarPaquete, btnEditar, btnEliminar;

    // Controlador y Frame
    private final AdminControlador adminControl;
    private final Frame parentFrame; // Necesario para levantar diálogos JDialog modales
    private final Usuarios adminLogueado;

    // Modelo y lista de paquetes
    private DefaultTableModel modeloPaquetes;
    private List<Paquete> listaPaquetes;

    public PanelPaquete(Frame parentFrame, AdminControlador adminControl, Usuarios adminLogueado) {
        this.parentFrame = parentFrame;
        this.adminControl = adminControl;
        this.adminLogueado = adminLogueado;

        setLayout(new BorderLayout());
        initComponents();
        configurarEventos();
        refrescarTablaPaquetes();
    }

    private void initComponents() {

        String[] columnas = {"ID_Paquete", "Descripción", "Remitente", "Destinatario", "Dirección de Entrega", "Peso", "Estado Paquete", "Fecha de Creacion"};
        modeloPaquetes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Configuracion modelo y la tabla
        tblPaquetes = new JTable(modeloPaquetes);

        // Agregamos el panel a la tabla
        add(new JScrollPane(tblPaquetes), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();

        // Configuración de botones
        btnActualizarPaquetes = new JButton("Actualizar Estado Paquetes");
        btnRegistrarPaquete = new JButton("Registrar Paquete");
        btnAsignarPaquete = new JButton("Asignar Paquete");
        btnEditar = new JButton("Editar Paquete");
        btnEliminar = new JButton("Eliminar Paquete");

        // Se agregan al panel los botones
        panelBotones.add(btnActualizarPaquetes);
        panelBotones.add(btnRegistrarPaquete);
        panelBotones.add(btnAsignarPaquete);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

    }

    private void configurarEventos() {
        // BotonActualizarPaquetes actualiza la tabla
        btnActualizarPaquetes.addActionListener(e -> refrescarTablaPaquetes());

        // BottonNuevoPaquete abre la ventana de nuevo paquete
        btnRegistrarPaquete.addActionListener(e -> {
            FrmNuevoPaquete ventanaNuevo = new FrmNuevoPaquete(parentFrame);
            ventanaNuevo.setVisible(true);

            if (ventanaNuevo.isExito()) {
                refrescarTablaPaquetes();
            }
        });

        // BotonEditar abre la ventana de editar paquete
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            // 1. Validación de interfaz defensiva: ¿Hay fila seleccionada?
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }

            // 2. Obtiene el ID del paquete seleccionado de la columna 0
            int id = (int) tblPaquetes.getValueAt(filaSeleccionada, 0);

            // 3. Invocamos la clase utilitaria de forma ESTÁTICA pasándole la lista en
            // memoria y el ID
            MetodosBusquedaId.buscarPaquetePorId(listaPaquetes, id)
                    .ifPresentOrElse(
                            // CAMINO A: ¿El paquete existe en el Optional? Java lo extrae automáticamente
                            // aquí
                            paqueteEncontrado -> {
                                // Abre la ventana de edición pasando el paquete garantizado
                                FrmNuevoPaquete ventanaEditar = new FrmNuevoPaquete(this, paqueteEncontrado);
                                ventanaEditar.setVisible(true);

                                // Si el formulario guardó con éxito, refrescamos la tabla
                                if (ventanaEditar.isExito()) {
                                    refrescarTablaPaquetes();
                                }
                            },
                            // CAMINO B: ¿El Optional vino vacío? (La lista estaba vacía o el ID no
                            // coincidió)
                            () -> {
                                JOptionPane.showMessageDialog(this,
                                        "No se pudieron recuperar los datos completos del paquete.\nEs posible que la lista no se haya cargado correctamente.",
                                        "Error de Búsqueda",
                                        JOptionPane.ERROR_MESSAGE);
                            });
        });

        // BotonEliminar elimina el paquete seleccionado
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }

            int idEliminar = (int) modeloPaquetes.getValueAt(filaSeleccionada, 0);
            String descripcion = (String) modeloPaquetes.getValueAt(filaSeleccionada, 1);

            int confirmado = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está completamente seguro de que desea eliminar el paquete ID: " + idEliminar + " (" + descripcion
                            + ")?\n"
                            + "Esta accion no se puede deshacer y afectara el inventario en bodega.",
                    "Confirmacion de eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmado != JOptionPane.YES_OPTION) {
                return;
            }

            boolean eliminado = adminControl.eliminarPaquete(idEliminar, adminLogueado.getId_usuario());

            if (eliminado) {
                JOptionPane.showMessageDialog(this,
                        "Paquete eliminado y registrado en el historial de logs correctamente.");
                refrescarTablaPaquetes();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el paquete.", "Error",
                        JOptionPane.ERROR_MESSAGE);

            }
        });
    }

    public void refrescarTablaPaquetes() {

        // 1. UX: Cambiamos el cursor de toda la ventana al de "Cargando"
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // 2. UX: Limpiamos la tabla y ponemos un mensaje temporal directo en la celda
        modeloPaquetes.setRowCount(0);
        // Agregamos una fila vacía con el texto en la columna del medio (Ej:
        // Descripción)
        modeloPaquetes.addRow(new Object[] { "", "⏳ Cargando datos desde el servidor...", "", "", "", "", "", "" });

        SwingWorker<List<Paquete>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<Paquete> doInBackground() throws Exception {
                // El hilo secundario va a la red de forma segura
                return adminControl.actualizarPaquetes();
            }

            @Override
            protected void done() {
                try {
                    listaPaquetes = get();

                    // Limpiamos nuestra fila temporal de "Cargando"
                    modeloPaquetes.setRowCount(0);

                    if (listaPaquetes != null && !listaPaquetes.isEmpty()) {
                        for (Paquete p : listaPaquetes) {
                            Object[] fila = {
                                    p.getId_paquete(),
                                    p.getDescripcion(),
                                    p.getRemitente(),
                                    p.getDestinatario(),
                                    p.getDireccion_entrega(),
                                    p.getPeso(),
                                    EstadoPaquete.obtenerTextoPorId(p.getId_estado()),
                                    p.getFecha_creacion()
                            };
                            modeloPaquetes.addRow(fila);
                        }
                    } else {
                        // UX: Si la red responde bien, pero no hay paquetes en BD
                        modeloPaquetes.addRow(new Object[] { "", "No hay paquetes registrados actualmente.", "", "", "",
                                "", "", "" });
                    }
                } catch (Exception ex) {
                    System.err.println("Error asincrónico al cargar paquetes: " + ex.getMessage());
                    modeloPaquetes.setRowCount(0);
                    modeloPaquetes.addRow(
                            new Object[] { "", "❌ Error de conexión con el servidor.", "", "", "", "", "", "" });

                } finally {
                    // 3. UX: ¡MUY IMPORTANTE! Restauramos el cursor normal pase lo que pase
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        worker.execute();
    }
}
