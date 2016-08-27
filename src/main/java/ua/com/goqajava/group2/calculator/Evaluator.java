package ua.com.goqajava.group2.calculator;

/**
 * The contract of the evaluator classes
 *
 * @author Dmitrij Lenchuk
 * @since 22.07.2016.
 */
public interface Evaluator {

    BigNumber calculate(String expression);

    BigNumber add(BigNumber value1, BigNumber value2);

    BigNumber subtract(BigNumber value1, BigNumber value2);

    BigNumber multiply(BigNumber value1, BigNumber value2);

    BigNumber divide(BigNumber value1, BigNumber value2);

}
