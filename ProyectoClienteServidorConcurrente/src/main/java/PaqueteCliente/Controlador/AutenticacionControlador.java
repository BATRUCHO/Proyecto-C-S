package PaqueteCliente.Controlador;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Dominio.Excepciones.MensajeRed;
import Dominio.Usuario;
import PaqueteCliente.ModeloRed.ClienteSocket;
import PaqueteCliente.Vista.FrmDashboardAdmin;
import PaqueteCliente.Vista.FrmDashboardConductor;


public class AutenticacionControlador {

    private ClienteSocket clienteSocket;

   public void iniciarSesion(String email, String password, JFrame vistaLogin) {
        // 1. Creamos un usuario temporal con las credenciales
        Usuario loginUser = new Usuario(0, "", new java.sql.Date(0), "", "", email, "", password, 0);

        // 2. Empaquetamos en el MensajeRed que ya definiste
        MensajeRed peticion = new MensajeRed("LOGIN", loginUser, true, "");

        // 3. Enviamos a través de tu clase ClienteSocket (usando Singleton)
        MensajeRed respuesta = clienteSocket.getInstancia().enviarPeticion(peticion);

        if (respuesta.isEstadoExito()) {
            Usuario usuarioLogueado = (Usuario) respuesta.getPayload();
            redirigirPorRol(usuarioLogueado, vistaLogin);
        } else {
            JOptionPane.showMessageDialog(vistaLogin, respuesta.getMensajeRespuesta(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redirigirPorRol(Usuario user, JFrame vistaLogin) {
        vistaLogin.dispose(); // Cerramos el login
        
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
