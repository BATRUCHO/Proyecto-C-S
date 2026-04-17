package Servicio;

import Dominio.Paquete;
import Dominio.Vehiculo;
import Dominio.EstadoPaquete;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class MonitorSistema {
    private static final ConcurrentHashMap<Integer, Paquete> paquetesActivos = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Vehiculo> vehiculosActivos = new ConcurrentHashMap<>();

    public void registrarPaquete(Paquete p) {
        paquetesActivos.put(p.getIdPaquete(), p);
    }

    public void registrarVehiculo(Vehiculo v) {
        vehiculosActivos.put(v.getIdVehiculo(), v);
    }

    public void actualizarEstadoPaquete(int id, EstadoPaquete estado) {
        Paquete p = paquetesActivos.get(id);
        if (p != null) {
            p.setEstado(estado);
            if (estado == EstadoPaquete.ENTREGADO) {
                paquetesActivos.remove(id);
            }
        }
    }

    public Map<Integer, Paquete> obtenerEstadoSistema() {
        return paquetesActivos;
    }
}