package Servicio;

import java.util.List;

import Dominio.EstadoPaquete;
import Dominio.Paquete;
import Dominio.Vehiculo;

public class GestorPaquetes {

    private void atributoDaoProvicionalPaquete;
    private void atributoDaoProvicionalVehiculo;
    pri


    
    // Aquí inyectarás el PaqueteDAO cuando el compañero de DB lo termine
    // private IPaqueteDAO paqueteDAO; 

    public GestorPaquetes() {
        // Constructor, quizás inicializar DAO o MonitorSistema
    }

    /**
     * Mapeado al método: + procesarEnvio(paquete : Paquete) : boolean
     * del UML.
     */
   public void registrarPaquete(Paquete paquete){
     
   }

   public void asignarVehiculo(Paquete paquete, Vehiculo vehiculo){

   }

   public double calcularCostoEnvio(Paquete paquete){
       return 0.0; 
   }

   public void  actualizarEstado(Paquete paquete, EstadoPaquete estado){

   }

   public List<Paquete> listarPaquetes(){
    return null;    
   }


}
