import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import view.ViewForConsole;

import static org.junit.Assert.assertEquals;

/**
 * Tests the console view.
 */
public class TestViewForConsole {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;
  private ViewForConsole view;

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
    view = new ViewForConsole();
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testDisplayMessage() {
    String message = "Test message";
    view.displayMessage(message);
    assertEquals(message + System.lineSeparator(), outContent.toString());
  }

  @Test
  public void testDisplayError() {
    String error = "Test error";
    view.displayError(error);
    assertEquals("Error: " + error + System.lineSeparator(), errContent.toString());
  }

  @Test
  public void testDisplayMultipleMessages() {
    String message1 = "First message";
    String message2 = "Second message";

    view.displayMessage(message1);
    view.displayMessage(message2);

    String expected = message1 + System.lineSeparator() +
            message2 + System.lineSeparator();
    assertEquals(expected, outContent.toString());
  }

  @Test
  public void testDisplayMultipleErrors() {
    String error1 = "First error";
    String error2 = "Second error";

    view.displayError(error1);
    view.displayError(error2);

    String expected = "Error: " + error1 + System.lineSeparator() +
            "Error: " + error2 + System.lineSeparator();
    assertEquals(expected, errContent.toString());
  }

  @Test
  public void testDisplayEmptyMessage() {
    view.displayMessage("");
    assertEquals(System.lineSeparator(), outContent.toString());
  }

  @Test
  public void testDisplayEmptyError() {
    view.displayError("");
    assertEquals("Error: " + System.lineSeparator(), errContent.toString());
  }

  @Test
  public void testDisplayNullMessage() {
    view.displayMessage(null);
    assertEquals("null" + System.lineSeparator(), outContent.toString());
  }

  @Test
  public void testDisplayNullError() {
    view.displayError(null);
    assertEquals("Error: null" + System.lineSeparator(), errContent.toString());
  }

  @Test
  public void testDisplayMessageWithSpecialCharacters() {
    String message = "Test\nwith\tspecial\rcharacters";
    view.displayMessage(message);
    assertEquals(message + System.lineSeparator(), outContent.toString());
  }
}