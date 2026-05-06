package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
   private static final int PUERTO = 54567;

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado en el puerto " + PUERTO);

            while (true) {
               Socket cliente = servidor.accept();
                System.out.println("Nuevo cliente conectado: " + cliente.getInetAddress().getHostAddress());

                // Iniciar un hilo para manejar al cliente
                HiloCliente hilo = new HiloCliente(cliente);
                hilo.start();
               
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    } 
    


}
