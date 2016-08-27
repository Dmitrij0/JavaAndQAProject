package ua.com.goqajava.group2.calculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static ua.com.goqajava.group2.calculator.Calculable.Sign.*;


@RunWith(value = Parameterized.class)
public class BigIntTest {

    private BigInt bigInt;
    private String value;
    private boolean valid;

    private static final String BASE_BIG_INT_STRING =
            "298371409847102398471902387409875890237456198612938746198237461892734689";


    public BigIntTest(String value, boolean valid) {
        this.value = value;
        this.valid = valid;
    }

    @Parameterized.Parameters(name = "value = {0}")
    public static Object[][] testData() {
        return new Object[][] {
                {"-7641982374698123764981273649812376", true},
                {"+28710928374019238471029384790128374901283749012837418902347", true},
                {"390485723094857203894572890347528903478905723409857238904750293847528903457", true},
                {"This is invalid number", false},
                {"8957209384572s0398457", false},
                {"0189741230984710982347-", false},
                {"0189741230984710982347+", false}
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void sum() throws Exception {
        assertEquals(new BigInteger(BASE_BIG_INT_STRING).add(new BigInteger(value)).toString(),
                new BigInt(BASE_BIG_INT_STRING).add(new BigInt(value)).toString().replaceFirst("\\+", ""));
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void subtract() throws Exception {
        assertEquals(
                new BigInteger(BASE_BIG_INT_STRING).subtract(new BigInteger(value)).toString(),
                new BigInt(BASE_BIG_INT_STRING).subtract(new BigInt(value)).toString().replaceFirst("\\+", ""));
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void multiply() throws Exception {
        assertEquals(
                new BigInteger(BASE_BIG_INT_STRING).multiply(new BigInteger(value)).toString(),
                new BigInt(BASE_BIG_INT_STRING).multiply(new BigInt(value)).toString().replaceFirst("\\+", ""));
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void division() throws Exception {
        assertEquals(
                new BigInteger(BASE_BIG_INT_STRING).divide(new BigInteger(value)).toString(),
                new BigInt(BASE_BIG_INT_STRING).divide(new BigInt(value)).toString().replaceFirst("\\+", ""));
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void negate() throws Exception {
        assertEquals(new BigInteger(value).negate().toString(),
                new BigInt(value).negate().toString().replaceFirst("\\+", ""));
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void compareTo() throws Exception {
        assertEquals(new BigInteger(BASE_BIG_INT_STRING).compareTo(new BigInteger(value)),
                new BigInt(BASE_BIG_INT_STRING).compareTo(new BigInt(value)));
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSign() throws Exception {
        final int sign = new BigInteger(value).signum();
        assertEquals(sign > 0 ? POSITIVE : sign < 0 ? NEGATIVE : ZERO, new BigInt(value).getSign());
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToString() throws Exception {
        assertEquals(new BigInteger(value).toString(), new BigInt(value).toString().replaceFirst("\\+", ""));
        if (valid) {
            throw new IllegalArgumentException();
        }
    }

}