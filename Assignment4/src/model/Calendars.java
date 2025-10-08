package model;

import java.util.TimeZone;

/**
 * Represents an application that can handle using multiple calendars at once. Has all the
 * functionality for a single calendar as well.
 */
public interface Calendars extends Calendar {
  /**
   * Creates a calendar.
   *
   * @param calendarName the name of the calendar
   * @param timezone     the timezone of the calendar
   */
  void createCalendar(String calendarName, TimeZone timezone) throws IllegalArgumentException;

  /**
   * Modifies a calendar's properties.
   *
   * @param name     calendar name to edit
   * @param property property to change
   * @param newValue new value for property
   * @throws IllegalArgumentException if calendar not found or invalid property
   */
  void editCalendar(String name, String property, String newValue) throws IllegalArgumentException;

  /**
   * Sets the active calendar.
   *
   * @param name calendar name to use
   * @throws IllegalArgumentException if calendar not found
   */
  void useCalendar(String name) throws IllegalArgumentException;

  /**
   * Copies an event to another calendar on a different date.
   *
   * @param eventName    event to copy
   * @param originalDate source date
   * @param calendarName destination calendar
   * @param newDate      destination date
   * @throws IllegalArgumentException if event, dates or calendar not found
   */
  void copyEvent(String eventName, String originalDate, String calendarName, String newDate)
          throws IllegalArgumentException;

  /**
   * Copies all events from a date to another calendar.
   *
   * @param originalDate source date
   * @param calendarName destination calendar
   * @param newDate      destination date
   * @throws IllegalArgumentException if dates or calendar not found
   */
  void copyEventsOn(String originalDate, String calendarName, String newDate)
          throws IllegalArgumentException;

  /**
   * Copies events within a date range to another calendar.
   *
   * @param startDate    range start date
   * @param endDate      range end date
   * @param calendarName destination calendar
   * @param newDate      destination start date
   * @throws IllegalArgumentException if dates or calendar not found
   */
  void copyEventsBetween(String startDate, String endDate, String calendarName, String newDate)
          throws IllegalArgumentException;

  /**
   * Gets a list of all calendars. Used for testing.
   *
   * @return string representation of calendars
   */
  String getCalendars();

}