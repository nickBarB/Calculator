import java.util.Scanner;

public class Calculator {
    private static final String EXIT_COMMAND = "s";
    private static final String RESET_COMMAND = "c";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Double currentNumber = readNumber(scanner, "Введите первое число или s для выхода:");
            if (currentNumber == null) {
                return;
            }

            while (true) {
                boolean canEnterNumber = currentNumber == 0.0;
                String operation = readInput(scanner,
                        canEnterNumber
                                ? "Введите число или операцию (+, -, *, /), c для обнуления или s для выхода:"
                                : "Введите операцию (+, -, *, /), c для обнуления или s для выхода:");
                if (operation == null) {
                    return;
                }

                if (RESET_COMMAND.equalsIgnoreCase(operation)) {
                    currentNumber = 0.0;
                    System.out.println("Результат: " + currentNumber);
                    continue;
                }

                boolean validOperation = switch (operation) {
                    case "+", "-", "*", "/" -> true;
                    default -> false;
                };
                if (canEnterNumber && !validOperation) {
                    try {
                        double number = Double.parseDouble(operation.replace(',', '.'));
                        if (Double.isFinite(number)) {
                            currentNumber = number;
                            System.out.println("Первое число: " + currentNumber);
                        } else {
                            System.out.println("Введите конечное число в диапазоне double.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Введите число или операцию (+, -, *, /).");
                    }
                    continue;
                }
                if (!validOperation) {
                    System.out.println("Допустимы только +, -, *, /. Повторите ввод операции.");
                    continue;
                }

                Double secondNumber = readNumber(scanner, "Введите второе число или s для выхода:");
                if (secondNumber == null) {
                    return;
                }

                try {
                    currentNumber = calculate(currentNumber, operation, secondNumber);
                    System.out.println("Результат: " + currentNumber);
                } catch (ArithmeticException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    private static double calculate(double firstNumber, String operation, double secondNumber) {
        if ("/".equals(operation) && secondNumber == 0.0) {
            throw new ArithmeticException("Деление на ноль недопустимо.");
        }

        double result = switch (operation) {
            case "+" -> firstNumber + secondNumber;
            case "-" -> firstNumber - secondNumber;
            case "*" -> firstNumber * secondNumber;
            case "/" -> firstNumber / secondNumber;
            default -> throw new IllegalArgumentException("Допустимы только +, -, *, /.");
        };
        if (!Double.isFinite(result)) {
            throw new ArithmeticException("Результат выходит за диапазон double.");
        }
        return result;
    }

    private static Double readNumber(Scanner scanner, String prompt) {
        while (true) {
            String token = readInput(scanner, prompt);
            if (token == null) {
                return null;
            }
            try {
                double number = Double.parseDouble(token.replace(',', '.'));
                if (Double.isFinite(number)) {
                    return number;
                }
                System.out.println("Введите конечное число в диапазоне double.");
            } catch (NumberFormatException exception) {
                System.out.println("Некорректное число. Пример ввода: 12.5 или 12,5.");
            }
        }
    }

    private static String readInput(Scanner scanner, String prompt) {
        System.out.println(prompt);
        if (scanner.hasNextLine()) {
            String token = scanner.nextLine().trim();
            if (!EXIT_COMMAND.equalsIgnoreCase(token)) {
                return token;
            }
        }
        System.out.println("Выходим из программы.");
        return null;
    }
}
