package calculator;

/**
 * Implementation of the Calculator interface that processes single-character inputs
 * for basic arithmetic operations (addition, subtraction, multiplication) with
 * whole numbers. It evaluates expressions just-in-time and displays the current state
 * or result.
 */
public class SimpleCalculator implements Calculator {
  private String currentDisplay;
  private int firstOperand;
  private int secondOperand;
  private char operator;
  private State currentState;

  private enum State {
    INITIAL,
    BUILDING_FIRST_OPERAND,
    OPERATOR_ENTERED,
    BUILDING_SECOND_OPERAND,
    RESULT_DISPLAYED
  }

  /**
   * Creates a new SimpleCalculator with initial empty state.
   */
  public SimpleCalculator() {
    this.currentDisplay = "";
    this.firstOperand = 0;
    this.secondOperand = 0;
    this.operator = '\0';
    this.currentState = State.INITIAL;
  }

  /**
   * Private constructor for creating a new calculator instance with specified state.
   */
  private SimpleCalculator(String currentDisplay, int firstOperand, int secondOperand,
                           char operator, State currentState) {
    this.currentDisplay = currentDisplay;
    this.firstOperand = firstOperand;
    this.secondOperand = secondOperand;
    this.operator = operator;
    this.currentState = currentState;
  }

  @Override
  public Calculator input(char c) {
    if (c == 'C') {
      return new SimpleCalculator();
    }

    SimpleCalculator newCalculator = new SimpleCalculator(
            this.currentDisplay,
            this.firstOperand,
            this.secondOperand,
            this.operator,
            this.currentState
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
      default:
        throw new IllegalStateException("Invalid calculator state");
    }
  }

  private Calculator handleInitialState(SimpleCalculator calc, char c) {
    if (Character.isDigit(c)) {
      calc.firstOperand = c - '0';
      calc.currentDisplay = Character.toString(c);
      calc.currentState = State.BUILDING_FIRST_OPERAND;
      return calc;
    } else {
      throw new IllegalArgumentException("Invalid input in initial state: " + c);
    }
  }

  private Calculator handleBuildingFirstOperand(SimpleCalculator calc, char c) {
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
      throw new IllegalArgumentException("Invalid equals input: no operator entered");
    } else {
      throw new IllegalArgumentException("Invalid input while building first operand: " + c);
    }
  }

  private Calculator handleOperatorEntered(SimpleCalculator calc, char c) {
    if (Character.isDigit(c)) {
      calc.secondOperand = c - '0';
      calc.currentDisplay += c;
      calc.currentState = State.BUILDING_SECOND_OPERAND;
      return calc;
    } else if (c == '+' || c == '-' || c == '*') {
      throw new IllegalArgumentException("Invalid consecutive operators");
    } else if (c == '=') {
      throw new IllegalArgumentException("Invalid equals input: no second operand");
    } else {
      throw new IllegalArgumentException("Invalid input after operator: " + c);
    }
  }

  private Calculator handleBuildingSecondOperand(SimpleCalculator calc, char c) {
    if (Character.isDigit(c)) {
      long newValue = (long) calc.secondOperand * 10 + (c - '0');
      if (newValue > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Operand overflow");
      }
      calc.secondOperand = (int) newValue;
      calc.currentDisplay += c;
      return calc;
    } else if (c == '=') {
      return performOperation(calc, calc.operator, calc.secondOperand);
    } else if (c == '+' || c == '-' || c == '*') {
      // Just-in-time evaluation: evaluate current operation first
      Calculator result = performOperation(calc, calc.operator, calc.secondOperand);
      SimpleCalculator newCalc = (SimpleCalculator) result;

      // Start new operation
      newCalc.operator = c;
      newCalc.secondOperand = 0;
      newCalc.currentDisplay = newCalc.currentDisplay + c;
      newCalc.currentState = State.OPERATOR_ENTERED;

      return newCalc;
    } else {
      throw new IllegalArgumentException("Invalid input while building second operand: " + c);
    }
  }

  private Calculator handleResultDisplayed(SimpleCalculator calc, char c) {
    if (Character.isDigit(c)) {
      calc.firstOperand = c - '0';
      calc.secondOperand = 0;
      calc.operator = '\0';
      calc.currentDisplay = Character.toString(c);
      calc.currentState = State.BUILDING_FIRST_OPERAND;
      return calc;
    } else if (c == '+' || c == '-' || c == '*') {
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
      return calc; // Repeated equals returns same result
    } else {
      throw new IllegalArgumentException("Invalid input after result: " + c);
    }
  }

  private Calculator performOperation(SimpleCalculator calc, char op, int operand) {
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
      result = 0; // Overflow results in 0
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