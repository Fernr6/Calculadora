// Contiene la lógica de las operaciones matemáticas.

import java.util.Stack;

public class CalculadoraCientifica {

    // Limpia el estado de la calculadora.
    public void clear() {
        // No hay estado persistente que limpiar aquí.
    }

    // --- Funciones Básicas ---
    public double add(double a, double b) { return a + b; }
    public double subtract(double a, double b) { return a - b; }
    public double multiply(double a, double b) { return a * b; }
    public double divide(double a, double b) {
        if (b == 0) throw new ArithmeticException("División por cero");
        return a / b;
    }

    // --- Funciones Científicas ---
    public double getSqrt(double value) {
        if (value < 0) throw new ArithmeticException("Raíz cuadrada de un número negativo");
        return Math.sqrt(value);
    }
    public double getPower(double base, double exponent) { return Math.pow(base, exponent); }
    public double getSin(double degrees) { return Math.sin(Math.toRadians(degrees)); }
    public double getCos(double degrees) { return Math.cos(Math.toRadians(degrees)); }
    public double getTan(double degrees) { return Math.tan(Math.toRadians(degrees)); }
    public double getASin(double value) {
        if (value < -1 || value > 1) throw new ArithmeticException("Dominio inválido para arcoseno");
        return Math.toDegrees(Math.asin(value));
    }
    public double getACos(double value) {
        if (value < -1 || value > 1) throw new ArithmeticException("Dominio inválido para arcocoseno");
        return Math.toDegrees(Math.acos(value));
    }
    public double getATan(double value) { return Math.toDegrees(Math.atan(value)); }
    public double getSinh(double value) { return Math.sinh(value); }
    public double getCosh(double value) { return Math.cosh(value); }
    public double getLn(double value) {
        if (value <= 0) throw new ArithmeticException("Logaritmo de un número no positivo");
        return Math.log(value);
    }
    public double getLog(double value) {
        if (value <= 0) throw new ArithmeticException("Logaritmo de un número no positivo");
        return Math.log10(value);
    }
    public long getFactorial(int n) {
        if (n < 0) throw new ArithmeticException("Factorial de un número negativo");
        if (n == 0 || n == 1) return 1;
        long result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }
    public double getPercentage(double value) { return value / 100.0; }

    /**
     * Evalúa una expresión matemática infija.
     * @param expression Expresión a evaluar.
     * @return Resultado de la evaluación.
     */
    public double evaluateExpression(String expression) {
        // Reemplaza constantes.
        expression = expression.replace("pi", String.valueOf(Math.PI));
        expression = expression.replace("e", String.valueOf(Math.E));
        expression = expression.replaceAll("\\s+", "");

        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.empty() && ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                if (ops.empty() || ops.peek() != '(') throw new IllegalArgumentException("Paréntesis desequilibrados");
                ops.pop();
            } else if (Character.isDigit(c) || c == '.') {
                StringBuilder sbuf = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sbuf.append(expression.charAt(i++));
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (isOperator(c)) {
                while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }

        while (!ops.empty()) {
            if (ops.peek() == '(') throw new IllegalArgumentException("Paréntesis desequilibrados");
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        if (values.size() != 1) throw new IllegalArgumentException("Expresión inválida");
        return values.pop();
    }

    // Verifica si un carácter es un operador.
    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    // Compara la precedencia de operadores.
    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if (op1 == '^') return true;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return true;
        return false;
    }

    // Aplica una operación a dos operandos.
    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return add(a, b);
            case '-': return subtract(a, b);
            case '*': return multiply(a, b);
            case '/': return divide(a, b);
            case '^': return getPower(a, b);
            default: throw new IllegalArgumentException("Operador desconocido: " + op);
        }
    }
}