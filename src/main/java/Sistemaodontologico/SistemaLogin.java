

package Sistemaodontologico;

import Sistemaodontologico.IGU.Login;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

public class SistemaLogin {

    public static void main(String[] args) {
        FlatMacLightLaf.setup();

        UIManager.put("TextComponent.arc", 15);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Button.arc", 20);
        UIManager.put("Component.arc", 12);
        UIManager.put( "Component.arrowType" , "triangle" );
        UIManager.put( "TabbedPane.selectedBackground" , Color.white );
        UIManager.put("TableHeader.background", new Color(245, 245, 245));
        UIManager.put("TableHeader.foreground", new Color(50, 50, 50));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));
         UIManager.put("TableHeader.height", 35);

        
        // Barras de desplazamiento redondeadas
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
         
        
        Login ventana = new Login();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        
        /*
        Usuario	Contraseña	Roles	Qué verás al entrar
        mrivas	Clinica2026	ADMINISTRADOR y ODONTOLOGO	Aparece el selector para elegir con cuál entrar
        jparedesClinica2026	ODONTOLOGO	Entra directo a la ventana del odontólogo
        kvega	Clinica2026	ODONTOLOGO	Entra directo a la ventana del odontólogo
        rcampos	Clinica2026	RECEPCIONISTA	Entra directo a la ventana de recepción*/
    }
}
