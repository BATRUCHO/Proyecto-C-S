package Dominio;

public enum EstadoPaquete {
    EN_BODEGA(1, "En Bodega"),
    EN_TRANSITO(2,"Asignado a Ruta o En Tránsito"),
    ENTREGADO(3, "Entregado"),
    CON_INCIDENCIA(4, "Con Incidencias");

    private final int id_estado;
    private final String descripcion;

    EstadoPaquete(int id_estado, String descripcion){
        this.id_estado = id_estado;
        this.descripcion = descripcion;
    }

    public int getIdEstado() {return id_estado;}
    public String getDescripcion() {return descripcion;}
    

    //Metodo analitico para buscar por ID numerico
    public static String obtenerTextoPorId(int idBuscar){
        for(EstadoPaquete estado : values()) {
            if(estado.getIdEstado() == idBuscar){
                return estado.getDescripcion();
            }
        }
        return "Desconocido";
    }
}


   