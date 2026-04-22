package PaqueteCliente.ModeloRed;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Dominio.Excepciones.MensajeRed;

public class ClienteSocket {
    
    // 1. Atributo estático que guardará la única instancia
    private static ClienteSocket instancia;
    
    // 2. Constructor privado para que nadie use "new" fuera de aquí
    private ClienteSocket() {}

    // 3. Método público para obtener la instancia única
    public static ClienteSocket getInstancia() {
        if (instancia == null) {
            instancia = new ClienteSocket();
        }
        return instancia;
    }

    public MensajeRed enviarPeticion(MensajeRed peticion) {
        // Usamos localhost y el puerto que ya definiste
        try (Socket socket = new Socket("localhost", 54567);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
            
            salida.writeObject(peticion);
            salida.flush();
            
            return (MensajeRed) entrada.readObject();
            
        } catch (Exception e) {
            return new MensajeRed("ERROR", null, false, "Error de conexión: " + e.getMessage());
        }
    }
}
