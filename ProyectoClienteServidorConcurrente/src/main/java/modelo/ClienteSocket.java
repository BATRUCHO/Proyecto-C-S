
package modelo;

import java.io.*;
import java.net.Socket;

public class ClienteSocket {

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;

    public void conectar(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);
    }

    public void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }

    public String recibirMensaje() throws IOException {
        return entrada.readLine();
    }

    public void cerrar() throws IOException {
        if (socket != null) socket.close();
    }
}