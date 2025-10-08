import org.junit.Test;
import org.junit.Assert;

import calculator.Calculator;
import calculator.SimpleCalculator;

/**
 * Test class for SimpleCalculator implementation.
 * Extends AbstractCalculatorTest to inherit common tests and adds SimpleCalculator-specific tests.
 */
public class SimpleCalculatorTest extends AbstractCalculatorTest {

  /**
   * Creates a SimpleCalculator instance for testing.
   *
   * @return a new SimpleCalculator instance
   */
  @Override
  protected Calculator createCalculator() {
    return new SimpleCalculator();
  }

  // ========== SIMPLE CALCULATOR SPECIFIC TESTS ==========

  /**
   * Test that repeated equals returns same result (SimpleCalculator behavior).
   */
  @Test
  public void testRepeatedEquals() {
    Calculator calc = createCalculator();
    calc = calc.input('7');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());

    // Repeated equals should return same result
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());

    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());
  }

  /**
   * Test that consecutive operators throw exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConsecutiveOperatorsThrowException() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('-'); // Should throw exception
  }

  /**
   * Test that equals without second operand throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEqualsWithoutSecondOperand() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('='); // Should throw exception
  }

  /**
   * Test that starting with plus throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartingWithPlus() {
    Calculator calc = createCalculator();
    calc = calc.input('+'); // Should throw exception
  }

  /**
   * Test multiple operations sequence.
   */
  @Test
  public void testMultipleOperationsSequence() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("56", calc.getResult());

    // Continue with another operation
    calc = calc.input('-');
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("46", calc.getResult());
  }

  /**
   * Test larger numbers.
   */
  @Test
  public void testLargerNumbers() {
    Calculator calc = createCalculator();
    calc = calc.input('1');
    calc = calc.input('2');
    calc = calc.input('3');
    calc = calc.input('4');
    calc = calc.input('5');
    calc = calc.input('*');
    calc = calc.input('2');
    calc = calc.input('=');
    Assert.assertEquals("24690", calc.getResult());
  }

  /**
   * Test division-like subtraction patterns.
   */
  @Test
  public void testSubtractionPatterns() {
    Calculator calc = createCalculator();
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('0');
    calc = calc.input('-');
    calc = calc.input('2');
    calc = calc.input('5');
    calc = calc.input('=');
    Assert.assertEquals("75", calc.getResult());
  }
}