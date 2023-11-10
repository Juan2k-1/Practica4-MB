package juan.motores_busqueda.practica4_mb;

import Controlador.ControladorLogin;
import com.formdev.flatlaf.FlatDarkLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author juald
 */
public class Practica4_MB {

    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(new FlatDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            // If Nimbus is not available, you can set the GUI to another look and feel
            Logger.getLogger(Practica4_MB.class.getName()).log(Level.SEVERE, null, e);
        }
        ControladorLogin c = new ControladorLogin();
    }
}
