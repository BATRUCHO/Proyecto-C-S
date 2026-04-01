package DAO;

import Conexion.Usuario;
import Dominio.EstadoVehiculo;
import Dominio.TipoVehiculo;
import Dominio.Vehiculo;
import java.time.LocalDateTime;
import Dominio.Paquete;
import Conexion.PaqueteDAO;
import Conexion.PaqueteDAOImpl;

public class TestUsuarioDAO {

public static void main(String[] args) {
     UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

     Usuario nuevoUsuario = new Usuario(
             "Felipe Gonzalez",
             "felipe@gmail.com",
             "1234",
             "Administrador"
        );
 boolean guardado = usuarioDAO.guardarUsuario(nuevoUsuario);

        if (guardado) {
            System.out.println("Prueba de guardado de usuario exitosa.");
        } else {
            System.out.println("No se pudo guardar el usuario.");
        }

 Usuario usuarioBuscado = usuarioDAO.buscarUsuarioPorCorreo("felipe@gmail.com");

        if (usuarioBuscado != null) {
            System.out.println("Usuario recuperado desde la base de datos:");
            System.out.println(usuarioBuscado);
        } else {
            System.out.println("No se encontró el usuario.");
        }

   
 VehiculoDAO vehiculoDAO = new VehiculoDAOImpl();

  Vehiculo nuevoVehiculo = new Vehiculo(
   0,
   TipoVehiculo.CAMION,
   "ABC123",LocalDateTime.now(), EstadoVehiculo.DISPONIBLE,true,LocalDateTime.now(),"San Jose"
        ) {
            @Override
            public double getCapacidadMaximaKG() {
                return 1500.0;
            }
            @Override
            public double getVelocidadPromedioKmH() {
                return 80.0;
            }
        };

 boolean vehiculoGuardado = vehiculoDAO.guardarVehiculo(nuevoVehiculo);

        if (vehiculoGuardado) {
            System.out.println("Prueba de guardado de vehiculo exitosa.");
        } else {
            System.out.println("No se pudo guardar el vehiculo.");
        }

 Vehiculo vehiculoBuscado = vehiculoDAO.buscarVehiculoPorPlaca("ABC123");

        if (vehiculoBuscado != null) {
            System.out.println("Vehiculo recuperado desde la base de datos:");
            System.out.println("ID: " + vehiculoBuscado.getIdVehiculo());
            System.out.println("Placa: " + vehiculoBuscado.getPlaca());
            System.out.println("Tipo: " + vehiculoBuscado.getTipovehiculo());
            System.out.println("Estado: " + vehiculoBuscado.getEstadoVehiculo());
            System.out.println("Activo: " + vehiculoBuscado.getActivo());
            System.out.println("Ubicacion: " + vehiculoBuscado.getUbicacionActual());
        } else {
            System.out.println("No se encontró el vehiculo.");
        }
    
    
    
PaqueteDAO paqueteDAO = new PaqueteDAOImpl();

Paquete nuevoPaquete = new Paquete(
        1,
        "Heredia Centro",
        "San Jose Centro",
        2.5,
        3500.0,
        "Documentos"
);

boolean paqueteGuardado = paqueteDAO.guardarPaquete(nuevoPaquete);

if (paqueteGuardado) {
    System.out.println("Prueba de guardado de paquete exitosa.");
} else {
    System.out.println("No se pudo guardar el paquete.");
}

Paquete paqueteBuscado = paqueteDAO.buscarPaquetePorContenido("Documentos");

if (paqueteBuscado != null) {
    System.out.println("Paquete recuperado desde la base de datos:");
    System.out.println("ID: " + paqueteBuscado.getIdPaquete());
    System.out.println("Fecha creación: " + paqueteBuscado.getFechaCreacion());
    System.out.println("Origen: " + paqueteBuscado.getDireccionOrigen());
    System.out.println("Destino: " + paqueteBuscado.getDireccionDestino());
    System.out.println("Peso: " + paqueteBuscado.getPesoKg());
    System.out.println("Costo envío: " + paqueteBuscado.getCostoEnvio());
    System.out.println("Estado: " + paqueteBuscado.getEstado());
    System.out.println("Contenido: " + paqueteBuscado.getContenido());
} else {
    System.out.println("No se encontró el paquete.");
}
}
}
