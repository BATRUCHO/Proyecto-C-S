package networking;

import protocolo.MensajeProtocolo;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteSocket {
    
    public static void main(String[] args) {

        try {

            Socket socket = new Socket("localhost", 5001);

            ObjectOutputStream salida =
                    new ObjectOutputStream(socket.getOutputStream());

            MensajeProtocolo mensaje =
                    new MensajeProtocolo(
                            MensajeProtocolo.TipoOperacion.LOGIN,
                            "Elián"
                    );

            salida.writeObject(mensaje);
            salida.flush();
            
            System.out.println("Mensaje enviado");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
