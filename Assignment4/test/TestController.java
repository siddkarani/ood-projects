import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import controller.CommandParser;
import model.Calendar;
import model.Event;
import view.View;

/**
 * Uses mocking to test the controller.
 */
public class TestController {
  private CommandParser parser;
  private StringBuilder log;

  @Before
  public void setup() {
    log = new StringBuilder();
    Calendar mockCalendar = new MockCalendar(log);
    View mockView = new MockView(log);
    parser = new CommandParser(mockCalendar, mockView);
  }

  @Test
  public void testCreateSimpleEvent() {
    String command = "create event \"Meeting\" from 2024-03-20T10:00 to 2024-03-20T11:00";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("createEvent called with subject: Meeting"));
  }

  @Test
  public void testCreateRecurringEventNTimes() {
    String command = "create event \"Class\" from 2024-03-20T09:00 to 2024-03-20T10:30 repeats " +
            "MWF for 5 times";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("createEventSeriesNTimes called"));
    Assert.assertTrue(log.toString().contains("weekdays: MWF"));
    Assert.assertTrue(log.toString().contains("times: 5"));
  }

  @Test
  public void testCreateRecurringEventUntil() {
    String command = "create event \"Meeting\" from 2024-03-20T14:00 to 2024-03-20T15:00 repeats " +
            "M until 2024-04-20T00:00";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("createEventSeriesUntil called"));
    Assert.assertTrue(log.toString().contains("weekdays: M"));
    Assert.assertTrue(log.toString().contains("until: 2024-04-20T00:00"));
  }

  @Test
  public void testEditSingleEvent() {
    String command = "edit event subject \"Meeting\" from 2024-03-20T10:00 to 2024-03-20T11:00 " +
            "with \"Updated Meeting\"";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("editEvent called"));
    Assert.assertTrue(log.toString().contains("Updated Meeting"));
  }

  @Test
  public void testEditEventSeries() {
    String command = "edit series subject \"Meeting\" from 2024-03-20T10:00 " +
            "with \"Updated Meeting\"";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("editEventSeries called"));
  }

  @Test
  public void testPrintDaySchedule() {
    String command = "print events on 2024-03-20";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("daySchedule called with date: 2024-03-20"));
  }

  @Test
  public void testShowStatus() {
    String command = "show status on 2024-03-20";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("isFree called with date: 2024-03-20"));
  }

  @Test
  public void testInvalidCommand() {
    String command = "invalid command";
    parser.executeCommand(command);
    Assert.assertTrue(log.toString().contains("displayError called"));
  }

  // mock implementation of Calendar interface
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

  // mock implementation of View interface
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