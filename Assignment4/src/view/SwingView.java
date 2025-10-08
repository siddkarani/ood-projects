package view;

import javax.swing.*;

public class SwingView implements View {
  @Override
  public void displayMessage(String message) {
    // For GUI, we might want to show important messages in a dialog
    if (message != null && !message.trim().isEmpty()) {
      SwingUtilities.invokeLater(() ->
              JOptionPane.showMessageDialog(null, message));
    }
  }

  @Override
  public void displayError(String message) {
    SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(null, message, "Error",
                    JOptionPane.ERROR_MESSAGE));
  }
}