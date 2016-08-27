package ua.com.goqajava.group2;


import ua.com.goqajava.group2.calculator.Calculator;

import java.util.Scanner;

public class CalculatorRunner {

    private static final String HELP_MESSAGE =
            "Please, enter a valid arithmetic expression or one of the following commands: ':help', ':?', ':exit', ':!'";

    private static final String ERROR_MESSAGE =
            "Error. The entered arithmetic expression is invalid. Please, enter a valid one.";

    private static final Scanner scanner = new Scanner(System.in);
    private static final Calculator calculator = new Calculator();

    public static void main(String[] args) {
        String commandLine;
        System.out.println(HELP_MESSAGE);
        while (true) {
            if ((commandLine = scanner.nextLine()) == null || commandLine.equals("")) {
                System.out.println("An expression is not found");
            } else if (commandLine.equals(":exit") ||
                            commandLine.equals(":!")) {
                break;
            }else if (commandLine.equals(":help") ||
                    commandLine.equals(":?")) {
                System.out.println(HELP_MESSAGE);
            } else {
                try {
                    calculator.calculate(commandLine);
                    System.out.println(calculator);
                } catch (IllegalArgumentException e) {
                    System.out.println(ERROR_MESSAGE);
                }
            }
        }
    }

}
