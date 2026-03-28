package networking;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorLogistica {
    private static final int PUERTO = 5000;

        public static void main(String[] args) {

            try (ServerSocket servidor = new ServerSocket(PUERTO)) {

                System.out.println("Servidor iniciado...");

                while (true) {

                    System.out.println("Esperando cliente...");
                    Socket cliente = servidor.accept();
                    System.out.println("Cliente conectado");

                    ClienteHandler handler = new ClienteHandler(cliente);
                    handler.start();

                }

        } catch (Exception e) {
               e.printStackTrace();
        }
    }
}
