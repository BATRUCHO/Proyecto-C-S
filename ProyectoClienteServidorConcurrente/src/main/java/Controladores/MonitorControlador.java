
package Controladores;

import Tabs.MonitorTiempoRealFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

public class MonitorControlador {

    private MonitorTiempoRealFrame vista;
    private Timer temporizador;

    public MonitorControlador(MonitorTiempoRealFrame vista) {
        this.vista = vista;
        iniciarMonitor();
    }

    private void iniciarMonitor() {

        temporizador = new Timer(3000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMonitor();
            }
        });

        temporizador.start();
    }

    private void actualizarMonitor() {

        // Simulación de actualización
        System.out.println("Actualizando estado de vehículos y paquetes...");
    }
}