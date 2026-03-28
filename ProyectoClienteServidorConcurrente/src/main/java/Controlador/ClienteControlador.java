
package Controlador;

import modelo.ClienteSocket;
import vista.ClienteVista;

import java.io.IOException;

public class ClienteControlador {

    private ClienteSocket modelo;
    private ClienteVista vista;

    public ClienteControlador(ClienteSocket modelo, ClienteVista vista) {
        this.modelo = modelo;
        this.vista = vista;

        initEventos();
    }

    private void initEventos() {
        vista.getBtnConectar().addActionListener(e -> conectar());
        vista.getBtnEnviar().addActionListener(e -> enviar());
    }

    private void conectar() {
        try {
            modelo.conectar(
                vista.getTxtHost().getText(),
                Integer.parseInt(vista.getTxtPuerto().getText())
            );
            vista.mostrarMensaje("Conectado al servidor");
        } catch (IOException ex) {
            vista.mostrarMensaje("Error al conectar: " + ex.getMessage());
        }
    }

    private void enviar() {
        try {
            String mensaje = vista.getTxtMensaje().getText();
            modelo.enviarMensaje(mensaje);

            String respuesta = modelo.recibirMensaje();
            vista.mostrarMensaje("Servidor: " + respuesta);

        } catch (IOException ex) {
            vista.mostrarMensaje("Error: " + ex.getMessage());
        }
    }
}