package Dominio;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;


public class Usuario extends Persona {

    public static AtomicInteger getContadorUsuarios() {
        return contadorUsuarios;
    }

    private final int id;
    private String password;
    private static final AtomicInteger contadorUsuarios = new AtomicInteger(0);
    private Rol rol;

public Usuario(String dni, LocalDate fechaNacimiento, String nombre, String apellido, String email, String telefono, String password, Rol rol) {
    super(dni, fechaNacimiento, nombre, apellido, email, telefono);
    this.id = contadorUsuarios.incrementAndGet(); // Thread-safe
    this.password = password;
    this.rol = rol;
    }

    // Getters

       public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    /////////SETTERS/////////
   
    public void setPassword(String password) { // revisar temas de seguridad
        this.password = password;
    }
    public void setRol(Rol rol) {  // revisar validaciones
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        } 
        if (!rol.isActivo()) {
            throw new IllegalArgumentException("El rol no está activo");
        } else {
            this.rol = rol;
        }
    }
}