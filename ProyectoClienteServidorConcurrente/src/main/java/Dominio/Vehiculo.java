package Dominio;

import java.io.Serializable;

public class Vehiculo implements Serializable {
    
    private int id_vehiculo;
    private String placa;
    private String marca;
    private String modelo;
    private int id_tipoVehiculo;
    private int id_estado_vehiculo;
    

    public Vehiculo(int id_vehiculo, String placa, String marca, String modelo, int id_tipoVehiculo, int id_estado_vehiculo) {
        this.id_vehiculo = id_vehiculo;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.id_tipoVehiculo = id_tipoVehiculo;
        this.id_estado_vehiculo = id_estado_vehiculo;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getId_tipoVehiculo() {
        return id_tipoVehiculo;
    }

    public void setId_tipoVehiculo(int id_tipoVehiculo) {
        this.id_tipoVehiculo = id_tipoVehiculo;
    }

    public int getId_estado_vehiculo() {
        return id_estado_vehiculo;
    }

    public void setId_estado_vehiculo(int id_estado_vehiculo) {
        this.id_estado_vehiculo = id_estado_vehiculo;
    }
      
}
