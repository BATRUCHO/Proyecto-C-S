package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import Dominio.EstadoPaquete;
import Dominio.Paquete;
import Dominio.Usuarios;
import Dominio.Vehiculo;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Controlador.AutenticacionControlador;

public class FrmDashboardAdmin extends JFrame {

    private Usuarios adminLogueado;
    private JTable tblPaquetes;

    private final AdminControlador adminControl = new AdminControlador(); // Instancia del controlador
    private AutenticacionControlador authControl = new AutenticacionControlador(); // Instancia del controlador

    private  List<Paquete> listaPaquetes;
    private  List<Vehiculo> listaVehiculos;
    private  List<Usuarios> listaUsuarios;

    private DefaultTableModel modeloPaquetes;
    private DefaultTableModel modeloVehiculos;
    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloRegistros;



    public FrmDashboardAdmin(Usuarios admin) { 
        this.adminLogueado = admin;

        setTitle("LogiTrack - Panel de Administración");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        refrescarTablaPaquetes();  

        // Proceso de cierre de sesion si sale desde la X
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                authControl.cerrarSesion(adminLogueado.getId_usuario());
                System.exit(0);
            }
        });
    }

    private void initComponents() {

    // 1. Panel de información superior (Nombre del admin)
    JPanel panelHeader = new JPanel(new BorderLayout());
    panelHeader.setBackground(new Color(45, 52, 54)); // Un gris oscuro profesional

    JLabel lblWelcome = new JLabel(" Bienvenido, " + adminLogueado.getNombre()); // Mensaje de bienvenida al usuario admin ingresado
    lblWelcome.setForeground(Color.WHITE);
    panelHeader.add(lblWelcome, BorderLayout.WEST);

    JButton btnCerrarSesion = new JButton("Cerrar Sesión"); // Botón para cerrar sesión
    btnCerrarSesion.setBackground(new Color(231, 76, 60));
    btnCerrarSesion.setForeground(Color.WHITE);
    btnCerrarSesion.setFocusPainted(false);

    btnCerrarSesion.addActionListener(e -> {

       int confirmar = JOptionPane.showConfirmDialog(
        this,
        "Esta seguro de que desea cerrar la sesion actual?",
        "Confirmacion salida",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            authControl.cerrarSesion(adminLogueado.getId_usuario());       
            dispose();
            new FrmLogin().setVisible(true);
        }
    });

    // 2. El Contenedor de Pestañas
    JTabbedPane pestañas = new JTabbedPane();
    pestañas.setFont(new Font("SansSerif", Font.BOLD, 12));

    // Añadimos las pestañas llamando a métodos que crean cada panel
    pestañas.addTab("📦 Gestión de Paquetes", crearPanelPaquetes());
    pestañas.addTab("🚙 Gestión de Vehículos", crearPanelVehiculos());
    pestañas.addTab("👥 Gestión de Usuarios", crearPanelUsuarios());
    pestañas.addTab("📖 Gestion de registros de Logs", crearPanelRegistros());

    // 3. Agregar al Frame
    add(panelHeader, BorderLayout.NORTH);
    add(pestañas, BorderLayout.CENTER);
    add(btnCerrarSesion, BorderLayout.EAST);

    }

    private JPanel crearPanelPaquetes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Configuración de la Tabla
        String[] columnas = {"ID_Paquete", "Descripción","Remitente", "Destinatario", "Dirección de Entrega", "Peso", "Estado Paquete","Fecha de Creacion"};
        modeloPaquetes = new DefaultTableModel(columnas, 0) {
    
        @Override
        public boolean isCellEditable(int row, int column) { return false; } 
        };
        
        tblPaquetes = new JTable(modeloPaquetes);
        panel.add(new JScrollPane(tblPaquetes), BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnActualizarPaquetes = new JButton("Actualizar Estado Paquetes");
        JButton btnRegistrarPaquete = new JButton("Registrar Paquete");
        JButton btnAsignarPaquete = new JButton("Asignar Paquete");
        JButton btnEditar = new JButton("Editar Paquete");
        JButton btnEliminar = new JButton("Eliminar Paquete");

        // Se agregan al panel los botones
        panelBotones.add(btnActualizarPaquetes);
        panelBotones.add(btnRegistrarPaquete);
        panelBotones.add(btnAsignarPaquete);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        
        //--------------BotonesEventos----------------//
        
        // BotonActualizarPaquetes actualiza la tabla
        btnActualizarPaquetes.addActionListener(e -> refrescarTablaPaquetes());

        // BottonNuevoPaquete abre la ventana de nuevo paquete
        btnRegistrarPaquete.addActionListener(e -> {
            FrmNuevoPaquete ventanaNuevo = new FrmNuevoPaquete(this);
            ventanaNuevo.setVisible(true);
            
            if (ventanaNuevo.isExito()) {
                refrescarTablaPaquetes();
            }
        });

        // BotonAsignarPaquete asigna el paquete seleccionado
        /*btnAsignarPaquete.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }

            int idPaquete = (int) tblPaquetes.getValueAt(filaSeleccionada, 0);
            int idConductor = adminLogueado.getId_usuario();
            boolean asignado = adminControl.asignarPaquete(idPaquete, idConductor);

            if (asignado) {
                refrescarTablaPaquetes();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo asignar el paquete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        });*/

        // BotonEditar abre la ventana de editar paquete
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            // Si la seleccion es igual -1 significa que no se selecciono nada
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }

            // Obtiene el ID del paquete seleccionado
            int id =(int) tblPaquetes.getValueAt(filaSeleccionada, 0);

            // Usa el metodo auxiliar para buscar el paquete por ID
            Paquete paqueteSeleccionado = buscarPaquetePorId(id);

            if(paqueteSeleccionado == null){
                JOptionPane.showMessageDialog(this, "No se pudieron recuperar los datos completos del paquete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Abre la ventana de edición con los datos del paquete a editar
            FrmNuevoPaquete ventanaEditar = new FrmNuevoPaquete(this, paqueteSeleccionado);
            ventanaEditar.setVisible(true);

            if (ventanaEditar.isExito()) {
                refrescarTablaPaquetes();
            }
            
        });

        // BotonEliminar elimina el paquete seleccionado
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tblPaquetes.getSelectedRow();

            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
                return;
            }

            int idEliminar =(int) modeloPaquetes.getValueAt(filaSeleccionada, 0);
            String descripcion = (String) modeloPaquetes.getValueAt(filaSeleccionada, 1);

            int confirmado = JOptionPane.showConfirmDialog(
                this,
                "¿Está completamente seguro de que desea eliminar el paquete ID: " + idEliminar + " (" + descripcion + ")?\n"
                + "Esta accion no se puede deshacer y afectara el inventario en bodega.",
                "Confirmacion de eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if(confirmado != JOptionPane.YES_OPTION){
                return;
            }

            boolean eliminado = adminControl.eliminarPaquete(idEliminar, adminLogueado.getId_usuario());

            if(eliminado){
                JOptionPane.showMessageDialog(this, "Paquete eliminado y registrado en el historial de logs correctamente.");
                refrescarTablaPaquetes();
            }else{
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el paquete.", "Error", JOptionPane.ERROR_MESSAGE); 
                
            }
        });
        
        return panel;
    }

    private JPanel crearPanelVehiculos() {
        JPanel panel = new JPanel(new BorderLayout());

        // Modelo de tabla para vehículos
        String[] columnas = { "ID_Vehiculo","Placa","Marca","Modelo","ID_tipo_vehiculo","Estado" };
        modeloVehiculos = new DefaultTableModel(columnas, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {return false;}
        };

        JTable tblVehiculos = new JTable(modeloVehiculos);
        panel.add(new JScrollPane(tblVehiculos), BorderLayout.CENTER);

        //Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnActualizarVehiculos = new JButton("Actualizar Estado Vehiculos");
        JButton btnRegistrarVehiculo = new JButton("Registrar Vehiculo");
        JButton btnEditar = new JButton("Editar Vehiculo");
        JButton btnEliminar = new JButton("Eliminar Vehiculo");

        // Se agregan al panel los botones
        panelBotones.add(btnActualizarVehiculos);
        panelBotones.add(btnRegistrarVehiculo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);


        panel.add(panelBotones, BorderLayout.SOUTH);

        //--------------BotonesEventos----------------//

        btnActualizarVehiculos.addActionListener(e -> refrescarTablaVehiculos());

        btnRegistrarVehiculo.addActionListener(e -> {
            FrmRegistrarVehiculo ventanaNuevo = new FrmRegistrarVehiculo(this);
            ventanaNuevo.setVisible(true);
            
            if (ventanaNuevo.isExito()) {
                refrescarTablaVehiculos();
            }
        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tblVehiculos.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un vehiculo de la tabla.");
                return;
            }

            int id =(int) tblVehiculos.getValueAt(filaSeleccionada, 0);
            Vehiculo vehiculoSeleccionado = buscarVehiculoPorId(id);

            if(vehiculoSeleccionado == null){
                JOptionPane.showMessageDialog(this, "No se pudieron recuperar los datos completos del vehiculo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            FrmRegistrarVehiculo ventanaEditar = new FrmRegistrarVehiculo(this, vehiculoSeleccionado);
            ventanaEditar.setVisible(true);

            if (ventanaEditar.isExito()) {
                refrescarTablaVehiculos();
            }
        });

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tblVehiculos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showInputDialog(this,"Por favor, seleccione un paquete de la tabla.");
                return;
            }
            int id = (int) tblVehiculos.getValueAt(filaSeleccionada, 0);
            boolean eliminado = adminControl.eliminarVehiculo(id);

            if(eliminado){
                refrescarTablaPaquetes();
            }else{
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el vehiculo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;

    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Modelo de tabla para usuarios
        String[] columnas = {"ID", "Nombre", "Email", "Rol", "Estado"};
        modeloUsuarios = new DefaultTableModel(columnas, 0);
        JTable tblUsuarios = new JTable(modeloUsuarios);
        
        // Panel de botones lateral o superior
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevoUsuario = new JButton("Nuevo Usuario");
        JButton btnEliminar = new JButton("Desactivar");
        
        panelAcciones.add(btnNuevoUsuario);
        panelAcciones.add(btnEliminar);
        
        panel.add(panelAcciones, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelRegistros() {

        JPanel panel = new JPanel(new BorderLayout());

        //Modelo para la tabla de registros
        String[] columnas = {"ID", "Usuario", "Accion","Descripcion", "Fecha"};
        modeloRegistros = new DefaultTableModel(columnas, 0);
        JTable tblRegistros = new JTable(modeloRegistros);

        panel.add(new JScrollPane(tblRegistros), BorderLayout.CENTER);

        return panel;

    }

    //--------------MetodosAuxiliares----------------//

    public void refrescarTablaVehiculos() {
        this.listaVehiculos = adminControl.actualizarVehiculos();
        modeloVehiculos.setRowCount(0);

        if(listaVehiculos != null && !listaVehiculos.isEmpty()){
            for (Vehiculo v : listaVehiculos) {
                Object[] fila = {
                    v.getId_vehiculo(),
                    v.getPlaca(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getId_tipoVehiculo(),
                    v.getId_estado_vehiculo()
                };
                modeloVehiculos.addRow(fila);
            }
        } else {
            System.out.println("No se recibieron paquetes del servidor.");
        }
    }
    
    public void refrescarTablaPaquetes() {
        // 1. Usamos la instancia de AdminControlador para obtener la lista
        this.listaPaquetes= adminControl.actualizarPaquetes();

        // 2. Limpiamos el modelo actual de la tabla
        modeloPaquetes.setRowCount(0);

        // 3. Validamos y llenamos la tabla de acuerdo a la lista recibida
        if (listaPaquetes != null && !listaPaquetes.isEmpty()) {
            for (Paquete p : listaPaquetes) {
                Object[] fila = {
                   p.getId_paquete(),
                   p.getDescripcion(),
                   p.getRemitente(),
                   p.getDestinatario(),
                   p.getDireccion_entrega(),
                   p.getPeso(),
                   EstadoPaquete.obtenerTextoPorId(p.getId_estado()),
                   p.getFecha_creacion() 
                };
                modeloPaquetes.addRow(fila);
            }
        } else {
            System.out.println("No se recibieron paquetes del servidor.");
        }
    }

    public void refrescarTablaUsuarios() {

    }

    public Paquete buscarPaquetePorId(int id) {
        if(listaPaquetes != null) {
            for (Paquete p : listaPaquetes) {
                if (p.getId_paquete() == id) {
                    return p; // Lo encontro, retorna el objeto paquete
                }
            }
        }
        return null; // No lo encontro
    }

    public Usuarios buscarUsuarioPorId(int id) {
        if(listaUsuarios != null) {
            for (Usuarios u : listaUsuarios) {
                if (u.getId_usuario() == id) {
                    return u; // Lo encontro, retorna el objeto usuario
                }
            }
        }
        return null; // No lo encontro
    }

    public Vehiculo buscarVehiculoPorId(int id) {
        if(listaVehiculos != null) {
            for (Vehiculo v : listaVehiculos) {
                if (v.getId_vehiculo() == id) {
                    return v; // Lo encontro, retorna el objeto vehiculo
                }
            }
        }
        return null; // No lo encontro
    }

    //Metodo prueba ventana admin//
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        // Creamos un objeto Date válido para SQL
        java.sql.Date fechaNac = java.sql.Date.valueOf("1990-01-01");

        // Agregamos 'new' y los tipos de datos correctos
        Usuarios adminPrueba = new Usuarios(1, "Admin", "Sistema", fechaNac, "12345678", 
                                           "admin@mail.com", "88888888", "admin123", 1);
        
        new FrmDashboardAdmin(adminPrueba).setVisible(true);
    });
    }
}