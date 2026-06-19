package Dominio;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;


public class Usuarios implements Serializable{

    private static final long serialVersionUID = 1L;

    private int  id_usuario;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private String dni;
    private String email;
    private String telefono;
    private String password;
    private Timestamp fechaCreacion;
    private boolean activo;
    private int id_rol;

  public Usuarios(int id_usuario, String nombre, String apellido, Date fechaNacimiento, String dni, String email, String telefono, String password, int id_rol) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.id_rol = id_rol;
        this.activo = true; 
    }

    // CONSTRUCTOR 2
    public Usuarios(int id_usuario, String nombre, String apellido, Date fechaNacimiento, String dni, String email, String telefono, String password, Timestamp fechaCreacion, boolean activo, int id_rol) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
        this.id_rol = id_rol;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIdRol() {
        return id_rol;
    }

    public void setIdRol(int id_rol) {
        this.id_rol = id_rol;
    }

}