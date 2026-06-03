package PaqueteCliente.Controlador;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Dominio.Excepciones.MensajeRed;
import Dominio.Usuarios;
import PaqueteCliente.ModeloRed.ClienteSocket;
import PaqueteCliente.Vista.FrmDashboardAdmin;
import PaqueteCliente.Vista.FrmDashboardConductor;


public class AutenticacionControlador {

    private ClienteSocket clienteSocket;

    //--------------MetodosControlLogin----------------//


   @SuppressWarnings("static-access")
    public void iniciarSesion(String email, String password, JFrame vistaLogin) {
        // 1. Creamos un usuario temporal con las credenciales
        Usuarios loginUser = new Usuarios(0, "", "", null, "", email, "", password, 0);

        // 2. Empaquetamos en el MensajeRed que ya definiste
        MensajeRed peticion = new MensajeRed("LOGIN", loginUser, true, "");

        // 3. Enviamos a través de tu clase ClienteSocket (usando Singleton)
        MensajeRed respuesta = clienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            Usuarios usuarioLogueado = (Usuarios) respuesta.getPayload();
            redirigirPorRol(usuarioLogueado, vistaLogin);
        } else {
            JOptionPane.showMessageDialog(vistaLogin, respuesta.getMensajeRespuesta(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean cerrarSesion(int idUsuario) {
        // Se crea la peticion de logout
        MensajeRed peticion = new MensajeRed("LOGOUT", idUsuario, true, "");
        // Enviamos a través de tu clase ClienteSocket (usando Singleton)
        MensajeRed respuesta = ClienteSocket.getInstancia().enviarPeticion(peticion);

        return respuesta.isEstadoExito();

    }

    //--------------MetodosAuxiliares----------------//

    private void redirigirPorRol(Usuarios user, JFrame vistaLogin) {
        // Cerrar la ventana de inicio de sesión
        vistaLogin.dispose(); 
        
        // Redirigir según el rol del usuario
        switch (user.getIdRol()) {
            case 1: // Administrador
                new FrmDashboardAdmin(user).setVisible(true);
                break;
            case 2: // Conductor
                new FrmDashboardConductor(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol no reconocido");
        }
    }







}
