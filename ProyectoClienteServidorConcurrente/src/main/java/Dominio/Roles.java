package Dominio;


public enum Roles   {

    CONDUCTORES(1,"Conductor"),
    ADMINISTRADOR(2,"Administrador");

    private final int id_rol;
    private final String nombre;


    Roles(int id_rol, String nombre) {
        this.id_rol = id_rol;
        this.nombre = nombre;
    }

    public int getId() {return id_rol;}
    public String getNombre() {return nombre;}

      //Metodo analitico para buscar por ID numerico
    public static String obtenerTextoPorId(int idBuscar){
        for(Roles rol : values()) {
            if(rol.getId() == idBuscar){
                return rol.getNombre();
            }
        }
        return "Desconocido";
    }



}
