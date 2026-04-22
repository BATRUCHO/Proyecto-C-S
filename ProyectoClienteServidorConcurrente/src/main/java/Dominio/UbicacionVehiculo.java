package Dominio;

import java.io.Serializable;
import java.sql.Timestamp;

public class UbicacionVehiculo implements Serializable {
    private int id_ubicacion;
    private int id_vehiculo;
    private double latitud;
    private double longitud;
    private Timestamp fecha_hora;

public UbicacionVehiculo(int idUbicacion, int idVehiculo, double latitud, double longitud, Timestamp fechaHora){
    this.id_ubicacion = idUbicacion;
    this.id_vehiculo = idVehiculo;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fecha_hora = fechaHora;

}

public int getId_ubicacion() {
    return id_ubicacion;
}

public void setId_ubicacion(int id_ubicacion) {
    this.id_ubicacion = id_ubicacion;
}

public int getId_vehiculo() {
    return id_vehiculo;
}

public void setId_vehiculo(int id_vehiculo) {
    this.id_vehiculo = id_vehiculo;
}

public double getLatitud() {
    return latitud;
}

public void setLatitud(double latitud) {
    this.latitud = latitud;
}

public double getLongitud() {
    return longitud;
}

public void setLongitud(double longitud) {
    this.longitud = longitud;
}

public Timestamp getFecha_hora() {
    return fecha_hora;
}

public void setFecha_hora(Timestamp fecha_hora) {
    this.fecha_hora = fecha_hora;
}


}
