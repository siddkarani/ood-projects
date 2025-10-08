package view;

/**
 * Displays output to the user.
 */
public interface View {
  /**
   * Displays a message to the user.
   * @param message message to be displayed
   */
  void displayMessage(String message);

  /**
   * Displays an error message to the user.
   * @param error error message to be displayed
   */
  void displayError(String error);
}