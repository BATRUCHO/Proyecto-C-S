package Dominio;

public enum TipoVehiculo {
    CAMIONETA(1, "Camioneta"),
    MOTOCICLETA(2, "Motocicleta"),
    CAMION_PESADO(3, "Camión pesado");

    private final int id_tipoVehiculo;
    private final String descripcion;


    TipoVehiculo(int id_tipoVehiculo, String descripcion) {
        this.id_tipoVehiculo = id_tipoVehiculo ;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {return descripcion;}
    public int getIdTipoVehiculo() {return id_tipoVehiculo ;}


    //Metodo analitico para buscar por ID numerico

    public static String obtenerTextoPorId(int idBuscar){
        for(TipoVehiculo tipo : values()) {
            if(tipo.getIdTipoVehiculo() == idBuscar){
                return tipo.getDescripcion();
            }
        }
        return "Desconocido";
    }


     public static int obtenerIdPorTexto(String textoBuscar){
        for(TipoVehiculo tipo : values()) {
            if(tipo.getDescripcion().equalsIgnoreCase(textoBuscar)){
                return tipo.getIdTipoVehiculo();
            }
        }
        return 0;
    }

}

 