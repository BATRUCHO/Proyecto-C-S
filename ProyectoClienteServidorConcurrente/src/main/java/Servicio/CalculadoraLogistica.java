package Servicio;

import java.util.List;
import java.util.Map;
import Dominio.Paquete;
import Dominio.Provincia;
import Dominio.TipoVehiculo;

public class CalculadoraLogistica {
    
    private double tarifaBase; 
    private double precioPorKm = 50.0; // Precio estándar por kilómetro
    private double costoTotal;

    public CalculadoraLogistica(Double tarifaBase) {
        this.tarifaBase = (tarifaBase != null) ? tarifaBase : 1500.0;
        this.costoTotal = 0;
    }

    /**
     * Calcula el costo total basado en la diferencia de distancia entre provincias.
     * @param paquete El paquete a evaluar.
     * @return El costo total del envío.
     */
    public double calcularCostoEnvio(Paquete paquete) {
        if (paquete == null) return 0.0;

        int kmOrigen = paquete.getProvinciaOrigen().getDistanciaKm();
        int kmDestino = paquete.getProvinciaDestino().getDistanciaKm();
        
        // Calculamos la distancia absoluta recorrida
        int distanciaRecorrida = Math.abs(kmDestino - kmOrigen);
        
        this.costoTotal = tarifaBase + (distanciaRecorrida * precioPorKm);
        return costoTotal;
    }

    



}






