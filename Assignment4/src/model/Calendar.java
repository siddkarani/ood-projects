package model;


/**
 * Represents a Calendar that can create, edit, and retrieve events.
 */
public interface Calendar {

  /**
   * Creates an event in a calendar.
   * Effect: adds an event to the calendar.
   *
   * @param event the event to be created
   */
  void createEvent(Event event);

  /**
   * Creates a recurring event a given number of times on given weekdays.
   * Effect: adds copies of the event to the calendar.
   *
   * @param event    the event to be repeated
   * @param weekdays weekdays the characters representing the weekdays (e.g., "MWF")
   * @param n        the number of times the event should be repeated
   */
  void createEventSeriesNTimes(Event event, String weekdays, int n);

  /**
   * Creates a recurring event that happens until the given date.
   * Effect: adds copies of the event to the calendar.
   *
   * @param event    the event to be repeated
   * @param weekdays weekdays the characters representing the weekdays (e.g., "MWF")
   * @param until    the date the event should repeat until
   */
  void createEventSeriesUntil(Event event, String weekdays, String until);

  /**
   * Changes the property of the given event whether it is single or part of a series.
   *
   * @param subject       the subject of the event to be changed
   * @param startDateTime the start date and time of the event to be changed
   * @param endDateTime   the end date and time of the event to be changed
   * @param property      the property to be changed
   * @param newValue   the new value of the property
   */
  void editEvent(String property, String subject, String startDateTime, String endDateTime,
                 String newValue);

  /**
   * Identifies event that starts at the given date and time and edit its property.
   * If this event is part of a series then the properties of all events in that series after
   * given date will be edited too.
   *
   * @param subject       the subject of the event to be changed
   * @param startDateTime the start date and time of the event to be changed
   * @param property      the property to be changed
   * @param newValue   the new value of the property
   */
  void editEvents(String property, String subject, String startDateTime, String newValue);

  /**
   * Identifies event that has the given subject and starts at the given date and time and edit
   * its property. Edits property of all events in the series (if it is a series), or edits
   * singular event.
   *
   * @param subject       the subject of the event to be changed
   * @param startDateTime the start date and time of the event to be changed
   * @param property      the property to be changed
   * @param newValue   the new value of the property
   */
  void editEventSeries(String property, String subject, String startDateTime, String newValue);

  /**
   * Returns a bulleted list of events on a given date, including their start and end times,
   * and their location if it exists.
   *
   * @param date the date to be checked.
   * @return a bulleted list of events on the given date.
   */
  String daySchedule(String date);

  /**
   * Returns a bulleted list of events between two given times, including their start and end times,
   * and their location if it exists.
   *
   * @param time1 starting date and time
   * @param time2 ending date and time
   * @return a bulleted list of events between the given times
   */
  String rangeSchedule(String time1, String time2);

  /**
   * Checks if the given date is available or busy.
   *
   * @param date the date to be checked
   * @return "Available" or "Busy"
   */
  String isFree(String date);
}