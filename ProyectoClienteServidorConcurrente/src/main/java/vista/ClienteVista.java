
package vista;

import javax.swing.*;
import java.awt.*;

public class ClienteVista extends JFrame {

    private JTextField txtHost = new JTextField("localhost");
    private JTextField txtPuerto = new JTextField("5000");
    private JTextField txtMensaje = new JTextField();

    private JButton btnConectar = new JButton("Conectar");
    private JButton btnEnviar = new JButton("Enviar");

    private JTextArea areaMensajes = new JTextArea();

    public ClienteVista() {
        setTitle("Cliente Socket");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelTop = new JPanel(new GridLayout(2,2));
        panelTop.add(new JLabel("Host:"));
        panelTop.add(txtHost);
        panelTop.add(new JLabel("Puerto:"));
        panelTop.add(txtPuerto);

        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.add(new JScrollPane(areaMensajes), BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new GridLayout(1,2));
        panelBottom.add(txtMensaje);
        panelBottom.add(btnEnviar);

        add(panelTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        add(btnConectar, BorderLayout.WEST);
    }

    public JButton getBtnConectar() { return btnConectar; }
    public JButton getBtnEnviar() { return btnEnviar; }

    public JTextField getTxtHost() { return txtHost; }
    public JTextField getTxtPuerto() { return txtPuerto; }
    public JTextField getTxtMensaje() { return txtMensaje; }

    public void mostrarMensaje(String msg) {
        areaMensajes.append(msg + "\n");
    }
}