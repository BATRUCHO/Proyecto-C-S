package PaqueteCliente.Vista.LoginVista;

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

import Dominio.Usuarios;
import PaqueteCliente.Controlador.AutenticacionControlador;
import PaqueteCliente.Utilidades.Encriptador;
import PaqueteCliente.Vista.AdminVista.FrmDashboardAdmin;
import PaqueteCliente.Vista.ConductorVista.FrmDashboardConductor;

public class FrmLogin extends JFrame {

    // Componentes de la interfaz
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblLogo;

    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;

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
        panelSuperior.setPreferredSize(new Dimension(400, 180));
        panelSuperior.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 10, 10, 10));

        lblLogo = new JLabel("", SwingConstants.CENTER);
        cargarLogo("logo.png");

        panelSuperior.add(lblLogo, BorderLayout.CENTER);

        // 2. Panel Central para el Formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
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

    private void cargarLogo(String nombreRecurso) {
        try {
            // Se llama el recurso desde la carpeta resources
            java.net.URL imgURL = getClass().getResource("/" + nombreRecurso);

            if (imgURL == null) {
                imgURL = getClass().getResource("/resources/" + nombreRecurso);
            }

            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);

                int anchoOriginal = icon.getIconWidth();
                int altoOriginal = icon.getIconHeight();

                // Lógica de escalado proporcional
                int nuevoAncho = 300; // Un poco más pequeño para dejar aire
                int nuevoAlto = (nuevoAncho * altoOriginal) / anchoOriginal;

                // Si después de calcular, el alto es demasiado para el panel, ajustamos por
                // alto
                if (nuevoAlto > 230) {
                    nuevoAlto = 230;
                    nuevoAncho = (nuevoAlto * anchoOriginal) / altoOriginal;
                }

                Image imgEscalada = icon.getImage().getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(imgEscalada));
                lblLogo.setText("");
            } else {
                lblLogo.setText("No se halló: " + nombreRecurso);
                lblLogo.setForeground(Color.RED);
            }
        } catch (Exception e) {
            lblLogo.setText("Error: " + e.getMessage());
        }
    }

    private void configurarEventos() {
        btnIngresar.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPassword.getPassword());

            // Escudo criptografico
            String passHash = Encriptador.hashSHA256(pass);

            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor complete las credenciales.",
                        "Error de Autenticación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llamar a controlador puente
            AutenticacionControlador auth = new AutenticacionControlador();
            Usuarios usuariosLogueado = auth.iniciarSesion(email, passHash);

            if (usuariosLogueado != null) {
                // Caso de exito//
                intentosFallidos = 0;
                // La vista maneja su propia redirección y cierre
                redirigirPorRol(usuariosLogueado);

            } else {
                // Caso de falla
                intentosFallidos++;

                // Limpiamos el campo de contraseña inmediatamente para borrar la clave de la
                // memoria y la UI
                txtPassword.setText("");

                if (intentosFallidos >= MAX_INTENTOS) {
                    btnIngresar.setEnabled(false); // Se bloquea el botón
                    JOptionPane.showMessageDialog(this,
                            "Has alcanzado el número máximo de intentos (" + MAX_INTENTOS
                                    + "). El acceso ha sido bloqueado por seguridad.",
                            "Bloqueo de Seguridad",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    int intentosRestantes = MAX_INTENTOS - intentosFallidos;
                    JOptionPane.showMessageDialog(this,
                            "Credenciales incorrectas. Intentos restantes: " + intentosRestantes,
                            "Error de Autenticación",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // -------------Metodos Auxiliares de Vista-------------//

    private void redirigirPorRol(Usuarios user) {

        dispose(); // Se cierra el login

        // Redirigir según el rol del usuario
        switch (user.getIdRol()) {
            case 1: // Administrador
                new FrmDashboardAdmin(user).setVisible(true);
                break;
            case 2: // Conductor
                new FrmDashboardConductor(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol no reconocido");
        }
    }

    // Método main para probar la vista individualmente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}
