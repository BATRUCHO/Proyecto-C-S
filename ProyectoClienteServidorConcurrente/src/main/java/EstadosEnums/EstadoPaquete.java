package Proyectos.Curso_Cliente_Servidor;

    public enum EstadoPaquete {
        PENDIENTE(1,"El paquete está pendiente de ser recogido"), 
        ASIGNADO(2,"El paquete ha sido asignado a un repartidor"),
        EN_TRANSITO(3,"El paquete está en tránsito"),
        INCIDENCIA(4,"El paquete tiene una incidencia"),
        ENTREGADO(5,"El paquete ha sido entregado");

    private final int codigo;
    private final String descripcion;

    EstadoPaquete(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;

    }
    public int getCodigo() {
        return codigo;
    }
    public String getDescripcion() {
        return descripcion;

    }

    public boolean puedeCambiarA(EstadoPaquete destinEstadoPaquete) {
        if (this == ENTREGADO) {
            return false;
        }
        return switch (this) {
            case PENDIENTE -> destinEstadoPaquete == ASIGNADO || destinEstadoPaquete == INCIDENCIA;  // -> operador lambda, usado en expresiones switch expresivas
            case ASIGNADO -> destinEstadoPaquete == EN_TRANSITO || destinEstadoPaquete == INCIDENCIA;
            case EN_TRANSITO -> destinEstadoPaquete == ENTREGADO || destinEstadoPaquete == INCIDENCIA;
            case INCIDENCIA -> destinEstadoPaquete == PENDIENTE || destinEstadoPaquete == ASIGNADO;
            default -> false;
        
        };
    }
}