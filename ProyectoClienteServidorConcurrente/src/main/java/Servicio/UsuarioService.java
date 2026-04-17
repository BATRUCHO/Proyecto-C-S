package Servicio;

public class UsuarioService {

    public boolean guardarUsuario(String nombre, String correo) {
        // aquí va lógica + conexión DB
        System.out.println("Guardando usuario: " + nombre);
        return true;
    }
}