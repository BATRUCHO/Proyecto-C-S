package Dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    protected final String dni;
    protected final LocalDate fechaNacimiento;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String telefono;

    protected Persona(String dni, LocalDate fechaNacimiento, String nombre, String apellido, String email, String telefono) {
        this.dni = Objects.requireNonNull(dni,"Error: DNI no puede ser nulo");
        this.fechaNacimiento = Objects.requireNonNull(fechaNacimiento,"Error: Fecha de nacimiento no puede ser nula");
        this.nombre = Objects.requireNonNull(nombre,"Error: Nombre no puede ser nulo");
        this.apellido = Objects.requireNonNull(apellido,"Error: Apellido no puede ser nulo");
        this.email = email; 
        this.telefono = telefono;
    }

    // Getters

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public String getDni() {
        return dni;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getEmail() {
        return email;
    }
    public String getTelefono() {
        return telefono;
    } 
}
