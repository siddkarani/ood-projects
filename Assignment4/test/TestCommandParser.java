import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import controller.CommandParser;
import model.Calendar;
import model.Event;
import view.View;

/**
 * Tests for Command Parser class.
 */
public class TestCommandParser {
  private Calendar mockCalendar;
  private View mockView;
  private CommandParser parser;
  private StringBuilder log;

  @Before
  public void setup() {
    log = new StringBuilder();
    mockCalendar = new MockCalendar(log);
    mockView = new MockView(log);
    parser = new CommandParser(mockCalendar, mockView);
  }

  // Constructor tests
  @Test
  public void testConstructorNullCalendar() {
    try {
      new CommandParser(null, mockView);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Calendar cannot be null", e.getMessage());
    }
  }

  @Test
  public void testConstructorNullView() {
    try {
      new CommandParser(mockCalendar, null);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("View cannot be null", e.getMessage());
    }
  }

  @Test
  public void testConstructorValid() {
    CommandParser validParser = new CommandParser(mockCalendar, mockView);
    Assert.assertNotNull(validParser);
  }

  // executeCommand validation tests
  @Test
  public void testExecuteCommandNull() {
    try {
      parser.executeCommand(null);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Command cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testExecuteCommandEmpty() {
    try {
      parser.executeCommand("");
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Command cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testExecuteCommandWhitespace() {
    try {
      parser.executeCommand("   ");
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Command cannot be null or empty", e.getMessage());
    }
  }

  // Exit command test
  @Test
  public void testExitCommand() {
    String result = parser.executeCommand("exit");
    Assert.assertEquals("Exiting...", result);

    result = parser.executeCommand("EXIT");
    Assert.assertEquals("Exiting...", result);

    result = parser.executeCommand("  exit  ");
    Assert.assertEquals("Exiting...", result);
  }

  // Create timed event tests
  @Test
  public void testCreateSimpleEvent() {
    String command = "create event \"Meeting One\" from 2024-03-20T10:00 to 2024-03-20T11:00";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This event was created successfully", result);
    Assert.assertTrue(log.toString().contains("createEvent called with subject: Meeting One"));
  }

  @Test
  public void testCreateEventWithoutQuotes() {
    String command = "create event Meeting from 2024-03-20T10:00 to 2024-03-20T11:00";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This event was created successfully", result);
    Assert.assertTrue(log.toString().contains("createEvent called with subject: Meeting"));
  }

  @Test
  public void testCreateRecurringEventNTimes() {
    String command = "create event \"Class\" from 2024-03-20T09:00 to 2024-03-20T10:30 " +
            "repeats MWF for 5 times";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This event was created successfully", result);
    Assert.assertTrue(log.toString().contains("createEventSeriesNTimes called"));
    Assert.assertTrue(log.toString().contains("weekdays: MWF"));
    Assert.assertTrue(log.toString().contains("times: 5"));
  }

  @Test
  public void testCreateRecurringEventUntil() {
    String command = "create event \"Meeting\" from 2024-03-20T14:00 to 2024-03-20T15:00 " +
            "repeats M until 2024-04-20T00:00";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This event was created successfully", result);
    Assert.assertTrue(log.toString().contains("createEventSeriesUntil called"));
    Assert.assertTrue(log.toString().contains("weekdays: M"));
    Assert.assertTrue(log.toString().contains("until: 2024-04-20T00:00"));
  }

  // Create all-day event tests
  @Test
  public void testCreateAllDayEvent() {
    String command = "create event \"Holiday\" on 2024-03-20";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This all-day event was created successfully", result);
    Assert.assertTrue(log.toString().contains("createEvent called"));
  }

  @Test
  public void testCreateAllDayEventWithQuotes() {
    String command = "create event \"All Day Meeting\" on 2024-03-20";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This all-day event was created successfully", result);
  }

  @Test
  public void testCreateAllDaySeriesNTimes() {
    String command = "create event \"Daily Stand\" on 2024-03-20 repeats MWF for 3 times";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This all-day event was created successfully", result);
    Assert.assertTrue(log.toString().contains("createEventSeriesNTimes called"));
  }

  @Test
  public void testCreateAllDaySeriesUntil() {
    String command = "create event \"Workshop\" on 2024-03-20 repeats MW until 2024-04-20";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This all-day event was created successfully", result);
    Assert.assertTrue(log.toString().contains("createEventSeriesUntil called"));
  }

  // Edit command tests
  @Test
  public void testEditSingleEvent() {
    String command = "edit event subject \"Meeting\" from 2024-03-20T10:00 " +
            "to 2024-03-20T11:00 with \"Updated Meeting\"";
    String result = parser.executeCommand(command);
    Assert.assertEquals("Event(s) edited successfully", result);
    Assert.assertTrue(log.toString().contains("editEvent called"));
    Assert.assertTrue(log.toString().contains("Updated Meeting"));
  }

  @Test
  public void testEditEvents() {
    String command = "edit events location \"Workshop\" from 2024-03-20T10:00 " +
            "with \"New Room\"";
    String result = parser.executeCommand(command);
    Assert.assertEquals("Event(s) edited successfully", result);
    Assert.assertTrue(log.toString().contains("editEvents called"));
  }

  @Test
  public void testEditEventSeries() {
    String command = "edit series subject \"Meeting\" from 2024-03-20T10:00 " +
            "with \"Updated Meeting\"";
    String result = parser.executeCommand(command);
    Assert.assertEquals("Event(s) edited successfully", result);
    Assert.assertTrue(log.toString().contains("editEventSeries called"));
  }

  @Test
  public void testEditInvalidType() {
    String command = "edit invalid subject \"Meeting\" from 2024-03-20T10:00 " +
            "to 2024-03-20T11:00 with \"Updated\"";
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  // Print/query command tests
  @Test
  public void testPrintDaySchedule() {
    String command = "print events on 2024-03-20";
    String result = parser.executeCommand(command);
    Assert.assertEquals("", result); // Mock returns empty
    Assert.assertTrue(log.toString().contains("daySchedule called with date: 2024-03-20"));
  }

  @Test
  public void testPrintRangeSchedule() {
    String command = "print events from 2024-03-20T00:00 to 2024-03-21T00:00";
    String result = parser.executeCommand(command);
    Assert.assertEquals("", result); // Mock returns empty
    Assert.assertTrue(log.toString().contains("rangeSchedule called"));
  }

  // Status command tests
  @Test
  public void testShowStatus() {
    String command = "show status on 2024-03-20";
    String result = parser.executeCommand(command);
    Assert.assertEquals("Available", result);
    Assert.assertTrue(log.toString().contains("isFree called with date: 2024-03-20"));
  }

  // Invalid command tests
  @Test
  public void testInvalidCommand() {
    String command = "invalid command";
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
    Assert.assertTrue(result.contains("Not a valid command: invalid command"));
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  @Test
  public void testInvalidCreateFormat() {
    String command = "create event invalid format";
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
    Assert.assertTrue(result.contains("Invalid create format"));
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  @Test
  public void testInvalidEditFormat() {
    String command = "edit invalid format";
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
    Assert.assertTrue(result.contains("Invalid edit format"));
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  @Test
  public void testInvalidPrintFormat() {
    String command = "print events invalid";
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
    Assert.assertTrue(result.contains("Invalid print format"));
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  @Test
  public void testInvalidStatusFormat() {
    String command = "show status invalid";
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
    Assert.assertTrue(result.contains("Invalid status format"));
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  // Subject extraction tests
  @Test
  public void testExtractSubjectWithQuotes() {
    String command = "create event \"Multi Word Subject\" from 2024-03-20T10:00 " +
            "to 2024-03-20T11:00";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This event was created successfully", result);
    Assert.assertTrue(log.toString().contains("Multi Word Subject"));
  }

  @Test
  public void testExtractSubjectWithoutQuotes() {
    String command = "create event SingleWord from 2024-03-20T10:00 to 2024-03-20T11:00";
    String result = parser.executeCommand(command);
    Assert.assertEquals("This event was created successfully", result);
    Assert.assertTrue(log.toString().contains("SingleWord"));
  }

  // DateTime parsing tests
  @Test
  public void testInvalidDateTimeFormat() {
    String command = "create event \"Test\" from invalid-date to 2024-03-20T11:00";
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  @Test
  public void testNullDateTimeHandling() {
    // This tests the internal parseDateTime method error handling
    String command = "print events from  to 2024-03-20T11:00"; // Empty start time
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
  }

  // Edge case tests
  @Test
  public void testCommandWithExtraSpaces() {
    String command = "  create   event   \"Test\"   from   2024-03-20T10:00   " +
            "to   2024-03-20T11:00  ";
    // This should fail due to regex pattern not matching extra spaces
    String result = parser.executeCommand(command);
    Assert.assertTrue(result.startsWith("Error:"));
  }

  @Test
  public void testCaseInsensitiveCommands() {
    String result1 = parser.executeCommand("CREATE event Test from 2024-03-20T10:00 " +
            "to 2024-03-20T11:00");
    Assert.assertTrue(result1.startsWith("Error:")); // Should fail - case sensitive

    String result2 = parser.executeCommand("EDIT event subject Test from 2024-03-20T10:00 " +
            "to 2024-03-20T11:00 with New");
    Assert.assertTrue(result2.startsWith("Error:")); // Should fail - case sensitive
  }

  @Test
  public void testEmptySubjectInQuotes() {
    String command = "create event \"\" from 2024-03-20T10:00 to 2024-03-20T11:00";
    String result = parser.executeCommand(command);
    // This will reach the model where empty subject validation occurs
    Assert.assertTrue(result.startsWith("Error:"));
  }

  // Mock implementations for testing
  private static class MockCalendar implements Calendar {
    private final StringBuilder log;

    public MockCalendar(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void createEvent(Event event) {
      log.append("createEvent called with subject: ").append(event.getSubject()).append("\n");
    }

    @Override
    public void createEventSeriesNTimes(Event event, String weekdays, int n) {
      log.append("createEventSeriesNTimes called\n")
              .append("weekdays: ").append(weekdays).append("\n")
              .append("times: ").append(n).append("\n");
    }

    @Override
    public void createEventSeriesUntil(Event event, String weekdays, String until) {
      log.append("createEventSeriesUntil called\n")
              .append("weekdays: ").append(weekdays).append("\n")
              .append("until: ").append(until).append("\n");
    }

    @Override
    public void editEvent(String property, String subject, String startDateTime, String endDateTime,
                          String newProperty) {
      log.append("editEvent called\n")
              .append("subject: ").append(subject).append("\n")
              .append("property: ").append(property).append("\n")
              .append("new value: ").append(newProperty).append("\n");
    }

    @Override
    public void editEvents(String property, String subject, String startDateTime,
                           String newProperty) {
      log.append("editEvents called\n");
    }

    @Override
    public void editEventSeries(String property, String subject, String startDateTime,
                                String newProperty) {
      log.append("editEventSeries called\n");
    }

    @Override
    public String daySchedule(String date) {
      log.append("daySchedule called with date: ").append(date).append("\n");
      return "";
    }

    @Override
    public String rangeSchedule(String time1, String time2) {
      log.append("rangeSchedule called\n");
      return "";
    }

    @Override
    public String isFree(String date) {
      log.append("isFree called with date: ").append(date).append("\n");
      return "Available";
    }
  }

  private static class MockView implements View {
    private final StringBuilder log;

    public MockView(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void displayMessage(String message) {
      log.append("displayMessage called: ").append(message).append("\n");
    }

    @Override
    public void displayError(String message) {
      log.append("displayError called: ").append(message).append("\n");
    }
  }
}