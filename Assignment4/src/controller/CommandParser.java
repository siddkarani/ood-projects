package controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Calendar;
import model.Event;
import view.View;

/**
 * Parses and executes commands on a Calendar. This uses a Command Pattern to parse inputs.
 */
public class CommandParser implements Command {
  private static final Pattern CREATE_EVENT_PATTERN = Pattern.compile(
          "create event\\s+(\"[^\"]+\"|\\S+)\\s+from\\s+(\\S+)\\s+to\\s+(\\S+)" +
                  "(?:\\s+repeats\\s+(\\S+)" +
                  "(?:\\s+for\\s+(\\d+)\\s+times|\\s+until\\s+(\\S+)))?");
  private static final Pattern CREATE_ALL_DAY_PATTERN = Pattern.compile(
          "create event\\s+(\"[^\"]+\"|\\S+)\\s+on\\s+(\\S+)(?:\\s+repeats\\s+(\\S+)" +
                  "(?:\\s+for\\s+(\\d+)\\s+times|\\s+until\\s+(\\S+)))?");
  private static final Pattern EDIT_EVENT_PATTERN = Pattern.compile(
          "edit (event|events|series) (\\S+) (\"[^\"]+\"|\\S+) from (\\S+)" +
                  "(?: to (\\S+))? with (.+)");
  private static final Pattern PRINT_EVENTS_PATTERN = Pattern.compile(
          "print events(?:\\s+on\\s+(\\S+)|\\s+from\\s+(\\S+)\\s+to\\s+(\\S+))");
  private static final Pattern SHOW_STATUS_PATTERN = Pattern.compile(
          "show status\\s+on\\s+(\\S+)");

  private final Calendar calendar;
  protected final View view;

  /**
   * Creates a CommandParser with the specified calendar and view.
   *
   * @param calendar the calendar to operate on
   * @param view     the view for displaying results
   * @throws IllegalArgumentException if either argument is null
   */
  public CommandParser(Calendar calendar, View view) throws IllegalArgumentException {
    if (calendar == null) {
      throw new IllegalArgumentException("Calendar cannot be null");
    }
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.calendar = calendar;
    this.view = view;
  }

  /**
   * Executes a command and returns a result message.
   *
   * @param command the command to be executed
   * @return a string response to the command
   * @throws IllegalArgumentException if the command is null or empty
   */
  public String executeCommand(String command) throws IllegalArgumentException {
    if (command == null || command.trim().isEmpty()) {
      throw new IllegalArgumentException("Command cannot be null or empty");
    }

    try {
      String trimmedCommand = command.trim();

      if (trimmedCommand.equalsIgnoreCase("exit")) {
        return "Exiting...";
      }
      // check what type of command it is and sends to helper methods
      if (trimmedCommand.startsWith("create event")) {
        return handleCreateCommand(trimmedCommand);
      } else if (trimmedCommand.startsWith("edit")) {
        return handleEditCommand(trimmedCommand);
      } else if (trimmedCommand.startsWith("print events")) {
        return handlePrintCommand(trimmedCommand);
      } else if (trimmedCommand.startsWith("show status")) {
        return handleStatusCommand(trimmedCommand);
      } else {
        throw new IllegalArgumentException("Not a valid command: " + command);
      }
    } catch (Exception e) {
      view.displayError(e.getMessage());
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Handles event creation commands.
   *
   * @param command the command to be executed.
   * @throws IllegalArgumentException if the command is not recognized or invalid
   */
  private String handleCreateCommand(String command) throws IllegalArgumentException {
    // timed event first
    Matcher matcher = CREATE_EVENT_PATTERN.matcher(command);
    if (matcher.matches()) {
      return handleTimedEventCreation(matcher);
    }

    // all-day event
    matcher = CREATE_ALL_DAY_PATTERN.matcher(command);
    if (matcher.matches()) {
      return handleAllDayEventCreation(matcher);
    }

    throw new IllegalArgumentException("Invalid create format");
  }

  /**
   * Handles creation of timed events.
   *
   * @param matcher the matcher for the timed event creation command.
   * @return a string response saying if it was created properly.
   */
  private String handleTimedEventCreation(Matcher matcher) {
    String subject = extractSubject(matcher.group(1));
    LocalDateTime start = parseDateTime(matcher.group(2));
    LocalDateTime end = parseDateTime(matcher.group(3));
    String weekdays = matcher.group(4);
    String timesStr = matcher.group(5);
    String untilStr = matcher.group(6);

    Event event = Event.getBuilder(subject, start)
            .endDateTime(end)
            .build();

    if (weekdays == null) {
      calendar.createEvent(event);
    } else if (timesStr != null) {
      calendar.createEventSeriesNTimes(event, weekdays, Integer.parseInt(timesStr));
    } else if (untilStr != null) {
      calendar.createEventSeriesUntil(event, weekdays, untilStr);
    }

    return "This event was created successfully";
  }

  /**
   * Handles creation of all-day events.
   *
   * @param matcher the matcher for the all-day event creation command.
   * @return a string response saying if it was created properly.
   */
  private String handleAllDayEventCreation(Matcher matcher) {
    String subject = extractSubject(matcher.group(1));
    String dateStr = matcher.group(2);
    String weekdays = matcher.group(3);
    String timesStr = matcher.group(4);
    String untilStr = matcher.group(5);

    // creates an all day event (8am-5pm)
    LocalDateTime start = LocalDateTime.of(LocalDateTime.parse(dateStr + "T00:00").toLocalDate(),
            LocalTime.of(8, 0));
    LocalDateTime end = LocalDateTime.of(LocalDateTime.parse(dateStr + "T00:00").toLocalDate(),
            LocalTime.of(17, 0));

    Event event = Event.getBuilder(subject, start)
            .endDateTime(end)
            .build();
    if (weekdays == null) {
      calendar.createEvent(event);
    } else if (timesStr != null) {
      calendar.createEventSeriesNTimes(event, weekdays, Integer.parseInt(timesStr));
    } else if (untilStr != null) {
      calendar.createEventSeriesUntil(event, weekdays, untilStr + "T00:00");
    }

    return "This all-day event was created successfully";
  }

  /**
   * Handles edit commands.
   *
   * @param command the command to be executed.
   * @return a string response saying if it was edited properly.
   * @throws IllegalArgumentException if the command is not recognized or invalid
   */
  private String handleEditCommand(String command) throws IllegalArgumentException {
    Matcher matcher = EDIT_EVENT_PATTERN.matcher(command);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid edit format");
    }

    String editType = matcher.group(1);
    String property = matcher.group(2);
    String subject = extractSubject(matcher.group(3));
    String startDateTime = matcher.group(4);
    String endDateTime = matcher.group(5);
    String newValue = matcher.group(6);


    // delegates to the right editor depending on the edit type
    switch (editType.toLowerCase()) {
      case "event":
        calendar.editEvent(property, subject, startDateTime, endDateTime, newValue);
        break;
      case "events":
        calendar.editEvents(property, subject, startDateTime, newValue);
        break;
      case "series":
        calendar.editEventSeries(property, subject, startDateTime, newValue);
        break;
      default:
        throw new IllegalArgumentException("Invalid edit type: " + editType);
    }

    return "Event(s) edited successfully";
  }

  /**
   * Handles print commands.
   *
   * @param command the command to be executed.
   * @return a string response saying if it was printed properly.
   * @throws IllegalArgumentException if the command is not recognized or invalid
   */
  private String handlePrintCommand(String command) throws IllegalArgumentException {
    return getString(command, PRINT_EVENTS_PATTERN, calendar);
  }

  static String getString(String command, Pattern pattern, Calendar calendar) {
    Matcher matcher = pattern.matcher(command);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid print format");
    }

    String singleDate = matcher.group(1);
    if (singleDate != null) {
      return calendar.daySchedule(singleDate);
    } else {
      String startTime = matcher.group(2);
      String endTime = matcher.group(3);
      if (startTime == null || endTime == null) {
        throw new IllegalArgumentException("Invalid date range format");
      }
      return calendar.rangeSchedule(startTime, endTime);
    }
  }

  /**
   * Handles show status commands.
   *
   * @param command the command to be executed.
   * @return a string response saying if it was printed properly.
   * @throws IllegalArgumentException if the command is not recognized or invalid
   */
  private String handleStatusCommand(String command) throws IllegalArgumentException {
    Matcher matcher = SHOW_STATUS_PATTERN.matcher(command);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid status format");
    }
    return calendar.isFree(matcher.group(1));
  }

  /**
   * Extracts subject from quoted or unquoted string.
   *
   * @param subject the subject string to be extracted.
   * @return a string representing the extracted subject
   */
  protected String extractSubject(String subject) {
    return getSubjectHelper(subject);
  }

  static String getSubjectHelper(String subject) {
    if (subject == null) {
      throw new IllegalArgumentException("Subject cannot be null");
    }
    if (subject.startsWith("\"") && subject.endsWith("\"") && subject.length() > 1) {
      return subject.substring(1, subject.length() - 1);
    }
    return subject;
  }

  /**
   * Parses date-time string to LocalDateTime.
   *
   * @param dateTime the date-time string to be parsed
   * @return a LocalDateTime object representing the parsed date-time
   * @throws IllegalArgumentException if the date-time string is not valid or null
   */
  protected LocalDateTime parseDateTime(String dateTime) {
    if (dateTime == null) {
      throw new IllegalArgumentException("DateTime cannot be null");
    }
    try {
      return LocalDateTime.parse(dateTime);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date-time format: " + dateTime);
    }
  }
}