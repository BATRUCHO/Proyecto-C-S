package Dominio;

import java.io.Serializable;
import java.sql.Date;


public class Usuario extends Persona implements Serializable{
    private final int idUsuario;
    private String password;
    private int idRol;


   // En Dominio.Usuario
    public Usuario(int idUsuario, String dni, Date fechaNacimiento, String nombre, 
               String apellido, String email, String telefono, String password, int idRol) {
    super(dni, fechaNacimiento, nombre, apellido, email, telefono); 
    this.idUsuario = idUsuario;
    this.password = password;
    this.idRol = idRol;
    }


    public int getIdUsuario() {
        return idUsuario;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }


}