// Define la interfaz gráfica de usuario (GUI) de la calculadora.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Ventana extends JFrame implements ActionListener {

    private JTextField display; // Muestra entradas y resultados.
    private JTextArea historyDisplay; // Muestra el historial de operaciones.
    private CalculadoraCientifica calculadora; // Lógica de la calculadora.

    private StringBuilder currentNumberString; // Número actual tecleado.
    private double lastResult; // Resultado de la última operación '='.
    private boolean isResultDisplayed; // Si el display muestra un resultado final.

    // Nuevas variables para la funcionalidad de repetir operación
    private String lastOperandString; // Almacena el último número antes del operador
    private String lastOperator;      // Almacena el último operador
    private boolean isRepeatOperation; // Bandera para saber si estamos en modo de repetición

    public Ventana() {
        // Configuración de la ventana.
        setTitle("Calculadora");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        calculadora = new CalculadoraCientifica();
        currentNumberString = new StringBuilder();
        lastResult = 0;
        isResultDisplayed = true;

        // Inicializar las nuevas variables
        lastOperandString = "";
        lastOperator = "";
        isRepeatOperation = false;

        // Configuración del display principal.
        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Inter", Font.PLAIN, 32));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        // Panel contenedor para el historial y los botones
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Configuración del panel de historial.
        historyDisplay = new JTextArea();
        historyDisplay.setEditable(false);
        historyDisplay.setFont(new Font("Inter", Font.PLAIN, 14));
        historyDisplay.setLineWrap(true);
        historyDisplay.setWrapStyleWord(true);
        JScrollPane historyScrollPane = new JScrollPane(historyDisplay);
        historyScrollPane.setPreferredSize(new Dimension(380, 100));
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Historial de Operaciones"));
        centerPanel.add(historyScrollPane, BorderLayout.NORTH); // Agrega el historial en la parte superior del centerPanel

        // Panel para los botones.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 5, 5, 5));

        // Etiquetas de los botones (modificadas para agrupar paréntesis)
        String[] buttonLabels = {
                "C", "CE", "%", "/", "sqrt",
                "sin", "cos", "tan", "ln", "log",
                "7", "8", "9", "*", "^",
                "4", "5", "6", "-", "!",
                "1", "2", "3", "+", "(",
                "0", ".", "=", ")", "pi",
                "e", "asin", "acos", "atan", "sinh", "cosh"
        };


        // Creación y adición de botones.
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Inter", Font.PLAIN, 20));
            button.addActionListener(this);
            button.setFocusPainted(false);
            button.setBackground(new Color(240, 240, 240));
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

            // Estilos específicos para algunos botones.
            switch (label) {
                case "=":
                    button.setBackground(new Color(78, 156, 206));
                    button.setForeground(Color.WHITE);
                    break;
                case "+": case "-": case "*": case "/": case "^":
                    button.setBackground(new Color(255, 165, 0));
                    button.setForeground(Color.WHITE);
                    break;
                case "C": case "CE":
                    button.setBackground(new Color(220, 50, 50));
                    button.setForeground(Color.WHITE);
                    break;
                case "sin": case "cos": case "tan": case "ln": case "log":
                case "sqrt": case "!": case "pi": case "e":
                case "asin": case "acos": case "atan": case "sinh": case "cosh":
                case "(": case ")": // Agregamos los paréntesis para el estilo
                    button.setBackground(new Color(100, 180, 100));
                    button.setForeground(Color.WHITE);
                    break;
            }
            buttonPanel.add(button);
        }

        centerPanel.add(buttonPanel, BorderLayout.CENTER); // Agrega el buttonPanel en el centro del centerPanel
        add(centerPanel, BorderLayout.CENTER); // Agrega el centerPanel completo al centro de la Ventana
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
                // Maneja dígitos y punto decimal.
                if (isResultDisplayed || isRepeatOperation) { // Si hay un resultado o estamos en modo repetición, empezar un número nuevo
                    display.setText(command);
                    currentNumberString.setLength(0);
                    currentNumberString.append(command);
                    isResultDisplayed = false;
                    isRepeatOperation = false; // Nueva entrada, salir del modo repetición
                } else {
                    if (command.equals(".") && currentNumberString.indexOf(".") != -1) {
                        return;
                    }
                    display.setText(display.getText() + command);
                    currentNumberString.append(command);
                }
            } else if (command.equals("C")) {
                // Borra todo.
                display.setText("0");
                historyDisplay.setText("");
                currentNumberString.setLength(0);
                lastResult = 0;
                isResultDisplayed = true;
                lastOperandString = "";
                lastOperator = "";
                isRepeatOperation = false;
                calculadora.clear();
            } else if (command.equals("CE")) {
                // Borra la última entrada.
                if (currentNumberString.length() > 0) {
                    String currentDisplayText = display.getText();
                    String numToRemove = currentNumberString.toString();
                    int lastIndex = currentDisplayText.lastIndexOf(numToRemove);
                    if (lastIndex != -1) {
                        display.setText(currentDisplayText.substring(0, lastIndex));
                    }
                    currentNumberString.setLength(0);
                    if (display.getText().isEmpty()) {
                        display.setText("0");
                        isResultDisplayed = true;
                        isRepeatOperation = false;
                    }
                } else if (isResultDisplayed) {
                    display.setText("0");
                    lastResult = 0;
                    isResultDisplayed = true;
                    isRepeatOperation = false;
                }
            } else if (command.equals("=")) {
                String expressionToEvaluate;
                double result;

                if (isRepeatOperation && !lastOperator.isEmpty() && !lastOperandString.isEmpty()) {
                    // Si estamos en modo repetición y hay una última operación válida
                    expressionToEvaluate = String.valueOf(lastResult) + lastOperator + lastOperandString;
                    result = calculadora.evaluateExpression(expressionToEvaluate);
                    historyDisplay.append(String.valueOf(lastResult) + lastOperator + lastOperandString + " = " + String.valueOf(result) + "\n");

                } else {
                    // Primera vez que se presiona '=' o se inició una nueva operación
                    expressionToEvaluate = display.getText();
                    result = calculadora.evaluateExpression(expressionToEvaluate);

                    // Almacenar la última operación para posibles repeticiones
                    if (currentNumberString.length() > 0) { // Si hay un número actual, significa que el último elemento fue un número
                        lastOperandString = currentNumberString.toString();
                        // Intentar extraer el último operador de la expresión si hay uno
                        // Esto es una simplificación; un parser más robusto sería mejor.
                        // Para operadores binarios simples:
                        int lastOpIndex = -1;
                        if (expressionToEvaluate.contains("+")) lastOpIndex = Math.max(lastOpIndex, expressionToEvaluate.lastIndexOf('+'));
                        if (expressionToEvaluate.contains("-")) lastOpIndex = Math.max(lastOpIndex, expressionToEvaluate.lastIndexOf('-'));
                        if (expressionToEvaluate.contains("*")) lastOpIndex = Math.max(lastOpIndex, expressionToEvaluate.lastIndexOf('*'));
                        if (expressionToEvaluate.contains("/")) lastOpIndex = Math.max(lastOpIndex, expressionToEvaluate.lastIndexOf('/'));
                        if (expressionToEvaluate.contains("^")) lastOpIndex = Math.max(lastOpIndex, expressionToEvaluate.lastIndexOf('^'));

                        if (lastOpIndex != -1) {
                            lastOperator = String.valueOf(expressionToEvaluate.charAt(lastOpIndex));
                        } else {
                            lastOperator = ""; // No hay operador binario simple para repetir
                        }
                    } else {
                        lastOperandString = String.valueOf(lastResult); // Si no hay número actual, el último operando es el resultado anterior
                        lastOperator = ""; // No se puede repetir sin un operador previo
                    }
                    isRepeatOperation = true; // Activar el modo repetición
                    historyDisplay.append(expressionToEvaluate + " = " + String.valueOf(result) + "\n");
                }

                display.setText(String.valueOf(result));
                lastResult = result;
                currentNumberString.setLength(0); // Limpiar para la próxima entrada de número
                currentNumberString.append(String.valueOf(result)); // Establecer el resultado como el número actual para futuras operaciones
                isResultDisplayed = true;

            } else if (command.equals("pi") || command.equals("e") || isFunction(command)) {
                // Maneja constantes y funciones científicas.
                // Estas operaciones no inician un modo de repetición de la última operación binaria.
                // Simplemente se aplican al valor actual o se insertan.
                isRepeatOperation = false;
                lastOperator = ""; // Limpiar el último operador ya que la operación no es binaria simple
                
                if (command.equals("pi")) {
                    if (isResultDisplayed || display.getText().equals("0")) {
                        display.setText(String.valueOf(Math.PI));
                    } else {
                        display.setText(display.getText() + String.valueOf(Math.PI));
                    }
                    currentNumberString.setLength(0);
                    currentNumberString.append(String.valueOf(Math.PI));
                    isResultDisplayed = false;
                } else if (command.equals("e")) {
                    if (isResultDisplayed || display.getText().equals("0")) {
                        display.setText(String.valueOf(Math.E));
                    } else {
                        display.setText(display.getText() + String.valueOf(Math.E));
                    }
                    currentNumberString.setLength(0);
                    currentNumberString.append(String.valueOf(Math.E));
                    isResultDisplayed = false;
                } else if (isFunction(command)) {
                    double valueToOperateOn;
                    if (currentNumberString.length() > 0 && !isResultDisplayed) {
                        valueToOperateOn = Double.parseDouble(currentNumberString.toString());
                    } else {
                        // Si no hay un número actual tecleado, toma el valor actual del display
                        valueToOperateOn = Double.parseDouble(display.getText());
                    }

                    double result = 0;
                    switch (command) {
                        case "sqrt": result = calculadora.getSqrt(valueToOperateOn); break;
                        case "!": result = calculadora.getFactorial((int) valueToOperateOn); break;
                        case "%": result = calculadora.getPercentage(valueToOperateOn); break;
                        case "sin": result = calculadora.getSin(valueToOperateOn); break;
                        case "cos": result = calculadora.getCos(valueToOperateOn); break;
                        case "tan": result = calculadora.getTan(valueToOperateOn); break;
                        case "ln": result = calculadora.getLn(valueToOperateOn); break;
                        case "log": result = calculadora.getLog(valueToOperateOn); break;
                        case "asin": result = calculadora.getASin(valueToOperateOn); break;
                        case "acos": result = calculadora.getACos(valueToOperateOn); break;
                        case "atan": result = calculadora.getATan(valueToOperateOn); break;
                        case "sinh": result = calculadora.getSinh(valueToOperateOn); break;
                        case "cosh": result = calculadora.getCosh(valueToOperateOn); break;
                    }
                    display.setText(String.valueOf(result));
                    lastResult = result;
                    currentNumberString.setLength(0);
                    currentNumberString.append(String.valueOf(result));
                    isResultDisplayed = true;
                }
            } else if (command.equals("(") || command.equals(")")) {
                // Maneja paréntesis. No inician modo de repetición.
                isRepeatOperation = false;
                lastOperator = ""; // Limpiar el último operador

                if (command.equals("(")) {
                    if (isResultDisplayed) {
                        display.setText("(");
                    } else {
                        display.setText(display.getText() + "(");
                    }
                } else { // command.equals(")")
                    if (isResultDisplayed && display.getText().equals("0")) {
                        return; // Evita añadir un ')' a un '0' vacío
                    }
                    display.setText(display.getText() + ")");
                }
                currentNumberString.setLength(0); // Limpiar currentNumberString para el contenido dentro/después del paréntesis
                isResultDisplayed = false;

            } else { // Operadores binarios (+, -, *, /, ^)
                // Almacenar el número actual y el operador para posibles repeticiones
                if (currentNumberString.length() > 0) {
                    lastOperandString = currentNumberString.toString();
                } else if (isResultDisplayed) {
                    // Si el display ya muestra un resultado y no se ha tecleado un nuevo número
                    // el lastOperand es el resultado anterior (útil para encadenar operaciones)
                    lastOperandString = String.valueOf(lastResult);
                } else {
                    // Si no hay currentNumberString ni resultado, no hay un operando claro para repetir
                    lastOperandString = "";
                }
                lastOperator = command;
                isRepeatOperation = false; // Al presionar un nuevo operador, se sale del modo repetición

                if (isResultDisplayed) {
                    display.setText(String.valueOf(lastResult) + command);
                } else {
                    display.setText(display.getText() + command);
                }
                currentNumberString.setLength(0); // Reset currentNumberString para el siguiente número
                isResultDisplayed = false;
            }
        } catch (NumberFormatException ex) {
            display.setText("Error: Formato");
            currentNumberString.setLength(0);
            currentNumberString.append("0");
            isResultDisplayed = true;
            isRepeatOperation = false;
            lastOperandString = "";
            lastOperator = "";
        } catch (ArithmeticException ex) {
            display.setText("Error: " + ex.getMessage());
            currentNumberString.setLength(0);
            currentNumberString.append("0");
            isResultDisplayed = true;
            isRepeatOperation = false;
            lastOperandString = "";
            lastOperator = "";
        } catch (IllegalArgumentException ex) {
            display.setText("Error: " + ex.getMessage());
            currentNumberString.setLength(0);
            currentNumberString.append("0");
            isResultDisplayed = true;
            isRepeatOperation = false;
            lastOperandString = "";
            lastOperator = "";
        } catch (Exception ex) {
            display.setText("Error");
            currentNumberString.setLength(0);
            currentNumberString.append("0");
            isResultDisplayed = true;
            isRepeatOperation = false;
            lastOperandString = "";
            lastOperator = "";
            ex.printStackTrace();
        }
    }

    // Verifica si un comando es una función científica.
    private boolean isFunction(String command) {
        return command.equals("sqrt") || command.equals("!") || command.equals("%") ||
                command.equals("sin") || command.equals("cos") || command.equals("tan") ||
                command.equals("ln") || command.equals("log") ||
                command.equals("asin") || command.equals("acos") || command.equals("atan") ||
                command.equals("sinh") || command.equals("cosh");
    }
}