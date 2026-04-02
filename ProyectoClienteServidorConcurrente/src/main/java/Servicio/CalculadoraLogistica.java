package Servicio;

import Dominio.Provincia;
import Dominio.TipoVehiculo;

public class CalculadoraLogistica {
    
    private final  double tarifaBase; 
    private final double PRECIO_POR_KM = 50.0;
    private final double PRECIO_POR_KG = 10.0;

    public CalculadoraLogistica(double tarifaBase) {
        this.tarifaBase = tarifaBase;
    }

    public double calcularCosto(double peso, Provincia origen, Provincia destino) {
        if (origen == null || destino == null) return 0.0;

        int kmOrigen = origen.getDistanciaKm();
        int kmDestino = destino.getDistanciaKm();
        int distanciaRecorrida = Math.abs(kmDestino - kmOrigen);
        
        return tarifaBase + (distanciaRecorrida * PRECIO_POR_KM) + (peso * PRECIO_POR_KG);
    }

    public double calcularTiempoEntrega(TipoVehiculo tipo, int distancia) {
        double velocidad = switch (tipo) {
            case BICICLETA -> 15.0;
            case MOTO -> 40.0;
            case CARRO -> 60.0;
            case CAMION -> 50.0;
            default -> 30.0;
        };
        
        return distancia / velocidad;
    }
}

 
