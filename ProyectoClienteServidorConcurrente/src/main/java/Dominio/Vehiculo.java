package Dominio;

import java.io.Serializable;

public class Vehiculo implements Serializable {
    
    private int id_vehiculo;
    private String placa;
    private String marca;
    private String modelo;
    

    public Vehiculo(int id_vehiculo, String placa, String marca, String modelo) {
        this.id_vehiculo = id_vehiculo;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
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
      
}
