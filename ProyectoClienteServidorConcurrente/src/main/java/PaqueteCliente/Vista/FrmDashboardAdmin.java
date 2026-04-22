package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Dominio.Paquete;
import Dominio.Usuario;
import PaqueteCliente.Controlador.AdminControlador;

public class FrmDashboardAdmin extends JFrame {

    private Usuario adminLogueado;
    private JTable tblPaquetes;
    private DefaultTableModel modeloPaquetes;
    // Instancia del controlador para usarla en toda la clase
    private AdminControlador adminControl = new AdminControlador();

    public FrmDashboardAdmin(Usuario admin) {
        this.adminLogueado = admin;
        
        setTitle("LogiTrack - Panel de Administración");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        
        // ¡PUNTO CLAVE!: Cargamos los datos apenas se abre la ventana
        refrescarTablaPaquetes();
    }

    private void initComponents() {
        // Barra Superior
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInfo.setBackground(new Color(41, 128, 185));
        JLabel lblUser = new JLabel("Bienvenido: " + adminLogueado.getNombre() + " (" + adminLogueado.getEmail() + ")  ");
        lblUser.setForeground(Color.WHITE);
        panelInfo.add(lblUser);
        add(panelInfo, BorderLayout.NORTH);

        // Pestañas
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("📦 Gestión de Paquetes", crearPanelPaquetes());
        
        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelPaquetes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Configuración de la Tabla
        String[] columnas = {"ID", "Descripción", "Destinatario", "Dirección", "Estado"};
        modeloPaquetes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Tabla de solo lectura
        };
        
        tblPaquetes = new JTable(modeloPaquetes);
        panel.add(new JScrollPane(tblPaquetes), BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnRefrescar = new JButton("Actualizar Lista");
        JButton btnNuevo = new JButton("Registrar Paquete");
        
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnNuevo);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // --- EVENTOS USANDO EL CONTROLADOR ---
        
        btnRefrescar.addActionListener(e -> refrescarTablaPaquetes());


        btnNuevo.addActionListener(e -> {
            FrmNuevoPaquete ventanaNuevo = new FrmNuevoPaquete(this);
            ventanaNuevo.setVisible(true);
            
            
            if (ventanaNuevo.isExito()) {
                refrescarTablaPaquetes();
            }
        });

        return panel;
    }

    /**
     * Usa el AdminControlador para pedir datos al servidor y limpiar/llenar la tabla
     */
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
}