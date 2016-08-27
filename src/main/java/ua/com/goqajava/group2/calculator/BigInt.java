package ua.com.goqajava.group2.calculator;

import java.util.Arrays;

import static ua.com.goqajava.group2.calculator.Calculable.Sign.*;


/**
 * The abstract class of a big numbers.
 * The class is immutable
 *
 * @author Dmitrij Lenchuk
 * @since 14.07.2016.
 */
public class BigInt implements BigNumber {

    public static final String VALID_VALUE_REGEXP = "(-|\\+)?\\d+";

    private final int[] values;
    private final Sign sign;
    private final int length;

    public BigInt(String values) {
        if (!values.matches(VALID_VALUE_REGEXP)) {
            throw new IllegalArgumentException("\n'" + values + "'\nisn't a valid number");
        }

        String validValue =
                new StringBuilder(values.replaceAll("(\\+|-)", "").replaceAll("^0+", "")).reverse().toString();
        int length = validValue.length();
        this.sign = length == 0 ? ZERO : values.charAt(0) == '-' ? NEGATIVE : POSITIVE;

        this.values = new int[length];
        this.length = length;

        for (int i = 0; i < length; i++) {
            this.values[i] = Integer.valueOf(String.valueOf(validValue.charAt(i)));
        }
    }

    private BigInt(int[] values, Sign sign, int length) {
        int currLength = this.length = Math.min(values.length, length);
        for (int i = 0; i < currLength; i++) {
            if (values[i] < 0 || values[i] > 9) {
                throw new IllegalArgumentException("\n'" + values[i] + "'\nisn't a valid cipher on '" + i + "'-th position");
            }
        }
        this.sign = sign;
        this.values = new int[currLength];
        System.arraycopy(values, 0, this.values, 0, currLength);
    }

    private BigInt(int[] values) {
        values = validate(values);
        this.sign = extractSign(values);
        this.values = truncate(values);
        this.length = this.values.length;
    }

    private BigInt(int[] values, Sign sign) {
        values = validate(values);
        this.sign = extractSign(values).multiply(sign);
        this.values = truncate(values);
        this.length = this.values.length;
    }

    private int[] validate(int[] value) {
        // Validation of data
        for (int i = 0; i < value.length; i++) {
            int item = value[i];
            if (Math.abs(item) > 9) {
                int currItem = item;
                int length = (int) Math.ceil(Math.log10(item + 1));
                for (int j = 0; j < length; j++) {
                    int oldItem = currItem;
                    currItem %= Math.pow(10, j + 1);
                    value[i + j] = currItem + (j == 0 ? 0 : value[i + j]);
                    currItem = (int) Math.ceil((oldItem - currItem) / 10);
                }
            }
        }
        return truncate(value);
    }

    private Sign extractSign(int[] values) {
        values = truncate(values);
        final Sign sign = isZero(values) ? ZERO : values[values.length - 1] < 0 ? NEGATIVE : POSITIVE;
        boolean needToCorrectNegativeItems = false;
        switch (sign) {
            case POSITIVE:
                for (int currItem : values) {
                    if (currItem < 0) {
                        needToCorrectNegativeItems = true;
                    }
                }
                break;
            case NEGATIVE:
                for (int i = 0; i < values.length; i++) {
                    if ((values[i] *= -1) < 0) {
                        needToCorrectNegativeItems = true;
                    }
                }
                break;
            default:
                return sign;
        }
        if (needToCorrectNegativeItems) {
            castItemsToOnceSign(values);
        }
        return sign;
    }

    private void castItemsToOnceSign(int[] values) {
        values = truncate(values);
        final Sign sign = isZero(values) ? ZERO : values[values.length - 1] < 0 ? NEGATIVE : POSITIVE;
        switch (sign) {
            case POSITIVE:
                for (int i = 0; i < values.length - 1; i++) {
                    if (values[i] < 0) {
                        values[i + 1]--;
                        values[i] += 10;
                    }
                }
                return;
            case NEGATIVE:
                for (int i = 0; i < values.length - 1; i++) {
                    if (values[i] > 0) {
                        values[i + 1]++;
                        values[i] -= 10;
                    }
                }
                return;
            default:
        }
    }

    private int[] truncate(int[] values) {
        int length = values.length;
        if (length == 0 || values[length - 1] != 0) {
            return values;
        }

        for (int i = length - 1; i >= 0; i--) {
            if (values[i] != 0) {
                if (i == length - 1) {
                    return values;
                } else {
                    int[] newValues = new int[i + 1];
                    System.arraycopy(values, 0, newValues, 0, newValues.length);
                    return newValues;
                }
            }
        }
        return new int[0];
    }

    @Override
    public BigNumber add(BigNumber addend) {
        if (sign == ZERO) {
            return new BigInt(addend.getValues(), addend.getSign(), addend.getLength());
        }
        if (addend.getSign() == ZERO) {
            return new BigInt(values, sign, length);
        }

        int thisLength = this.length;
        int addendLength = addend.getLength();
        int resultLength = Math.max(thisLength, addendLength) + 1;
        int[] sum = new int[resultLength];
        int[] add = addend.getValues();

        for (int i = 0; i < resultLength; i++) {
            int thisItem = i < thisLength ? values[i] : 0;
            int addItem = i < addendLength ? add[i] : 0;
            sum[i] = thisItem * (sign == POSITIVE ? 1 : -1) + addItem * (addend.getSign() == POSITIVE ? 1 : -1);
        }
        return new BigInt(sum);
    }

    @Override
    public BigNumber subtract(BigNumber deduction) {
        return add(deduction.negate());
    }

    @Override
    public BigNumber multiply(BigNumber multiplier) {

        if (sign == ZERO || multiplier.getSign() == ZERO) {
            return new BigInt(new int[0], ZERO, 0);
        }

        int thisLength = this.length;
        int multiplierLength = multiplier.getLength();

        int[] value = this.getValues();
        int[] multi = multiplier.getValues();

        int[] product = new int[thisLength + multiplierLength];
        Arrays.fill(product, 0);

        for (int i = 0; i < multiplierLength; i++) {
            for (int j = 0; j < thisLength; j++) {
                product[i + j] += value[j] * multi[i];
            }
        }
        return new BigInt(product, sign.multiply(multiplier.getSign()));
    }

    @Override
    public BigNumber divide(BigNumber divisor) {
        Sign resultSign = sign.multiply(divisor.getSign());
        BigNumber absThis = this.abs();
        BigNumber absDivisor = divisor.abs();

        if (isZero() || absThis.compareTo(absDivisor) < 0) {
            return new BigInt(new int[0], ZERO, 0);
        }
        if (absDivisor.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        if (Arrays.equals(absThis.getValues(), absDivisor.getValues())) {
            return new BigInt(new int[]{1}, resultSign);
        }
        if (Arrays.equals(absDivisor.getValues(), new int[]{1})) {
            return new BigInt(absThis.getValues(), resultSign);
        }

        int thisLength = this.length;
        int divLength = absDivisor.getLength();
        int resultLength = (thisLength - divLength) + 1;

        // 1...0
        int[] min = new int[resultLength == 1 ? 1 : resultLength - 1];
        Arrays.fill(min, 0);
        min[min.length - 1] = 1;

        // 100...0
        int[] max = new int[resultLength + 1];
        Arrays.fill(max, 0);
        max[max.length - 1] = 1;

        int[] curr = min;
        BigInt estimate;
        BigInt oldEstimate = null;
        BigNumber estimateResult;

        int compareResult;
        while (true) {
            estimate = new BigInt(curr);

            if (estimate.equals(oldEstimate) ||
                absThis.equals(estimateResult = estimate.multiply(absDivisor)) ||
                (compareResult = absThis.compareTo(estimateResult)) == 0) {
                return resultSign == NEGATIVE ? estimate.negate() : estimate;
            }

            if (compareResult > 0) {
                min = curr;
            } else {
                max = curr;
            }
            curr = average(min, max);
            oldEstimate = estimate;
        }
    }

    private int[] average(int[] value1, int[] value2) {
        return new BigInt(value1).average(new BigInt(value2)).getValues();
    }

    public BigNumber average(BigNumber value) {
        int[] result = this.add(value).getValues();
        final int resultLength = result.length;
        int oldValue;
        for (int i = resultLength - 1; i >= 0; i--) {
            oldValue = result[i];
            result[i] /= 2;
            if (i > 0) {
                result[i - 1] += (oldValue % 2) * 10;
            }
        }
        return new BigInt(result);
    }

    @Override
    public BigNumber negate() {
        return new BigInt(values, sign.invert(), length);
    }

    @Override
    public BigNumber abs() {
        return sign == NEGATIVE ? new BigInt(values, POSITIVE, length) : this;
    }

    @Override
    public boolean isZero() {
        return isZero(values);
    }

    private boolean isZero(int[] values) {
        return values == null || values.length == 0;
    }

    @Override
    public int[] getValues() {
        return values;
    }

    @Override
    public Sign getSign() {
        return sign;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(BigNumber that) {
        Sign sign = this.subtract(that).getSign();
        return sign == POSITIVE ? 1 : sign == NEGATIVE ? -1 : 0;
    }

    @Override
    public String toString() {
        if (values == null || values.length == 0 || sign == ZERO) {
            return "+0";
        }
        StringBuilder stringBuilder = new StringBuilder(values.length).append(sign == NEGATIVE ? '-' : "+");
        for (int i = length - 1; i >= 0; i--) {
            stringBuilder.append(values[i]);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        BigInt that = (BigInt) obj;

        if (length != that.length) return false;
        if (sign != that.sign) return false;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(values);
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        return result;
    }
}
