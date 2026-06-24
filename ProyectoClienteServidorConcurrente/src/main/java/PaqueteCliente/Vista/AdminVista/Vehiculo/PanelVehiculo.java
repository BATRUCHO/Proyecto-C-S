package PaqueteCliente.Vista.AdminVista.Vehiculo;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import Dominio.EstadoVehiculo;
import Dominio.TipoVehiculo;

import Dominio.Usuarios;
import Dominio.Vehiculo;

import java.awt.*;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Utilidades.MetodosBusquedaId;
import java.util.List;

public class PanelVehiculo extends JPanel {

    private JTable tblVehiculo;
    private JButton btnActualizarVehiculos, btnRegistrarVehiculo, btnEditar, btnEliminar;

    private final AdminControlador adminControl;
    private final Frame parentFrame; // Necesario para levantar diálogos JDialog modales
    private final Usuarios adminLogueado;

    private DefaultTableModel modeloVehiculos;
    private List<Vehiculo> listaVehiculos;

    public PanelVehiculo(Frame parentFrame, AdminControlador controlador, Usuarios adminLogueado) {
        this.parentFrame = parentFrame;
        this.adminControl = controlador;
        this.adminLogueado = adminLogueado;

        setLayout(new BorderLayout());
        initComponents();
        configurarEventos();
        refrescarTablaVehiculos();
    }

    private void initComponents() {

        String[] columnas = {"ID_Vehiculo", "Placa", "Marca", "Modelo", "Tipo Vehiculo", "Estado del Vehiculo"};
        modeloVehiculos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblVehiculo = new JTable(modeloVehiculos);
        add(new JScrollPane(tblVehiculo), BorderLayout.CENTER);
        JPanel panelBotones = new JPanel();

        btnActualizarVehiculos = new JButton("Actualizar Estado Vehiculos");
        btnRegistrarVehiculo = new JButton("Registrar Vehiculo");
        btnEditar = new JButton("Editar Vehiculo");
        btnEliminar = new JButton("Eliminar Vehiculo");

        panelBotones.add(btnActualizarVehiculos);
        panelBotones.add(btnRegistrarVehiculo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {

        btnActualizarVehiculos.addActionListener(e -> refrescarTablaVehiculos());

        btnRegistrarVehiculo.addActionListener(e -> {
            FrmRegistrarVehiculo ventanaNuevo = new FrmRegistrarVehiculo(parentFrame);
            ventanaNuevo.setVisible(true);

            if (ventanaNuevo.isExito()) {
                refrescarTablaVehiculos();
            }
        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tblVehiculo.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un vehiculo de la tabla.");
                return;
            }

            int id = (int) tblVehiculo.getValueAt(filaSeleccionada, 0);

            MetodosBusquedaId.buscarVehiculoPorId(listaVehiculos, id)
                    .ifPresentOrElse(
                            vehiculoEncontrado -> {
                                FrmRegistrarVehiculo ventanaEditar = new FrmRegistrarVehiculo(this, vehiculoEncontrado);
                                ventanaEditar.setVisible(true);

                                if (ventanaEditar.isExito()) {
                                    refrescarTablaVehiculos();
                                }
                            },
                            () -> JOptionPane.showMessageDialog(this,
                                    "No se pudieron recuperar los datos completos del Vehiculo.\nEs posible que la lista no se haya cargado correctamente.",
                                    "Error de Búsqueda",
                                    JOptionPane.ERROR_MESSAGE));
        });

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tblVehiculo.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showInputDialog(this, "Por favor, seleccione un vehiculo de la tabla.");
                return;
            }

            int idEliminar = (int) tblVehiculo.getValueAt(filaSeleccionada, 0);
            String descripcion = (String) tblVehiculo.getValueAt(filaSeleccionada, 1);

            int confirmado = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está completamente seguro de que desea eliminar el vehiculo ID: " + idEliminar + " ("
                            + descripcion + ")?\n"
                            + "Esta accion no se puede deshacer y afectara el transporte del paquete.",
                    "Confirmacion de eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmado != JOptionPane.YES_OPTION) {
                return; // Abortamos la ejecucion
            }

            boolean eliminado = adminControl.eliminarVehiculo(idEliminar, adminLogueado.getId_usuario());

            if (eliminado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Vehiculo eliminado y registrado en el historial de logs correctamente.",
                        "Eliminacion Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                refrescarTablaVehiculos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el vehiculo.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void refrescarTablaVehiculos() {

        // 1. UX: Cambiamos el cursor de toda la ventana al de "Cargando"
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // 2. UX: Limpiamos la tabla y ponemos un mensaje temporal directo en la celda
        modeloVehiculos.setRowCount(0);
        // Agregamos una fila vacía con el texto en la columna del medio (Ej:
        // Descripción)
        modeloVehiculos.addRow(new Object[] { "", "⏳ Cargando datos desde el servidor...", "", "", "", "", "", "" });

        SwingWorker<List<Vehiculo>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<Vehiculo> doInBackground() throws Exception {
                return adminControl.actualizarVehiculos();
            }

            @Override
            protected void done() {
                try {
                    listaVehiculos = get();

                    modeloVehiculos.setRowCount(0);

                    if (listaVehiculos != null && !listaVehiculos.isEmpty()) {
                        for (Vehiculo v : listaVehiculos) {
                            Object[] fila = {
                                    v.getId_vehiculo(),
                                    v.getPlaca(),
                                    v.getMarca(),
                                    v.getModelo(),
                                    TipoVehiculo.obtenerTextoPorId(v.getId_tipoVehiculo()),
                                    EstadoVehiculo.obtenerTextoPorId(v.getId_estado_vehiculo())
                            };
                            modeloVehiculos.addRow(fila);
                        }
                    } else {
                        modeloVehiculos.addRow(
                                new Object[] { "", "No hay vehiculos registrados en el sistema", "", "", "", "" });
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar vehiculos desde el servidor" + e.getMessage());
                    modeloVehiculos.setRowCount(0);
                    modeloVehiculos.addRow(
                            new Object[] { "", "Error al cargar vehiculos desde el servidor", "", "", "", "" });
                } finally {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        worker.execute();
    }
}