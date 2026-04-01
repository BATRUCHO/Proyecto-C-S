package Servicio;

import DAO.UsuarioDAO;

public class AutentificadorServicio {
//ATRIBUTO usuarioDAO // Referencia al DAO
    private UsuarioDAO usuarioDAO;

//    CONSTRUCTOR(dao):
    public AutentificadorServicio(UsuarioDAO dao) {
        this.usuarioDAO = dao;
    }
//    FUNCION validarCredenciales(email, password):
    public Usuario validarCredenciales(String email, String password) {
//        usuario = usuarioDAO.buscarPorEmail(email)
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
//        SI usuario NO ES NULO Y usuario.password == password:
        if (usuario != null && usuario.getPassword().equals(password)) {
            if (usuario.getRol().isActivo()) {
                return usuario;
            } else {
                throw new RuntimeException("Usuario desactivado");
            }
        }
        return null;
    }     
     
}
