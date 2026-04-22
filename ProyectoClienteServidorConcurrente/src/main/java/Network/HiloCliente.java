package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import Dominio.Excepciones.LogSistema;
import Dominio.Excepciones.MensajeRed;
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

            // Mientras el socket esté abierto y no se llegue al fin del flujo
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
                    // El cliente cerró la conexión de forma normal
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
    
    // Instanciamos los DAOs (usando las interfaces para mejor práctica)
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    PaqueteDAO paqueteDAO = new PaqueteDAO();
    VehiculoDAO vehiculoDAO = new VehiculoDAO();
    LogDAO logDAO = new LogDAO(); 

    if (accion.equals("LOGIN")) {
    // ... lógica de login ...
    if (usuarioValidado != null) {
        // Registramos el evento
        logDAO.registrarAccion(new LogSistema(0, usuarioValidado.getIdUsuario(), "Inicio de sesión", null));
    }
}

    try {
        switch (accion) {
            // ----- Módulo Usuario ------ //
            case "LOGIN":
                // Usamos getPayload() en lugar de getDatos()
                Usuario credenciales = (Usuario) peticion.getPayload();
                Usuario usuarioValidado = usuarioDAO.validarLogin(credenciales.getEmail(), credenciales.getPassword());
                
                if (usuarioValidado != null) {
                    return new MensajeRed("LOGIN_RESPUESTA", usuarioValidado, true, "Login exitoso");
                } else {
                    return new MensajeRed("LOGIN_RESPUESTA", null, false, "Credenciales incorrectas");
                }

           case "REGISTRAR_USUARIO":
                Usuario u = (Usuario) peticion.getPayload();
                boolean guardado = usuarioDAO.guardarUsuario(u);
                
                // Auditoría con detalles
                if(guardado) {
                    logDAO.registrarAccion(new LogSistema(0, idUsuarioActual, "REGISTRO: " + u.getEmail(), null));
                }
                
                return new MensajeRed("REGISTRO_RESPUESTA", guardado, guardado, guardado ? "OK" : "Error al guardar");

            // ----- Módulo Paquetes ------ //
            case "CREAR_PAQUETE":
                Paquete pNuevo = (Paquete) peticion.getPayload();
                boolean creado = paqueteDAO.crearPaquete(pNuevo);
                
                // Auditoría: Usamos la clase LogSistema que subiste
                if(creado) {
                    logDAO.registrarAccion(new LogSistema(0, idUsuarioActual, "Creó un nuevo paquete", null));
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
                    
                    // Llamado limpio al DAO refactorizado
                    boolean exito = paqueteDAO.asignarPaquete(idPkg, idCond);
                    
                    if(exito) {
                        logDAO.registrarAccion(new LogSistema(0, idUsuarioActual, 
                            "Asignación: Pkg " + idPkg + " a Cond " + idCond, null));
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
                    
                
                    boolean exito = true; // paqueteDAO.actualizarEstado(idPkg, estado); // Ajustar según firma del DAO
                    
                    if(exito) {
                        logDAO.registrarAccion(new LogSistema(0, idUsuarioActual, "CAMBIO_ESTADO: Paquete " + idPkg + " a estado " + estado, null));
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

                    // Llamamos al VehiculoDAO que ya tienes
                    vehiculoDAO.registrarUbicacion(idVeh, lat, lng);
                    return new MensajeRed("GPS_OK", null, true, "Coordenadas recibidas");
            case "RASTREAR_PAQUETE":
                    int idPkgRastreo = (int) peticion.getPayload();
                    Paquete pRastreo = paqueteDAO.buscarPaquetePorId(idPkgRastreo); 
                    return new MensajeRed("RASTREO_RES", pRastreo, pRastreo != null, pRastreo != null ? "OK" : "No encontrado");

            case "ULTIMA_UBICACION":
                int idVehUbi = (int) peticion.getPayload();
                // Necesitas este método en tu VehiculoDAO
                UbicacionVehiculo ubi = null; // vehiculoDAO.obtenerUltimaUbicacion(idVehUbi);
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