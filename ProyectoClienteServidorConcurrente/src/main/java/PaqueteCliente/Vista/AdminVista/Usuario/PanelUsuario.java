package PaqueteCliente.Vista.AdminVista.Usuario;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import Dominio.Roles;
import Dominio.Usuarios;

import java.awt.*;
import java.util.List;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Utilidades.MetodosBusquedaId;

public class PanelUsuario extends JPanel {

    private JTable tblUsuarios;
    private JButton btnNuevoUsuario, btnActualizarUsuarios, btnAlterarEstadoUsuario, btnEditar;

    private final AdminControlador adminControl;
    private final Frame parentFrame; // Necesario para levantar diálogos JDialog modales

    private DefaultTableModel modeloUsuarios;
    private List<Usuarios> listaUsuarios;

    public PanelUsuario(Frame parentFrame, AdminControlador adminControl) {
        this.parentFrame = parentFrame;
        this.adminControl = adminControl;

        setLayout(new BorderLayout());
        initComponents();
        configurarEventos();
        refrescarTablaUsuarios();
    }

    private void initComponents() {

        String[] columnas = {"ID", "Nombre", "Apellido", "Fecha Nacimiento", "DNI", "Email", "Telefono", "Password", "Rol", "Fecha Creacion", "Estado"};
        modeloUsuarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblUsuarios = new JTable(modeloUsuarios);
        add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);
        JPanel panelBotones = new JPanel();

        btnActualizarUsuarios = new JButton("Actualizar Estado Usuarios");
        btnNuevoUsuario = new JButton("Nuevo Usuario");
        btnAlterarEstadoUsuario = new JButton("EstadoUsuario");
        btnEditar = new JButton("Editar");

        panelBotones.add(btnActualizarUsuarios);
        panelBotones.add(btnNuevoUsuario);
        panelBotones.add(btnAlterarEstadoUsuario);
        panelBotones.add(btnEditar);

        add(panelBotones, BorderLayout.SOUTH);

    }

    private void configurarEventos() {
        btnActualizarUsuarios.addActionListener(e -> refrescarTablaUsuarios());

        btnNuevoUsuario.addActionListener(e -> {
            FrmNuevoUsuario ventanaNueva = new FrmNuevoUsuario(parentFrame); // error
            ventanaNueva.setVisible(true);

            if (ventanaNueva.isExito()) {
                refrescarTablaUsuarios();
            }
        });

        btnAlterarEstadoUsuario.addActionListener(e -> {
            int filaSeleccionada = tblUsuarios.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario de la tabla.");
                return;
            }
            int id = (int) tblUsuarios.getValueAt(filaSeleccionada, 0);

            int confirmado = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está completamente seguro de que desea alterar el estado del usuario ID: " + id + "?",
                    "Confirmacion de alterar estado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmado != JOptionPane.YES_OPTION) {
                return;
            }

            boolean exito = adminControl.alterarEstadoUsuario(id);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Estado del usuario actualizado.");
                refrescarTablaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo alterar el estado del usuario.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tblUsuarios.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario de la tabla.");
                return;
            }

            int id = (int) tblUsuarios.getValueAt(filaSeleccionada, 0);
            MetodosBusquedaId.buscarUsuarioPorId(listaUsuarios, id)
                    .ifPresentOrElse(
                            usuarioEncontrado -> {

                                FrmNuevoUsuario ventanaEditar = new FrmNuevoUsuario(this, usuarioEncontrado);
                                ventanaEditar.setVisible(true);

                                if (ventanaEditar.isExito()) {
                                    refrescarTablaUsuarios();
                                }
                            },
                            () -> JOptionPane.showMessageDialog(this,
                                    "No se pudieron recuperar los datos completos del usuario. \nEs posible que la lista no se haya cargado correctamente.",
                                    "Error de Búsqueda",
                                    JOptionPane.ERROR_MESSAGE)

                    );
        });

    }

    public void refrescarTablaUsuarios() {

        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        modeloUsuarios.setRowCount(0);
        modeloUsuarios.addRow(new Object[] { "", "⏳ Cargando datos desde el servidor...", "", "", "", "", "", "" });

        // 2. Creamos el trabajador en segundo plano
        SwingWorker<List<Usuarios>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<Usuarios> doInBackground() throws Exception {
                // El metodo corre en un hilo aparte. Por lo que es seguro llamar al metodo del
                // controlador de red
                return adminControl.actualizarUsuarios();
            }

            @Override
            protected void done() {
                // Este metodo se ejecuta automaticamente cuando el doInBackground termina
                // A continuacion el hilo visual vuelve a correr (EDT)
                try {
                    // Obtener la lista que retorno el doInBackground
                    listaUsuarios = get();

                    // limpiamos y pintamos la tabla
                    modeloUsuarios.setRowCount(0);
                    if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                        for (Usuarios u : listaUsuarios) {
                            Object[] fila = {
                                    u.getId_usuario(),
                                    u.getNombre(),
                                    u.getApellido(),
                                    u.getFechaNacimiento(),
                                    u.getDni(),
                                    u.getEmail(),
                                    u.getTelefono(),
                                    "********",
                                    Roles.obtenerTextoPorId(u.getIdRol()),
                                    u.getFechaCreacion(),
                                    u.isActivo() ? "Activo" : "Inactivo"
                            };
                            modeloUsuarios.addRow(fila);
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Error asincronico al cargar usuarios: " + ex.getMessage());
                    modeloUsuarios.setRowCount(0);
                    modeloUsuarios.addRow(
                            new Object[] { "", "❌ Error de conexión con el servidor.", "", "", "", "", "", "" });
                } finally {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        // Ejecutamos el trabajador secundario
        worker.execute();
    }

}
