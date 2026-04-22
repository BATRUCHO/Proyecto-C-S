package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import Dominio.Paquete;
import Dominio.Usuario;
import PaqueteCliente.Controlador.RutaControlador;

public class FrmDashboardConductor extends JFrame {

    private Usuario conductorLogueado;
    private JTable tblEntregas;
    private DefaultTableModel modeloTabla;
    private RutaControlador rutaControl = new RutaControlador();
    private Timer timerGps; // Para automatizar el envío de ubicación

    public FrmDashboardConductor(Usuario conductor) {
        this.conductorLogueado = conductor;

        setTitle("LogiTrack - Panel de Conductor | " + conductor.getNombre());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        refrescarEntregas();
        iniciarSimuladorGps();
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

        String[] columnas = {"ID Paquete", "Destinatario", "Dirección", "Estado"};
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

        // Eventos
        btnRefrescar.addActionListener(e -> refrescarEntregas());
        
        btnEntregado.addActionListener(e -> marcarEntregado());

        btnCerrarSesion.addActionListener(e -> {
            timerGps.stop(); // Detener GPS al salir
            new FrmLogin().setVisible(true);
            dispose();
        });
    }

    private void refrescarEntregas() {
        // Usamos el controlador para traer solo mis paquetes
        List<Paquete> lista = rutaControl.obtenerMisPaquetes(conductorLogueado.getIdUsuario());
        modeloTabla.setRowCount(0);

        if (lista != null) {
            for (Paquete p : lista) {
                Object[] fila = {
                    p.getId_paquete(),
                    p.getDestinatario(),
                    p.getDireccion_entrega(),
                    p.getId_estado() == 2 ? "En Ruta" : "Pendiente"
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    private void marcarEntregado() {
        int fila = tblEntregas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete de la lista.");
            return;
        }

        int idPkg = (int) tblEntregas.getValueAt(fila, 0);
        // Estado 3 = Entregado
        if (rutaControl.actualizarEstadoEntrega(idPkg, 3)) {
            JOptionPane.showMessageDialog(this, "¡Paquete entregado con éxito!");
            refrescarEntregas();
        }
    }

    private void iniciarSimuladorGps() {
        // Simulamos que el GPS envía la ubicación cada 30 segundos
        timerGps = new Timer(30000, e -> {
            // Coordenadas de prueba (San José, CR)
            double lat = 9.9281 + (Math.random() * 0.01);
            double lon = -84.0907 + (Math.random() * 0.01);
            
            // Asumimos que el usuario tiene un id_vehiculo asociado
            rutaControl.enviarUbicacionGps(conductorLogueado.getIdUsuario(), lat, lon);
            System.out.println("GPS: Ubicación actualizada automáticamente.");
        });
        timerGps.start();
    }
}
