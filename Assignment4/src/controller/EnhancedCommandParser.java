package controller;


import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Calendars;
import view.View;


/**
 * Enhanced CommandParser that adds support for multiple calendars, timezones, and advanced event
 * copying.
 * Delegates common event-related commands to the parent CommandParser.
 */
public class EnhancedCommandParser extends CommandParser {

  // new patterns for multicalendar management
  private static final Pattern CREATE_CALENDAR_PATTERN = Pattern.compile(
          "create calendar --name ([^\"].*?|\"[^\"]+\") --timezone ([\\w/_-]+)");
  private static final Pattern EDIT_CALENDAR_PATTERN = Pattern.compile(
          "edit calendar --name ([^\"].*?|\"[^\"]+\") --property " +
                  "([^\"].*?|\"[^\"]+\") ([^\"].*?|\"[^\"]+\")");
  private static final Pattern USE_CALENDAR_PATTERN = Pattern.compile(
          "use calendar --name ([^\"].*?|\"[^\"]+\")");
  private static final Pattern COPY_EVENT_PATTERN = Pattern.compile(
          "copy event ([^\"].*?|\"[^\"]+\") on (\\S+) --target " +
                  "([^\"].*?|\"[^\"]+\") to (\\S+)");
  private static final Pattern COPY_EVENTS_ON_PATTERN = Pattern.compile(
          "copy events on (\\S+) --target ([^\"].*?|\"[^\"]+\") to (\\S+)");
  private static final Pattern COPY_EVENTS_BETWEEN_PATTERN = Pattern.compile(
          "copy events between (\\S+) and (\\S+) --target " +
                  "([^\"].*?|\"[^\"]+\") to (\\S+)");

  private final Calendars calendars;

  /**
   * Creates an EnhancedCommandParser with the specified calendars system and view.
   *
   * @param calendars the calendars system to operate on
   * @param view      the view for displaying results
   * @throws IllegalArgumentException if either argument is null
   */
  public EnhancedCommandParser(Calendars calendars, View view) throws IllegalArgumentException {
    super(calendars, view); // Reuse the parent class constructor
    if (calendars == null) {
      throw new IllegalArgumentException("Calendars cannot be null");
    }
    this.calendars = calendars;
  }

  /**
   * Executes a command and returns a result message.
   * Handles additional commands related to calendars and advanced event management.
   *
   * @param command the command to be executed
   * @return a string response to the command
   * @throws IllegalArgumentException if the command is null or empty
   */
  @Override
  public String executeCommand(String command) throws IllegalArgumentException {
    if (command == null || command.trim().isEmpty()) {
      return "Error: Command cannot be null or empty";
    }

    String trimmedCommand = command.trim();

    try {
      if (trimmedCommand.equalsIgnoreCase("exit")) {
        return "Exiting...";
      }

      // handle calendar-specific commands
      if (trimmedCommand.startsWith("create calendar")) {
        return handleCreateCalendarCommand(trimmedCommand);
      } else if (trimmedCommand.startsWith("edit calendar")) {
        return handleEditCalendarCommand(trimmedCommand);
      } else if (trimmedCommand.startsWith("use calendar")) {
        return handleUseCalendarCommand(trimmedCommand);
      } else if (trimmedCommand.startsWith("copy events")) {
        return handleCopyEventsCommand(trimmedCommand);
      } else if (trimmedCommand.startsWith("copy event")) {
        return handleCopyEventCommand(trimmedCommand);
      }

      // delegate other commands to the CommandParser class
      return super.executeCommand(command);
    } catch (Exception e) {
      view.displayError(e.getMessage());
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Handles create calendar commands.
   *
   * @param command the create calendar command
   * @return success message
   * @throws IllegalArgumentException if command format is invalid
   */
  private String handleCreateCalendarCommand(String command) throws IllegalArgumentException {
    Matcher matcher = CREATE_CALENDAR_PATTERN.matcher(command);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid create calendar format");
    }

    String calendarName = matcher.group(1);
    String timezoneId = matcher.group(2);

    TimeZone timezone = TimeZone.getTimeZone(timezoneId);
    calendars.createCalendar(calendarName, timezone);
    return "Calendar '" + calendarName + "' created successfully";
  }

  /**
   * Handles edit calendar commands.
   *
   * @param command the edit calendar command
   * @return success message
   * @throws IllegalArgumentException if command format is invalid
   */
  private String handleEditCalendarCommand(String command) throws IllegalArgumentException {
    Matcher matcher = EDIT_CALENDAR_PATTERN.matcher(command);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid edit calendar format");
    }

    String calendarName = matcher.group(1);
    String property = matcher.group(2);
    String newValue = matcher.group(3);

    calendars.editCalendar(calendarName, property, newValue);
    return "Calendar '" + calendarName + "' updated successfully";
  }

  /**
   * Handles use calendar commands to set the active calendar.
   *
   * @param command the use calendar command
   * @return success message
   * @throws IllegalArgumentException if command format is invalid
   */
  private String handleUseCalendarCommand(String command) throws IllegalArgumentException {
    Matcher matcher = USE_CALENDAR_PATTERN.matcher(command);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid use calendar format");
    }

    String calendarName = matcher.group(1);
    calendars.useCalendar(calendarName);
    return "Now using calendar: " + calendarName;
  }

  /**
   * Handles copy event commands.
   *
   * @param command the command to copy a single event
   * @return success message
   * @throws IllegalArgumentException if command format is invalid
   */
  private String handleCopyEventCommand(String command) throws IllegalArgumentException {
    Matcher matcher = COPY_EVENT_PATTERN.matcher(command);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid copy event format");
    }

    String eventName = extractSubject(matcher.group(1));
    String originalDate = matcher.group(2);
    String targetCalendar = matcher.group(3);
    String newDate = matcher.group(4);

    calendars.copyEvent(eventName, originalDate, targetCalendar, newDate);
    return "Event copied successfully";
  }

  /**
   * Handles copy events on command.
   *
   * @param command the command to copy events
   * @return a success message
   * @throws IllegalArgumentException if command format is invalid
   */
  private String handleCopyEventsCommand(String command) throws IllegalArgumentException {
    Matcher matcher = COPY_EVENTS_ON_PATTERN.matcher(command);
    if (matcher.matches()) {
      String sourceDate = matcher.group(1);
      String targetCalendar = matcher.group(2);
      String targetDate = matcher.group(3);

      calendars.copyEventsOn(sourceDate, targetCalendar, targetDate);
      return "Events copied successfully";
    }

    matcher = COPY_EVENTS_BETWEEN_PATTERN.matcher(command);
    if (matcher.matches()) {
      String startDate = matcher.group(1);
      String endDate = matcher.group(2);
      String targetCalendar = matcher.group(3);
      String targetDate = matcher.group(4);

      calendars.copyEventsBetween(startDate, endDate, targetCalendar, targetDate);
      return "Events copied successfully";
    }

    throw new IllegalArgumentException("Invalid format for copy events command");
  }


}