package PaqueteCliente.Utilidades;

import java.util.Collections;
import java.util.Optional;

import Dominio.Paquete;
import Dominio.Usuarios;
import Dominio.Vehiculo;

public class MetodosBusquedaId {

    // 🔒 Constructor privado: Evita que se creen instancias innecesarias en la
    // memoria RAM
    private MetodosBusquedaId() {
        throw new IllegalStateException("Clase utilitaria - No debe ser instanciada");
    }

    public static Optional<Paquete> buscarPaquetePorId(java.util.List<Paquete> lista, int id) {
        return Optional.ofNullable(lista) // Verifica si la lista no es nula
                .orElseGet(Collections::emptyList) // Si es nula, devuelve una lista vacía
                .stream() // Convierte la lista en un flujo de datos
                .filter(p -> p.getId_paquete() == id) // Se recibe una operacion lamda y evalua cada elemento de la
                                                      // lista, usando la condicion mencionada
                .findFirst(); //
    }

    public static Optional<Usuarios> buscarUsuarioPorId(java.util.List<Usuarios> lista, int id) {
        return Optional.ofNullable(lista)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(u -> u.getId_usuario() == id)
                .findFirst();
    }

    public static Optional<Vehiculo> buscarVehiculoPorId(java.util.List<Vehiculo> lista, int id) {
        return Optional.ofNullable(lista)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(v -> v.getId_vehiculo() == id)
                .findFirst();
    }

}
