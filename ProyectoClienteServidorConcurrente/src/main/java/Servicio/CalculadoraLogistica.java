package Servicio;

import java.util.List;
import java.util.Map;

import Dominio.Provincia;
import Dominio.TipoVehiculo;

public class CalculadoraLogistica {
    
    private double tarifaBase;
    private Map<Provincia,Integer> distanciaEntreProvincias;
    private double costoTotal;

    public CalcularCosto()




 // Metodo   

 public void calcularCostoTotal() {
        double costoBase = 5.0;
        double factorDistancia = (provinciaOrigen == provinciaDestino) ? 1.0 : 1.5;
        this.costoTotal = (costoBase + (pesoKg * 2.0)) * factorDistancia;
    }

}
