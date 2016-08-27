package ua.com.goqajava.group2.calculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class CalculatorTest {

    private String expression;
    private BigNumber result;
    private boolean valid;

    public CalculatorTest(String expression, BigNumber result, boolean valid) {
        this.expression = expression;
        this.result = result;
        this.valid = valid;
    }

    @Parameterized.Parameters(name = "{0} ~ {1}")
    public static Object[][] testData() {
        return new Object[][] {
                {"10 + 15 * 2", new BigInt("40"), true},
                {"10 + 10 + 10", new BigInt("30"), true},
                {"10 - 10 + 10 - 10 + 10 - 10", new BigInt("0"), true},
                {"12 - (30 + 13) * 10", new BigInt("-418"), true},
                {"98123746189  * -9123419237 + 8170239847109238741241 /19283746189237", new BigInt("-895224073586804352491"), true},
                {"((-3 + 2)*(2  +4)/(3-1) + (1 - 5))", new BigInt("-7"), true},
                {"12724617 + (71263876*(12938719-287346782/13576)+ (13102983+1827381263)/(1827349182*(319283789123 - 12312312312) / (84091827349 + 2983479823479237489 - 2789347)/1089274) - 8234729347)+ 10928347190823740192387489012374901237489", new BigInt("0"), false},
                {"98 + ) - (1902837)/ 189273891", new BigInt("0"), false},
                {"1+)2+3(+4", new BigInt("0"), false},
                {"23748273 + 12873461AAA89723", new BigInt("0"), false}
        };
    }

    @Test(expected = Exception.class)
    public void calculate() throws Exception {
        assertEquals(result, new Calculator(expression).getResult());
        if (valid) {
            throw new Exception("Is Valid");
        }
    }

}