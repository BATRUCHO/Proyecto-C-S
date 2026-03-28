package Controlador;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehiculoController {

    private final List<Vehiculo> vehiculos = new ArrayList<>();

    public void addVehiculo(Vehiculo v) {
        if (v != null) {
            vehiculos.add(v);
        }
    }

    public List<Vehiculo> getVehiculos() {
        return Collections.unmodifiableList(vehiculos);
    }

    public void clearVehiculos() {
        vehiculos.clear();
    }
}
