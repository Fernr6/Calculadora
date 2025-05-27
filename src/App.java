import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Ejecuta la GUI en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new Ventana().setVisible(true));
    }
}