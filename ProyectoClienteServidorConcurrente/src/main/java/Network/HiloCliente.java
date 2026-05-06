package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import Dominio.Excepciones.LogSistema;
import Dominio.Excepciones.MensajeRed;
import LoggerFile.LoggerManager;
import Dominio.Paquete;
import Dominio.UbicacionVehiculo;
import Dominio.Usuario;
import Network.DAO.LogDAO;
import Network.DAO.PaqueteDAO;
import Network.DAO.UsuarioDAO;
import Network.DAO.VehiculoDAO;


public class HiloCliente extends Thread{
    private Socket socketCliente;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;

    private int idUsuarioActual = 0;
    private Usuario usuarioValidado;


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
                        LoggerManager.log(idUsuarioActual, "CREAR_PAQUETE", "ID de usuario " + idUsuarioActual + " creó un paquete"); 
                    }
                    return new MensajeRed("RESPUESTA_CREAR", creado, creado, creado ? "OK" : "Error en DB");

                case "LISTAR_PAQUETES":
                    List<Paquete> lista = paqueteDAO.listarPaquetes();
                    return new MensajeRed("LISTA_PAQUETES_RESPUESTA", lista, true, "Lista obtenida");

                case "ASIGNAR_PAQUETE":
                    try {
                        String[] datos = peticion.getPayload().toString().split(":");
                        int idPkg = Integer.parseInt(datos[0]);
                        int idCond = Integer.parseInt(datos[1]);
                        
                        boolean exito = paqueteDAO.asignarPaquete(idPkg, idCond);
                        
                        if(exito) {
                            logDAO.registrarEvento(new LogSistema(0, idUsuarioActual, 
                                "Asignación: Pkg " + idPkg + " a Cond " + idCond, null, null));
                        }
                        
                        return new MensajeRed("ASIGNACION_RESPUESTA", exito, exito, 
                            exito ? "Paquete asignado" : "Error en base de datos");
                    } catch (Exception e) {
                        return new MensajeRed("ASIGNACION_RESPUESTA", false, false, "Error: " + e.getMessage());
                    } 

                case "ACTUALIZAR_ESTADO_PAQUETE":
                        String[] partes = peticion.getPayload().toString().split(":");
                        int idPkg = Integer.parseInt(partes[0]);
                        int estado = Integer.parseInt(partes[1]);
                        
                        boolean exito = true;
                        
                        if(exito) {
                            logDAO.registrarEvento(new LogSistema(0, idUsuarioActual, "CAMBIO_ESTADO: Paquete " + idPkg + " a estado " + estado, null, null));
                        }
                        return new MensajeRed("ESTADO_RES", exito, exito, exito ? "Estado actualizado" : "Error");

                case "LISTAR_PAQUETES_CONDUCTOR":
                        int idCond = (int) peticion.getPayload();
                        List<Paquete> misPaquetes = paqueteDAO.listarPaquetesPorConductor(idCond);
                        return new MensajeRed("LISTA_CONDUCTOR_RES", misPaquetes, true, "Paquetes obtenidos");


                        // ----- Módulo Vehículos ------ //

                        
                case "ACTUALIZAR_GPS":
                        String[] puntos = peticion.getPayload().toString().split(":");
                        int idVeh = Integer.parseInt(puntos[0]);
                        double lat = Double.parseDouble(puntos[1]);
                        double lng = Double.parseDouble(puntos[2]);

                        vehiculoDAO.registrarUbicacion(idVeh, lat, lng);
                        return new MensajeRed("GPS_OK", null, true, "Coordenadas recibidas");
                case "RASTREAR_PAQUETE":
                        int idPkgRastreo = (int) peticion.getPayload();
                        Paquete pRastreo = paqueteDAO.buscarPaquetePorId(idPkgRastreo); 
                        return new MensajeRed("RASTREO_RES", pRastreo, pRastreo != null, pRastreo != null ? "OK" : "No encontrado");

                case "ULTIMA_UBICACION":
                    int idVehUbi = (int) peticion.getPayload();
                    UbicacionVehiculo ubi = null; 
                    return new MensajeRed("UBICACION_RES", ubi, ubi != null, ubi != null ? "OK" : "Sin datos GPS");
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
                if(socketCliente != null) socketCliente.close();
            }catch (IOException e) {
                System.err.println("Error al cerrar conexiones: " + e.getMessage());
            }
        }
}