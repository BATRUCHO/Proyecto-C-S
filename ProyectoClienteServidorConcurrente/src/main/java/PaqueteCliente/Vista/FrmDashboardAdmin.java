package PaqueteCliente.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
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
import Dominio.EstadoVehiculo;
import Dominio.Excepciones.LogSistema;
import Dominio.Paquete;
import Dominio.Roles;
import Dominio.TipoVehiculo;
import Dominio.Usuarios;
import Dominio.Vehiculo;
import PaqueteCliente.Controlador.AdminControlador;
import PaqueteCliente.Controlador.AutenticacionControlador;
import PaqueteCliente.Utilidades.CSVExporter;


public class FrmDashboardAdmin extends JFrame {

    private Usuarios adminLogueado;
    private JTable tblPaquetes;

    private final AdminControlador adminControl = new AdminControlador(); // Instancia del controlador
    private AutenticacionControlador authControl = new AutenticacionControlador(); // Instancia del controlador

    private  List<Paquete> listaPaquetes;
    private  List<Vehiculo> listaVehiculos;
    private  List<Usuarios> listaUsuarios;
    private  List<LogSistema> listaLogs;


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
        String[] columnas = { "ID_Vehiculo","Placa","Marca","Modelo","Tipo Vehiculo","Estado del vehiculo" };
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

            int idEliminar = (int) tblVehiculos.getValueAt(filaSeleccionada, 0);
            String descripcion = (String) tblVehiculos.getValueAt(filaSeleccionada, 1);

            int confirmado = JOptionPane.showConfirmDialog(
                 this,
                "¿Está completamente seguro de que desea eliminar el vehiculo ID: " + idEliminar + " (" + descripcion + ")?\n"
                + "Esta accion no se puede deshacer y afectara el transporte del paquete.",
                "Confirmacion de eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if(confirmado != JOptionPane.YES_OPTION){
                return; //Abortamos la ejecucion
            }

            boolean eliminado = adminControl.eliminarVehiculo(idEliminar, adminLogueado.getId_usuario());

            if(eliminado){
                JOptionPane.showMessageDialog(
                    this,
                    "Vehiculo eliminado y registrado en el historial de logs correctamente.",
                    "Eliminacion Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                refrescarTablaVehiculos();
            }else{
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el vehiculo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;

    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Modelo de tabla para usuarios
        String[] columnas = {"ID", "Nombre","Apellido", "Fecha Nacimiento", "DNI", "Email", "Telefono", "Password", "Rol", "Fecha de creacion","Estado del usuario"};
        modeloUsuarios = new DefaultTableModel(columnas, 0){

            @Override
            public boolean isCellEditable(int row, int column) { return false; } 
        };

        JTable tblUsuarios = new JTable(modeloUsuarios);
        panel.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);

        
        // Panel de botones lateral o superior
        JPanel panelBotones = new JPanel();

        JButton btnActualizarUsuarios = new JButton("Actualizar Estado Usuarios");
        JButton btnNuevoUsuario = new JButton("Nuevo Usuario");
        JButton btnAlterarEstadoUsuario = new JButton("EstadoUsuario");
        JButton btnEditar = new JButton("Editar");
        
        panelBotones.add(btnActualizarUsuarios);
        panelBotones.add(btnNuevoUsuario);
        panelBotones.add(btnAlterarEstadoUsuario);
        panelBotones.add(btnEditar);
        
        panel.add(panelBotones, BorderLayout.SOUTH);

        //--------------BotonesEventos----------------//

        btnActualizarUsuarios.addActionListener(e -> refrescarTablaUsuarios());

        btnNuevoUsuario.addActionListener(e -> {
            FrmNuevoUsuario ventanaNueva = new FrmNuevoUsuario(this);
            ventanaNueva.setVisible(true);
            
            if (ventanaNueva.isExito()) {
                refrescarTablaUsuarios();
            }
        });

        btnAlterarEstadoUsuario.addActionListener(e -> {
            int filaSeleccionada = tblUsuarios.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario de la tabla.");
                return;
            }
            int id = (int) tblUsuarios.getValueAt(filaSeleccionada, 0);

            int confirmado = JOptionPane.showConfirmDialog(
                this,
                "¿Está completamente seguro de que desea alterar el estado del usuario ID: " + id + "?",
                "Confirmacion de alterar estado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if(confirmado != JOptionPane.YES_OPTION){
                return;
            }

            boolean exito = adminControl.alterarEstadoUsuario(id);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Estado del usuario actualizado.");
                refrescarTablaUsuarios();
            }else{
                JOptionPane.showMessageDialog(this, "No se pudo alterar el estado del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tblUsuarios.getSelectedRow();

            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla.");
            }
            int id =(int) tblUsuarios.getValueAt(filaSeleccionada, 0);
            Usuarios usuarioSeleccionado = buscarUsuarioPorId(id);

            if(usuarioSeleccionado == null){
                 JOptionPane.showMessageDialog(this, "No se pudieron recuperar los datos completos del Usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            FrmNuevoUsuario ventanaEditar = new FrmNuevoUsuario(this, usuarioSeleccionado);
            ventanaEditar.setVisible(true);

            if (ventanaEditar.isExito()) {
                refrescarTablaUsuarios();
            }

        });
        
        return panel;
    }
  
    private JPanel crearPanelRegistros() {

        JPanel panel = new JPanel(new BorderLayout());

        //Modelo para la tabla de registros
        String[] columnas = {"ID", "Usuario","Nombre Completo", "Rol", "Accion","Descripcion", "Fecha"};
        modeloRegistros = new DefaultTableModel(columnas, 0);
        JTable tblRegistros = new JTable(modeloRegistros);

        panel.add(new JScrollPane(tblRegistros), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        JButton btnActualizarLogs = new JButton("Actualizar Logs");
        JButton btnExportarCSV = new JButton("Descargar Archivo");

        panelBotones.add(btnActualizarLogs);
        panelBotones.add(btnExportarCSV);


        panel.add(panelBotones, BorderLayout.SOUTH);

        //--------------BotonesEventos----------------//

        btnActualizarLogs.addActionListener(e -> { refrescarTablaLogs();});

        btnExportarCSV.addActionListener(e -> {

            //1. Validamos que tengamos datos en la lista, por lo que el sistema tenga datos para descargar de la lista Logs
            if (listaLogs == null || listaLogs.isEmpty()) {
                this.listaLogs = adminControl.listarEventosSistema();
            }
                // 2. Definimos las opciones que verá el usuario
                Object[] opciones = { "Datos_Logs.csv", "Datos_Paquetes.csv", "Datos_Vehiculos.csv", "Datos_Usuarios.csv"};

                // 3. Desplegamos el menú de selección estructurado
                Object seleccion = JOptionPane.showInputDialog(
                    this,                                                   // 1. Component (Componente padre)
                    "Seleccione el reporte que desea exportar a Excel:",    // 2. Object (Mensaje interno)
                    "Extractor de Reportes Corporativos",                   // 3. String (Título de la ventana)
                    JOptionPane.QUESTION_MESSAGE,                           // 4. int (Tipo de mensaje/ícono)
                    null,                                                   // 5. Icon (Icono personalizado, null usa defecto)
                    opciones,                                               // 6. Object[] (El arreglo de opciones para el ComboBox)
                    opciones[0]                                             // 7. Object (La opción seleccionada por defecto)

                );
                
                // 4. Control defensivo por si el usuario presiona "Cancelar" o cierra la ventana
                if (seleccion == null){
                    return;
                }

                //5. Control defencibo por si el usuario presiona "Cancelar" o cierra la ventana
                String opcionElegida = seleccion.toString();

                switch (opcionElegida) {
                    case "Datos_Logs.csv" -> {
                        CSVExporter.exportarLogs(this, this.listaLogs);

                    }
                    case "Datos_Paquetes.csv" -> {
                        JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                    case "Datos_Vehiculos.csv" -> {
                        JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                    case "Datos_Usuarios.csv" -> {
                        JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                    default -> {
                        JOptionPane.showMessageDialog(this, "Opción no válida seleccionada." +opcionElegida, "Error", JOptionPane.ERROR_MESSAGE);  
                    }
                }
            
            
            });

        return panel;

    }

    //--------------MetodosAuxiliares----------------//

    public void refrescarTablaLogs(){
        this.listaLogs = adminControl.listarEventosSistema();
        modeloRegistros.setRowCount(0);

        if(listaLogs != null && !listaLogs.isEmpty()){
            for (LogSistema l : listaLogs) {
                Object[] fila = {
                    l.getId_log(),
                    l.getId_usuario(),
                    l.getNombre_completo(),
                    l.getRol_usuario(),
                    l.getAccion(),
                    l.getDetalles(),
                    l.getFecha_hora()
                };
                modeloRegistros.addRow(fila);
            }
        } else {
            System.out.println("No se recibieron logs del servidor.");
        }
    }

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
                    TipoVehiculo.obtenerTextoPorId(v.getId_tipoVehiculo()),
                    EstadoVehiculo.obtenerTextoPorId(v.getId_estado_vehiculo())
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
        this.listaUsuarios = adminControl.actualizarUsuarios();
        modeloUsuarios.setRowCount(0);

        if(listaUsuarios != null && !listaUsuarios.isEmpty()){
            for (Usuarios u : listaUsuarios) {
                Object[] fila = {
                    u.getId_usuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getFechaNacimiento(),
                    u.getDni(),
                    u.getEmail(),
                    u.getTelefono(),
                    u.getPassword(),
                    Roles.obtenerTextoPorId(u.getIdRol()),
                    u.getFechaCreacion(),
                    u.isActivo() ? "Activo" : "Inactivo",
                };
                modeloUsuarios.addRow(fila);
            }
        } else {
            System.out.println("No se recibieron usuarios del servidor.");
        }
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