package PaqueteCliente.Vista;

import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import PaqueteCliente.Controlador.AdminControlador;

public class FrmNuevoUsuario extends JDialog {
    private JTextField txtDni, txtNombre, txtApellido, txtEmail, txtFecha;
    private JPasswordField txtPass;
    private JComboBox<String> cbRol;
    private AdminControlador adminControl = new AdminControlador();

    public FrmNuevoUsuario(Frame parent) {
        super(parent, "Registrar Nuevo Personal", true);
        setSize(350, 500);
        setLayout(new GridLayout(8, 2, 10, 10));
        setLocationRelativeTo(parent);

        add(new JLabel(" DNI:")); txtDni = new JTextField(); add(txtDni);
        add(new JLabel(" Nombre:")); txtNombre = new JTextField(); add(txtNombre);
        add(new JLabel(" Apellido:")); txtApellido = new JTextField(); add(txtApellido);
        add(new JLabel(" Fecha Nac (YYYY-MM-DD):")); txtFecha = new JTextField(); add(txtFecha);
        add(new JLabel(" Email:")); txtEmail = new JTextField(); add(txtEmail);
        add(new JLabel(" Contraseña:")); txtPass = new JPasswordField(); add(txtPass);
        add(new JLabel(" Rol:")); 
        cbRol = new JComboBox<>(new String[]{"Administrador", "Conductor"}); 
        add(cbRol);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        add(btnGuardar);
    }

    private void guardar() {
        try {
            // Conversión de datos
            Date fechaSql = Date.valueOf(txtFecha.getText());
            int rolId = cbRol.getSelectedIndex() + 1; // Admin=1, Conductor=2

            boolean exito = adminControl.registrarUsuario(
                txtNombre.getText(), txtApellido.getText(), fechaSql,
                txtDni.getText(), txtEmail.getText(), "8888-8888", 
                new String(txtPass.getPassword()), rolId
            );

            if (exito) {
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Verifique el formato de fecha.");
        }
    }
}
