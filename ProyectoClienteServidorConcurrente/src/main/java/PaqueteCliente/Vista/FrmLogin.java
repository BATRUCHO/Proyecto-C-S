package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import PaqueteCliente.Controlador.AutenticacionControlador;

public class FrmLogin extends JFrame {

    // Componentes de la interfaz
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblLogo;

    public FrmLogin() {
        // Configuraciones básicas del JFrame
        setTitle("Sistema LogiTrack - Acceso");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setResizable(false);
        setLayout(new BorderLayout());

        initComponents();
        configurarEventos();
    }

    private void initComponents() {
        // 1. Panel Superior para el Logo
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setPreferredSize(new Dimension(400, 200));

        lblLogo = new JLabel("", SwingConstants.CENTER);
        // Aquí cargamos tu foto. Asegúrate de que la ruta sea correcta.
        cargarLogo("src/resources/logo.png"); 
        
        panelSuperior.add(lblLogo, BorderLayout.CENTER);

        // 2. Panel Central para el Formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(null); // Layout libre para posicionar exacto
        panelFormulario.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBounds(130, 20, 150, 30);
        panelFormulario.add(lblTitulo);

        // Campo Email
        JLabel lblEmail = new JLabel("Correo Electrónico:");
        lblEmail.setBounds(50, 70, 200, 20);
        panelFormulario.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(50, 95, 300, 35);
        panelFormulario.add(txtEmail);

        // Campo Contraseña
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(50, 150, 200, 20);
        panelFormulario.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 175, 300, 35);
        panelFormulario.add(txtPassword);

        // Botón Ingresar
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBounds(100, 240, 200, 45);
        btnIngresar.setBackground(new Color(41, 128, 185)); // Azul profesional
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelFormulario.add(btnIngresar);

        // Agregar paneles al Frame
        add(panelSuperior, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
    }

    private void cargarLogo(String ruta) {
        try {
            ImageIcon icon = new ImageIcon(ruta);
            // Redimensionar imagen para que quepa en el espacio (200x150)
            Image img = icon.getImage().getScaledInstance(180, 130, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("LOGO NO ENCONTRADO");
        }
    }

    private void configurarEventos() {
        btnIngresar.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPassword.getPassword());

            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete las credenciales.");
                return;
            }

            // Llamamos al controlador de autenticación que ya definimos
            AutenticacionControlador auth = new AutenticacionControlador();
            auth.iniciarSesion(email, pass, this);
        });
    }

    // Método main para probar la vista individualmente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}
