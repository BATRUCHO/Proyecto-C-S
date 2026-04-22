package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Dominio.Paquete;
import Dominio.UbicacionVehiculo;
import PaqueteCliente.Controlador.ClienteControlador;

public class FrmRastreoCliente extends JFrame {
    private JTextField txtIdPaquete;
    private JTextArea txtResultado;
    private ClienteControlador clienteControl = new ClienteControlador();

    public FrmRastreoCliente() {
        setTitle("LogiTrack - Rastreo de Envíos");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBusqueda.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel lblInstruccion = new JLabel("Ingrese su número de guía (ID):", SwingConstants.CENTER);
        txtIdPaquete = new JTextField();
        JButton btnBuscar = new JButton("Rastrear Paquete");
        btnBuscar.setBackground(new Color(41, 128, 185));
        btnBuscar.setForeground(Color.WHITE);

        panelBusqueda.add(lblInstruccion);
        panelBusqueda.add(txtIdPaquete);
        panelBusqueda.add(btnBuscar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setBackground(new Color(236, 240, 241));

        add(panelBusqueda, BorderLayout.NORTH);
        add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscarPaquete());
    }

    private void buscarPaquete() {
        try {
            int id = Integer.parseInt(txtIdPaquete.getText());
            Paquete pkg = clienteControl.rastrearPaquete(id);

            if (pkg != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("--- DETALLE DEL ENVÍO ---\n");
                sb.append("Descripción: ").append(pkg.getDescripcion()).append("\n");
                sb.append("Destino: ").append(pkg.getDireccion_entrega()).append("\n");
                sb.append("Estado: ").append(interpretarEstado(pkg.getId_estado())).append("\n\n");

                // Buscamos la ubicación GPS si tiene vehículo asignado
                UbicacionVehiculo ubi = clienteControl.obtenerUbicacionActual(pkg.getId_vehiculo());
                if (ubi != null) {
                    sb.append("--- UBICACIÓN EN TIEMPO REAL ---\n");
                    sb.append("Latitud: ").append(ubi.getLatitud()).append("\n");
                    sb.append("Longitud: ").append(ubi.getLongitud()).append("\n");
                    sb.append("Última actualización: ").append(ubi.getFecha_hora());
                }
                txtResultado.setText(sb.toString());
            } else {
                txtResultado.setText("Código de paquete no encontrado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número de ID válido.");
        }
    }

    private String interpretarEstado(int id) {
        if (id == 1) return "En Bodega";
        if (id == 2) return "En Camino (Reparto)";
        if (id == 3) return "Entregado";
        return "Desconocido";
    }
}
