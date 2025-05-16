
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
        Etiquetas();
        Botones();
        RadioBotones();

    }
    
    private void Paneles(){
        
        panel = new JPanel(); //Creacion de panel
        panel.setLayout( null); //Desactivar diseño
        this.getContentPane().add(panel); //agregar panel a ventana

    }
    
    private void Etiquetas(){

        JLabel etiqueta = new JLabel("Lenguajes:", SwingConstants.CENTER); //Crear etiqueta (texto, alineacion horizontal)
    
        etiqueta.setBounds(40,80,100,20); //Posicion de etiqueta
    
        //etiqueta.setForeground(Color.BLACK); //Color de letra de etiqueta
        etiqueta.setOpaque(false); //Establecer fondo de etiqueta
        etiqueta.setBackground(Color.BLACK); //Color de fondo de etiqueta
        //etiqueta.setFont(new Font("times new roman",Font.BOLD,30)); //Fuente de texto
    
        panel.add(etiqueta); //Agregar etiqueta

    }

    private void Botones(){
        JButton boton1 = new JButton();
        boton1.setText("Validar");
        boton1.setEnabled(true); //Controlar si el boton es utilizable
        boton1.setMnemonic('a'); //Utilizar boton con alt + 'a'
        boton1.setBounds(50, 220, 80, 50); //Posicion y tamaño de boton
        boton1.setForeground(Color.BLUE);//Color de texto en boton
        //boton1.setFont(new Font("times new roman", 3, 20)); //Ajustes Font de texto del boton
                
        panel.add(boton1);
    }

    private void RadioBotones(){
        JRadioButton radio1 = new JRadioButton("Java", false);
        radio1.setBounds(50,100,100,50);

        JRadioButton radio2 = new JRadioButton("PHP", false);
        radio2.setBounds(50,135,100,50);

        JRadioButton radio3 = new JRadioButton("C++", false);
        radio3.setBounds(50,165,100,50);

        panel.add(radio1);
        panel.add(radio2);
        panel.add(radio3);

        ButtonGroup radBotones = new ButtonGroup();
        radBotones.add(radio1);
        radBotones.add(radio2);
        radBotones.add(radio3);

    }

}
