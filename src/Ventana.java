import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.*; 

public class Ventana extends JFrame implements ActionListener {

    private JTextField display;
    private JTextArea historyDisplay;
    private CalculadoraCientifica calculadora;

    private StringBuilder currentNumberString;
    private double lastResult;
    private boolean isResultDisplayed;

    private String lastOperandString;
    private String lastOperator;
    private boolean isRepeatOperation;

    private static final Set<Character> BINARY_OPERATORS = Set.of('+', '-', '*', '/', '^');

    public Ventana() {
        setTitle("Calculadora");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color backgroundDark = new Color(30, 30, 30);
        Color panelDark = new Color(45, 45, 45);
        Color textLight = new Color(240, 240, 240);
        Color textAccent = new Color(150, 150, 150);
        Color buttonBgNormal = new Color(60, 60, 60);
        Color buttonFgNormal = textLight;
        Color buttonBorder = new Color(75, 75, 75);

        Color operatorBg = new Color(255, 120, 0);
        Color equalsBg = new Color(0, 120, 180);
        Color clearBg = new Color(180, 50, 50);
        Color scientificBg = new Color(70, 130, 180);

        getContentPane().setBackground(backgroundDark);

        calculadora = new CalculadoraCientifica();
        currentNumberString = new StringBuilder();
        isResultDisplayed = true;

        lastOperandString = "";
        lastOperator = "";
        isRepeatOperation = false;

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Inter", Font.PLAIN, 38));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        display.setBackground(panelDark);
        display.setForeground(textLight);
        add(display, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundDark);

        historyDisplay = new JTextArea();
        historyDisplay.setEditable(false);
        historyDisplay.setFont(new Font("Inter", Font.PLAIN, 16));
        historyDisplay.setLineWrap(true);
        historyDisplay.setWrapStyleWord(true);
        historyDisplay.setBackground(panelDark);
        historyDisplay.setForeground(textAccent);

        JScrollPane historyScrollPane = new JScrollPane(historyDisplay);
        historyScrollPane.setPreferredSize(new Dimension(380, 120));
        historyScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonBorder, 1),
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Historial de Operaciones",
                        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                        new Font("Inter", Font.BOLD, 12), textAccent)
        ));
        historyScrollPane.getVerticalScrollBar().setBackground(panelDark);
        centerPanel.add(historyScrollPane, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(9, 5, 8, 8)); // 9 filas para +/- y DEL
        buttonPanel.setBackground(backgroundDark);

        String[] buttonLabels = {
                "C", "CE", "%", "/", "sqrt",
                "sin", "cos", "tan", "ln", "log",
                "7", "8", "9", "*", "^",
                "4", "5", "6", "-", "!",
                "1", "2", "3", "+", "(",
                "0", ".", "=", ")", "pi",
                "e", "asin", "acos", "atan", "sinh", "cosh", "+/-", "DEL"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Inter", Font.BOLD, 22));
            button.addActionListener(this);
            button.setFocusPainted(false);
            button.setBackground(buttonBgNormal);
            button.setForeground(buttonFgNormal);
            button.setBorder(BorderFactory.createLineBorder(buttonBorder, 1, true));

            switch (label) {
                case "=":
                    button.setBackground(equalsBg);
                    button.setForeground(Color.WHITE);
                    break;
                case "+": case "-": case "*": case "/": case "^":
                    button.setBackground(operatorBg);
                    button.setForeground(Color.WHITE);
                    break;
                case "C": case "CE": case "DEL": 
                    button.setBackground(clearBg);
                    button.setForeground(Color.WHITE);
                    break;
                case "sin": case "cos": case "tan": case "ln": case "log":
                case "sqrt": case "!": case "pi": case "e":
                case "asin": case "acos": case "atan": case "sinh": case "cosh":
                case "(": case ")": case "%": case "+/-": // +/- tambien es una función científica/manipuladora
                    button.setBackground(scientificBg);
                    button.setForeground(Color.WHITE);
                    break;
                case ".":
                    button.setBackground(buttonBgNormal);
                    button.setForeground(buttonFgNormal);
                    break;
            }
            buttonPanel.add(button);
        }

        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void handleError(String message) {
        display.setText("Error: " + message);
        currentNumberString.setLength(0);
        currentNumberString.append("0");
        isResultDisplayed = true;
        isRepeatOperation = false;
        lastOperandString = "";
        lastOperator = "";
    }

    private void resetCalculatorState() {
        currentNumberString.setLength(0);
        lastResult = 0;
        isResultDisplayed = true;
        lastOperandString = "";
        lastOperator = "";
        isRepeatOperation = false;
        calculadora.clear();
    }

    private void updateDisplayWithResult(double result) {
        display.setText(String.valueOf(result));
        lastResult = result;
        currentNumberString.setLength(0);
        currentNumberString.append(String.valueOf(result));
        isResultDisplayed = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
                if (command.equals(".") && display.getText().contains(".") && (isResultDisplayed || isRepeatOperation || currentNumberString.length() > 0)) {
                    return;
                }

                if (isResultDisplayed || isRepeatOperation || display.getText().equals("0")) {
                    display.setText(command);
                    currentNumberString.setLength(0);
                    currentNumberString.append(command);
                    isResultDisplayed = false;
                    isRepeatOperation = false;
                } else {
                    display.setText(display.getText() + command);
                    currentNumberString.append(command);
                }
            } else if (command.equals("C")) {
                display.setText("0");
                historyDisplay.setText("");
                resetCalculatorState();
            } else if (command.equals("CE")) {
                 if (currentNumberString.length() > 0) {
                    // Borra el número actual completo
                    String currentDisplayText = display.getText();
                    String numToRemove = currentNumberString.toString();
                    int lastIndex = currentDisplayText.lastIndexOf(numToRemove);
                    if (lastIndex != -1) {
                        display.setText(currentDisplayText.substring(0, lastIndex));
                    }
                    currentNumberString.setLength(0);
                    if (display.getText().isEmpty() || display.getText().equals("(")) {
                        display.setText("0");
                        isResultDisplayed = true;
                        isRepeatOperation = false;
                    }
                } else if (isResultDisplayed) {
                    // Si se muestra un resultado, resetea a 0
                    display.setText("0");
                    resetCalculatorState();
                } else {
                    // Si no hay currentNumberString y no es resultado, borra el ultimo char del display
                    String currentDisplayText = display.getText();
                    if (currentDisplayText.length() > 0) {
                        display.setText(currentDisplayText.substring(0, currentDisplayText.length() - 1));
                    }
                    if (display.getText().isEmpty()) {
                        display.setText("0");
                        isResultDisplayed = true;
                    }
                }
            } else if (command.equals("DEL")) {
                if (isResultDisplayed) {
                    display.setText("0"); // Si es un resultado, resetea a 0.
                    resetCalculatorState();
                    historyDisplay.setText(""); // Opcional: limpiar historial al borrar resultado.
                } else if (display.getText().length() > 0 && !display.getText().equals("0")) {
                    String currentText = display.getText();
                    display.setText(currentText.substring(0, currentText.length() - 1));
                    if (currentNumberString.length() > 0) {
                        currentNumberString.setLength(currentNumberString.length() - 1); // Borra del número actual
                    }
                    if (display.getText().isEmpty()) {
                        display.setText("0");
                        isResultDisplayed = true;
                    }
                }
                isRepeatOperation = false;
            } else if (command.equals("+/-")) { // Nuevo: cambiar signo
                if (currentNumberString.length() > 0) {
                    double num = Double.parseDouble(currentNumberString.toString());
                    num *= -1;
                    currentNumberString.setLength(0);
                    currentNumberString.append(String.valueOf(num));

                    // Actualizar el display para reflejar el cambio en el numero actual
                    String currentDisplayText = display.getText();
                    // Buscar el número para reemplazarlo.
                    int lastNumStart = currentDisplayText.lastIndexOf(num < 0 ? String.valueOf(num).substring(1) : String.valueOf(-num));
                    if (lastNumStart != -1 && (lastNumStart + String.valueOf(num).length() == currentDisplayText.length())) {
                        display.setText(currentDisplayText.substring(0, lastNumStart) + String.valueOf(num));
                    } else {
                        // Si no se pudo reemplazar en contexto, se muestra el numero cambiado.
                        display.setText(String.valueOf(num));
                    }
                } else if (isResultDisplayed) {
                    lastResult *= -1;
                    updateDisplayWithResult(lastResult);
                } else {

                }
                isRepeatOperation = false;
            } else if (command.equals("=")) {
                String expressionToEvaluate;
                double result;

                if (isRepeatOperation && !lastOperator.isEmpty() && !lastOperandString.isEmpty()) {
                    expressionToEvaluate = String.valueOf(lastResult) + lastOperator + lastOperandString;
                    result = calculadora.evaluateExpression(expressionToEvaluate);
                    historyDisplay.append(String.valueOf(lastResult) + lastOperator + lastOperandString + " = " + String.valueOf(result) + "\n");
                } else {
                    expressionToEvaluate = display.getText();
                    result = calculadora.evaluateExpression(expressionToEvaluate);

                    lastOperator = "";
                    int lastOpIndex = -1;
                    for (char op : BINARY_OPERATORS) {
                        int index = expressionToEvaluate.lastIndexOf(op);
                        if (index != -1 && index > lastOpIndex) {
                            lastOpIndex = index;
                            lastOperator = String.valueOf(op);
                        }
                    }

                    int lastNumberStart = expressionToEvaluate.length();
                    for (int i = expressionToEvaluate.length() - 1; i >= 0; i--) {
                        char ch = expressionToEvaluate.charAt(i);
                        if (!Character.isDigit(ch) && ch != '.') {
                            lastNumberStart = i + 1;
                            break;
                        }
                        if (i == 0) lastNumberStart = 0;
                    }
                    lastOperandString = expressionToEvaluate.substring(lastNumberStart);

                    isRepeatOperation = true;
                    historyDisplay.append(expressionToEvaluate + " = " + String.valueOf(result) + "\n");
                }
                updateDisplayWithResult(result);

            } else if (command.equals("pi") || command.equals("e") || isFunction(command)) {
                isRepeatOperation = false;
                lastOperator = "";

                String valueToAdd = "";
                if (command.equals("pi")) {
                    valueToAdd = String.valueOf(Math.PI);
                } else if (command.equals("e")) {
                    valueToAdd = String.valueOf(Math.E);
                }

                if (!valueToAdd.isEmpty()) {
                    if (isResultDisplayed || display.getText().equals("0")) {
                        display.setText(valueToAdd);
                    } else {
                        display.setText(display.getText() + valueToAdd);
                    }
                    currentNumberString.setLength(0);
                    isResultDisplayed = false;
                } else {
                    double valueToOperateOn;
                    if (currentNumberString.length() > 0 && !isResultDisplayed) {
                        valueToOperateOn = Double.parseDouble(currentNumberString.toString());
                    } else {
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
                    updateDisplayWithResult(result);
                }
            } else if (command.equals("(") || command.equals(")")) {
                isRepeatOperation = false;
                lastOperator = "";

                if (command.equals("(")) {
                    if (isResultDisplayed || display.getText().equals("0")) {
                        display.setText("(");
                    } else {
                        display.setText(display.getText() + "(");
                    }
                } else {
                    if (isResultDisplayed && display.getText().equals("0")) {
                        return;
                    }
                    display.setText(display.getText() + ")");
                }
                currentNumberString.setLength(0);
                isResultDisplayed = false;

            } else { // Operadores binarios (+, -, *, /, ^)
                if (currentNumberString.length() > 0) {
                    lastOperandString = currentNumberString.toString();
                } else if (isResultDisplayed) {
                    lastOperandString = String.valueOf(lastResult);
                } else {
                    lastOperandString = "";
                }
                lastOperator = command;
                isRepeatOperation = false;

                if (isResultDisplayed) {
                    display.setText(String.valueOf(lastResult) + command);
                } else {
                    display.setText(display.getText() + command);
                }
                currentNumberString.setLength(0);
                isResultDisplayed = false;
            }
        } catch (NumberFormatException ex) {
            handleError("Formato inválido");
        } catch (ArithmeticException ex) {
            handleError(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            handleError(ex.getMessage());
        } catch (Exception ex) {
            handleError("Error desconocido");
            ex.printStackTrace();
        }
    }

    private boolean isFunction(String command) { //Verifica si se ha elegido una funcion
        return command.equals("sqrt") || command.equals("!") || command.equals("%") ||
                command.equals("sin") || command.equals("cos") || command.equals("tan") ||
                command.equals("ln") || command.equals("log") ||
                command.equals("asin") || command.equals("acos") || command.equals("atan") ||
                command.equals("sinh") || command.equals("cosh");
    }
}