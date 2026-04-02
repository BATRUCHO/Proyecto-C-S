package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServidorLogistica {
    private int puerto;
    private ServerSocket serverSocket;
    private List<ClienteHandler> clientes;
    private boolean ejecutando;

    public ServidorLogistica(int puerto) {
        this.puerto = puerto;
        this.clientes = Collections.synchronizedList(new ArrayList<>());
    }

    public void iniciarServidor() {
        try {
            serverSocket = new ServerSocket(puerto);
            ejecutando = true;
            System.out.println(">>> Servidor de Logística iniciado en puerto " + puerto);
            aceptarConexiones();
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    private void aceptarConexiones() {
        while (ejecutando) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClienteHandler handler = new ClienteHandler(clientSocket, this);
                registrarCliente(handler);
                new Thread(handler).start();
            } catch (IOException e) {
                if (ejecutando) System.err.println("Error aceptando conexión: " + e.getMessage());
            }
        }
    }

    public void registrarCliente(ClienteHandler handler) {
        clientes.add(handler);
        System.out.println("Nuevo cliente conectado. Total activos: " + clientes.size());
    }
}
