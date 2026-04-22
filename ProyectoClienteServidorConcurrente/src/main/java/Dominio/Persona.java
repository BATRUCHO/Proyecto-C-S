package Dominio;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;


public abstract class Persona implements Serializable {
    protected final String dni;
    protected final Date fechaNacimiento;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String telefono;

    protected Persona(String dni, Date fechaNacimiento, String nombre, String apellido, String email, String telefono) {
        this.dni = Objects.requireNonNull(dni,"Error: DNI no puede ser nulo");
        this.fechaNacimiento = Objects.requireNonNull(fechaNacimiento,"Error: Fecha de nacimiento no puede ser nula");
        this.nombre = Objects.requireNonNull(nombre,"Error: Nombre no puede ser nulo");
        this.apellido = Objects.requireNonNull(apellido,"Error: Apellido no puede ser nulo");
        this.email = email; 
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
