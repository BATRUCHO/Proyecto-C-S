package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Dominio.EstadoPaquete;
import Dominio.Paquete;
import Servicio.GestorPaquetes;

public class ClienteHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private GestorPaquetes gestorPaquetes;
    private ServidorLogistica servidor;

    public ClienteHandler(Socket socket, ServidorLogistica servidor) {
        this.clientSocket = socket;
        this.servidor = servidor;
        this.gestorPaquetes = new GestorPaquetes();
        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error creando streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                MensajeProtocolo mensaje = (MensajeProtocolo) input.readObject();
                if (mensaje != null) {
                    procesarMensaje(mensaje);
                    if (mensaje.getTipoOperacion() == TipoOperacion.DESCONEXION) break;
                }
            }
        } catch (Exception e) {
            System.err.println("Cliente desconectado abruptamente");
        } finally {
            cerrarConexion();
        }
    }

    public void procesarMensaje(MensajeProtocolo m) {
        System.out.println("Procesando: " + m.getTipoOperacion());
        
        switch (m.getTipoOperacion()) {
            case REPORTAR_UBICACION:
                // Lógica para actualizar ubicación en tiempo real
                break;
            case NOTIFICAR_ENTREGA:
                Paquete p = (Paquete) m.getDatos();
                gestorPaquetes.actualizarEstado(p, EstadoPaquete.ENTREGADO);
                break;
            case DESCONEXION:
                System.out.println("Cerrando sesión de cliente");
                break;
        }
    }

    public void cerrarConexion() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
