package Dominio;

public enum EstadoVehiculo {
    DISPONIBLE(1,"Disponible"),
    EN_RUTA(2,"En ruta"),
    EN_MANTENIMIENTO(3,"En mantenimiento");

    private final int id_estado_vehiculo;
    private final String descripcion;
        
    EstadoVehiculo(int id_estado_vehiculo, String descripcion) {
        this.id_estado_vehiculo = id_estado_vehiculo;
        this.descripcion = descripcion;
    }
    
    public int getId_estado_vehiculo() {
        return id_estado_vehiculo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    //Metodo analitico para buscar por ID numerico
    public static String obtenerTextoPorId(int idBuscar){
        for(EstadoVehiculo estado : values()) {
            if(estado.getId_estado_vehiculo() == idBuscar)
                return estado.getDescripcion();    
        }
        return "Desconocido";
    }

    public static int obtenerIdPorTexto(String textoBuscar){
        for(EstadoVehiculo estado : values()) {
            if(estado.getDescripcion().equalsIgnoreCase(textoBuscar)){
                return estado.getId_estado_vehiculo();
            }
        }
        return 0;
    }

}
