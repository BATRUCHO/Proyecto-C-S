package Dominio;

public enum EstadoVehiculo {
    DISPONIBLE("Disponible"),
    EN_RUTA("En ruta"),
    EN_MANTENIMIENTO("En mantenimiento");

    private final String texto;
        
    EstadoVehiculo(String texto) {
        this.texto = texto;
        }
        
        public String getTexto() {
            return texto;
        }
        
}
