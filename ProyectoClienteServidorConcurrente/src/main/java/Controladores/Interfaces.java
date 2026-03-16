
package Controladores;

import Tabs.TablaVehiculos;
import Controladores.VehiculoControlador;

public class Interfaces {

    public static void main(String[] args) {

        TablaVehiculos vistaVehiculos = new TablaVehiculos();
        VehiculoControlador controlador = new VehiculoControlador(vistaVehiculos);

        vistaVehiculos.setVisible(true);
    }
}
