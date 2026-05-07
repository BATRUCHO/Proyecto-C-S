package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
        String[] columnas = {"ID_Paquete", "Descripción","Remitente", "Destinatario", "Dirección_Entrega", "Estado","Fecha_Creacion"};
        modeloPaquetes = new DefaultTableModel(columnas, 0) {
    
        public boolean isCellEditable(int row, int column) { return false; } 
        };
        
        tblPaquetes = new JTable(modeloPaquetes);
        panel.add(new JScrollPane(tblPaquetes), BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnRefrescar = new JButton("Actualizar Estado Paquetes");
        JButton btnNuevoPaquete = new JButton("Registrar Paquete");
        JButton btnEliminar = new JButton("Eliminar Paquete");
        JButton btnEditar = new JButton("Editar Paquete");

        // Se agregan al panel los botones
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnNuevoPaquete);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnEditar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        
        // --- EVENTOS USANDO EL CONTROLADOR ---
        
        btnRefrescar.addActionListener(e -> refrescarTablaPaquetes());

        btnNuevoPaquete.addActionListener(e -> {
            FrmNuevoPaquete ventanaNuevo = new FrmNuevoPaquete(this);
            ventanaNuevo.setVisible(true);
            
            
            if (ventanaNuevo.isExito()) {
                refrescarTablaPaquetes();
            }
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


// Cambiar esta logica//

    public void refrescarTablaPaquetes() {
        // 1. Usamos la instancia de AdminControlador para obtener la lista
        List<Paquete> lista = adminControl.obtenerTodosLosPaquetes();

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
            case 2: return "Asignado a Ruta";
            case 3: return "Entregado";
            default: return "Desconocido";
        }
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