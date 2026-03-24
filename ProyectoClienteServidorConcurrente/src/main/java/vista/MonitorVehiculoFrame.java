package vista;

import com.hbatrucho.proyecto.controlador.MonitorController;

import javax.swing.*;
import java.awt.*;

public class MonitorVehiculoFrame extends JFrame {

    private final MonitorController controller = new MonitorController();
    private final JLabel lblEstado = new JLabel("Estado: Desconocido");

    public MonitorVehiculoFrame() {
        super("Monitor de Vehículos");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Área de estado
        JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estadoPanel.add(new JLabel("Estado general:"));
        estadoPanel.add(lblEstado);

        // Botón de refrescar (sin socket)
        JButton btnRefresh = new JButton("Refrescar estado");
        btnRefresh.addActionListener(e -> refrescarEstado());

        panel.add(estadoPanel, BorderLayout.NORTH);
        panel.add(btnRefresh, BorderLayout.SOUTH);

        add(panel);

        // Carga inicial
        refrescarEstado();
    }

    private void refrescarEstado() {
        String estado = controller.obtenerEstadoGeneral();
        lblEstado.setText(estado);
    }

    // Método de prueba
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MonitorVehiculoFrame().setVisible(true);
        });
    }
}