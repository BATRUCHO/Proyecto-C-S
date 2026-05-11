package PaqueteCliente.Controlador;

import java.sql.Date;
import java.util.List;

import Dominio.Excepciones.MensajeRed;
import Dominio.Paquete;
import Dominio.Usuario;
import PaqueteCliente.ModeloRed.ClienteSocket;

public class AdminControlador {

    //----------------MetodosControlUsuario----------------//

  public boolean registrarUsuario(String nombre, String apellido, Date fechaNac, 
                                String dni, String email, String telefono, 
                                String password, int idRol) {
    
        Usuario nuevoUsuario = new Usuario(0, dni, fechaNac, nombre, apellido, email, telefono, password, idRol);

        MensajeRed peticion = new MensajeRed("REGISTRAR_USUARIO", nuevoUsuario, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (!respuesta.isEstadoExito()) {
            System.err.println("Error en registro: " + respuesta.getMensajeRespuesta());
        }

        return respuesta.isEstadoExito();
    }

    //----------------MetodosControlPaquete----------------//

    public List<Paquete> obtenerTodosLosPaquetes() {
   
        MensajeRed peticion = new MensajeRed("LISTAR_PAQUETES", null, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            return (List<Paquete>) respuesta.getPayload();
        } else {
            System.err.println("Error: " + respuesta.getMensajeRespuesta());
            return null;
        }
    }

    public boolean asignarPaquete(int idPaquete, int idConductor) {

        String datosAsignacion = idPaquete + ":" + idConductor;

        MensajeRed peticion = new MensajeRed("ASIGNAR_PAQUETE", datosAsignacion, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    }

    public boolean registrarNuevoPaquete(Double peso, String desc, String remitente, String dest, String dirEntrega) {

        Paquete nuevoPaquete = new Paquete(0, desc, remitente, dest, dirEntrega, peso, 1, null, 0);
        MensajeRed peticion = new MensajeRed("CREAR_PAQUETE", nuevoPaquete, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if(!respuesta.isEstadoExito()){
            System.err.println("Error del servidor: " + respuesta.getMensajeRespuesta());
        }
        return respuesta.isEstadoExito();
    }

    

    

}
