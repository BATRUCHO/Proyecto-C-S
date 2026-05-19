package PaqueteCliente.Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Dominio.Paquete;
import PaqueteCliente.Controlador.AdminControlador;

public class FrmNuevoPaquete extends JDialog {

    private JTextField txtDescripcion, txtDestinatario, txtDireccion,txtIdPaquete, txtFechaCreacion, txtPeso;
    private JButton btnGuardar, btnCancelar;
    private AdminControlador adminControl = new AdminControlador();
    private boolean exito = false; // Para avisar al Dashboard si debe refrescar la tabla 
    private Paquete paqueteExistente = null;


    // Constructor 1 : Para crear un nuevo paquete
    public FrmNuevoPaquete(Frame parent) {
        super(parent, "Registrar Nuevo Paquete", true); // true lo hace modal
        setSize(400, 520);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);

        initComponents();
        configurarEventos();

    }

    // Constructor 2 : Para editar un paquete existente (Sobrecarga)
    public FrmNuevoPaquete(Frame parent, Paquete paquete) {
        this(parent); // Llama automaticamente al constor uno para inicializar
        this.paqueteExistente = paquete; // Se guarda el paquete existente

        if (paqueteExistente != null) {
            llenarDatosParaEditar();   
        }
    }

    private void initComponents() {
        
        // Título - Centrado (y=20)
        JLabel lblTitulo = new JLabel("DATOS DEL PAQUETE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 400, 30); // Usamos 400 de ancho para centrar el texto
        add(lblTitulo);

        // 1. ID Seguimiento (y=70)
        addLabel("ID Seguimiento:", 50, 70);
        txtIdPaquete = new JTextField("Autogenerado");
        txtIdPaquete.setBounds(180, 70, 170, 30); // X=180 para que el label y el campo estén en la misma línea
        txtIdPaquete.setEditable(false);
        txtIdPaquete.setEnabled(false);
        add(txtIdPaquete);

        // 2. Fecha (y=110)
        addLabel("Fecha Registro:", 50, 110);
        txtFechaCreacion = new JTextField(new java.sql.Date(System.currentTimeMillis()).toString());
        txtFechaCreacion.setBounds(180, 110, 170, 30);
        txtIdPaquete.setEditable(false);
        txtFechaCreacion.setEnabled(false);
        add(txtFechaCreacion);

        // 3. Peso (y=150)
        addLabel("Peso (kg):", 50, 150);
        txtPeso = new JTextField("0.0");
        txtPeso.setBounds(180, 150, 170, 30);
        add(txtPeso);

        String placeholder = "0,0";
        txtPeso.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e){
                //Al hacer click, si el texto es el guia, se borra
                if(txtPeso.getText().equals(placeholder)) {
                    txtPeso.setText("");
                    txtPeso.setForeground(Color.BLACK); // Cambia a color negro al escritorio
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e){
                // Al salir, si el usuario no escribio nada, se vuelve a poner la guia
                if(txtPeso.getText().isEmpty()){
                    txtPeso.setText(placeholder);
                    txtPeso.setForeground(Color.GRAY); // Cambia a color gris al salir
                }
            }
        });

        txtPeso.setText(placeholder);
        txtPeso.setForeground(Color.GRAY);



        // --- SECCIÓN DE TEXTO (AQUÍ USAMOS ANCHO COMPLETO) ---

        // 4. Descripción (y=200)
        addLabel("Descripción del paquete:", 50, 190);
        txtDescripcion = addTextField(50, 215); // x=50, y=215

        // 5. Destinatario (y=255)
        addLabel("Nombre del destinatario:", 50, 255);
        txtDestinatario = addTextField(50, 280); // x=50, y=280

        // 6. Dirección (y=320)
        addLabel("Dirección de Entrega:", 50, 320);
        txtDireccion = addTextField(50, 345); // x=50, y=345

        // 7. Botones (y=410)
        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBounds(50, 410, 130, 40);
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        add(btnGuardar);

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBounds(220, 410, 130, 40);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        add(btnCancelar);

        // Tama;o de la ventana
        this.setSize(400, 520); 
    }

    private void configurarEventos() {

        //Boton para cancelar accion de registro
        btnCancelar.addActionListener(e -> dispose());

        //Boton para guardar accion de registro
        btnGuardar.addActionListener(e -> {
            // 1. Captura de datos y limpieza de espacios
            String desc = txtDescripcion.getText().trim();
            String dest = txtDestinatario.getText().trim();
            String dir = txtDireccion.getText().trim();
            String pesoTexto = txtPeso.getText().trim();
            String placeholder = "0,0"; // O "0.0" según definiste arriba

            // 2. Validación de campos de texto obligatorios
            if (desc.isEmpty() || dest.isEmpty() || dir.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción, el destinatario y la dirección son obligatorios.");
                return;
            }

            // 3. Validación lógica del Peso (Evitar NumberFormatException)
            double pesoFinal = 0.0;
            if (!pesoTexto.equals(placeholder) && !pesoTexto.isEmpty()) {
                try {
                    // Reemplazamos coma por punto por si el usuario usa formato latino
                    pesoFinal = Double.parseDouble(pesoTexto.replace(",", "."));
                    
                    if (pesoFinal <= 0) {
                        JOptionPane.showMessageDialog(this, "El peso debe ser mayor a 0.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para el peso.");
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe indicar el peso del paquete.");
                return;
            }

            // 4. Llamada al controlador con la variable ya validada
           boolean res;
           if (paqueteExistente == null){
                //Modo crear
             res = adminControl.registrarNuevoPaquete(pesoFinal, desc, "Remitente", dest, dir);
            }else{
                //Modo editar
                paqueteExistente.setDescripcion(desc);
                paqueteExistente.setDestinatario(dest);
                paqueteExistente.setDireccion_entrega(dir);
                paqueteExistente.setPeso(pesoFinal);

                res = adminControl.editarPaquete(paqueteExistente);
            }

            if (res) {
                JOptionPane.showMessageDialog(this, "Paquete editado exitosamente.");
                this.exito = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error de red: No se pudo conectar con el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    //---------------Metodos auxiliares--------------------//

    //Metodo de automaticacion para los titulos //
    private void addLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setBounds(x, y, 150, 25);
        add(label);
    }

    //Metodo de automaticacion para los campos//
    private JTextField addTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 300, 30);
        add(field);
        return field;
    }

    //Metodo de automaticacion para los botones//
    public boolean isExito() {
        return exito;
    }

    //Metodo para la edicion del paquete
    public void llenarDatosParaEditar() {

        //Se cambia el titulo de la ventana
        setTitle("Editar Paquete #" + paqueteExistente.getId_paquete());

        //Carga de datos en los campos del Jtextfield
        txtIdPaquete.setText(Integer.toString(paqueteExistente.getId_paquete()));
        txtDescripcion.setText(paqueteExistente.getDescripcion());
        txtDestinatario.setText(paqueteExistente.getDestinatario());
        txtDireccion.setText(paqueteExistente.getDireccion_entrega());

        txtPeso.setText(Double.toString(paqueteExistente.getPeso()));
        txtPeso.setForeground(Color.BLACK);
    }

    // Método main para probar la vista individualmente//
    public static void main(String[] args) {
        new FrmNuevoPaquete(null).setVisible(true);
    }
}
