package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import Dominio.Excepciones.MensajeRed;
import Dominio.Paquete;
import Dominio.Usuarios;
import Dominio.Vehiculo;

import LoggerFile.LoggerManager;

import Network.DAO.LogDAO;
import Network.DAO.PaqueteDAO;
import Network.DAO.UsuarioDAO;
import Network.DAO.VehiculoDAO;


public class HiloCliente extends Thread{
    private final Socket socketCliente;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;

    private int idUsuarioActual = 0;
    private Usuarios usuarioValidado;


    public HiloCliente(Socket socket) {
        this.socketCliente = socket;
    }

    @Override
    public void run() { 
        try { 

            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            salida.flush();
            entrada = new ObjectInputStream(socketCliente.getInputStream());

            while (!socketCliente.isClosed()) {
                try {
                    Object objectRecibido = entrada.readObject();
                    
                    if (objectRecibido instanceof MensajeRed) {
                        MensajeRed peticion = (MensajeRed) objectRecibido;
                        MensajeRed respuesta = procesarPeticion(peticion);
                        salida.writeObject(respuesta);
                        salida.flush();
                    }
                } catch (java.io.EOFException e) {
                    break; 
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Cliente desconectado o error: " + e.getMessage());  
        } finally {
            cerrarConexiones();
        }
    }

    private MensajeRed procesarPeticion(MensajeRed peticion) {
        String accion = peticion.getAccion().trim();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        PaqueteDAO paqueteDAO = new PaqueteDAO();
        VehiculoDAO vehiculoDAO = new VehiculoDAO();
        LogDAO logDAO = new LogDAO(); 

        try {
            switch (accion) {

                // ----- Módulo Usuario ------ //

                case "LOGIN":
                        Usuario credenciales = (Usuario) peticion.getPayload();
                        usuarioValidado = usuarioDAO.validarLogin(credenciales.getEmail(), credenciales.getPassword());
                        
                        if (usuarioValidado != null) {
                            this.idUsuarioActual = usuarioValidado.getIdUsuario(); 
                            LoggerManager.log(idUsuarioActual, "LOGIN", "Inicio de sesión exitoso"); 
                            return new MensajeRed("LOGIN_RESPUESTA", usuarioValidado, true, "Login exitoso");
                        } else {
                            LoggerManager.log(0, "LOGIN_FALLIDO", "Intento fallido: " + credenciales.getEmail());
                            return new MensajeRed("LOGIN_RESPUESTA", null, false, "Credenciales incorrectas");
                        }

                case "LOGOUT":
                        LoggerManager.log(idUsuarioActual, "LOGOUT", "El usuario cerró la sesión de forma voluntaria.");
                        return new MensajeRed("LOGOUT_RESPUESTA", true, true, "Seccion Finalizada");

                case "REGISTRAR_USUARIO":
                        Usuario u = (Usuario) peticion.getPayload();
                        boolean guardado = usuarioDAO.registrarUsuario(u);
                        
                        if(guardado) {
                            this.idUsuarioActual = u.getIdUsuario();
                            LoggerManager.log(idUsuarioActual, "REGISTRO", "Registro exitoso");
                            return new MensajeRed("REGISTRO_RESPUESTA", guardado, guardado, guardado ? "OK" : "Error al guardar");
                        }else{
                            LoggerManager.log(0, "REGISTRO_FALLIDO", "Intento fallido: " + u.getEmail());
                            return new MensajeRed("REGISTRO_RESPUESTA", null, false, "Error al guardar");
                        }

                case "ELIMINAR_USUARIO":
                        int idUsuarioEliminar = (int) peticion.getPayload();
                        Usuario usuarioEliminado = usuarioDAO.eliminarUsuario(idUsuarioEliminar);
                        
                        if (usuarioEliminado != null) {
                            LoggerManager.log(idUsuarioActual, "ELIMINAR_USUARIO", "Eliminación exitosa: " + usuarioEliminado.getEmail());
                            return new MensajeRed("ELIMINAR_RESPUESTA", usuarioEliminado, true, "Usuario eliminado");
                        } else {
                            LoggerManager.log(0, "ELIMINAR_FALLIDO", "Intento fallido: " + idUsuarioEliminar);
                            return new MensajeRed("ELIMINAR_RESPUESTA", null, false, "Usuario no encontrado");
                        }

                case "LISTAR_USUARIOS":
                        List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
                        return new MensajeRed("LISTA_USUARIOS_RESPUESTA", listaUsuarios, true, "Lista obtenida");

                // ----- Módulo Paquetes ------ //

                case "CREAR_PAQUETE":
                    Paquete pNuevo = (Paquete) peticion.getPayload();
                    boolean creado = paqueteDAO.crearPaquete(pNuevo);
                    
                    if(creado) {
                        LoggerManager.log(idUsuarioActual, "CREAR_PAQUETE", 
                            "Usuario " + idUsuarioActual + " creó un paquete" + pNuevo.getDescripcion()); 

                        return new MensajeRed("RESPUESTA_CREAR",pNuevo,true,"Paquete creado exitosamente");
                    } else {
                    return new MensajeRed("RESPUESTA_CREAR",null,false,"Error al insertar en la base de datos");
                    }

                case "LISTAR_PAQUETES":
                    List<Paquete> lista = paqueteDAO.listarPaquetes();
                    return new MensajeRed("LISTA_PAQUETES_RESPUESTA", lista, true, "Lista obtenida");



                case "EDITAR_PAQUETE":
                    Paquete pEditar = (Paquete) peticion.getPayload();
                    boolean editado = paqueteDAO.EditarPaquete(pEditar);
                    if(editado) {
                        logDAO.registrarEvento(idUsuarioActual, "EDITAR_PAQUETE", "Paquete " + pEditar.getId_paquete() + " editado");
                    }
                    return new MensajeRed("EDITAR_RESPUESTA", editado, editado, editado ? "Paquete actualizado" : "Error al actualizar");
                    
                case "ELIMINAR_PAQUETE":
                    int idEliminar = (int) peticion.getPayload();
                    boolean eliminado = paqueteDAO.eliminarPaquete(idEliminar);
                    if(eliminado) {
                        logDAO.registrarEvento(idUsuarioActual, "ELIMINAR_PAQUETE", "Paquete " + idEliminar + " eliminado");
                    }
                    return new MensajeRed("ELIMINAR_RESPUESTA", eliminado, eliminado, eliminado ? "Paquete eliminado" : "Error al eliminar");          

                        // ----- Módulo Vehículos ------ //
                        
                case "LISTAR_VEHICULOS":
                    List<Vehiculo> listaVehiculos = vehiculoDAO.listarVehiculosActivos();
                    return new MensajeRed("LISTAR_VEHICULOS_RESPUESTA", listaVehiculos, true, "Lista obtenida");
                
                case "REGISTRAR_VEHICULO":
                    Vehiculo v = (Vehiculo) peticion.getPayload();
                    boolean registrado = vehiculoDAO.registrarVehiculo(v);

                    if(registrado) {
                        LoggerManager.log(idUsuarioActual, "REGISTRAR_VEHICULO"
                        , "Usuario " + idUsuarioActual + " creó un vehiculo" + v.getPlaca());
                        return new MensajeRed("REGISTRO_RESPUESTA", registrado, registrado, registrado ? "OK" : "Error al guardar");
                    } else {
                        return new MensajeRed("REGISTRO_RESPUESTA", null, false, "Error al insertar en la base de datos el vehiculo");
                    }

                case "EDITAR_VEHICULO":
                    Vehiculo vEditar = (Vehiculo) peticion.getPayload();
                    boolean editadoVehiculo = vehiculoDAO.editarVehiculo(vEditar);

                    if(editadoVehiculo) {
                        LoggerManager.log(idUsuarioActual, "EDITAR_VEHICULO"
                        , "Usuario " + idUsuarioActual + " editó un vehiculo" + vEditar.getPlaca());
                    }
                    return new MensajeRed("EDITAR_RESPUESTA", editadoVehiculo, editadoVehiculo, editadoVehiculo ? "OK" : "Error al editar el vehiculo");
                
                case "ELIMINAR_VEHICULO":
                    int idEliminarVehiculo = (int) peticion.getPayload();
                    boolean eliminadoVehiculo = vehiculoDAO.eliminarVehiculo(idEliminarVehiculo);

                    if(eliminadoVehiculo) {
                        LoggerManager.log(idUsuarioActual, "ELIMINAR_VEHICULO"
                        , "Usuario " + idUsuarioActual + " eliminó un vehiculo" + idEliminarVehiculo);
                    }
                    return new MensajeRed("ELIMINAR_RESPUESTA", eliminadoVehiculo, eliminadoVehiculo, eliminadoVehiculo ? "OK" : "Error al eliminar el vehiculo");
                    

                    // ----- Módulo Asignaciones ------ //

                    /*  
                case "ASIGNAR_PAQUETE":
                    try {
                        String[] datos = peticion.getPayload().toString().split(":");
                        int idPkg = Integer.parseInt(datos[0]);
                        int idCond = Integer.parseInt(datos[1]);
                        
                        boolean exito = paqueteDAO.asignarPaquete(idPkg, idCond);
                        
                        if(exito) {
                            logDAO.registrarEvento(idUsuarioActual, "ASIGNACION", "Asignación: Pkg " + idPkg + " a Cond " + idCond);
                        }
                        
                        return new MensajeRed("ASIGNACION_RESPUESTA", exito, exito, 
                            exito ? "Paquete asignado" : "Error en base de datos");
                    } catch (Exception e) {
                        return new MensajeRed("ASIGNACION_RESPUESTA", false, false, "Error: " + e.getMessage());
                    }

                    */

                default:
                    return new MensajeRed("DESCONOCIDO", null, false, "La acción no existe");
            }
        } catch (Exception e) {
            System.err.println("Error en procesarPeticion: " + e.getMessage());
            return new MensajeRed("ERROR_SERVIDOR", null, false, e.getMessage());
        }
    }

        private void cerrarConexiones() {
            try {
                //Cierre seguro de liberacion de recursos
                if(entrada != null) entrada.close();
                if(salida != null) salida.close();
                if(socketCliente != null && !socketCliente.isClosed()) socketCliente.close();
                
            }catch (IOException e) {
                System.err.println("Error al liberar sockets: " + e.getMessage());
            }
        }
}