package controller;

import model.Calendars;
import model.Event;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Controller for GUI interactions. Handles communication between CalendarGUI and the model.
 */
public class GUIController {
  private final Calendars calendars;

  /**
   * Creates a GUI controller.
   * @param calendars the calendar model
   */
  public GUIController(Calendars calendars) {
    if (calendars == null) {
      throw new IllegalArgumentException("Calendars cannot be null");
    }
    this.calendars = calendars;
  }

  /**
   * Creates a new calendar.
   * @param name calendar name
   * @param timezone timezone
   * @return success message or error
   */
  public String createCalendar(String name, TimeZone timezone) {
    try {
      if (name == null || name.trim().isEmpty()) {
        return "Error: Calendar name cannot be empty";
      }
      calendars.createCalendar(name, timezone);
      return "Calendar '" + name + "' created successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Switches to a different calendar.
   * @param name calendar name
   * @return success message or error
   */
  public String useCalendar(String name) {
    try {
      if (name == null || name.trim().isEmpty()) {
        return "Error: Calendar name cannot be empty";
      }
      calendars.useCalendar(name);
      return "Now using calendar: " + name;
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Creates an event.
   * @param event the event to create
   * @return success message or error
   */
  public String createEvent(Event event) {
    try {
      if (event == null) {
        return "Error: Event cannot be null";
      }
      calendars.createEvent(event);
      return "Event created successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Creates an event series with N times repetition.
   * @param event base event
   * @param weekdays weekday string
   * @param times number of times
   * @return success message or error
   */
  public String createEventSeriesNTimes(Event event, String weekdays, int times) {
    try {
      if (event == null) {
        return "Error: Event cannot be null";
      }
      if (weekdays == null || weekdays.trim().isEmpty()) {
        return "Error: Weekdays cannot be empty";
      }
      if (times <= 0) {
        return "Error: Number of times must be positive";
      }
      calendars.createEventSeriesNTimes(event, weekdays, times);
      return "Event series created successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Creates an event series until a specific date.
   * @param event base event
   * @param weekdays weekday string
   * @param until end date
   * @return success message or error
   */
  public String createEventSeriesUntil(Event event, String weekdays, String until) {
    try {
      if (event == null) {
        return "Error: Event cannot be null";
      }
      if (weekdays == null || weekdays.trim().isEmpty()) {
        return "Error: Weekdays cannot be empty";
      }
      if (until == null || until.trim().isEmpty()) {
        return "Error: Until date cannot be empty";
      }
      calendars.createEventSeriesUntil(event, weekdays, until);
      return "Event series created successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Edits an event.
   * @param property property to edit
   * @param subject event subject
   * @param startDateTime start date time
   * @param endDateTime end date time
   * @param newValue new value
   * @return success message or error
   */
  public String editEvent(String property, String subject, String startDateTime,
                          String endDateTime, String newValue) {
    try {
      if (property == null || property.trim().isEmpty()) {
        return "Error: Property cannot be empty";
      }
      if (subject == null || subject.trim().isEmpty()) {
        return "Error: Subject cannot be empty";
      }
      if (startDateTime == null || startDateTime.trim().isEmpty()) {
        return "Error: Start date time cannot be empty";
      }
      calendars.editEvent(property, subject, startDateTime, endDateTime, newValue);
      return "Event edited successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Edits multiple events.
   * @param property property to edit
   * @param subject event subject
   * @param startDateTime start date time
   * @param newValue new value
   * @return success message or error
   */
  public String editEvents(String property, String subject, String startDateTime, String newValue) {
    try {
      if (property == null || property.trim().isEmpty()) {
        return "Error: Property cannot be empty";
      }
      if (subject == null || subject.trim().isEmpty()) {
        return "Error: Subject cannot be empty";
      }
      if (startDateTime == null || startDateTime.trim().isEmpty()) {
        return "Error: Start date time cannot be empty";
      }
      calendars.editEvents(property, subject, startDateTime, newValue);
      return "Events edited successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Edits an event series.
   * @param property property to edit
   * @param subject event subject
   * @param startDateTime start date time
   * @param newValue new value
   * @return success message or error
   */
  public String editEventSeries(String property, String subject, String startDateTime, String newValue) {
    try {
      if (property == null || property.trim().isEmpty()) {
        return "Error: Property cannot be empty";
      }
      if (subject == null || subject.trim().isEmpty()) {
        return "Error: Subject cannot be empty";
      }
      if (startDateTime == null || startDateTime.trim().isEmpty()) {
        return "Error: Start date time cannot be empty";
      }
      calendars.editEventSeries(property, subject, startDateTime, newValue);
      return "Event series edited successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Gets the schedule for a specific date.
   * @param date the date
   * @return schedule string
   */
  public String getSchedule(String date) {
    try {
      if (date == null || date.trim().isEmpty()) {
        return "Error: Date cannot be empty";
      }
      return calendars.daySchedule(date);
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Gets schedule for a date range.
   * @param startDate start date
   * @param endDate end date
   * @return schedule string
   */
  public String getScheduleRange(String startDate, String endDate) {
    try {
      if (startDate == null || startDate.trim().isEmpty()) {
        return "Error: Start date cannot be empty";
      }
      if (endDate == null || endDate.trim().isEmpty()) {
        return "Error: End date cannot be empty";
      }
      return calendars.rangeSchedule(startDate, endDate);
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Checks if a date/time is free.
   * @param dateTime the date time to check
   * @return "Available" or "Busy" or error
   */
  public String checkAvailability(String dateTime) {
    try {
      if (dateTime == null || dateTime.trim().isEmpty()) {
        return "Error: Date time cannot be empty";
      }
      return calendars.isFree(dateTime);
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Gets list of all calendars.
   * @return calendar list
   */
  public String getCalendars() {
    try {
      return calendars.getCalendars();
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Edits calendar properties.
   * @param name calendar name
   * @param property property to edit
   * @param newValue new value
   * @return success message or error
   */
  public String editCalendar(String name, String property, String newValue) {
    try {
      if (name == null || name.trim().isEmpty()) {
        return "Error: Calendar name cannot be empty";
      }
      if (property == null || property.trim().isEmpty()) {
        return "Error: Property cannot be empty";
      }
      calendars.editCalendar(name, property, newValue);
      return "Calendar edited successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Copies an event to another calendar.
   * @param eventName event name
   * @param originalDate original date
   * @param targetCalendar target calendar
   * @param newDate new date
   * @return success message or error
   */
  public String copyEvent(String eventName, String originalDate, String targetCalendar, String newDate) {
    try {
      if (eventName == null || eventName.trim().isEmpty()) {
        return "Error: Event name cannot be empty";
      }
      if (originalDate == null || originalDate.trim().isEmpty()) {
        return "Error: Original date cannot be empty";
      }
      if (targetCalendar == null || targetCalendar.trim().isEmpty()) {
        return "Error: Target calendar cannot be empty";
      }
      if (newDate == null || newDate.trim().isEmpty()) {
        return "Error: New date cannot be empty";
      }
      calendars.copyEvent(eventName, originalDate, targetCalendar, newDate);
      return "Event copied successfully";
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }
}