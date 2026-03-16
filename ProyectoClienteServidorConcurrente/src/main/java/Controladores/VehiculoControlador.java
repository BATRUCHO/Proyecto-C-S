
package Controladores;

import Tabs.TablaVehiculos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class VehiculoControlador {

    private TablaVehiculos vista;

    public VehiculoControlador(TablaVehiculos vista) {
        this.vista = vista;
        iniciarEventos();
    }

    private void iniciarEventos() {

        vista.getBtnCrear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearVehiculo();
            }
        });

        vista.getBtnCargar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarVehiculo();
            }
        });

        vista.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarVehiculo();
            }
        });
    }

    private void crearVehiculo() {
        JOptionPane.showMessageDialog(vista, "Vehículo creado correctamente");
    }

    private void cargarVehiculo() {
        JOptionPane.showMessageDialog(vista, "Vehículo cargado");
    }

    private void eliminarVehiculo() {
        JOptionPane.showMessageDialog(vista, "Vehículo eliminado");
    }
}