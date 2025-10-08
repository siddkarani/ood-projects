package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import view.View;

/**
 * Represents the model for an application that can handle having more than one Calendar.
 */
public class MultipleCalendars extends TimezoneCalendar implements Calendars {
  private final Map<String, TimezoneCalendar> calendarsByName;
  private final View view;
  private TimezoneCalendar currentCalendar;

  /**
   * Constructs an app that handles more than one Calendar.
   *
   * @param view the view
   */
  public MultipleCalendars(View view) {
    super(view, TimeZone.getDefault()
    );

    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.calendarsByName = new HashMap<>();
    this.view = view;

  }

  @Override
  public void createCalendar(String calendarName, TimeZone timezone)
          throws IllegalArgumentException {
    if (calendarsByName.containsKey(calendarName)) {
      throw new IllegalArgumentException("A calendar with this name already exists");
    }
    if (timezone.getID().equals("GMT") && !timezone.toString().equals("GMT")) {
      throw new IllegalArgumentException("Invalid timezone");
    }

    TimezoneCalendar newCalendar = new TimezoneCalendar(this.view, timezone);
    calendarsByName.putIfAbsent(calendarName, newCalendar);

  }


  @Override
  public void editCalendar(String name, String property, String newValue)
          throws IllegalArgumentException {
    CalendarsProperty calendarsProperty = CalendarsProperty.fromString(property);

    TimezoneCalendar calendarToChange = calendarsByName.get(name);
    if (calendarToChange == null) {
      throw new IllegalArgumentException("Calendar not found");
    }

    switch (calendarsProperty) {
      case NAME:
        if (calendarsByName.containsKey(newValue)) {
          throw new IllegalArgumentException("A calendar with this name already exists");
        }
        calendarsByName.remove(name);
        calendarsByName.put(newValue, calendarToChange);
        break;

      case TIMEZONE:
        TimeZone newTimeZone = TimeZone.getTimeZone(newValue);
        // means that the timezone couldnt be parsed
        // got GMT but it wasnt GMT - > invalid
        if (newTimeZone.getID().equals("GMT") && !newValue.equals("GMT")) {
          throw new IllegalArgumentException("Invalid timezone");
        }
        calendarToChange.updateTimes(newTimeZone);
        break;

      default:
        throw new IllegalArgumentException("Invalid property");
    }
  }

  @Override
  public void useCalendar(String name) throws IllegalArgumentException {
    TimezoneCalendar calendar = calendarsByName.get(name);
    if (calendar == null) {
      throw new IllegalArgumentException("Calendar not found");
    }
    this.currentCalendar = calendar;
  }


  private void validateCurrentCalendar() throws IllegalStateException,
          IllegalArgumentException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar currently in use");
    }
  }

  private TimezoneCalendar validateTargetCalendar(String calendarName) {
    if (!calendarsByName.containsKey(calendarName)) {
      throw new IllegalArgumentException("No calendar found with that name");
    }
    return calendarsByName.get(calendarName);
  }

  @Override
  public void copyEvent(String eventName, String originalDate, String calendarName, String newDate)
          throws IllegalArgumentException {
    validateCurrentCalendar();
    TimezoneCalendar targetCalendar = validateTargetCalendar(calendarName);

    // look for the event
    List<Event> eventsByStartDate =
            currentCalendar.eventsByStartDate.get(LocalDateTime.parse(originalDate));
    if (eventsByStartDate == null || eventsByStartDate.isEmpty()) {
      throw new IllegalArgumentException("Event not found");
    }

    Event eventToCopy = eventsByStartDate.stream()
            .filter(e -> e.getSubject().equals(eventName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Event not found"));

    // Convert times between timezones
    LocalDateTime targetStartTime = LocalDateTime.parse(newDate)
            .atZone(targetCalendar.timeZone.toZoneId())
            .withZoneSameInstant(currentCalendar.timeZone.toZoneId())
            .toLocalDateTime();

    Duration duration = Duration.between(eventToCopy.getStartDateTime(),
            eventToCopy.getEndDateTime());
    LocalDateTime targetEndTime = targetStartTime.plus(duration);

    // Convert back to target calendar's timezone
    LocalDateTime finalStartTime = targetStartTime
            .atZone(currentCalendar.timeZone.toZoneId())
            .withZoneSameInstant(targetCalendar.timeZone.toZoneId())
            .toLocalDateTime();

    LocalDateTime finalEndTime = targetEndTime
            .atZone(currentCalendar.timeZone.toZoneId())
            .withZoneSameInstant(targetCalendar.timeZone.toZoneId())
            .toLocalDateTime();

    Event newEvent = Event.getBuilder(eventToCopy.getSubject(), finalStartTime)
            .endDateTime(finalEndTime)
            .description(eventToCopy.getDescription())
            .location(eventToCopy.getLocation())
            .status(eventToCopy.getStatus())
            .seriesId(eventToCopy.getSeriesId())
            .build();

    targetCalendar.createEvent(newEvent);
  }

  @Override
  public void copyEventsOn(String date, String targetCalendarName, String newDate)
          throws IllegalArgumentException {
    validateCurrentCalendar();
    TimezoneCalendar targetCalendar = validateTargetCalendar(targetCalendarName);

    LocalDateTime sourceDate = LocalDateTime.parse(date + "T00:00");
    LocalDateTime nextDay = sourceDate.plusDays(1);
    List<Event> eventsToday = currentCalendar.findEventsInRange(sourceDate, nextDay);

    if (eventsToday.isEmpty()) {
      throw new IllegalArgumentException("No events found on this day");
    }

    // Calculate the time difference between dates for offset
    Duration dateDiff = Duration.between(
            LocalDateTime.parse(date + "T00:00"),
            LocalDateTime.parse(newDate + "T00:00"));

    for (Event event : eventsToday) {
      // Convert event times to target calendar's timezone
      LocalDateTime sourceStart = event.getStartDateTime()
              .atZone(currentCalendar.timeZone.toZoneId())
              .withZoneSameInstant(targetCalendar.timeZone.toZoneId())
              .toLocalDateTime();

      LocalDateTime sourceEnd = event.getEndDateTime()
              .atZone(currentCalendar.timeZone.toZoneId())
              .withZoneSameInstant(targetCalendar.timeZone.toZoneId())
              .toLocalDateTime();

      // Apply the date difference while preserving the time
      LocalDateTime targetStart = sourceStart.plus(dateDiff);
      LocalDateTime targetEnd = sourceEnd.plus(dateDiff);

      Event newEvent = Event.getBuilder(event.getSubject(), targetStart)
              .endDateTime(targetEnd)
              .description(event.getDescription())
              .location(event.getLocation())
              .status(event.getStatus())
              .seriesId(event.getSeriesId())
              .build();

      targetCalendar.createEvent(newEvent);
    }
  }

  @Override
  public void copyEventsBetween(String startDate, String endDate, String targetCalendarName,
                                String newStartDate)
          throws IllegalArgumentException {
    validateCurrentCalendar();
    TimezoneCalendar targetCalendar = validateTargetCalendar(targetCalendarName);

    LocalDateTime rangeStart = LocalDateTime.parse(startDate + "T00:00");
    LocalDateTime rangeEnd = LocalDateTime.parse(endDate + "T23:59:59");
    List<Event> eventsInRange = currentCalendar.findEventsInRange(rangeStart, rangeEnd);

    if (eventsInRange.isEmpty()) {
      throw new IllegalArgumentException("No events found in this range of times");
    }

    // Calculate the time difference between start dates for offset
    Duration dateDiff = Duration.between(
            LocalDateTime.parse(startDate + "T00:00"),
            LocalDateTime.parse(newStartDate + "T00:00"));

    for (Event event : eventsInRange) {
      // Convert event times to target calendar's timezone
      LocalDateTime sourceStart = event.getStartDateTime()
              .atZone(currentCalendar.timeZone.toZoneId())
              .withZoneSameInstant(targetCalendar.timeZone.toZoneId())
              .toLocalDateTime();

      LocalDateTime sourceEnd = event.getEndDateTime()
              .atZone(currentCalendar.timeZone.toZoneId())
              .withZoneSameInstant(targetCalendar.timeZone.toZoneId())
              .toLocalDateTime();

      // Apply the date difference while preserving the time
      LocalDateTime targetStart = sourceStart.plus(dateDiff);
      LocalDateTime targetEnd = sourceEnd.plus(dateDiff);

      Event newEvent = Event.getBuilder(event.getSubject(), targetStart)
              .endDateTime(targetEnd)
              .description(event.getDescription())
              .location(event.getLocation())
              .status(event.getStatus())
              .seriesId(event.getSeriesId())
              .build();

      targetCalendar.createEvent(newEvent);
    }
  }

  @Override
  public String getCalendars() throws IllegalStateException {
    if (calendarsByName.isEmpty()) {
      return "No calendars";
    }
    List<String> sortedNames = new ArrayList<>(calendarsByName.keySet());
    Collections.sort(sortedNames);
    return String.join("\n", sortedNames);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param event the event to be created
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public void createEvent(Event event) throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    currentCalendar.createEvent(event);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param event    the event to be repeated
   * @param weekdays weekdays the characters representing the weekdays (e.g., "MWF")
   * @param n        the number of times the event should be repeated
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public void createEventSeriesNTimes(Event event, String weekdays, int n)
          throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    currentCalendar.createEventSeriesNTimes(event, weekdays, n);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param event    the event to be repeated
   * @param weekdays weekdays the characters representing the weekdays (e.g., "MWF")
   * @param until    the date the event should repeat until
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public void createEventSeriesUntil(Event event, String weekdays, String until)
          throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    currentCalendar.createEventSeriesUntil(event, weekdays, until);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param property      the property to be changed
   * @param subject       the subject of the event to be changed
   * @param startDateTime the start date and time of the event to be changed
   * @param endDateTime   the end date and time of the event to be changed
   * @param newValue      the new value of the property
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public void editEvent(String property, String subject, String startDateTime,
                        String endDateTime, String newValue) throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    currentCalendar.editEvent(property, subject, startDateTime, endDateTime, newValue);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param property      the property to be changed
   * @param subject       the subject of the event to be changed
   * @param startDateTime the start date and time of the event to be changed
   * @param newValue      the new value of the property
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public void editEvents(String property, String subject, String startDateTime,
                         String newValue) throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    currentCalendar.editEvents(property, subject, startDateTime, newValue);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param property      the property to be changed
   * @param subject       the subject of the event to be changed
   * @param startDateTime the start date and time of the event to be changed
   * @param newValue      the new value of the property
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public void editEventSeries(String property, String subject,
                              String startDateTime, String newValue) throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    currentCalendar.editEventSeries(property, subject, startDateTime, newValue);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param date the date to be checked.
   * @return A string of the schedule of the day
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public String daySchedule(String date) throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    return currentCalendar.daySchedule(date);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param time1 starting date and time
   * @param time2 ending date and time
   * @return a string schedule of the days in this range
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public String rangeSchedule(String time1, String time2) throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    return currentCalendar.rangeSchedule(time1, time2);
  }

  /**
   * Delegates this function to the prexisting SingleCalendar class.
   *
   * @param date the date to be checked
   * @return busy if events occur on that day, availible otherwise
   * @throws IllegalStateException if no calendar is selected
   */
  @Override
  public String isFree(String date) throws IllegalStateException {
    if (currentCalendar == null) {
      throw new IllegalStateException("No calendar selected");
    }
    return currentCalendar.isFree(date);
  }


}