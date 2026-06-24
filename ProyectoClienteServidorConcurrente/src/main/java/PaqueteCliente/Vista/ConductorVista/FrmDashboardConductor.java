package PaqueteCliente.Vista.ConductorVista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import Dominio.Usuarios;
import PaqueteCliente.Controlador.RutaControlador;

public class FrmDashboardConductor extends JFrame {

    private Usuarios conductorLogueado;
    private JTable tblEntregas;
    private DefaultTableModel modeloTabla;
    private RutaControlador rutaControl = new RutaControlador();
    private Timer timerGps; // Para automatizar el envío de ubicación

    public FrmDashboardConductor(Usuarios conductor) {
        this.conductorLogueado = conductor;

        setTitle("LogiTrack - Panel de Conductor | " + conductor.getNombre());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();

    }

    private void initComponents() {
        // Barra Superior: Identidad y Estado del GPS
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(39, 174, 96)); // Verde para conductores

        JLabel lblNombre = new JLabel("  Conductor: " + conductorLogueado.getNombre());
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton btnCerrarSesion = new JButton("Salir");

        panelNorte.add(lblNombre, BorderLayout.WEST);
        panelNorte.add(btnCerrarSesion, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);

        // Centro: Lista de Paquetes Asignados
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder("Mis Entregas Pendientes"));

        String[] columnas = { "ID Paquete", "Destinatario", "Dirección", "Estado" };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tblEntregas = new JTable(modeloTabla);
        panelCentro.add(new JScrollPane(tblEntregas), BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Sur: Acciones de Entrega
        JPanel panelSur = new JPanel();
        JButton btnEntregado = new JButton("Marcar como Entregado");
        JButton btnRefrescar = new JButton("Actualizar Lista");

        btnEntregado.setBackground(new Color(41, 128, 185));
        btnEntregado.setForeground(Color.WHITE);

        panelSur.add(btnRefrescar);
        panelSur.add(btnEntregado);
        add(panelSur, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        // Creamos un objeto Date válido para SQL
        java.sql.Date fechaNac = java.sql.Date.valueOf("1990-01-01");
        Usuarios conductorPrueba = new Usuarios(2, "Conductor", "Sistema", fechaNac, "12345678",
                "conductor@mail.com", "88888888", "admin123", 2);

        new FrmDashboardConductor(conductorPrueba).setVisible(true);
    }

}
