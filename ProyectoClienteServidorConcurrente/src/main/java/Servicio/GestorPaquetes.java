package Servicio;

import java.util.List;
import DAO.PaqueteDAO;
import DAO.VehiculoDAO;
import Dominio.Paquete;
import Dominio.Vehiculo;


public class GestorPaquetes {
    
    private PaqueteDAO paqueteDAO; 
    private VehiculoDAO vehiculoDAO;
    private CalculadoraLogistica calculadoraLogistica;
    private MonitorSistema monitorSistema;

    public GestorPaquetes() {
        this.paqueteDAO = new PaqueteDAO();
        this.vehiculoDAO = new VehiculoDAO();
        this.calculadoraLogistica = new CalculadoraLogistica();
        this.monitorSistema = new MonitorSistema();
    }

    // Aquí corregí el acceso a los atributos (asumiendo que existen en la clase Paquete)
    public void registrarPaquete(Paquete paquete){
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        // Agregué paréntesis a isEmpty()
        if (paquete.getPesoKg() <= 0 || paquete.getContenido() == null || paquete.getContenido().isEmpty()) {
            throw new IllegalArgumentException("El paquete no es válido");
        }
        this.paqueteDAO.registrarPaquete(paquete);
   }

    public void asignarVehiculo(Paquete paquete, Vehiculo vehiculo){
        
        
    }

    public double calcularCostoEnvio(){
        return 0.0; 
    }

    public void  actualizarEstado(){

    }

    public List<Paquete> listarPaquetes(){
        return null;    
    }


}
