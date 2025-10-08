import org.junit.Test;
import org.junit.Assert;
import calculator.Calculator;
import calculator.SmartCalculator;

/**
 * Debug test for SmartCalculator assignment examples.
 */
public class DebugTest {

  /**
   * Test the exact assignment examples.
   */
  @Test
  public void testAssignmentExamples() {
    Calculator calc = new SmartCalculator();

    // Test: 32 + 24 = produces 56
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("56", calc.getResult());

    // Test: 32 + 24 = = produces 80
    calc = calc.input('=');
    Assert.assertEquals("80", calc.getResult());

    // Test: 32 + 24 = = = produces 104
    calc = calc.input('=');
    Assert.assertEquals("104", calc.getResult());
  }

  /**
   * Test missing second operand examples.
   */
  @Test
  public void testMissingOperandExamples() {
    Calculator calc = new SmartCalculator();

    // Test: 32 + = produces 64
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('=');
    Assert.assertEquals("64", calc.getResult());

    // Test: 32 + = = produces 96
    calc = calc.input('=');
    Assert.assertEquals("96", calc.getResult());
  }

  /**
   * Test consecutive operators examples.
   */
  @Test
  public void testConsecutiveOperatorExamples() {
    Calculator calc = new SmartCalculator();

    // Test: 32 + - 24 = produces 8
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('+');
    calc = calc.input('-');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("8", calc.getResult());
  }

  /**
   * Test leading plus examples.
   */
  @Test
  public void testLeadingPlusExamples() {
    Calculator calc = new SmartCalculator();

    // Test: + 32 - 24 = produces 8
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('2');
    calc = calc.input('-');
    calc = calc.input('2');
    calc = calc.input('4');
    calc = calc.input('=');
    Assert.assertEquals("8", calc.getResult());
  }

  /**
   * Test edge case with equals after result.
   */
  @Test
  public void testEdgeCases() {
    Calculator calc = new SmartCalculator();

    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('=');
    Assert.assertEquals("8", calc.getResult());

    // Press equals again - should repeat operation
    calc = calc.input('=');
    Assert.assertEquals("11", calc.getResult());

    // Start new operation after result
    calc = calc.input('+');
    calc = calc.input('2');
    calc = calc.input('=');
    Assert.assertEquals("13", calc.getResult());
  }

  /**
   * Test display behavior during consecutive operators.
   */
  @Test
  public void testDisplayBehavior() {
    Calculator calc = new SmartCalculator();

    calc = calc.input('1');
    calc = calc.input('0');
    Assert.assertEquals("10", calc.getResult());

    calc = calc.input('+');
    Assert.assertEquals("10+", calc.getResult());

    calc = calc.input('-');
    Assert.assertEquals("10-", calc.getResult());

    calc = calc.input('5');
    calc = calc.input('=');
    Assert.assertEquals("5", calc.getResult());
  }

  /**
   * Test state management after clear.
   */
  @Test
  public void testStateManagement() {
    Calculator calc = new SmartCalculator();

    calc = calc.input('5');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('=');
    calc = calc.input('C');

    // After clear, leading plus should work
    calc = calc.input('+');
    calc = calc.input('7');
    calc = calc.input('+');
    calc = calc.input('3');
    calc = calc.input('=');
    Assert.assertEquals("10", calc.getResult());
  }
}