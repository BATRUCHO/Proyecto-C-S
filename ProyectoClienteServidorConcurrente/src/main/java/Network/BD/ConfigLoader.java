package Network.BD;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigLoader {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("❌ ERROR: No se encontró db.properties en el classpath");
            } else {
                properties.load(input);
                System.out.println("✅ Archivo db.properties cargado correctamente.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();   
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            System.err.println("⚠️ ADVERTENCIA: La llave '" + key + "' no existe en db.properties");
            return null;
        }
        
        if (value.startsWith("${") && value.endsWith("}")) {
            String envKey = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envKey);
            if (envValue == null) {
                System.err.println("❌ ERROR: La variable de entorno '" + envKey + "' no está definida en el sistema.");
            }
            return envValue;
        }
        return value;
    }
}
