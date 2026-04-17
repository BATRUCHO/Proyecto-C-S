
package vista;

import Servicio.UsuarioService;

public class UsuarioController {

    private UsuarioService service;

    public UsuarioController() {
        service = new UsuarioService();
    }

    public void registrarUsuario(String nombre, String correo) {
        boolean resultado = service.guardarUsuario(nombre, correo);

        if (resultado) {
            System.out.println("Usuario registrado correctamente");
        } else {
            System.out.println("Error al registrar");
        }
    }
}
