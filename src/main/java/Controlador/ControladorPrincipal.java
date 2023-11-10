package Controlador;

import Vista.VistaMensaje;
import Vista.VistaPorDefecto;
import Vista.VistaPrincipal;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 *
 * @author Juan Alberto Dominguez Vazquez
 */
public class ControladorPrincipal implements ActionListener {

    private VistaMensaje vMensaje;
    private VistaPorDefecto vPorDefecto;
    private VistaPrincipal vPrincipal;

    public ControladorPrincipal(VistaMensaje vMensaje)
    {
        this.vMensaje = vMensaje;
        this.vPorDefecto = new VistaPorDefecto();
        this.vPrincipal = new VistaPrincipal();
        
        addListeners();

        vPrincipal.setLayout(new CardLayout());
        
        this.vPrincipal.add(vPorDefecto);   
        vPrincipal.setLocationRelativeTo(null); //Para que la ventana se muestre en el centro de la pantalla
        vPrincipal.setVisible(true); // Para hacer la ventana visible
        vPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Para que la ventana se cierra cuando le doy a cerrar
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        
    }
    
    private void addListeners() {
        //this.vPrincipal.jMenuConsultas.addActionListener(this);
        //this.vPrincipal.jMenuDocumentos.addActionListener(this);
        this.vPrincipal.jMenuItemCerrarServidor.addActionListener(this);
        this.vPrincipal.jMenuItemDesconectar.addActionListener(this);
        this.vPrincipal.jMenuItemIndexar.addActionListener(this);
        this.vPrincipal.jMenuItemMostrarIndexados.addActionListener(this);
        this.vPrincipal.jMenuItemRealizarConsulta.addActionListener(this);
        this.vPrincipal.jMenuItemSalir.addActionListener(this);
    }
}
