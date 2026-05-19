package PaqueteCliente.Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Dominio.Vehiculo;
import PaqueteCliente.Controlador.AdminControlador;


public class FrmRegistrarVehiculo extends JDialog {

    private JTextField txtIdVehiculo,txtPlaca, txtMarca, txtModelo;
    private JButton btnGuardar, btnCancelar;
    private AdminControlador adminControl = new AdminControlador();
    private boolean exito = false; // Para avisar al Dashboard si debe refrescar la tabla 
    private Vehiculo vehiculoExistente = null;
    private final Queue<String> placasDisponibles = new ConcurrentLinkedQueue<>();

    // Constructor 1 : Para crear un nuevo vehiculo

    public FrmRegistrarVehiculo(Frame parent) {
        super(parent, "Registrar Nuevo Vehiculo", true); // true lo hace modal
        setSize(400, 520);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);

        initComponents();
        configurarEventos();
        

    }

    public FrmRegistrarVehiculo(Frame parent, Vehiculo vehiculo) {
        this(parent); // Llama automaticamente al constor uno para inicializar
        this.vehiculoExistente = vehiculo; // Se guarda el vehiculo existente

        if (vehiculoExistente != null) {
            llenarDatosParaEditar();   
        }
    }

    private void initComponents() {

        //Titulo
        JLabel lblTitulo = new JLabel("DATOS DEL VEHICULO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 400,30); // Usamos 400 de ancho para centrar el texto
        add(lblTitulo);

        addLabel("ID Vehiculo:", 50, 70);
        txtIdVehiculo = new JTextField("Autogenerado_vehiculo");
        txtIdVehiculo.setBounds(180, 70, 170, 30); // X=180 para que el label y el campo estén en la misma línea
        txtIdVehiculo.setEditable(false);
        txtIdVehiculo.setEnabled(false);
        add(txtIdVehiculo);

        addLabel("Placa", WIDTH, WIDTH);



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

    public void listaRamdon() {

        List<String> listaTemporal = new ArrayList<>();
        
        //1. Rellenar lista
        for (int i = 0; i <= 9999; i++) {
            // % - inicia la regla, 0 - indica el caracter de relleno, 4 - longitud total, d - argumento de numero entero (decimal)
            listaTemporal.add(String.format("%04d", i)); 
        }
        //2. Desordenar 
        Collections.shuffle(listaTemporal);

        //3. Se pasa la lista temporal a la cola
        this.placasDisponibles.addAll(listaTemporal);
    }

    public String generarPlacaUnica(){
        String parteNumerica = placasDisponibles.poll();

        if(parteNumerica == null){
            throw new IllegalStateException("No hay más placas disponibles");
        }
        return "XY" + parteNumerica;  
    }

    //Metodo para la edicion del paquete
    public void llenarDatosParaEditar() {

        //Se cambia el titulo de la ventana
        setTitle("Editar Paquete #" + VehiculoExistente.getId_paquete());

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
        new FrmRegistrarVehiculo(null).setVisible(true);
    }


}
