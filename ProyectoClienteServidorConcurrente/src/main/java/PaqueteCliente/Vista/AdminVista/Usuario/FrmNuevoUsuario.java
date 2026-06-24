package PaqueteCliente.Vista.AdminVista.Usuario;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JDateChooser;

import Dominio.Roles;
import Dominio.Usuarios;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Utilidades.Encriptador;

public class FrmNuevoUsuario extends JDialog {

    private JTextField txtId, txtNombre, txtApellido, txtDni, txtEmail, txtTelefono;
    private JButton btnGuardar, btnCancelar;
    private final AdminControlador adminControl = new AdminControlador();
    private JComboBox<String> cbRol;
    private Usuarios usuarioExistente = null;
    private boolean exito = false; // Para avisar al Dashboard si debe refrescar la tabla

    private JDateChooser jdFechaNacimiento;

    private JPasswordField txtPass;
    private JButton btnVerPass;
    private boolean passVisible = false;

    public FrmNuevoUsuario(Frame parent) {
        super(parent, "Registrar Nuevo Personal", true);
        setSize(400, 520);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);

        initComponents();
        configurarEventos();
    }

    // Constructor utilizado para editar
    public FrmNuevoUsuario(PanelUsuario parentFrame, Usuarios usuarioEncontrado) {
        super((Frame) SwingUtilities.getWindowAncestor(parentFrame), "Editar Datos del Usuario", true);
        setSize(400, 520);
        setLocationRelativeTo(parentFrame);
        setLayout(null);
        setResizable(false);

        initComponents();
        configurarEventos();

        this.usuarioExistente = usuarioEncontrado;

        if (usuarioExistente != null) {
            llenarDatosParaEditar();
        }
    }

    private void initComponents() {

        // 1. Titulo
        JLabel lblTitulo = new JLabel("DATOS DEL USUARIO", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 400, 30);
        add(lblTitulo);

        // 2. ID
        addLabel("ID", 50, 70);
        txtId = new JTextField(" --Autogenerado por Sistema-- ");
        txtId.setBounds(100, 70, 170, 30);
        txtId.setEditable(false);
        txtId.setForeground(Color.BLACK);
        txtId.setBackground(Color.WHITE);
        add(txtId);

        // 3. Nombre
        addLabel("Nombre de usuario:", 50, 100);
        txtNombre = addTextField(50, 125);

        // 4. Apellido
        addLabel("Apellido de usuario:", 50, 150);
        txtApellido = addTextField(50, 175);

        // 4. DNI
        addLabel("DNI:", 50, 200);
        txtDni = addTextField(50, 225);

        // 5. Email
        addLabel("Email:", 50, 250);
        txtEmail = addTextField(50, 275);

        // 6. Telefono
        addLabel("Telefono:", 50, 300);
        txtTelefono = addTextField(50, 325);

        // 7. Password
        addLabel("Password:", 50, 350);
        txtPass = new JPasswordField();
        txtPass.setBounds(50, 375, 200, 30);
        add(txtPass);

        // 8. Boton para revisar la contraseña
        btnVerPass = new JButton("👁");
        btnVerPass.setBounds(275, 375, 55, 30);
        btnVerPass.setFocusable(false); // Evita que el boton robe el foco
        add(btnVerPass);

        // 8. Rol
        addLabel("Rol:", 50, 425);
        cbRol = new JComboBox<>(new String[] { " -Seleccionar- ", "Administrador", "Conductor" });
        cbRol.setBounds(180, 425, 125, 30);
        add(cbRol);

        // 9. Fecha de nacimiento
        addLabel("Fecha de nacimiento:", 50, 475);
        jdFechaNacimiento = new JDateChooser();
        jdFechaNacimiento.setBounds(180, 475, 125, 30);
        jdFechaNacimiento.setDateFormatString("dd/MM/yyyy");

        // 1. CAPTURA DEL COMPONENTE TEXTO INTERNO
        // El editor visual es en realidad un JTextField encubierto, lo recuperamos con
        // un casteo seguro
        JTextField textEditor = (JTextField) jdFechaNacimiento.getDateEditor().getUiComponent();
        // 2. CONFIGURACIÓN DE BLOQUEO Y ESTRELLA ESTÉTICA
        textEditor.setEditable(false); // Bloquea el teclado manual (¡No se puede escribir!)
        textEditor.setBackground(Color.WHITE); // Forzamos el fondo blanco limpio para que no sea transparente
        textEditor.setForeground(Color.BLACK); // Aseguramos que las letras de la fecha se lean en negro nítido

        add(jdFechaNacimiento);

        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBounds(50, 550, 130, 40);
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        add(btnGuardar);

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBounds(220, 550, 130, 40);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        add(btnCancelar);

        this.setSize(400, 720);

    }

    private void configurarEventos() {

        // Boton para cancelar accion de registro
        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(e -> {

            // 1. Captura de datos básicos
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();

            String dni = txtDni.getText().trim();
            if (!txtDni.getText().trim().matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El DNI debe contener solo números.");
                return;
            }
            int CantidadLetras = dni.length();
            if (CantidadLetras != 9) {
                JOptionPane.showMessageDialog(this, "El DNI debe tener 9 caracteres.");
                return;
            }

            String email = txtEmail.getText().trim();
            String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!email.matches(regexEmail)) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un email válido.");
                return;
            }
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete las credenciales.");
                return;
            }

            String telefono = txtTelefono.getText().trim();
            if (!txtTelefono.getText().trim().matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números.");
                return;
            }

            char[] passwordArreglo = txtPass.getPassword();
            if (passwordArreglo.length == 0) {
                JOptionPane.showMessageDialog(this, "La contraseña es obligatoria.");
                return;
            }

            // Convertimos a String únicamente para pasarlo por tu función de encriptación
            String pass = new String(passwordArreglo);
            String passHash = Encriptador.hashSHA256(pass);

            // 3. Traducimos los textos visuales a los IDs reales de la Base de Datos
            String rol = cbRol.getSelectedItem().toString();
            int rolId = Roles.obtenerIdPorTexto(rol);

            // Se blinda la opcion de ingreso de fecha vacia, si al usuario se le olvida
            // finalizar un dato
            // la fecha no colapsara con NullPointerException
            // Lo que causara una validacion controlada null
            java.sql.Date fechaNacimientoDate = (jdFechaNacimiento.getDate() != null)
                    ? new java.sql.Date(jdFechaNacimiento.getDate().getTime())
                    : null;

            if (fechaNacimientoDate == null) {
                JOptionPane.showMessageDialog(this, "La fecha de nacimiento es obligatoria.");
                return; // Detiene el flujo de guardado de forma segura
            }

            // 4. Validación estricta
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || email.isEmpty() || telefono.isEmpty()
                    || passHash.isEmpty() || rolId == 0) {
                JOptionPane.showMessageDialog(this, "Todos los datos son obligatorios y deben ser válidos.");
                return;
            }

            boolean res;
            String mensajeExito;

            if (usuarioExistente == null) {
                // Modo crear
                res = adminControl.registrarNuevoUsuario(nombre, apellido, fechaNacimientoDate, dni, email, telefono,
                        passHash, rolId);
                mensajeExito = "Usuario creado exitosamente.";
            } else {
                // Modo editar
                mensajeExito = "Usuario editado exitosamente.";
                usuarioExistente.setNombre(nombre);
                usuarioExistente.setApellido(apellido);
                usuarioExistente.setFechaNacimiento(fechaNacimientoDate);
                usuarioExistente.setDni(dni);
                usuarioExistente.setEmail(email);
                usuarioExistente.setTelefono(telefono);
                usuarioExistente.setPassword(passHash);
                usuarioExistente.setIdRol(rolId);

                res = adminControl.editarUsuario(usuarioExistente);
            }

            if (res) {
                JOptionPane.showMessageDialog(this, mensajeExito);
                this.exito = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error de red: No se pudo conectar con el servidor.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        btnVerPass.addActionListener(e -> {
            if (!passVisible) {
                // Modo: Mostrar contraseña
                // El caracter (char) 0 le dice a Java que no use ninguna máscara visual
                txtPass.setEchoChar((char) 0);
                btnVerPass.setText("🔒"); // Cambiamos el texto del boton para indicar que se muestra la contraseña
                passVisible = true;
            } else {
                // Modo: Ocultar contraseña
                // Retornamos al puntito estándar para enmascarar el texto
                txtPass.setEchoChar('*');
                btnVerPass.setText("👁"); // Volvemos al icono del boton
                passVisible = false;

            }
            // Le devolvemos el foco al cuadro de texto para que el usuario pueda seguir
            // escribiendo
            txtPass.requestFocusInWindow();
        });
    }

    // ---------------Metodos auxiliares--------------------//

    // Metodo de automaticacion para los titulos //
    private void addLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setBounds(x, y, 150, 25);
        add(label);
    }

    // Metodo de automaticacion para los campos//
    private JTextField addTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 200, 30);
        add(field);
        return field;
    }

    // Metodo de automaticacion para los botones//
    public boolean isExito() {
        return exito;
    }

    // Metodo para la edicion del Usuario
    public void llenarDatosParaEditar() {

        // Se cambia el titulo de la ventana
        setTitle("Editar Usuario #" + usuarioExistente.getId_usuario());

        // Carga de datos en los campos del Jtextfield
        txtId.setText(String.valueOf(usuarioExistente.getId_usuario()));
        txtNombre.setText(usuarioExistente.getNombre());
        txtApellido.setText(usuarioExistente.getApellido());
        jdFechaNacimiento.setDate(usuarioExistente.getFechaNacimiento());
        txtDni.setText(usuarioExistente.getDni());
        txtEmail.setText(usuarioExistente.getEmail());
        txtTelefono.setText(usuarioExistente.getTelefono());
        txtPass.setText(usuarioExistente.getPassword());
        cbRol.setSelectedItem(Roles.obtenerTextoPorId(usuarioExistente.getIdRol()));

    }

    // Método main para probar la vista individualmente//

    public static void main(String[] args) {
        new FrmNuevoUsuario(null).setVisible(true);
    }
}
