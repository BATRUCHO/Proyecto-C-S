package protocolo;
import java.io.Serializable;

public class MensajeProtocolo implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TipoOperacion {
        LOGIN,
        REGISTRAR_PAQUETE,
        ACTUALIZAR_ESTADO,
        UBICACION_VEHICULO
    }

    private TipoOperacion tipo;
    private Object datos;

    public MensajeProtocolo(TipoOperacion tipo, Object datos) {
        this.tipo = tipo;
        this.datos = datos;
    }

    public TipoOperacion getTipo() {
        return tipo;
    }

    public Object getDatos() {
        return datos;
    }

    @Override
    public String toString() {
        return "Tipo: " + tipo + " | Datos: " + datos;
    }
}
