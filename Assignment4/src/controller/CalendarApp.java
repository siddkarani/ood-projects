package controller;

import java.io.FileReader;
import java.util.Scanner;
import model.Calendars;
import model.MultipleCalendars;
import view.View;
import view.ViewForConsole;
import view.CalendarGUI;
import java.io.InputStreamReader;
import javax.swing.SwingUtilities;

/**
 * Main application class for the Calendar application. Represents a Calendar App that can
 * be run in either interactive, headless, or GUI mode.
 */
public class CalendarApp {

  /**
   * Entry point for the CalendarApp.
   * Supports interactive, headless, and GUI modes.
   *
   * @param args command line arguments specifying mode and optional file
   */
  public static void main(String[] args) {
    try {
      // No arguments = GUI mode
      if (args.length == 0) {
        SwingUtilities.invokeLater(() -> {
          new CalendarGUI().setVisible(true);
        });
        return;
      }

      // Validate arguments for text modes
      validateArguments(args);
      View view = new ViewForConsole();
      Calendars calendars = new MultipleCalendars(view);
      EnhancedCommandParser parser = new EnhancedCommandParser(calendars, view);

      String mode = args[1].toLowerCase();

      // delegate to appropriate method based on mode argument
      if ("interactive".equals(mode)) {
        runWithReadable(parser, new InputStreamReader(System.in), view);
      } else if ("headless".equals(mode)) {
        if (args.length < 3) {
          throw new IllegalArgumentException("Headless mode requires a filename");
        }
        runWithReadable(parser, new FileReader(args[2]), view);
      } else {
        throw new IllegalArgumentException("Invalid mode: " + mode);
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      printUsage();
    }
  }

  /**
   * Validates command line arguments.
   * @param args the command line arguments to validate
   * @throws IllegalArgumentException  throws an exception if invalid
   */
  private static void validateArguments(String[] args) throws IllegalArgumentException {
    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      throw new IllegalArgumentException("Invalid arguments");
    }
  }

  /**
   * Prints usage information.
   */
  private static void printUsage() {
    System.out.println("Usage:");
    System.out.println("  java -jar Program.jar                           (GUI mode)");
    System.out.println("  java -jar Program.jar --mode interactive        (Interactive text mode)");
    System.out.println("  java -jar Program.jar --mode headless <file>    (Headless mode)");
  }

  /**
   * Processes commands using any `Readable` source (interactive or headless mode).
   *
   * @param parser the command parser to process commands
   * @param inputSource the readable source for commands
   * @param view the view for displaying results
   */
  public static void runWithReadable(EnhancedCommandParser parser, Readable inputSource,
                                     View view) {
    if (parser == null || inputSource == null || view == null) {
      throw new IllegalArgumentException("Parser, input source, and view cannot be null");
    }

    try (Scanner scanner = new Scanner(inputSource)) {
      view.displayMessage("Calendar Application");
      view.displayMessage("(Type 'exit' to quit in interactive mode)");

      boolean foundExit = false;

      while (scanner.hasNextLine()) {
        String command = scanner.nextLine().trim();

        if (command.isEmpty()) {
          continue;
        }

        // display the result and execute
        String result = parser.executeCommand(command);
        view.displayMessage(result);

        if (command.equalsIgnoreCase("exit")) {
          foundExit = true;
          break;
        }
      }

      if (!foundExit) {
        throw new IllegalStateException("There is no exit command in this input.");
      }
    } catch (Exception e) {
      view.displayError("Error processing commands: " + e.getMessage());
    }
  }
}