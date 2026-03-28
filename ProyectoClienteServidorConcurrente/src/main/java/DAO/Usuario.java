package Conexion;

/**
 *
 * @author Asus Vivobook
 */
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String clave;
    private String rol;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String correo, String clave, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

    public Usuario(String nombre, String correo, String clave, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
public String toString() {
    return "Usuario{" +
            "idUsuario=" + idUsuario +
            ", nombre='" + nombre + '\'' +
            ", correo='" + correo + '\'' +
            ", clave='" + clave + '\'' +
            ", rol='" + rol + '\'' +
            '}';

}
}
