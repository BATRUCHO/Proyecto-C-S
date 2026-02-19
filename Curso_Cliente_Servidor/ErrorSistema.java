package Proyectos.Curso_Cliente_Servidor;

public abstract class ErrorSistema {

    private final String mensaje;
    private final int codigoError;
    private final Throwable causa;
    private static final long serialVersionUID = 1L;

     // Constructor//

    public ErrorSistema(String mensaje, int codigoError, Throwable causa) {
        this.mensaje = mensaje;
        this.codigoError = codigoError;
        this.causa = causa;
        this.causa.initCause(causa);
    }
    
}
