/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

public class Rol implements Serializable {

    private final String nombreRol;
    private final String descripcion;  
    private boolean activo; 
    private final Set<Permiso> permisosAsignados; 

    public Rol(String nombreRol, String descripcion, boolean activo, Set<Permiso> permisosAsignados) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.activo = activo;
        this.permisosAsignados = EnumSet.copyOf(permisosAsignados);
    }

    // Getters
    
    public String getNombreRol() {
        return nombreRol;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public boolean isActivo() {
        return activo;
    }
    public Set<Permiso> getPermisosAsignados() {
        return EnumSet.copyOf(permisosAsignados);
    }
}