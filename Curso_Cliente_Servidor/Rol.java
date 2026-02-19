package Proyectos.Curso_Cliente_Servidor;

import java.util.EnumSet;
import java.util.Set;

public class Rol {

    private final String nombreRol;
    private String descripcion;  // descripcion podría ser final (si decides que no cambia)
    private boolean activo; // activo podría usarse para soft-disable de roles
    private final Set<EstadoPermiso> permisosAsignados; // Podrías validar permisosAsignados ≠ null (más adelante)

    public Rol(String nombreRol, String descripcion, boolean activo, Set<EstadoPermiso> permisosAsignados) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.activo = activo;
        this.permisosAsignados = EnumSet.copyOf(permisosAsignados);
    }
    public String getNombreRol() {
        return nombreRol;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public boolean isActivo() {
        return activo;
    }
    public Set<EstadoPermiso> getPermisosAsignados() {
        return EnumSet.copyOf(permisosAsignados);
    }
}