package PaqueteCliente.Controlador;

import java.util.List;

import Dominio.Excepciones.MensajeRed;
import Dominio.Paquete;
import PaqueteCliente.ModeloRed.ClienteSocket;

public class RutaControlador {

    /**
     * Obtiene solo los paquetes asignados al conductor que inició sesión.
     */
    public List<Paquete> obtenerMisPaquetes(int idUsuarioConductor) {
        // Enviamos el ID del conductor como payload
        MensajeRed peticion = new MensajeRed("LISTAR_PAQUETES_CONDUCTOR", idUsuarioConductor, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            return (List<Paquete>) respuesta.getPayload();
        }
        return null;
    }

    /**
     * Cambia el estado del paquete (Ej: De "Asignado" a "En Tránsito" o "Entregado").
     */
    public boolean actualizarEstadoEntrega(int idPaquete, int nuevoEstado) {
        String datos = idPaquete + ":" + nuevoEstado;

        MensajeRed peticion = new MensajeRed("ACTUALIZAR_ESTADO_PAQUETE", datos, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        return respuesta.isEstadoExito();
    }

    public boolean enviarUbicacionGps(int id_vehiculo, double latitud, double longitud) {
        String datosGps = id_vehiculo + ":" + latitud + ":" + longitud;

        MensajeRed peticion = new MensajeRed("ACTUALIZAR_GPS", datosGps, true, "");
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);
        
        return respuesta.isEstadoExito();
    }
}
