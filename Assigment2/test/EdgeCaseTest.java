import org.junit.Test;
import org.junit.Assert;
import calculator.Calculator;
import calculator.SmartCalculator;

/**
 * Test edge cases for SmartCalculator implementation.
 */
public class EdgeCaseTest {

  /**
   * Test equals immediately after operator.
   */
  @Test
  public void testEqualsAfterOperator() {
    Calculator calc = new SmartCalculator();
    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());
  }

  /**
   * Test what happens after multiple consecutive operators.
   */
  @Test
  public void testMultipleConsecutiveOperators() {
    Calculator calc = new SmartCalculator();
    calc = calc.input('8');
    calc = calc.input('+');
    calc = calc.input('-');
    calc = calc.input('*');
    calc = calc.input('+');
    calc = calc.input('2');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());
  }

  /**
   * Test leading plus with missing operand.
   */
  @Test
  public void testLeadingPlusWithMissingOperand() {
    Calculator calc = new SmartCalculator();
    calc = calc.input('+');
    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());
  }

  /**
   * Test zero operations.
   */
  @Test
  public void testZeroOperations() {
    Calculator calc = new SmartCalculator();
    calc = calc.input('0');
    calc = calc.input('+');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("0", calc.getResult());

    calc = calc.input('=');
    Assert.assertEquals("0", calc.getResult());
  }

  /**
   * Test operator after result.
   */
  @Test
  public void testOperatorAfterResult() {
    Calculator calc = new SmartCalculator();
    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('=');
    Assert.assertEquals("8", calc.getResult());

    calc = calc.input('*');
    calc = calc.input('2');
    calc = calc.input('=');
    Assert.assertEquals("16", calc.getResult());
  }

  /**
   * Test consecutive operators then digits.
   */
  @Test
  public void testConsecutiveOperatorsThenDigits() {
    Calculator calc = new SmartCalculator();
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('+');
    calc = calc.input('-');
    calc = calc.input('*');
    calc = calc.input('1');
    calc = calc.input('2');
    calc = calc.input('=');
    Assert.assertEquals("120", calc.getResult());
  }

  /**
   * Test the specific assignment overflow example.
   * MAX + 1 overflows to 0, then 0 - 10 = -10
   */
  @Test
  public void testAssignmentOverflowExample() {
    Calculator calc = new SmartCalculator();

    // Build Integer.MAX_VALUE
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

    // MAX + 1 should overflow to 0
    calc = calc.input('+');
    calc = calc.input('1');
    calc = calc.input('=');
    Assert.assertEquals("0", calc.getResult());

    // Then 0 - 10 = -10
    calc = calc.input('-');
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("-10", calc.getResult());
  }
}