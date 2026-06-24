package PaqueteCliente.Utilidades;

import java.security.MessageDigest; //para encriptar contraseñas.

public class Encriptador {

    private Encriptador() {
    }

    public static String hashSHA256(String textoPlano) {
        if (textoPlano == null || textoPlano.isEmpty()) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(textoPlano.getBytes());

            // Convierte los bytes o arreglo en una cadena hexadecimal String.
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
