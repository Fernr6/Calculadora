
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Ventana extends JFrame {

    public JPanel panel;

    public Ventana(){
        setSize(500,700); //Tamaño de ventana
        setLocationRelativeTo(null); //Colocar ventana en el centro al abrir programa
        setMinimumSize(new Dimension(300,500)); //Tamaño minimo de ventana

        setDefaultCloseOperation(EXIT_ON_CLOSE); //Cerrar programa al cerrar ventana
        setTitle("Calculadora"); //Titulo ventana

        initComponentes(); 
    }

    private void initComponentes(){
        Paneles();
        //Etiquetas();
        Botones();
        
    }
    
    private void Paneles(){
        
        panel = new JPanel(); //Creacion de panel
        panel.setLayout( null); //Desactivar diseño
        this.getContentPane().add(panel); //agregar panel a ventana

    }
    
    private void Etiquetas(){

        JLabel etiqueta = new JLabel("Hola", SwingConstants.CENTER); //Crear etiqueta (texto, alineacion horizontal)
    
        etiqueta.setBounds(10,10,50,20); //Posicion de etiqueta
    
        etiqueta.setForeground(Color.WHITE); //Color de letra de etiqueta
        etiqueta.setOpaque(true); //Establecer fondo de etiqueta
        etiqueta.setBackground(Color.BLACK); //Color de fondo de etiqueta
        etiqueta.setFont(new Font("times new roman",Font.BOLD,30)); //Fuente de texto
    
        panel.add(etiqueta); //Agregar etiqueta

    }

    private void Botones(){
        JButton boton1 = new JButton();
        boton1.setText("Hola");
        boton1.setBounds(100, 100, 100, 40);
        boton1.setEnabled(true); //Controlar si el boton es utilizable
        boton1.setMnemonic('a'); //Utilizar boton con alt + 'a'
        panel.add(boton1);
    }

}
