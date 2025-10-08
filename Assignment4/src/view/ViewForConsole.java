package view;

/**
 * Displays output to the console.
 */
public class ViewForConsole implements View {

  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  @Override
  public void displayError(String error) {
    System.err.println("Error: " + error);
  }
}