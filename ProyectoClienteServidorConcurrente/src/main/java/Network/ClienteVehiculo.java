package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Dominio.Vehiculo;
import Dominio.Paquete;

public class ClienteVehiculo {
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private Vehiculo vehiculoAsociado;

    public boolean conectar(String ip, int puerto) {
        try {
            this.socket = new Socket(ip, puerto);
            this.salida = new ObjectOutputStream(socket.getOutputStream());
            this.entrada = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conectado al servidor de logística");
            return true;
        } catch (IOException e) {
            System.err.println("No se pudo conectar: " + e.getMessage());
            return false;
        }
    }

    public void enviarReporte(Paquete paquete) {
        try {
            MensajeProtocolo mensaje = new MensajeProtocolo(TipoOperacion.NOTIFICAR_ENTREGA, paquete);
            salida.writeObject(mensaje);
            salida.flush();
        } catch (IOException e) {
            System.err.println("Error al enviar reporte: " + e.getMessage());
        }
    }

    public void desconectar() {
        try {
            if (salida != null) {
                salida.writeObject(new MensajeProtocolo(TipoOperacion.DESCONEXION, null));
                salida.flush();
            }
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
