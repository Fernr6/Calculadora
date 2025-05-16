
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
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
        //Etiquetas();
        Botones();
        //RadioBotones();

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
        boton1.setEnabled(true); //Controlar si el boton es utilizable
        boton1.setMnemonic('a'); //Utilizar boton con alt + 'a'
        boton1.setBounds(200, 100, 80, 50); //Posicion y tamaño de boton
        boton1.setForeground(Color.BLUE);//Color de texto en boton
        boton1.setFont(new Font("times new roman", 3, 20)); //Ajustes Font de texto del boton
        
        JButton boton2 = new JButton();
        boton2.setText("Adios");
        boton2.setEnabled(true);
        boton2.setOpaque(true);
        ImageIcon click = new ImageIcon("images.png");
        boton2.setBounds(200, 200, 80, 50);
        boton2.setIcon(new ImageIcon(click.getImage().getScaledInstance(boton2.getWidth(), boton2.getHeight(), Image.SCALE_SMOOTH)));
        
        panel.add(boton1);
        panel.add(boton2);
    }

    private void RadioBotones(){
        JRadioButton radio1 = new JRadioButton("Opcion 1", false);
        radio1.setBounds(50,100,100,50);

        JRadioButton radio2 = new JRadioButton("Opcion 2", false);
        radio2.setBounds(50,135,100,50);

        JRadioButton radio3 = new JRadioButton("Opcion 3", false);
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
