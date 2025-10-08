import org.junit.Test;
import org.junit.Assert;

import calculator.Calculator;

/**
 * Abstract test class containing common tests that apply to both
 * SimpleCalculator and SmartCalculator implementations.
 * Concrete test classes should extend this and implement createCalculator().
 */
public abstract class AbstractCalculatorTest {

  /**
   * Factory method to create calculator instance.
   * Must be implemented by concrete test classes.
   *
   * @return a Calculator instance for testing
   */
  protected abstract Calculator createCalculator();

  // ========== BASIC FUNCTIONALITY TESTS ==========

  /**
   * Test initial state of calculator.
   */
  @Test
  public void testInitialState() {
    Calculator calc = createCalculator();
    Assert.assertEquals("", calc.getResult());
  }

  /**
   * Test single digit input.
   */
  @Test
  public void testSingleDigitInput() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    Assert.assertEquals("5", calc.getResult());
  }

  /**
   * Test multiple digit input.
   */
  @Test
  public void testMultipleDigitInput() {
    Calculator calc = createCalculator();
    calc = calc.input('1');
    calc = calc.input('2');
    calc = calc.input('3');
    Assert.assertEquals("123", calc.getResult());
  }

  /**
   * Test simple addition.
   */
  @Test
  public void testSimpleAddition() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("56", calc.getResult());
  }

  /**
   * Test simple subtraction.
   */
  @Test
  public void testSimpleSubtraction() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('-');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("8", calc.getResult());
  }

  /**
   * Test simple multiplication.
   */
  @Test
  public void testSimpleMultiplication() {
    Calculator calc = createCalculator();
    calc = calc.input('6');
    calc = calc.input('*');
    calc = calc.input('8');
    calc = calc.input('=');
    Assert.assertEquals("48", calc.getResult());
  }

  /**
   * Test negative result.
   */
  @Test
  public void testNegativeResult() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('-');
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("-5", calc.getResult());
  }

  /**
   * Test zero operands.
   */
  @Test
  public void testZeroOperands() {
    Calculator calc = createCalculator();
    calc = calc.input('0');
    calc = calc.input('+');
    calc = calc.input('5');
    calc = calc.input('=');
    Assert.assertEquals("5", calc.getResult());
  }

  /**
   * Test clear functionality.
   */
  @Test
  public void testClearFunctionality() {
    Calculator calc = createCalculator();
    calc = calc.input('7');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('C');
    Assert.assertEquals("", calc.getResult());
  }

  /**
   * Test display during input sequence.
   */
  @Test
  public void testDisplayDuringInput() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    Assert.assertEquals("32", calc.getResult());

    calc = calc.input('+');
    Assert.assertEquals("32+", calc.getResult());

    calc = calc.input('2');
    calc = calc.input('4');
    Assert.assertEquals("32+24", calc.getResult());
  }

  // ========== ERROR HANDLING TESTS ==========

  /**
   * Test invalid character input.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCharacter() {
    Calculator calc = createCalculator();
    calc = calc.input('a');
  }

  /**
   * Test starting with minus operator.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartingWithMinus() {
    Calculator calc = createCalculator();
    calc = calc.input('-');
  }

  /**
   * Test starting with multiplication operator.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartingWithMultiply() {
    Calculator calc = createCalculator();
    calc = calc.input('*');
  }

  // ========== OVERFLOW TESTS ==========

  /**
   * Test operand overflow.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOperandOverflow() {
    Calculator calc = createCalculator();

    // Build Integer.MAX_VALUE (2147483647)
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

    // This should overflow
    calc = calc.input('8');
  }

  /**
   * Test result overflow returns 0.
   */
  @Test
  public void testResultOverflow() {
    Calculator calc = createCalculator();

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

    calc = calc.input('+');
    calc = calc.input('1');
    calc = calc.input('=');

    // Should overflow and return 0
    Assert.assertEquals("0", calc.getResult());
  }

  /**
   * Test operand is retained after overflow attempt.
   */
  @Test
  public void testOperandRetainedAfterOverflow() {
    Calculator calc = createCalculator();

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

    try {
      calc = calc.input('8'); // Should overflow
      Assert.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Continue with operation using retained value
    calc = calc.input('+');
    calc = calc.input('1');
    calc = calc.input('=');

    // Should overflow and return 0
    Assert.assertEquals("0", calc.getResult());
  }

  // ========== IMMUTABILITY TESTS ==========

  /**
   * Test input does not modify original object.
   */
  @Test
  public void testInputImmutability() {
    Calculator calc1 = createCalculator();
    Calculator calc2 = calc1.input('5');

    Assert.assertEquals("", calc1.getResult());
    Assert.assertEquals("5", calc2.getResult());
  }

  /**
   * Test immutability with multiple operations.
   */
  @Test
  public void testImmutabilityMultipleOperations() {
    Calculator calc1 = createCalculator();
    Calculator calc2 = calc1.input('5');
    Calculator calc3 = calc2.input('+');
    Calculator calc4 = calc3.input('3');
    Calculator calc5 = calc4.input('=');

    Assert.assertEquals("", calc1.getResult());
    Assert.assertEquals("5", calc2.getResult());
    Assert.assertEquals("5+", calc3.getResult());
    Assert.assertEquals("5+3", calc4.getResult());
    Assert.assertEquals("8", calc5.getResult());
  }
}