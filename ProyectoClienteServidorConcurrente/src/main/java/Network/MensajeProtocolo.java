package Network;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MensajeProtocolo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private TipoOperacion tipoOperacion;
    private Object datos;
    private LocalDateTime timestamp;

    public MensajeProtocolo(TipoOperacion tipoOperacion, Object datos) {
        this.tipoOperacion = tipoOperacion;
        this.datos = datos;
        this.timestamp = LocalDateTime.now();
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public Object getDatos() {
        return datos;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Mensaje [" + tipoOperacion + "] enviado a las " + timestamp;
    }
}
