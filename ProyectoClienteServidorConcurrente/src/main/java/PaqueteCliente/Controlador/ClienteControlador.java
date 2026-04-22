package PaqueteCliente.Controlador;

import Dominio.Excepciones.MensajeRed;
import Dominio.Paquete;
import Dominio.UbicacionVehiculo;
import PaqueteCliente.ModeloRed.ClienteSocket;

public class ClienteControlador {

    /**
     * Busca un paquete por su ID o código de seguimiento.
     */
    public Paquete rastrearPaquete(int idPaquete) {
        MensajeRed peticion = new MensajeRed("RASTREAR_PAQUETE", idPaquete, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            return (Paquete) respuesta.getPayload();
        }
        return null;
    }

    /**
     * Obtiene la última ubicación conocida del vehículo que lleva el paquete.
     */
    public UbicacionVehiculo obtenerUbicacionActual(int idVehiculo) {
        MensajeRed peticion = new MensajeRed("ULTIMA_UBICACION", idVehiculo, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            return (UbicacionVehiculo) respuesta.getPayload();
        }
        return null;
    }
}