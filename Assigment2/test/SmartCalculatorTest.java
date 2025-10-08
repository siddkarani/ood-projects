import org.junit.Test;
import org.junit.Assert;

import calculator.Calculator;
import calculator.SmartCalculator;

/**
 * Test class for SmartCalculator implementation.
 * Extends AbstractCalculatorTest to inherit common tests and adds smart feature tests.
 */
public class SmartCalculatorTest extends AbstractCalculatorTest {

  /**
   * Creates a SmartCalculator instance for testing.
   *
   * @return a new SmartCalculator instance
   */
  @Override
  protected Calculator createCalculator() {
    return new SmartCalculator();
  }

  // ========== SMART FEATURE TESTS ==========

  /**
   * Test multiple equals functionality.
   * 32 + 24 = produces 56
   * 32 + 24 = = produces 80 (56 + 24)
   * 32 + 24 = = = produces 104 (80 + 24)
   */
  @Test
  public void testMultipleEquals() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("56", calc.getResult());

    // Second equals: 56 + 24 = 80
    calc = calc.input('=');
    Assert.assertEquals("80", calc.getResult());

    // Third equals: 80 + 24 = 104
    calc = calc.input('=');
    Assert.assertEquals("104", calc.getResult());
  }

  /**
   * Test multiple equals with subtraction.
   */
  @Test
  public void testMultipleEqualsSubtraction() {
    Calculator calc = createCalculator();
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('0');
    calc = calc.input('-');
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("90", calc.getResult());

    // Second equals: 90 - 10 = 80
    calc = calc.input('=');
    Assert.assertEquals("80", calc.getResult());

    // Third equals: 80 - 10 = 70
    calc = calc.input('=');
    Assert.assertEquals("70", calc.getResult());
  }

  /**
   * Test multiple equals with multiplication.
   */
  @Test
  public void testMultipleEqualsMultiplication() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('*');
    calc = calc.input('2');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());

    // Second equals: 10 * 2 = 20
    calc = calc.input('=');
    Assert.assertEquals("20", calc.getResult());

    // Third equals: 20 * 2 = 40
    calc = calc.input('=');
    Assert.assertEquals("40", calc.getResult());
  }

  /**
   * Test missing second operand functionality.
   * 32 + = produces 64 (32 + 32)
   * 32 + = = produces 96 (64 + 32)
   */
  @Test
  public void testMissingSecondOperand() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('=');
    Assert.assertEquals("64", calc.getResult());

    // Second equals: 64 + 32 = 96
    calc = calc.input('=');
    Assert.assertEquals("96", calc.getResult());
  }

  /**
   * Test missing second operand with subtraction.
   */
  @Test
  public void testMissingSecondOperandSubtraction() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('0');
    calc = calc.input('-');
    calc = calc.input('=');
    Assert.assertEquals("0", calc.getResult()); // 50 - 50 = 0

    // Second equals: 0 - 50 = -50
    calc = calc.input('=');
    Assert.assertEquals("-50", calc.getResult());
  }

  /**
   * Test missing second operand with multiplication.
   */
  @Test
  public void testMissingSecondOperandMultiplication() {
    Calculator calc = createCalculator();
    calc = calc.input('7');
    calc = calc.input('*');
    calc = calc.input('=');
    Assert.assertEquals("49", calc.getResult()); // 7 * 7 = 49

    // Second equals: 49 * 7 = 343
    calc = calc.input('=');
    Assert.assertEquals("343", calc.getResult());
  }

  /**
   * Test consecutive operators functionality.
   * 32 + - 24 = should ignore the + and use -, producing 8
   */
  @Test
  public void testConsecutiveOperators() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('-');  // Should replace the +
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("8", calc.getResult()); // 32 - 24 = 8
  }

  /**
   * Test consecutive operators with display updates.
   */
  @Test
  public void testConsecutiveOperatorsDisplay() {
    Calculator calc = createCalculator();
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    Assert.assertEquals("32+", calc.getResult());

    calc = calc.input('-');  // Should replace the +
    Assert.assertEquals("32-", calc.getResult());

    calc = calc.input('*');  // Should replace the -
    Assert.assertEquals("32*", calc.getResult());

    calc = calc.input('5');
    calc = calc.input('=');
    Assert.assertEquals("160", calc.getResult()); // 32 * 5 = 160
  }

  /**
   * Test consecutive operators after second operand started.
   */
  @Test
  public void testConsecutiveOperatorsAfterSecondOperand() {
    Calculator calc = createCalculator();
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('+');
    calc = calc.input('5');
    Assert.assertEquals("10+5", calc.getResult());

    calc = calc.input('-');  // Should replace + with -
    Assert.assertEquals("10-5", calc.getResult());

    calc = calc.input('=');
    Assert.assertEquals("5", calc.getResult()); // 10 - 5 = 5
  }

  /**
   * Test leading plus operator functionality.
   * + 32 - 24 = should ignore the + and produce 8
   */
  @Test
  public void testLeadingPlusOperator() {
    Calculator calc = createCalculator();
    calc = calc.input('+');  // Leading plus should be ignored
    Assert.assertEquals("", calc.getResult()); // Display should remain empty

    calc = calc.input('3');
    calc = calc.input('2');
    Assert.assertEquals("32", calc.getResult());

    calc = calc.input('-');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("8", calc.getResult()); // 32 - 24 = 8
  }

  /**
   * Test that leading minus still throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testLeadingMinusInvalid() {
    Calculator calc = createCalculator();
    calc = calc.input('-');  // Should throw exception
  }

  /**
   * Test that leading multiplication still throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testLeadingMultiplyInvalid() {
    Calculator calc = createCalculator();
    calc = calc.input('*');  // Should throw exception
  }

  /**
   * Test simple case of missing operand to debug.
   */
  @Test
  public void testSimpleMissingOperand() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult()); // 5 + 5 = 10
  }

  /**
   * Test overflow with missing operand.
   */
  @Test
  public void testOverflowWithMissingOperand() {
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

    calc = calc.input('+');
    calc = calc.input('='); // MAX + MAX should overflow to 0
    Assert.assertEquals("0", calc.getResult());
  }

  /**
   * Test complex scenario combining multiple smart features.
   */
  @Test
  public void testComplexSmartFeatures() {
    Calculator calc = createCalculator();
    calc = calc.input('+');  // Leading plus (ignored)
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('+');
    calc = calc.input('-');  // Consecutive operators (use -)
    calc = calc.input('=');  // Missing second operand (use 10)
    Assert.assertEquals("0", calc.getResult()); // 10 - 10 = 0

    // Multiple equals
    calc = calc.input('=');
    Assert.assertEquals("-10", calc.getResult()); // 0 - 10 = -10
  }

  /**
   * Test all smart features together.
   */
  @Test
  public void testAllSmartFeaturesTogether() {
    Calculator calc = createCalculator();

    // Leading plus
    calc = calc.input('+');
    calc = calc.input('1');
    calc = calc.input('2');

    // Consecutive operators
    calc = calc.input('+');
    calc = calc.input('-');
    calc = calc.input('*');

    // Missing second operand
    calc = calc.input('=');
    Assert.assertEquals("144", calc.getResult()); // 12 * 12 = 144

    // Multiple equals
    calc = calc.input('=');
    Assert.assertEquals("1728", calc.getResult()); // 144 * 12 = 1728
  }

  /**
   * Test zero with smart features.
   */
  @Test
  public void testZeroWithSmartFeatures() {
    Calculator calc = createCalculator();
    calc = calc.input('0');
    calc = calc.input('+');
    calc = calc.input('='); // 0 + 0 = 0
    Assert.assertEquals("0", calc.getResult());

    calc = calc.input('='); // 0 + 0 = 0
    Assert.assertEquals("0", calc.getResult());
  }

  /**
   * Test negative results with multiple equals.
   */
  @Test
  public void testNegativeWithMultipleEquals() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('-');
    calc = calc.input('1');
    calc = calc.input('0');
    calc = calc.input('=');
    Assert.assertEquals("-5", calc.getResult());

    calc = calc.input('='); // -5 - 10 = -15
    Assert.assertEquals("-15", calc.getResult());

    calc = calc.input('='); // -15 - 10 = -25
    Assert.assertEquals("-25", calc.getResult());
  }

  /**
   * Test smart features after clear.
   */
  @Test
  public void testSmartFeaturesAfterClear() {
    Calculator calc = createCalculator();
    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('=');
    calc = calc.input('C'); // Clear

    // Should start fresh - leading plus should work
    calc = calc.input('+');
    calc = calc.input('7');
    calc = calc.input('*');
    calc = calc.input('2');
    calc = calc.input('=');
    Assert.assertEquals("14", calc.getResult());
  }

  /**
   * Test that invalid input after leading plus throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInputAfterLeadingPlus() {
    Calculator calc = createCalculator();
    calc = calc.input('+');
    calc = calc.input('a'); // Invalid character
  }

  /**
   * Test that operators after leading plus (without digits) throw exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOperatorAfterLeadingPlus() {
    Calculator calc = createCalculator();
    calc = calc.input('+');
    calc = calc.input('-'); // Should throw exception
  }

  /**
   * Test equals immediately after leading plus throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEqualsAfterLeadingPlus() {
    Calculator calc = createCalculator();
    calc = calc.input('+');
    calc = calc.input('='); // Should throw exception
  }

  /**
   * Test that SmartCalculator behavior differs from SimpleCalculator for repeated equals.
   */
  @Test
  public void testSmartVsSimpleBehaviorDifference() {
    Calculator calc = createCalculator();
    calc = calc.input('7');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());

    // Smart calculator should repeat the operation
    calc = calc.input('=');
    Assert.assertEquals("13", calc.getResult()); // 10 + 3 = 13 (smart behavior)

    // Not 10 like SimpleCalculator would return
    Assert.assertNotEquals("10", calc.getResult());
  }
}