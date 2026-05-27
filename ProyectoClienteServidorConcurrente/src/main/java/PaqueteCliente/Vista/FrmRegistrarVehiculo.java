package PaqueteCliente.Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Dominio.Vehiculo;
import PaqueteCliente.Controlador.AdminControlador;


public class FrmRegistrarVehiculo extends JDialog {

    private JTextField txtIdVehiculo,txtPlaca,txtEstado;
    private JButton btnGuardar, btnCancelar;
    private AdminControlador adminControl = new AdminControlador();
    private boolean exito = false; // Para avisar al Dashboard si debe refrescar la tabla 
    private Vehiculo vehiculoExistente = null;

    private final Queue<String> placasDisponibles = new ConcurrentLinkedQueue<>();
    private JComboBox<String> cbMarca;
    private JComboBox<String> cbModelo;
    private JComboBox<String> cbTipo;

    // Constructor 1 : Para crear un nuevo vehiculo

    public FrmRegistrarVehiculo(Frame parent) {
        super(parent, "Registrar Nuevo Vehiculo", true); // true lo hace modal
        setSize(450, 600);
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
        txtIdVehiculo.setBounds(180, 70, 170, 30); 
        txtIdVehiculo.setEditable(false);
        txtIdVehiculo.setEnabled(false);
        add(txtIdVehiculo);

        addLabel("Placa", 50, 110);
        txtPlaca = addTextField(50, 135);
        listaRamdon();
        txtPlaca.setText(generarPlacaUnica());
        txtPlaca.setEditable(false);
        txtPlaca.setEnabled(false);

        addLabel("Estado", 50, 160);
        txtEstado = addTextField(50, 185); 
        txtEstado.setText("Disponible");
        txtEstado.setEditable(false);
        txtEstado.setEnabled(false);
        
        addLabel("Marca de vehiculo", 50, 220);
        cbMarca = new JComboBox<>(new String[]{"Selecionar dato","Toyota","Mercedez-Beans","Suzuki","Iveco","Freightliner","Volvo","Ford","Hino","Honda","Nissan"});
        cbMarca.setBounds(180, 220, 170, 30);
        add(cbMarca);

        addLabel("Modelo de vehiculo", 50, 270);
        cbModelo = new JComboBox<>(new String[]{
        "Selecionar dato","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002",
        "2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014",
        "2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026"});
        cbModelo.setBounds(180, 270, 170, 30);
        add(cbModelo);

        addLabel("Tipo de vehiculo", 50, 320);  //Mejorar
        cbTipo = new JComboBox<>(new String[]{"Selecionar dato","1","2","3"});
        cbTipo.setBounds(180, 320, 170, 30);
        add(cbTipo);

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
            int indexMarca = cbMarca.getSelectedIndex();
            int indexModelo = cbModelo.getSelectedIndex();
            int indexTipo = cbTipo.getSelectedIndex();
            
            String placa = txtPlaca.getText().trim();
            String estado = txtEstado.getText().trim(); 

            // 2. Validación estricta de selección (evitamos index 0 que es "Selecionar dato")
            if (indexMarca == 0 || indexModelo == 0 || indexTipo == 0 || placa.isEmpty() || estado.isEmpty()){
                JOptionPane.showMessageDialog(this, "Todos los datos deben son obligatorios");
                return;
            }

            // 3. Llamada al controlador con la variable ya validada

            String marca = (String) cbMarca.getSelectedItem();
            String modelo = (String) cbModelo.getSelectedItem();
            int tipo = Integer.parseInt((String) cbTipo.getSelectedItem());


            boolean res;
            if (vehiculoExistente == null){
                //Modo crear
                res = adminControl.registrarNuevoVehiculo(placa, marca, modelo, tipo, estado);
            }else{
                //Modo editar
                vehiculoExistente.setPlaca(placa);
                vehiculoExistente.setMarca(marca);
                vehiculoExistente.setModelo(modelo);
                vehiculoExistente.setId_tipo_vehiculo(tipo);
                vehiculoExistente.setEstado(estado);

                res = adminControl.editarVehiculo(vehiculoExistente);
            }

            if (res) {
                JOptionPane.showMessageDialog(this, "Vehiculo editado exitosamente.");
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

    //Metodo para generar placas aleatorias unicas 
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
        setTitle("Editar Vehiculo #" + vehiculoExistente.getId_vehiculo());

        //Carga de datos en los campos del Jtextfield
        txtIdVehiculo.setText(Integer.toString(vehiculoExistente.getId_vehiculo()));
        txtPlaca.setText(vehiculoExistente.getPlaca());
 
        //Carga de datos en los combobox
        cbMarca.setSelectedItem((Object) vehiculoExistente.getMarca());
        cbModelo.setSelectedItem((Object) vehiculoExistente.getModelo());
        cbTipo.setSelectedItem((Object) String.valueOf(vehiculoExistente.getId_tipo_vehiculo()));
        txtEstado.setText(vehiculoExistente.getEstado());

    }

    // Método main para probar la vista individualmente//
    public static void main(String[] args) {
        new FrmRegistrarVehiculo(null).setVisible(true);
    }

}
