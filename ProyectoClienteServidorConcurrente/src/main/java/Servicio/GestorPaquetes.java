package Servicio;

import java.util.List;

import DAO.PaqueteDAO;
import DAO.VehiculoDAO;
import Servicio.CalculadoraLogistica;
import Servicio.MonitorSistema;


public class GestorPaquetes {
   
    private PaqueteDAO paqueteDAO;
    private VehiculoDAO vehiculoDAO;
    private CalculadoraLogistica calculadoraLogistica;
    private MonitorSistema monitorSistema;

    public GestorPaquetes() {
        // Constructor, quizás inicializar DAO o MonitorSistema
        this.paqueteDAO = new PaqueteDAO();
        this.vehiculoDAO = new VehiculoDAO();
        this.calculadoraLogistica = new CalculadoraLogistica();
        this.monitorSistema = new MonitorSistema();
    }

   public void registrarPaquete(IPaqueteDAO paqueteDAO){
 
   }


   public void asignarVehiculo(){

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
