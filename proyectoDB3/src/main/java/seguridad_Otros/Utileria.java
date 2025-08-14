package seguridad_Otros;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

public class Utileria {

    public static String encriptarContrasena(String contrasena){
        return BCrypt.hashpw(contrasena, BCrypt.gensalt());
    }

    public static boolean validarContrasena(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
