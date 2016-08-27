package ua.com.goqajava.group2.calculator;

/**
 * The contract of the calculable classes
 *
 * @author Dmitrij Lenchuk
 * @since 14.07.2016.
 */
public interface Calculable {

    enum Sign {
        POSITIVE,
        ZERO,
        NEGATIVE;

        Sign invert() {
            return this == POSITIVE ? NEGATIVE : this == NEGATIVE ? POSITIVE : ZERO;
        }

        Sign multiply(Sign multiplier) {
            return this == ZERO || multiplier == ZERO ? ZERO :
                    this == POSITIVE ? multiplier == NEGATIVE ? NEGATIVE : POSITIVE :
                            this == NEGATIVE ? multiplier == NEGATIVE ? POSITIVE : NEGATIVE : null;
        }

    }

    BigNumber add(BigNumber addend);

    BigNumber subtract(BigNumber deduction);

    BigNumber multiply(BigNumber multiplier);

    BigNumber divide(BigNumber divisor);

    BigNumber average(BigNumber value);

    BigNumber abs();

    BigNumber negate();

    boolean isZero();

    int[] getValues();

    Sign getSign();

    int getLength();

}
