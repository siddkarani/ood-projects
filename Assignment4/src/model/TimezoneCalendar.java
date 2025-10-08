package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import view.View;

/**
 * Represents a TimezoneCalendar that has all the functionality of a SingleCalendar,
 * but it can handle timezone changes.
 */
public class TimezoneCalendar extends SingleCalendar {
  TimeZone timeZone;

  /**
   * Constructs a timezoneCalendar object.
   * @param view view
   * @param timeZone the timezone of the calendar
   */
  public TimezoneCalendar(View view, TimeZone timeZone) {
    super(view);
    this.timeZone = timeZone;
  }

  /**
   * Updates all events' start and end times when calendar timezone is changed.
   * Events are preserved but their times are adjusted to match the new timezone.
   * @param newTimeZone the new timezone to update to
   * @throws IllegalArgumentException if newTimeZone is null
   */
  public void updateTimes(TimeZone newTimeZone) {
    if (newTimeZone == null) {
      throw new IllegalArgumentException("New timezone cannot be null");
    }

    TimeZone oldTimeZone = this.timeZone;
    List<Event> allEvents = new ArrayList<>();

    // collect events
    for (List<Event> events : eventsByStartDate.values()) {
      allEvents.addAll(events);
    }

    // get rid of all old events
    eventsByStartDate.clear();

    // update the timezone
    this.timeZone = newTimeZone;

    // add the events back with the right time for this timezone
    for (Event event : allEvents) {
      LocalDateTime newStart = event.getStartDateTime()
              .atZone(oldTimeZone.toZoneId())
              .withZoneSameInstant(newTimeZone.toZoneId())
              .toLocalDateTime();

      LocalDateTime newEnd = event.getEndDateTime()
              .atZone(oldTimeZone.toZoneId())
              .withZoneSameInstant(newTimeZone.toZoneId())
              .toLocalDateTime();

      Event updatedEvent = Event.getBuilder(event.getSubject(), newStart)
              .endDateTime(newEnd)
              .description(event.getDescription())
              .location(event.getLocation())
              .status(event.getStatus())
              .seriesId(event.getSeriesId())
              .build();

      eventsByStartDate.computeIfAbsent(newStart, k -> new ArrayList<>()).add(updatedEvent);

    }
  }
}