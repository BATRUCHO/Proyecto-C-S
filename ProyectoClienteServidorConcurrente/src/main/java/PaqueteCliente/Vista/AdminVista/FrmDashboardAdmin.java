package PaqueteCliente.Vista.AdminVista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import Dominio.Usuarios;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Controlador.AutenticacionControlador;

import PaqueteCliente.Vista.LoginVista.FrmLogin;

import PaqueteCliente.Vista.AdminVista.Log.PanelLog;
import PaqueteCliente.Vista.AdminVista.Paquete.PanelPaquete;
import PaqueteCliente.Vista.AdminVista.Usuario.PanelUsuario;
import PaqueteCliente.Vista.AdminVista.Vehiculo.PanelVehiculo;

public class FrmDashboardAdmin extends JFrame {

    private Usuarios adminLogueado;
    private AutenticacionControlador authControl = new AutenticacionControlador(); // Instancia del controlador
    private AdminControlador adminControl = new AdminControlador();

    public FrmDashboardAdmin(Usuarios admin) {
        this.adminLogueado = admin;

        setTitle("LogiTrack - Panel de Administración");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();

        // Proceso de cierre de sesion si sale desde la X
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                authControl.cerrarSesion(adminLogueado.getId_usuario());
                System.exit(0);
            }
        });
    }

    private void initComponents() {

        // 1. Panel de información superior (Nombre del admin)
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(45, 52, 54)); // Un gris oscuro profesional

        JLabel lblWelcome = new JLabel(" Bienvenido, " + adminLogueado.getNombre()); // Mensaje de bienvenida al usuario
                                                                                     // admin ingresado
        lblWelcome.setForeground(Color.WHITE);
        panelHeader.add(lblWelcome, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Salir"); // Botón para cerrar sesión
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);

        btnCerrarSesion.addActionListener(e -> {

            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de que desea salir?",
                    "Confirmación de salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmar == JOptionPane.YES_OPTION) {
                authControl.cerrarSesion(adminLogueado.getId_usuario());
                dispose();
                new FrmLogin().setVisible(true);
            }
        });
        panelHeader.add(btnCerrarSesion, BorderLayout.EAST);

        // 2. El Contenedor de Pestañas
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Añadimos las pestañas llamando a métodos que crean cada panel
        pestañas.addTab("📦 Gestión de Paquetes", new PanelPaquete(this, adminControl, adminLogueado));
        pestañas.addTab("🚙 Gestión de Vehículos", new PanelVehiculo(this, adminControl, adminLogueado));
        pestañas.addTab("👥 Gestión de Usuarios", new PanelUsuario(this, adminControl));
        pestañas.addTab("📖 Historial de Logs", new PanelLog(this, adminControl));

        // 3. Agregar al Frame
        add(panelHeader, BorderLayout.NORTH);
        add(pestañas, BorderLayout.CENTER);
    }

    // Metodo prueba ventana admin//
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Creamos un objeto Date válido para SQL
            java.sql.Date fechaNac = java.sql.Date.valueOf("1990-01-01");

            // Agregamos 'new' y los tipos de datos correctos
            Usuarios adminPrueba = new Usuarios(1, "Admin", "Sistema", fechaNac, "12345678",
                    "admin@mail.com", "88888888", "admin123", 1);

            new FrmDashboardAdmin(adminPrueba).setVisible(true);
        });
    }
}