// Clase principal para iniciar la aplicación.

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Ejecuta la GUI en el Event Dispatch Thread.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crea y muestra la ventana de la calculadora.
                new Ventana().setVisible(true);
            }
        });
    }
}