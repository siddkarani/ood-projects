package calculator;

/**
 * Implementation of the Calculator interface that processes single-character inputs
 * for basic arithmetic operations with "smart" features. This calculator is backward
 * compatible with SimpleCalculator but adds advanced functionality like multiple equals,
 * missing operand handling, consecutive operators, and leading plus operator support.
 */
public class SmartCalculator implements Calculator {
  private String currentDisplay;
  private int firstOperand;
  private int secondOperand;
  private char operator;
  private State currentState;

  // Smart calculator specific fields
  private int lastOperand;      // For repeated operations
  private char lastOperator;    // For repeated operations
  private boolean hasLastOperation; // Track if we have a last operation to repeat

  private enum State {
    INITIAL,
    BUILDING_FIRST_OPERAND,
    OPERATOR_ENTERED,
    BUILDING_SECOND_OPERAND,
    RESULT_DISPLAYED,
    LEADING_PLUS_ENTERED  // Special state for leading +
  }

  /**
   * Creates a new SmartCalculator with initial empty state.
   */
  public SmartCalculator() {
    this.currentDisplay = "";
    this.firstOperand = 0;
    this.secondOperand = 0;
    this.operator = '\0';
    this.currentState = State.INITIAL;
    this.lastOperand = 0;
    this.lastOperator = '\0';
    this.hasLastOperation = false;
  }

  /**
   * Private constructor for creating a new calculator instance with specified state.
   */
  private SmartCalculator(String currentDisplay, int firstOperand,
                          int secondOperand,
                          char operator, State currentState, int lastOperand,
                          char lastOperator, boolean hasLastOperation) {
    this.currentDisplay = currentDisplay;
    this.firstOperand = firstOperand;
    this.secondOperand = secondOperand;
    this.operator = operator;
    this.currentState = currentState;
    this.lastOperand = lastOperand;
    this.lastOperator = lastOperator;
    this.hasLastOperation = hasLastOperation;
  }

  @Override
  public Calculator input(char c) {
    if (c == 'C') {
      return new SmartCalculator();
    }

    SmartCalculator newCalculator = new SmartCalculator(
            this.currentDisplay,
            this.firstOperand,
            this.secondOperand,
            this.operator,
            this.currentState,
            this.lastOperand,
            this.lastOperator,
            this.hasLastOperation
    );

    switch (newCalculator.currentState) {
      case INITIAL:
        return handleInitialState(newCalculator, c);
      case BUILDING_FIRST_OPERAND:
        return handleBuildingFirstOperand(newCalculator, c);
      case OPERATOR_ENTERED:
        return handleOperatorEntered(newCalculator, c);
      case BUILDING_SECOND_OPERAND:
        return handleBuildingSecondOperand(newCalculator, c);
      case RESULT_DISPLAYED:
        return handleResultDisplayed(newCalculator, c);
      case LEADING_PLUS_ENTERED:
        return handleLeadingPlusEntered(newCalculator, c);
      default:
        throw new IllegalStateException("Invalid calculator state");
    }
  }

  /**
   * Handles input in the initial state.
   */
  private Calculator handleInitialState(SmartCalculator calc, char c) {
    if (Character.isDigit(c)) {
      calc.firstOperand = c - '0';
      calc.currentDisplay = Character.toString(c);
      calc.currentState = State.BUILDING_FIRST_OPERAND;
      return calc;
    } else if (c == '+') {
      // Smart feature: leading plus is allowed
      calc.currentState = State.LEADING_PLUS_ENTERED;
      calc.currentDisplay = "";
      return calc;
    } else if (c == '-' || c == '*') {
      // Other operators at start are invalid
      throw new IllegalArgumentException("Invalid input in initial state: " + c);
    } else if (c == '=') {
      throw new IllegalArgumentException("Invalid equals input: no operands entered");
    } else {
      throw new IllegalArgumentException("Invalid input in initial state: " + c);
    }
  }

  /**
   * Handles input after leading plus.
   */
  private Calculator handleLeadingPlusEntered(SmartCalculator calc, char c) {
    if (Character.isDigit(c)) {
      // Start building first operand, ignoring the leading plus
      calc.firstOperand = c - '0';
      calc.currentDisplay = Character.toString(c);
      calc.currentState = State.BUILDING_FIRST_OPERAND;
      return calc;
    } else {
      throw new IllegalArgumentException("Invalid input after leading plus: " + c);
    }
  }

  /**
   * Handles input while building the first operand.
   */
  private Calculator handleBuildingFirstOperand(SmartCalculator calc, char c) {
    if (Character.isDigit(c)) {
      long newValue = (long) calc.firstOperand * 10 + (c - '0');
      if (newValue > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Operand overflow");
      }
      calc.firstOperand = (int) newValue;
      calc.currentDisplay += c;
      return calc;
    } else if (c == '+' || c == '-' || c == '*') {
      calc.operator = c;
      calc.currentDisplay += c;
      calc.currentState = State.OPERATOR_ENTERED;
      return calc;
    } else if (c == '=') {
      // Only allow if we have a previous operation to repeat
      if (calc.hasLastOperation) {
        return performOperation(calc, calc.lastOperator,
                calc.lastOperand, false);
      } else {
        throw new IllegalArgumentException("Invalid equals input: no operator entered");
      }
    } else {
      throw new IllegalArgumentException("Invalid input while" +
              " building first operand: " + c);
    }
  }

  /**
   * Handles input after an operator has been entered.
   */
  private Calculator handleOperatorEntered(SmartCalculator calc, char c) {
    if (Character.isDigit(c)) {
      calc.secondOperand = c - '0';
      calc.currentDisplay += c;
      calc.currentState = State.BUILDING_SECOND_OPERAND;
      return calc;
    } else if (c == '+' || c == '-' || c == '*') {
      // Smart feature: consecutive operators - replace the previous operator
      calc.operator = c;
      calc.currentDisplay = calc.currentDisplay.substring(0,
              calc.currentDisplay.length() - 1) + c;
      return calc;
    } else if (c == '=') {
      // Smart feature: missing second operand, use first operand as second
      return performOperation(calc, calc.operator, calc.firstOperand,
              true);
    } else {
      throw new IllegalArgumentException("Invalid input after operator: " + c);
    }
  }

  /**
   * Handles input while building the second operand.
   */
  private Calculator handleBuildingSecondOperand(SmartCalculator calc,
                                                 char c) {
    if (Character.isDigit(c)) {
      long newValue = (long) calc.secondOperand * 10 + (c - '0');
      if (newValue > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Operand overflow");
      }
      calc.secondOperand = (int) newValue;
      calc.currentDisplay += c;
      return calc;
    } else if (c == '=') {
      return performOperation(calc, calc.operator,
              calc.secondOperand, true);
    } else if (c == '+' || c == '-' || c == '*') {
      // Smart feature: Just-in-time evaluation + consecutive operators
      // First evaluate current operation
      Calculator intermediate = performOperation(calc, calc.operator,
              calc.secondOperand, true);
      SmartCalculator newCalc = (SmartCalculator) intermediate;

      // Then set up new operation with consecutive operator
      newCalc.operator = c;
      newCalc.currentDisplay = newCalc.currentDisplay + c;
      newCalc.currentState = State.OPERATOR_ENTERED;

      return newCalc;
    } else {
      throw new IllegalArgumentException("Invalid input while" +
              " building second operand: " + c);
    }
  }

  /**
   * Handles input after a result is displayed.
   */
  private Calculator handleResultDisplayed(SmartCalculator calc,
                                           char c) {
    if (Character.isDigit(c)) {
      // Start a new calculation with this digit
      calc.firstOperand = c - '0';
      calc.secondOperand = 0;
      calc.operator = '\0';
      calc.currentDisplay = Character.toString(c);
      calc.currentState = State.BUILDING_FIRST_OPERAND;
      return calc;
    } else if (c == '+' || c == '-' || c == '*') {
      // Continue calculation with the result as first operand
      int currentResult;
      try {
        currentResult = Integer.parseInt(calc.currentDisplay);
      } catch (NumberFormatException e) {
        currentResult = 0;
      }

      calc.firstOperand = currentResult;
      calc.operator = c;
      calc.secondOperand = 0;
      calc.currentDisplay = calc.currentDisplay + c;
      calc.currentState = State.OPERATOR_ENTERED;
      return calc;
    } else if (c == '=') {
      // Smart feature: multiple equals - repeat last operation
      if (calc.hasLastOperation) {
        int currentResult;
        try {
          currentResult = Integer.parseInt(calc.currentDisplay);
        } catch (NumberFormatException e) {
          currentResult = 0;
        }
        calc.firstOperand = currentResult;
        return performOperation(calc, calc.lastOperator,
                calc.lastOperand, false);
      } else {
        // No last operation to repeat, just return same result
        return calc;
      }
    } else {
      throw new IllegalArgumentException("Invalid input " +
              "after result: " + c);
    }
  }

  /**
   * Performs the arithmetic operation and handles overflow.
   */
  private Calculator performOperation(SmartCalculator calc,
                                      char op, int operand,
                                      boolean updateLastOperation) {
    int result;

    try {
      switch (op) {
        case '+':
          result = Math.addExact(calc.firstOperand, operand);
          break;
        case '-':
          result = Math.subtractExact(calc.firstOperand, operand);
          break;
        case '*':
          result = Math.multiplyExact(calc.firstOperand, operand);
          break;
        default:
          throw new IllegalArgumentException("Invalid operator: " + op);
      }
    } catch (ArithmeticException e) {
      // If the arithmetic overflows, set result to 0
      result = 0;
    }

    if (updateLastOperation) {
      // Store the operation for potential repetition
      calc.lastOperator = op;
      calc.lastOperand = operand;
      calc.hasLastOperation = true;
    }

    calc.currentDisplay = String.valueOf(result);
    calc.firstOperand = result;
    calc.secondOperand = 0;
    calc.operator = '\0';
    calc.currentState = State.RESULT_DISPLAYED;
    return calc;
  }

  @Override
  public String getResult() {
    return currentDisplay;
  }
}