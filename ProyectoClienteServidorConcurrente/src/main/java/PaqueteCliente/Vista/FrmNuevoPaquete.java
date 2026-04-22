package PaqueteCliente.Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import PaqueteCliente.Controlador.AdminControlador;

public class FrmNuevoPaquete extends JDialog {

    private JTextField txtDescripcion, txtDestinatario, txtDireccion;
    private JButton btnGuardar, btnCancelar;
    private AdminControlador adminControl = new AdminControlador();
    private boolean exito = false; // Para avisar al Dashboard si debe refrescar la tabla

    public FrmNuevoPaquete(Frame parent) {
        super(parent, "Registrar Nuevo Paquete", true); // true lo hace modal
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);

        initComponents();
        configurarEventos();
    }

    private void initComponents() {
        // Título
        JLabel lblTitulo = new JLabel("DATOS DEL PAQUETE");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBounds(110, 20, 200, 30);
        add(lblTitulo);

        // Descripción
        addLabel("Descripción:", 50, 70);
        txtDescripcion = addTextField(50, 95);

        // Destinatario
        addLabel("Nombre del Destinatario:", 50, 150);
        txtDestinatario = addTextField(50, 175);

        // Dirección
        addLabel("Dirección de Entrega:", 50, 230);
        txtDireccion = addTextField(50, 255);

    
        // Botones
        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBounds(50, 380, 130, 40);
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        add(btnGuardar);

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBounds(220, 380, 130, 40);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        add(btnCancelar);
    }

    // Métodos auxiliares para no repetir código de diseño
    private void addLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(x, y, 200, 20);
        add(lbl);
    }

    private JTextField addTextField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 300, 30);
        add(txt);
        return txt;
    }

    private void configurarEventos() {
        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(e -> {
            // 1. Validar campos básicos
            if (txtDescripcion.getText().isEmpty() || txtDestinatario.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción y destinatario son obligatorios.");
                return;
            }

            // 2. Llamar al controlador
            boolean res = adminControl.registrarNuevoPaquete(
                txtDescripcion.getText(),
                "Remitente General",
                txtDestinatario.getText(),
                txtDireccion.getText()
            );

            if (res) {
                JOptionPane.showMessageDialog(this, "Paquete registrado exitosamente.");
                this.exito = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isExito() { return exito; }
}
