package Dominio;

import java.io.Serializable;

public class Conductores extends Usuario implements Serializable {
    private int idConductor;
    private int idVehiculoAsignado; // FK hacia la tabla vehiculos

    // Constructor simplificado para mapeo rápido desde la tabla conductores
// Nuevo constructor simplificado en Dominio.Conductores
public Conductores(int idConductor, String nombre, String cedula, int idUsuario, int idVehiculoAsignado) {
    // Llamamos al constructor de Usuario con lo mínimo necesario
    super(idUsuario, cedula, null, nombre, "", "", "", "", 0); 
    this.idConductor = idConductor;
    this.idVehiculoAsignado = idVehiculoAsignado;
}

    // Getters y Setters
    public int getIdConductor() { return idConductor; }
    public int getIdVehiculoAsignado() { return idVehiculoAsignado; }
    public void setIdVehiculoAsignado(int idVehiculoAsignado) { this.idVehiculoAsignado = idVehiculoAsignado; }
}
