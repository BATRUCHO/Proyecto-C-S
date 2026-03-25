package networking;

import protocolo.MensajeProtocolo;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClienteHandler extends Thread{
    
    private Socket socket;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            ObjectInputStream entrada =
                    new ObjectInputStream(socket.getInputStream());

            MensajeProtocolo mensaje =
                    (MensajeProtocolo) entrada.readObject();

            System.out.println("Handler iniciado");
            System.out.println("Esperando mensaje...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
