package Servicio;

import java.util.List;

import DAO.VehiculoDAOImpl;
import Dominio.EstadoVehiculo; 
import Dominio.Vehiculo;

public class GestorVehiculos  {
    private VehiculoDAOImpl vehiculoDAOImpl;
    private MonitorSistema monitor;

    public GestorVehiculos() {
        this.monitor = new MonitorSistema();
        this.vehiculoDAO = new VehiculoDAO();
    }

    public void registrarVehiculo(Vehiculo v) {
     if (vehiculoDAO.(v.getPlaca()) == null && vehiculoDAO.buscarPorId(v.getIdVehiculo)) == null{ 
         vehiculoDAO.registrar(v);
         monitor.registrarVehiculo(v);
     }
    }

    public void actualizarVehiculo(Vehiculo v) {
        vehiculoDAO.actualizar(v);
        monitor.registrarVehiculo(v);
    }

    public Vehiculo obtenerVehiculoDisponible(double peso) {
        return vehiculoDAO.obtenerDisponiblePorCapacidad(peso);
    }

    public void actualizarEstadoVehiculo(int id, EstadoVehiculo estado) {
        Vehiculo v = vehiculoDAO.buscarPorId(id);
        if (v != null) {
            v.setEstado(estado);
            vehiculoDAO.actualizar(v);
            monitor.registrarVehiculo(v);
        }
    }
    public List<Vehiculo> listarVehiculosActivos() {
        return vehiculoDAO.listarVehiculosActivos();
    }

}