package PaqueteCliente.Controlador;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import Dominio.Excepciones.MensajeRed;
import Dominio.Paquete;
import Dominio.Usuarios;
import Dominio.Vehiculo;
import PaqueteCliente.ModeloRed.ClienteSocket;

public class AdminControlador {

    //----------------MetodosControlUsuario----------------//

  public boolean registrarUsuario(String nombre, String apellido, Date fechaNacimiento, 
                                String dni, String email, String telefono, 
                                String password, int idRol) {
    
        Usuarios nuevoUsuario = new Usuarios(
            0, 
            nombre, 
            apellido, 
            fechaNacimiento, 
            dni, 
            email, 
            telefono, 
            password, 
            idRol
        );

        MensajeRed peticion = new MensajeRed("REGISTRAR_USUARIO", nuevoUsuario, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (!respuesta.isEstadoExito()) {
            System.err.println("Error en registro: " + respuesta.getMensajeRespuesta());
        }

        return respuesta.isEstadoExito();
    }

    public boolean eliminarUsuario(int idUsuario) {

        MensajeRed peticion = new MensajeRed("ELIMINAR_USUARIO", idUsuario, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    } 


    //----------------MetodosControlPaquete----------------//

    @SuppressWarnings("unchecked")
    public List<Paquete> actualizarPaquetes() {
        MensajeRed peticion = new MensajeRed("LISTAR_PAQUETES", null, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            return (List<Paquete>) respuesta.getPayload();
        } else {
            System.err.println("Error: " + respuesta.getMensajeRespuesta());
            return null;
        }
    }

     public boolean registrarNuevoPaquete(String descripcion, String remitente, String destinatario, String direccion_entrega, BigDecimal peso) {

        Paquete nuevoPaquete = new Paquete( // Instanciamos el objeto con el orden estricto de sus campos de dominio
            0, // id_paquete = 0 (porque es autoincrement)
            descripcion,
            remitente,
            destinatario,
            direccion_entrega,
            peso,
            null // fecha_creacion = null (porque la estampa el servidor con NOW())
        );
 
        nuevoPaquete.setId_estado(1); // Inyectamos el estado operativo inicial fijo (Regla de negocio: Nace en bodega)


        MensajeRed peticion = new MensajeRed("CREAR_PAQUETE", nuevoPaquete, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if(!respuesta.isEstadoExito()){
            System.err.println("Error del servidor: " + respuesta.getMensajeRespuesta());
        }
        return respuesta.isEstadoExito();
    }

    /*public boolean asignarPaquete(int idPaquete, int idConductor) {
        String datosAsignacion = idPaquete + ":" + idConductor;

        MensajeRed peticion = new MensajeRed("ASIGNAR_PAQUETE", datosAsignacion, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    } */

    public boolean editarPaquete(Paquete paquete) {
        
        MensajeRed peticion = new MensajeRed("EDITAR_PAQUETE", paquete, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    }

    public boolean eliminarPaquete(int idPaquete , int id_usuario) {
    // Colocamos las variables en el orden exacto del constructor de MensajeRed
    // 1. Accion ("ELIMINAR_PAQUETE")
    // 2. Payload (idPaquete)
    // 3. EstadoExito (true)
    // 4. MensajeRespuesta ("")
    // 5. idUsuarioSender (id_usuario) <--- El ID real viaja aquí al final
    
        MensajeRed peticion = new MensajeRed("ELIMINAR_PAQUETE", idPaquete, true, "",id_usuario);
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    
    }

    //----------------MetodosVehiculo----------------//

    @SuppressWarnings("unchecked")
    public List<Vehiculo> actualizarVehiculos() {
        MensajeRed peticion = new MensajeRed("LISTAR_VEHICULOS", null, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            return (List<Vehiculo>) respuesta.getPayload();
        }else{
            System.err.println("Error: " + respuesta.getMensajeRespuesta());
            return null;
        }
    }

    public boolean registrarNuevoVehiculo(String placa, String marca, String modelo, int id_tipoVehiculo, int id_estado_vehiculo) { 

            Vehiculo nuevoVehiculo = new Vehiculo(        
                0, // Dato autogenerado
                placa, 
                marca, 
                modelo, 
                id_tipoVehiculo,
                id_estado_vehiculo
                );

            MensajeRed peticion = new MensajeRed("REGISTRAR_VEHICULO", nuevoVehiculo, true, "");
            MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

            if(!respuesta.isEstadoExito()){
                System.err.println("Error del servidor: " + respuesta.getMensajeRespuesta());
            }
            return respuesta.isEstadoExito();
        
    }

    public boolean editarVehiculo(Vehiculo vehiculo) {

        MensajeRed peticion = new MensajeRed("EDITAR_VEHICULO", vehiculo, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    }

    public boolean eliminarVehiculo(int idVehiculo, int id_usuario){
        
        MensajeRed peticion = new MensajeRed("ELIMINAR_VEHICULO", idVehiculo, true, "", id_usuario);
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    }




}
