package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import Dominio.Paquete;
import Dominio.Usuario;
import PaqueteCliente.Controlador.AdminControlador;

public class FrmDashboardAdmin extends JFrame {

    private Usuario adminLogueado;
    private JTable tblPaquetes;
    private DefaultTableModel modeloPaquetes;
    private AdminControlador adminControl = new AdminControlador(); // Instancia del controlador
    private  List<Paquete> listaPaquetes;



    public FrmDashboardAdmin(Usuario admin) { 
        this.adminLogueado = admin;

        setTitle("LogiTrack - Panel de Administración");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        
        refrescarTablaPaquetes(); // 
    }

    private void initComponents() {

    // 1. Panel de información superior (Nombre del admin, botón cerrar sesión)
    JPanel panelHeader = new JPanel(new BorderLayout());
    panelHeader.setBackground(new Color(45, 52, 54)); // Un gris oscuro profesional

    JLabel lblWelcome = new JLabel(" Bienvenido, " + adminLogueado.getNombre()); // Mensaje de bienvenida al usuario admin ingresado
    lblWelcome.setForeground(Color.WHITE);
    panelHeader.add(lblWelcome, BorderLayout.WEST);

    JButton btnCerrarSesion = new JButton("Cerrar Sesión"); // Botón para cerrar sesión
    btnCerrarSesion.setBackground(new Color(231, 76, 60));
    btnCerrarSesion.setForeground(Color.WHITE);
    btnCerrarSesion.setFocusPainted(false);
    btnCerrarSesion.addActionListener(e -> {
        this.dispose();
        new FrmLogin().setVisible(true);
    });

    // 2. El Contenedor de Pestañas
    JTabbedPane pestañas = new JTabbedPane();
    pestañas.setFont(new Font("SansSerif", Font.BOLD, 12));

    // Añadimos las pestañas llamando a métodos que crean cada panel
    pestañas.addTab("📦 Gestión de Paquetes", crearPanelPaquetes());
    pestañas.addTab("🚙 Gestión de Vehículos", crearPanelVehiculos());
    pestañas.addTab("👥 Gestión de Usuarios", crearPanelUsuarios());
    pestañas.addTab("📖 Gestion de registros de Logs", crearPanelRegistros());

    // 3. Agregar al Frame
    add(panelHeader, BorderLayout.NORTH);
    add(pestañas, BorderLayout.CENTER);
    add(btnCerrarSesion, BorderLayout.EAST);

    }

    private JPanel crearPanelPaquetes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Configuración de la Tabla
        String[] columnas = {"ID_Paquete", "Descripción","Remitente", "Destinatario", "Dirección de Entrega", "Estado","Fecha de Creacion"};
        modeloPaquetes = new DefaultTableModel(columnas, 0) {
    
        @Override
        public boolean isCellEditable(int row, int column) { return false; } 
        };
        
        tblPaquetes = new JTable(modeloPaquetes);
        panel.add(new JScrollPane(tblPaquetes), BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnActualizarPaquetes = new JButton("Actualizar Estado Paquetes");
        JButton btnRegistrarPaquete = new JButton("Registrar Paquete");
        JButton btnAsignarPaquete = new JButton("Asignar Paquete");
        JButton btnEditar = new JButton("Editar Paquete");
        JButton btnEliminar = new JButton("Eliminar Paquete");

        // Se agregan al panel los botones
        panelBotones.add(btnActualizarPaquetes);
        panelBotones.add(btnRegistrarPaquete);
        panelBotones.add(btnAsignarPaquete);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        
        //--------------BotonesEventos----------------//
        
        // BotonActualizarPaquetes actualiza la tabla
        btnActualizarPaquetes.addActionListener(e -> refrescarTablaPaquetes());

        // BottonNuevoPaquete abre la ventana de nuevo paquete
        btnRegistrarPaquete.addActionListener(e -> {
            FrmNuevoPaquete ventanaNuevo = new FrmNuevoPaquete(this);
            ventanaNuevo.setVisible(true);
            
            if (ventanaNuevo.isExito()) {
                refrescarTablaPaquetes();
            }
        });

        // BotonAsignarPaquete asigna el paquete seleccionado
        btnAsignarPaquete.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }

            int idPaquete = (int) tblPaquetes.getValueAt(filaSeleccionada, 0);
            int idConductor = adminLogueado.getIdUsuario();
            boolean asignado = adminControl.asignarPaquete(idPaquete, idConductor);

            if (asignado) {
                refrescarTablaPaquetes();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo asignar el paquete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        });

        // BotonEditar abre la ventana de editar paquete
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            // Si la seleccion es igual -1 significa que no se selecciono nada
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }

            // Obtiene el ID del paquete seleccionado
            int id =(int) tblPaquetes.getValueAt(filaSeleccionada, 0);

            // Usa el metodo auxiliar para buscar el paquete por ID
            Paquete paqueteSeleccionado = buscarPaquetePorId(id);

            if(paqueteSeleccionado == null){
                JOptionPane.showMessageDialog(this, "No se pudieron recuperar los datos completos del paquete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Abre la ventana de edición con los datos del paquete a editar
            FrmNuevoPaquete ventanaEditar = new FrmNuevoPaquete(this, paqueteSeleccionado);
            ventanaEditar.setVisible(true);

            if (ventanaEditar.isExito()) {
                refrescarTablaPaquetes();
            }
            
        });

        // BotonEliminar elimina el paquete seleccionado
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }
            
            int id = (int) tblPaquetes.getValueAt(filaSeleccionada, 0);
            boolean eliminado = adminControl.eliminarPaquete(id);

            if(eliminado){
                refrescarTablaPaquetes();
            }else{
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el paquete.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });
        
        return panel;
    }

    private JPanel crearPanelVehiculos() {
        JPanel panel = new JPanel(new BorderLayout());

        // Modelo de tabla para vehículos
        String[] columnas = { "ID_Vehiculo","Placa","Marca","Modelo","ID_tipo_vehiculo","Estado" };
        DefaultTableModel modeloVehiculos = new DefaultTableModel(columnas, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {return false;}
        };

        JTable tblVehiculos = new JTable(modeloVehiculos);
        panel.add(new JScrollPane(tblVehiculos), BorderLayout.CENTER);

        //Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnActualizarVehiculos = new JButton("Actualizar Estado Vehiculos");
        JButton btnRegistrarVehiculo = new JButton("Registrar Vehiculo");
        JButton btnEditar = new JButton("Editar Vehiculo");
        JButton btnEliminar = new JButton("Eliminar Vehiculo");

        // Se agregan al panel los botones
        panelBotones.add(btnActualizarVehiculos);
        panelBotones.add(btnRegistrarVehiculo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);


        panel.add(panelBotones, BorderLayout.SOUTH);

        //--------------BotonesEventos----------------//

        btnActualizarVehiculos.addActionListener(e -> {
        });

        btnRegistrarVehiculo.addActionListener(e -> {
        });

        btnEditar.addActionListener(e -> {
        });

        btnEliminar.addActionListener(e -> {
        });

        return panel;

    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Modelo de tabla para usuarios
        String[] columnas = {"ID", "Nombre", "Email", "Rol", "Estado"};
        DefaultTableModel modeloUsuarios = new DefaultTableModel(columnas, 0);
        JTable tblUsuarios = new JTable(modeloUsuarios);
        
        // Panel de botones lateral o superior
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevoUsuario = new JButton("Nuevo Usuario");
        JButton btnEliminar = new JButton("Desactivar");
        
        panelAcciones.add(btnNuevoUsuario);
        panelAcciones.add(btnEliminar);
        
        panel.add(panelAcciones, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelRegistros() {

        JPanel panel = new JPanel(new BorderLayout());

        //Modelo para la tabla de registros
        String[] columnas = {"ID", "Usuario", "Accion","Descripcion", "Fecha"};
        DefaultTableModel modeloRegistros = new DefaultTableModel(columnas, 0);
        JTable tblRegistros = new JTable(modeloRegistros);

        panel.add(new JScrollPane(tblRegistros), BorderLayout.CENTER);

        return panel;

    }

    //--------------MetodosAuxiliares----------------//
    
    public void refrescarTablaPaquetes() {
        // 1. Usamos la instancia de AdminControlador para obtener la lista
        List<Paquete> lista = adminControl.actualizarPaquetes();

        // 2. Limpiamos el modelo actual de la tabla
        modeloPaquetes.setRowCount(0);

        // 3. Validamos y llenamos
        if (lista != null && !lista.isEmpty()) {
            for (Paquete p : lista) {
                Object[] fila = {
                    p.getId_paquete(),
                    p.getDescripcion(),
                    p.getDestinatario(),
                    p.getDireccion_entrega(),
                    interpretarEstado(p.getId_estado()) // Convertimos el ID numérico a texto
                };
                modeloPaquetes.addRow(fila);
            }
        } else {
            System.out.println("No se recibieron paquetes del servidor.");
        }
    }

    private String interpretarEstado(int idEstado) {
        switch (idEstado) {
            case 1: return "En Bodega";
            case 2: return "Asignado a Ruta o En Tránsito";
            case 3: return "Entregado";
            case 4: return "Con Incidencias";
            default: return "Desconocido";
        }
    }

    public Paquete buscarPaquetePorId(int id) {
        if(listaPaquetes != null) {
            for (Paquete p : listaPaquetes) {
                if (p.getId_paquete() == id) {
                    return p; // Lo encontro, retorna el objeto paquete
                }
            }
        }
        return null; // No lo encontro
    }



    //Metodo prueba ventana admin//
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        // Creamos un objeto Date válido para SQL
        java.sql.Date fechaNac = java.sql.Date.valueOf("1990-01-01");

        // Agregamos 'new' y los tipos de datos correctos
        Usuario adminPrueba = new Usuario(1, "Admin", fechaNac, "Sistema", "12345678", 
                                          "admin@mail.com", "88888888", "admin123", 1);
        
        new FrmDashboardAdmin(adminPrueba).setVisible(true);
    });
    }
}