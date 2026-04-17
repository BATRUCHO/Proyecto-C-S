
package vista;

import java.util.Scanner;

public class UsuarioView {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UsuarioController controller = new UsuarioController();

        System.out.println("Nombre:");
        String nombre = sc.nextLine();

        System.out.println("Correo:");
        String correo = sc.nextLine();

        controller.registrarUsuario(nombre, correo);
    }
}