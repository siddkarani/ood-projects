package calculator;

/**
 * This interface represents a calculator that processes input one character at a time.
 * It simulates a calculator where each input represents a button press, processing inputs
 * "just in time" rather than waiting for an entire expression.
 */
public interface Calculator {

  /**
   * Process a single character input.
   *
   * @param c the character to process as input
   * @return a Calculator object that represents the calculator after processing the input
   * @throws IllegalArgumentException if the input is invalid or causes an overflow in an operand
   */
  Calculator input(char c) throws IllegalArgumentException;

  /**
   * Get the current result of the calculator.
   * This represents what would be displayed on the calculator screen.
   *
   * @return the current result as a String
   */
  String getResult();
}