package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
   private static final int PUERTO = 54567;
   private static final int MAX_HILOS = 50; // Limite de hilos concurrentes


    public static void main(String[] args) {

        ExecutorService piscinaHilos = Executors.newFixedThreadPool(MAX_HILOS);

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {

            System.out.println("Servidor iniciado en el puerto " + PUERTO);

            while (true) {
                System.out.println("Esperando conexiones...");
                Socket cliente = servidor.accept();
                piscinaHilos.execute(new HiloCliente(cliente));
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        } finally {
            piscinaHilos.shutdown();
        }
            
    } 

}
