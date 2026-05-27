package Dominio;

import java.io.Serializable;
import java.sql.Timestamp;

public class Asignaciones  implements Serializable{
    private int id_asignacion;
    private Timestamp fecha_asignacion;

    public Asignaciones(int id_asignacion, Timestamp fecha_asignacion) {
        this.id_asignacion = id_asignacion;
        this.fecha_asignacion = fecha_asignacion;
    }

    public int getId_asignacion() {
        return id_asignacion;
    }

    public void setId_asignacion(int id_asignacion) {
        this.id_asignacion = id_asignacion;
    }

    public Timestamp getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(Timestamp fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }

}

