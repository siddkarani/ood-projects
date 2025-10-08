import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import controller.EnhancedCommandParser;
import model.Calendars;
import model.MultipleCalendars;
import view.View;
import view.ViewForConsole;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for enhanced calendar functionality.
 * Tests multiple calendars, timezones, and event copying capabilities.
 */
public class TestEnhancedCalendarFunctionality {
  private EnhancedCommandParser parser;
  private View view;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setup() {
    System.setOut(new PrintStream(outContent));
    view = new ViewForConsole();
    Calendars calendars = new MultipleCalendars(view);
    parser = new EnhancedCommandParser(calendars, view);
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  public void testCreateCalendar() {
    String result = parser.executeCommand("create calendar --name Work --timezone" +
            " America/New_York");
    assertEquals("Calendar 'Work' created successfully", result);

    // test duplicate name
    result = parser.executeCommand("create calendar --name Work --timezone America/Los_Angeles");
    assertTrue(result.startsWith("Error:"));
  }

  @Test
  public void testUseCalendar() {
    parser.executeCommand("create calendar --name Work One --timezone America/New_York");

    String result = parser.executeCommand("use calendar --name Work One");
    assertEquals("Now using calendar: Work One", result);
  }

  @Test
  public void testEditCalendar() {
    parser.executeCommand("create calendar --name Work one --timezone America/New_York");
    String result = parser.executeCommand("edit calendar --name Work one --property name New work");
    assertEquals("Calendar 'Work one' updated successfully", result);
  }

  @Test
  public void testCopyEvent() {
    parser.executeCommand("create calendar --name Work --timezone America/New_York");
    parser.executeCommand("create calendar --name Personal --timezone America/Los_Angeles");

    parser.executeCommand("use calendar --name Work");
    parser.executeCommand("create event Meeting from 2025-01-01T09:00 to 2025-01-01T10:00");

    String result = parser.executeCommand("copy event Meeting on 2025-01-01T09:00 --target" +
            " Personal to 2025-01-02T09:00");
    assertEquals("Event copied successfully", result);
  }

  @Test
  public void testCopyEventsOn() {
    parser.executeCommand("create calendar --name Work --timezone America/New_York");
    parser.executeCommand("create calendar --name Personal --timezone America/Los_Angeles");

    parser.executeCommand("use calendar --name Work");
    parser.executeCommand("create event Meeting from 2025-01-01T09:00 to 2025-01-01T10:00");

    String result = parser.executeCommand("copy events on 2025-01-01 --target Personal " +
            "to 2025-01-02");
    assertEquals("Events copied successfully", result);
    //TODO: needs fixing
  }

  @Test
  public void testCopyEventsBetween() {
    parser.executeCommand("create calendar --name Work --timezone America/New_York");
    parser.executeCommand("create calendar --name Personal --timezone America/Los_Angeles");

    parser.executeCommand("use calendar --name Work");
    parser.executeCommand("create event Meeting from 2025-01-01T09:00 to 2025-01-01T10:00");
    parser.executeCommand("create event Review from 2025-01-02T14:00 to 2025-01-02T15:00");

    String result = parser.executeCommand("copy events between 2025-01-01 and 2025-01-02 --target" +
            " Personal to 2025-01-15");
    assertEquals("Events copied successfully", result);
  }

  @Test
  public void testEventCreation() {
    parser.executeCommand("create calendar --name Work --timezone America/New_York");
    parser.executeCommand("use calendar --name Work");

    String result = parser.executeCommand("create event Meeting from 2025-01-01T09:00 to " +
            "2025-01-01T10:00");
    assertEquals("This event was created successfully", result);
  }

  @Test
  public void testInvalidCommands() {

    String result = parser.executeCommand("invalid command");
    assertTrue(result.startsWith("Error:"));

    result = parser.executeCommand("");
    assertTrue(result.startsWith("Error:"));
  }

  @Test
  public void testNullCommand() {
    String result = parser.executeCommand(null);
    assertTrue(result.startsWith("Error:"));
  }

  @Test
  public void testConstructors() {
    try {
      new EnhancedCommandParser(null, view);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Calendar cannot be null", e.getMessage());
    }
  }

  @Test
  public void testExitCommand() {
    String result = parser.executeCommand("exit");
    assertEquals("Exiting...", result);
  }
}