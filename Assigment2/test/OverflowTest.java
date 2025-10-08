import org.junit.Test;
import org.junit.Assert;
import calculator.Calculator;
import calculator.SmartCalculator;

/**
 * Test overflow scenarios for SmartCalculator.
 */
public class OverflowTest {

  /**
   * Test overflow scenario step by step.
   */
  @Test
  public void testOverflowDebugging() {
    Calculator calc = new SmartCalculator();

    // Build MAX value
    calc = calc.input('2');
    calc = calc.input('1');
    calc = calc.input('4');
    calc = calc.input('7');
    calc = calc.input('4');
    calc = calc.input('8');
    calc = calc.input('3');
    calc = calc.input('6');
    calc = calc.input('4');
    calc = calc.input('7');

    calc = calc.input('+');
    calc = calc.input('1');
    calc = calc.input('=');
    Assert.assertEquals("0", calc.getResult());

    calc = calc.input('-');
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("-10", calc.getResult());
  }

  /**
   * Test specific overflow case with intermediate steps.
   */
  @Test
  public void testSpecificOverflowCase() {
    Calculator calc = new SmartCalculator();

    // Build MAX value
    calc = calc.input('2');
    calc = calc.input('1');
    calc = calc.input('4');
    calc = calc.input('7');
    calc = calc.input('4');
    calc = calc.input('8');
    calc = calc.input('3');
    calc = calc.input('6');
    calc = calc.input('4');
    calc = calc.input('7');
    Assert.assertEquals("2147483647", calc.getResult());

    calc = calc.input('+');
    calc = calc.input('1');
    Assert.assertEquals("2147483647+1", calc.getResult());

    calc = calc.input('-');
    Assert.assertEquals("0-", calc.getResult());

    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("-10", calc.getResult());
  }
}