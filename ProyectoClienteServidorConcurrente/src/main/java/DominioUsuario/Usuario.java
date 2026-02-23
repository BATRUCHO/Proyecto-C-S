package Proyectos.Curso_Cliente_Servidor;

import java.util.Objects;
import java.sql.Date;

public class Usuario extends Persona {

    private final int id;
    private String password;
    private static int contadorUsuarios; 
    private Rol rol;

    public Usuario(String dni, Date fechaNacimiento, String nombre, String apellido, String email, String telefono, String password, Rol rol) {

        super(dni, fechaNacimiento, nombre, apellido, email, telefono);
        this.password = Objects.requireNonNull(password, "La contraseña no puede ser nula");
        this.id = ++contadorUsuarios; // arreglar despues para que no sea un thread unsafe // multihilo
        this.rol = Objects.requireNonNull(rol, "El rol no puede ser nulo");
    }

    ////////GETTERS/////////
    public String getPassword() {
        return password;
    }
    public int getId() {
        return id;
    }
    public static int getContadorUsuarios() {
        return contadorUsuarios;
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