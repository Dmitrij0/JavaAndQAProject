package ua.com.goqajava.group2.calculator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Calculator class
 *
 * @author Dmitrij Lenchuk
 * @since 21.07.2016.
 */
public class Calculator implements Evaluator {

    private static final String PATTERN_VALID_EXPRESSION = "[0-9()+\\-*/]*";
    private static final String PATTERN_PARENTHESIS = "\\([^()]*\\)";
    private static final String PATTERN_ALONE_PARENTHESIS = "^(\\([^()]*\\)|[^()]*)$";
    private static final String PATTERN_FIRST_OR_LAST_PARENTHESIS = "^\\(|\\)$";
    private static final String PATTERN_MULTIPLY = "\\d+\\*(\\+|-)?\\d+";
    private static final String PATTERN_DIVIDE = "\\d+/(\\+|-)?\\d+";
    private static final String PATTERN_PLUS = "(\\+|-)?\\d+\\+(\\+|-)?\\d+";
    private static final String PATTERN_MINUS = "(\\+|-)?\\d+-(\\+|-)?\\d+";
    private static final Pattern patternParenthesis = Pattern.compile(PATTERN_PARENTHESIS);
    private static final Pattern patternMultiply = Pattern.compile(PATTERN_MULTIPLY);
    private static final Pattern patternDivide = Pattern.compile(PATTERN_DIVIDE);
    private static final Pattern patternPlus = Pattern.compile(PATTERN_PLUS);
    private static final Pattern patternMinus = Pattern.compile(PATTERN_MINUS);

    private String expression;
    private BigNumber result;

    public Calculator() {
        this("");
    }

    public Calculator(String expression) {
        this.expression = verifyAndValidate(expression);
        calculate(expression);
    }

    public String getExpression() {
        return expression;
    }

    public BigNumber getResult() {
        return result;
    }

    public BigNumber calculate(String expression) {
        this.expression = verifyAndValidate(expression);
        return result = new BigInt(simplify(this.expression));
    }

    private String verifyAndValidate(String expression) {
        expression = expression.replaceAll("\\s+", "");
        if (!expression.matches(PATTERN_VALID_EXPRESSION)) {
            throw new IllegalArgumentException("The expression '" + expression + "' is invalid.");
        }
        return expression;
    }

    private String simplify(String expression) {
        if (expression == null || expression.equals("")) {
            return "0";
        }
        Matcher matcher = patternParenthesis.matcher(expression);
        if (matcher.find()) {
            return simplify(matcher.replaceFirst(calculateSimpleExpression(matcher.group())));
        }
        return calculateSimpleExpression(expression);
    }

    private String calculateSimpleExpression(String expression) {
        if (!expression.matches(PATTERN_ALONE_PARENTHESIS)) {
            throw new IllegalArgumentException("The expression '" + expression + "' isn't simple");
        }
        return testForMinus(testForPlus(testForDivide(testForMultiply(
                expression.replaceAll(PATTERN_FIRST_OR_LAST_PARENTHESIS, "")))));
    }

    private String testForMultiply(String expression) {
        Matcher matcher = patternMultiply.matcher(expression);
        if (matcher.find()) {
            String[] numbers = matcher.group().split("\\*");
            return calculateSimpleExpression(matcher.replaceFirst(multiply(numbers[0], numbers[1]).toString())
                    .replaceAll("(\\+\\+|--)", "+").replaceAll("(\\+-|-\\+)", "-"));
        }
        return expression;
    }

    private String testForDivide(String expression) {
        Matcher matcher = patternDivide.matcher(expression);
        if (matcher.find()) {
            String[] numbers = matcher.group().split("/");
            return calculateSimpleExpression(matcher.replaceFirst(divide(numbers[0], numbers[1]).toString())
                    .replaceAll("(\\+\\+|--)", "+").replaceAll("(\\+-|-\\+)", "-"));
        }
        return expression;
    }

    private String testForPlus(String expression) {
        Matcher matcher = patternPlus.matcher(expression);
        if (matcher.find()) {
            String[] numbers = matcher.group().split("\\+");
            return calculateSimpleExpression(matcher.replaceFirst(
                    add(numbers[numbers.length - 2], numbers[numbers.length - 1]).toString())
                    .replaceAll("(\\+\\+|--)", "+").replaceAll("(\\+-|-\\+)", "-"));
        }
        return expression;
    }

    private String testForMinus(String expression) {
        Matcher matcher = patternMinus.matcher(expression);
        if (matcher.find()) {
            String[] numbers = matcher.group().split("-");
            return calculateSimpleExpression(matcher.replaceFirst(
                    subtract((numbers.length == 3 ? "-" : "") + numbers[numbers.length - 2], numbers[numbers.length - 1]).toString())
                    .replaceAll("(\\+\\+|--)", "+").replaceAll("(\\+-|-\\+)", "-"));
        }
        return expression;
    }

    private BigNumber add(String value1, String value2) {
        return new BigInt(value1).add(new BigInt(value2));
    }

    public BigNumber add(BigNumber value1, BigNumber value2) {
        expression = verifyAndValidate(value1.toString() + "+" + value2.toString());
        return result = value1.add(value2);
    }

    private BigNumber subtract(String value1, String value2) {
        return new BigInt(value1).subtract(new BigInt(value2));
    }

    public BigNumber subtract(BigNumber value1, BigNumber value2) {
        expression = verifyAndValidate(value1.toString() + "-" + value2.toString());
        return result = value1.subtract(value2);
    }

    private BigNumber multiply(String value1, String value2) {
        return new BigInt(value1).multiply(new BigInt(value2));
    }

    public BigNumber multiply(BigNumber value1, BigNumber value2) {
        expression = verifyAndValidate(value1.toString() + "*" + value2.toString());
        return result = value1.multiply(value2);
    }

    private BigNumber divide(String value1, String value2) {
        return new BigInt(value1).divide(new BigInt(value2));
    }

    public BigNumber divide(BigNumber value1, BigNumber value2) throws ArithmeticException {
        expression = verifyAndValidate(value1.toString() + "/" + value2.toString());
        return result = value1.divide(value2);
    }

    @Override
    public String toString() {
        return expression == null || expression.equals("") ? "0" :
                expression
                        .replaceAll("\\+", " + ")
                        .replaceAll("-", " - ") +
                        (expression.matches(".*/.*") ? " ~ " : " = ") + result.toString().replaceAll("^\\+\\s*", "");
    }
}
