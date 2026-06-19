package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Dominio.Excepciones.MensajeRed;
import Dominio.Paquete;
import Dominio.Usuarios;
import Dominio.Vehiculo;
import LoggerFile.LoggerManager;
import Network.DAO.LogDAO;
import Network.DAO.PaqueteDAO;
import Network.DAO.UsuarioDAO;
import Network.DAO.VehiculoDAO;

public class HiloCliente extends Thread {

    private final Socket socketCliente;
    private int idUsuarioActual = 0;
    private Usuarios usuarioValidado;

    public HiloCliente(Socket socket) {
        this.socketCliente = socket;
    }

    @Override
    public void run() { 
        // Usamos try-with-resources aquí también para asegurar el cierre hermético
        try (
            ObjectOutputStream salida = new ObjectOutputStream(socketCliente.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socketCliente.getInputStream());
        ){
            // Aseguramos la cabecera del stream de salida
            salida.flush();

            // Leemos LA petición (Una sola por conexión, sin while infinito)
            Object objectRecibido = entrada.readObject();

            if(objectRecibido instanceof MensajeRed peticion){
                MensajeRed respuesta = procesarPeticion(peticion); // switch gigante
                salida.writeObject(respuesta);
                salida.flush(); // Empuja los datos por la red
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Conexión finalizada o interrumpida con cliente." + e.getMessage());
        } finally {
            // El socket base si lo cerrramos a mano por si acaso
            try{ if(socketCliente != null && !socketCliente.isClosed()) socketCliente.close(); 

            } catch (IOException e) { /* log */}
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private MensajeRed procesarPeticion(MensajeRed peticion) {
        String accion = peticion.getAccion().trim();

        // Determinamos quien hace la petición a la accion actual
        int idUsuarioOperacion = (peticion.getIdUsuarioSender() > 0 ) ? peticion.getIdUsuarioSender() : this.idUsuarioActual;
        

        try {
            return switch (accion) {

                // ----- Módulo Usuario ------ //
                case "LOGIN" -> {
                    Usuarios credenciales = (Usuarios) peticion.getPayload();
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    usuarioValidado = usuarioDAO.validarLogin(credenciales.getEmail(), credenciales.getPassword());
                    
                    if (usuarioValidado != null) {
                        this.idUsuarioActual = usuarioValidado.getId_usuario();
                        // Usamos el ID recién validado
                        LoggerManager.log(this.idUsuarioActual, "LOGIN", "Inicio de sesión exitoso");
                        yield new MensajeRed("LOGIN_RESPUESTA", usuarioValidado, true, "Login exitoso");
                    } else {
                        LoggerManager.log(0, "LOGIN_FALLIDO", "Intento fallido: " + credenciales.getEmail());
                        yield new MensajeRed("LOGIN_RESPUESTA", null, false, "Credenciales incorrectas");
                    }
                }

                case "LOGOUT" -> {
                    LoggerManager.log(idUsuarioOperacion, "LOGOUT", "El usuario cerró la sesión de forma voluntaria.");
                    yield new MensajeRed("LOGOUT_RESPUESTA", true, true, "Seccion Finalizada");
                }

                case "REGISTRAR_USUARIO" -> {
                    Usuarios u = (Usuarios) peticion.getPayload();
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    boolean guardado = usuarioDAO.registrarUsuario(u);
                    
                    if(guardado) {
                        LoggerManager.log(idUsuarioOperacion, "REGISTRO", "Registro exitoso de usuario: " + u.getEmail());
                        yield new MensajeRed("REGISTRO_RESPUESTA", guardado, guardado, "OK");
                    } else {
                        LoggerManager.log(idUsuarioOperacion, "REGISTRO_FALLIDO", "Intento fallido: " + u.getEmail());
                        yield new MensajeRed("REGISTRO_RESPUESTA", null, false, "Error al guardar");
                    }
                }

                case "ALTERAR_ESTADO_USUARIO" -> {
                    int idUsuarioInactivado = (int) peticion.getPayload();
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    boolean cambioEstado = usuarioDAO.alternarEstadoUsuario(idUsuarioInactivado); // Llamamos al metodo del Dao y lo guardamos en una variable booleana
                   
                    if (cambioEstado) { // Si es afirmativo entonces se inactivó el usuario y se registra el log, sino se registra un intento fallido
                        LoggerManager.log(idUsuarioOperacion, "ALTERAR_ESTADO_USUARIO", "ALTERAR_ESTADO_USUARIO exitosa del usuario ID: " + idUsuarioInactivado);
                        yield new MensajeRed("ALTERAR_ESTADO_USUARIO", true, true,  "Usuario a cambio a estado");
                    } else {
                        LoggerManager.log(idUsuarioOperacion, "ALTERAR_ESTADO_USUARIO", "Intento fallido sobre ID: " + idUsuarioInactivado);
                        yield new MensajeRed("ALTERAR_ESTADO_USUARIO", null, false, "Usuario no encontrado");
                    }
                }

                case "EDITAR_USUARIO" -> { 
                    Usuarios uEditar = (Usuarios) peticion.getPayload();
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    boolean editado = usuarioDAO.editarUsuario(uEditar);

                    if(editado) {
                        LoggerManager.log(idUsuarioOperacion, "EDITAR_USUARIO", "Usuario " + uEditar.getId_usuario() + " editado un usuario");
                    }
                    yield new MensajeRed("EDITAR_USUARIO", editado, editado, editado ? "Usuario actualizado" : "Error al actualizar");
                }

                case "LISTAR_USUARIOS" -> {
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    yield new MensajeRed("LISTA_USUARIOS_RESPUESTA", usuarioDAO.listarUsuarios(), true, "Lista obtenida");
                }

                // ----- Módulo Paquetes ------ //
                case "CREAR_PAQUETE" -> {
                    Paquete pNuevo = (Paquete) peticion.getPayload();
                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    boolean creado = paqueteDAO.crearPaquete(pNuevo);             
                    
                    if(creado) {
                        
                        LoggerManager.log(idUsuarioOperacion, "CREAR_PAQUETE", "Usuario " + idUsuarioOperacion + " creó un paquete: " + pNuevo.getDescripcion());
                        yield new MensajeRed("RESPUESTA_CREAR", pNuevo, true, "Paquete creado exitosamente");
                    } else {
                        yield new MensajeRed("RESPUESTA_CREAR", null, false, "Error al insertar en la base de datos");
                    }
                }
                
                case "LISTAR_PAQUETES" -> {
                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    yield new MensajeRed("LISTA_PAQUETES_RESPUESTA", paqueteDAO.listarPaquetes(), true, "Lista obtenida");
                }

                case "EDITAR_PAQUETE" -> {
                    Paquete pEditar = (Paquete) peticion.getPayload();
                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    boolean editado = paqueteDAO.EditarPaquete(pEditar);

                    if(editado) {
                        LoggerManager.log(idUsuarioOperacion, "EDITAR_PAQUETE", "Paquete " + pEditar.getId_paquete() + " editado");
                    }
                    yield new MensajeRed("EDITAR_RESPUESTA", editado, editado, editado ? "Paquete actualizado" : "Error al actualizar");
                }
                    
                case "ELIMINAR_PAQUETE" -> {
                    int idEliminar = (int) peticion.getPayload();
                    PaqueteDAO paqueteDAO = new PaqueteDAO();
                    boolean eliminado = paqueteDAO.eliminarPaquete(idEliminar);

                    if(eliminado) {
                        
                        LoggerManager.log(idUsuarioOperacion, "ELIMINAR_PAQUETE", "Paquete " + idEliminar + " eliminado");
                    }
                    yield new MensajeRed("ELIMINAR_RESPUESTA", eliminado, eliminado, eliminado ? "Paquete eliminado" : "Error al eliminar");
                }

                // ----- Módulo Vehículos ------ //
                case "LISTAR_VEHICULOS" -> { 
                    VehiculoDAO vehiculoDAO = new VehiculoDAO();
                    yield new MensajeRed("LISTAR_VEHICULOS_RESPUESTA", vehiculoDAO.listarVehiculos(), true, "Lista obtenida");
                }
                
                case "REGISTRAR_VEHICULO" -> {
                    Vehiculo v = (Vehiculo) peticion.getPayload();
                    VehiculoDAO vehiculoDAO = new VehiculoDAO();
                    boolean registrado = vehiculoDAO.registrarVehiculo(v);

                    if(registrado) {
                        LoggerManager.log(idUsuarioOperacion, "REGISTRAR_VEHICULO", "Usuario " + idUsuarioOperacion + " creó un vehiculo: " + v.getPlaca());
                        yield new MensajeRed("REGISTRO_RESPUESTA", registrado, registrado, "OK");
                    } else {
                        yield new MensajeRed("REGISTRO_RESPUESTA", null, false, "Error al insertar en la base de datos el vehiculo");
                    }
                }

                case "EDITAR_VEHICULO" -> {
                    Vehiculo vEditar = (Vehiculo) peticion.getPayload();
                    VehiculoDAO vehiculoDAO = new VehiculoDAO();
                    boolean editadoVehiculo = vehiculoDAO.editarVehiculo(vEditar);

                    if(editadoVehiculo) {
                        LoggerManager.log(idUsuarioOperacion, "EDITAR_VEHICULO", "Usuario " + idUsuarioOperacion + " editó un vehiculo: " + vEditar.getPlaca());
                    }
                    yield new MensajeRed("EDITAR_RESPUESTA", editadoVehiculo, editadoVehiculo, editadoVehiculo ? "OK" : "Error al editar el vehiculo");
                }
                
                case "ELIMINAR_VEHICULO" -> {
                    int idEliminarVehiculo = (int) peticion.getPayload();
                    VehiculoDAO vehiculoDAO = new VehiculoDAO();
                    boolean eliminadoVehiculo = vehiculoDAO.eliminarVehiculo(idEliminarVehiculo);

                    if(eliminadoVehiculo) {
                        LoggerManager.log(idUsuarioOperacion, "ELIMINAR_VEHICULO", "Usuario " + idUsuarioOperacion + " eliminó un vehiculo: " + idEliminarVehiculo);
                    }
                    yield new MensajeRed("ELIMINAR_RESPUESTA", eliminadoVehiculo, eliminadoVehiculo, eliminadoVehiculo ? "OK" : "Error al eliminar el vehiculo");
                }

                // ----- Módulo Asignaciones ------ //
                case "ASIGNAR_PAQUETE" -> {
                    try {
                        PaqueteDAO paqueteDAO = new PaqueteDAO();
                        String[] datos = peticion.getPayload().toString().split(":");
                        int idPkg = Integer.parseInt(datos[0]);
                        int idCond = Integer.parseInt(datos[1]);
                        
                        boolean exito = paqueteDAO.crearPaquete(null); 
                        if(exito) {
                            LoggerManager.log(idUsuarioOperacion, "ASIGNACION", "Asignación: Pkg " + idPkg + " a Cond " + idCond);
                        }
                        yield new MensajeRed("ASIGNACION_RESPUESTA", exito, exito, exito ? "Paquete asignado" : "Error en base de datos");
                    } catch (Exception e) {
                        yield new MensajeRed("ASIGNACION_RESPUESTA", false, false, "Error: " + e.getMessage());
                    }
                }
                
                // ----- Módulo logs ------ //

                case "LISTAR_LOGS" -> {
                    LogDAO logDAO = new LogDAO();
                    yield new MensajeRed("LISTA_LOGS_RESPUESTA", logDAO.listarEventosSistema(), true, "Lista obtenida");
                }


                default -> new MensajeRed("DESCONOCIDO", null, false, "La acción no existe");
            }; 
            
        } catch (Exception e) {
            System.err.println("Error en procesarPeticion: " + e.getMessage());
            // Ajustado al nuevo constructor de 5 parámetros de MensajeRed pasándole 0 por defecto en caso de catástrofe
            return new MensajeRed("ERROR_SERVIDOR", null, false, e.getMessage(), 0);
        }
    }

}
