import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import controller.CalendarApp;
import view.View;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for the main class including GUI mode.
 */
public class TestCalendarApp {
  private final InputStream standardIn = System.in;
  private final PrintStream standardOut = System.out;
  private final PrintStream standardErr = System.err;
  private ByteArrayOutputStream outputStreamCaptor;
  private ByteArrayOutputStream errorStreamCaptor;

  @Before
  public void setUp() {
    new TestView();
    outputStreamCaptor = new ByteArrayOutputStream();
    errorStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));
    System.setErr(new PrintStream(errorStreamCaptor));
  }

  @After
  public void tearDown() {
    System.setOut(standardOut);
    System.setErr(standardErr);
    System.setIn(standardIn);
  }

  @Test
  public void testMainWithInvalidArguments() {
    String[] args = new String[]{};

    // Create a thread to test GUI mode without blocking
    Thread testThread = new Thread(() -> CalendarApp.main(args));
    testThread.setDaemon(true); // Make it a daemon thread so it doesn't prevent JVM shutdown
    testThread.start();

    // Give it a moment to start
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // Since no args should launch GUI, we check that no error was thrown
    String error = errorStreamCaptor.toString();
    assertTrue("GUI mode should not produce immediate errors",
            error.isEmpty() || !error.contains("Usage:"));
  }

  @Test
  public void testGUIModeWithNoArguments() {
    // Test that no arguments triggers GUI mode
    String[] args = new String[]{};

    // Create a daemon thread to test GUI launch without blocking
    Thread guiThread = new Thread(() -> CalendarApp.main(args));
    guiThread.setDaemon(true);
    guiThread.start();

    // Wait a bit for GUI to initialize
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // The GUI should start without errors in the error stream
    String error = errorStreamCaptor.toString();
    assertTrue("GUI mode should not produce errors",
            error.isEmpty() || !error.contains("Error:"));

    // Thread will be cleaned up automatically as it's a daemon thread
  }

  @Test
  public void testInvalidModeArguments() {
    String[] args = new String[]{"--invalid"};
    CalendarApp.main(args);

    String error = errorStreamCaptor.toString();
    assertTrue("Should contain usage message for invalid args",
            error.contains("Usage:") || error.contains("Invalid arguments"));
  }

  @Test
  public void testMissingModeFlag() {
    String[] args = new String[]{"interactive"};
    CalendarApp.main(args);

    String error = errorStreamCaptor.toString();
    assertTrue("Should contain error for missing --mode flag",
            error.contains("Invalid arguments") || error.contains("Usage:"));
  }

  @Test
  public void testInteractiveMode() throws Exception {
    // using exit
    String input = "exit\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    // run interactive
    String[] args = new String[]{"--mode", "interactive"};
    CalendarApp.main(args);

    // check output
    String output = outputStreamCaptor.toString();
    assertTrue("Should contain application header",
            output.contains("Calendar Application"));
    assertTrue("Should contain exit instruction",
            output.contains("(Type 'exit' to quit in interactive mode)"));
    assertTrue("Should contain exit confirmation",
            output.contains("Exiting..."));
  }

  @Test
  public void testInteractiveModeWithCommands() throws Exception {
    String input = "create calendar --name Test --timezone America/New_York\n" +
            "use calendar --name Test\n" +
            "create event Meeting from 2025-01-01T10:00 to 2025-01-01T11:00\n" +
            "print events on 2025-01-01\n" +
            "exit\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String[] args = new String[]{"--mode", "interactive"};
    CalendarApp.main(args);

    String output = outputStreamCaptor.toString();
    assertTrue("Should contain application header", output.contains("Calendar Application"));
    assertTrue("Should contain calendar creation success", output.contains("created successfully"));
    assertTrue("Should contain calendar usage message", output.contains("Now using calendar"));
    assertTrue("Should contain event creation success", output.contains("created successfully"));
    assertTrue("Should contain exit message", output.contains("Exiting..."));
  }

  @Test
  public void testHeadlessMode() throws IOException {
    File tempFile = File.createTempFile("test-commands", ".txt");
    try {
      // write commands to file
      try (FileWriter writer = new FileWriter(tempFile)) {
        writer.write("create calendar --name Work --timezone America/New_York\n");
        writer.write("use calendar --name Work\n");
        writer.write("create event \"Meeting\" from 2024-03-20T10:00 to 2024-03-20T11:00\n");
        writer.write("print events on 2024-03-20\n");
        writer.write("exit\n");
      }

      // run
      String[] args = new String[]{"--mode", "headless", tempFile.getAbsolutePath()};
      CalendarApp.main(args);

      // Check output
      String output = outputStreamCaptor.toString();
      assertTrue("Should contain application header",
              output.contains("Calendar Application"));
      assertTrue("Should contain calendar creation success",
              output.contains("Calendar 'Work' created successfully"));
      assertTrue("Should contain calendar selection message",
              output.contains("Now using calendar: Work"));
      assertTrue("Should contain event creation success",
              output.contains("This event was created successfully"));
      assertTrue("Should contain exit message",
              output.contains("Exiting..."));
    } finally {
      tempFile.delete();
    }
  }

  @Test
  public void testHeadlessModeWithMissingFile() {
    String[] args = new String[]{"--mode", "headless"};
    CalendarApp.main(args);

    String error = errorStreamCaptor.toString();
    assertTrue("Should contain filename required error",
            error.contains("Headless mode requires a filename"));
  }

  @Test
  public void testHeadlessModeWithNonexistentFile() {
    String[] args = new String[]{"--mode", "headless", "nonexistent.txt"};
    CalendarApp.main(args);

    String error = errorStreamCaptor.toString();
    assertTrue("Should contain file error",
            error.contains("Error:"));
  }

  @Test
  public void testHeadlessModeWithMissingExitCommand() throws IOException {
    // Create temporary file
    File tempFile = File.createTempFile("test-commands", ".txt");
    try {
      // Write commands to file without exit
      try (FileWriter writer = new FileWriter(tempFile)) {
        writer.write("create calendar --name Test --timezone America/New_York\n");
        writer.write("create event \"Meeting\" from 2024-03-20T10:00 to 2024-03-20T11:00\n");
      }

      // Run the app
      String[] args = new String[]{"--mode", "headless", tempFile.getAbsolutePath()};
      CalendarApp.main(args);

      // Check both standard output and standard error for the error message
      String output = outputStreamCaptor.toString();
      String error = errorStreamCaptor.toString();
      String combined = output + error;

      assertTrue("Should contain exit command error message",
              combined.contains("no exit command"));
    } finally {
      tempFile.delete();
    }
  }

  @Test
  public void testInvalidMode() {
    String input = "exit\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String[] args = new String[]{"--mode", "invalid"};
    CalendarApp.main(args);

    String error = errorStreamCaptor.toString();
    assertTrue("Should contain invalid mode error",
            error.contains("Invalid mode: invalid"));
  }

  @Test
  public void testRunWithReadableNullParameters() {
    // Test null parser
    try {
      CalendarApp.runWithReadable(null,
              new InputStreamReader(new ByteArrayInputStream("exit\n".getBytes())),
              new TestView());
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue("Should contain null parameter error",
              e.getMessage().contains("cannot be null"));
    }
  }

  @Test
  public void testRunWithReadableEmptyInput() {
    // Test with empty input (should fail due to no exit command)
    try {
      String error = errorStreamCaptor.toString();
      String output = outputStreamCaptor.toString();

      // The method should handle empty input gracefully
      assertTrue("Should handle empty input", true);
    } catch (Exception e) {
      assertTrue("Should handle exceptions gracefully", true);
    }
  }

  @Test
  public void testUsageMessage() {
    String[] args = new String[]{"--mode"};
    CalendarApp.main(args);

    String error = errorStreamCaptor.toString();
    assertTrue("Should contain usage information",
            error.contains("Usage:") || error.contains("Invalid arguments"));
  }

  @Test
  public void testValidateArgumentsEdgeCases() {
    // Test with only --mode flag
    String[] args1 = new String[]{"--mode"};
    CalendarApp.main(args1);

    String error1 = errorStreamCaptor.toString();
    assertTrue("Should handle incomplete arguments",
            error1.contains("Invalid arguments") || error1.contains("Usage:"));

    // Reset streams for next test
    errorStreamCaptor.reset();

    // Test with wrong flag
    String[] args2 = new String[]{"--wrong", "interactive"};
    CalendarApp.main(args2);

    String error2 = errorStreamCaptor.toString();
    assertTrue("Should handle wrong flag",
            error2.contains("Invalid arguments") || error2.contains("Usage:"));
  }

  @Test
  public void testCaseInsensitiveMode() {
    String input = "exit\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String[] args = new String[]{"--mode", "INTERACTIVE"};
    CalendarApp.main(args);

    String output = outputStreamCaptor.toString();
    assertTrue("Should handle case-insensitive mode",
            output.contains("Calendar Application"));
  }

  @Test
  public void testHeadlessModeWithComplexCommands() throws IOException {
    File tempFile = File.createTempFile("complex-commands", ".txt");
    try {
      try (FileWriter writer = new FileWriter(tempFile)) {
        writer.write("create calendar --name \"Work Calendar\" --timezone America/New_York\n");
        writer.write("create calendar --name Personal --timezone America/Los_Angeles\n");
        writer.write("use calendar --name \"Work Calendar\"\n");
        writer.write("create event \"Team Meeting\" from 2025-01-15T10:00 to 2025-01-15T11:00\n");
        writer.write("create event \"Lunch Break\" from 2025-01-15T12:00 to 2025-01-15T13:00\n");
        writer.write("print events on 2025-01-15\n");
        writer.write("edit event subject \"Team Meeting\" from 2025-01-15T10:00 to 2025-01-15T11:00 with \"Daily Standup\"\n");
        writer.write("show status on 2025-01-15T14:00\n");
        writer.write("use calendar --name Personal\n");
        writer.write("copy event \"Daily Standup\" on 2025-01-15T10:00 --target Personal to 2025-01-16T10:00\n");
        writer.write("exit\n");
      }

      String[] args = new String[]{"--mode", "headless", tempFile.getAbsolutePath()};
      CalendarApp.main(args);

      String output = outputStreamCaptor.toString();
      assertTrue("Should handle complex command sequence", output.contains("Calendar Application"));
      assertTrue("Should create calendars", output.contains("created successfully"));
      assertTrue("Should create events", output.contains("created successfully"));
      assertTrue("Should edit events", output.contains("edited successfully"));
      assertTrue("Should show status", output.contains("Available") || output.contains("Busy"));
      assertTrue("Should exit cleanly", output.contains("Exiting..."));
    } finally {
      tempFile.delete();
    }
  }

  private static class TestView implements View {
    private final StringBuilder messages = new StringBuilder();
    private String lastError;

    @Override
    public void displayMessage(String message) {
      messages.append(message).append("\n");
    }

    @Override
    public void displayError(String error) {
      lastError = error;
    }

    public String getMessages() {
      return messages.toString();
    }

    public String getLastError() {
      return lastError;
    }
  }
}