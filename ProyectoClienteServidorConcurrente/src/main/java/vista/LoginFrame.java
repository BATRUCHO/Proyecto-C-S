package vista;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private final JTextField tfUsuario = new JTextField(15);
    private final JPasswordField pfPassword = new JPasswordField(15);
    private final JLabel lblStatus = new JLabel(" ");

    public LoginFrame() {
        super("Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(340, 180);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // Usuario
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(tfUsuario, gbc);

        // Contraseña
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(pfPassword, gbc);

        // Botón
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnLogin = new JButton("Iniciar sesión");
        panel.add(btnLogin, gbc);

        // Estado
        gbc.gridy = 3;
        panel.add(lblStatus, gbc);

        add(panel);

        // Acción de login (sin socket)
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String usuario = tfUsuario.getText();
        String password = new String(pfPassword.getPassword());

        LoginController controller = new LoginController();
        boolean exito = controller.login(usuario, password);

        if (exito) {
            lblStatus.setText("Login correcto. ¡Bienvenido!");
            // Aquí podrías abrir la siguiente ventana (MonitorVehiculoFrame) si lo deseas
            // Por ejemplo:
            // new MonitorVehiculoFrame().setVisible(true);
        } else {
            lblStatus.setText("Credenciales inválidas. Intenta de nuevo.");
        }
    }

    // Método de entrada para pruebas rápidas
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}