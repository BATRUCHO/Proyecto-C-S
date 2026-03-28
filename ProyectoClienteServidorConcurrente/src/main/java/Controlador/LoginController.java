package Controlador;

public class LoginController {

    // Validación mock (no se conectará a ningún socket)
    public boolean login(String username, String password) {
        // Ejemplo simple: usuario/contraseña válidos
        if (username == null || password == null) return false;
        return ("admin".equalsIgnoreCase(username) && "admin".equals(password)) ||
               ("user".equalsIgnoreCase(username) && "user".equals(password));
    }
}