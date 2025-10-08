package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import view.View;

/**
 * Implementation of the Calendar interface that manages calendar events.
 */
public class SingleCalendar implements Calendar {
  final Map<LocalDateTime, List<Event>> eventsByStartDate;

  /**
   * Constructs a SingleCalendar with the specified view.
   *
   * @param view the view for displaying messages and errors
   * @throws IllegalArgumentException if the view is null
   */
  public SingleCalendar(View view) throws IllegalArgumentException {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.eventsByStartDate = new HashMap<>();

  }

  @Override
  public void createEvent(Event event) throws IllegalArgumentException {
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }

    if (isDuplicate(event)) {
      throw new IllegalArgumentException("An event with the same name and time already exists");
    }

    // adds it to the list if it doesnt exist already
    eventsByStartDate.computeIfAbsent(event.getStartDateTime(), k ->
            new ArrayList<>()).add(event);
  }


  @Override
  public void createEventSeriesNTimes(Event event, String weekdays, int n) {
    // create an event series to generate a proper seriesId
    EventSeries series = EventSeries.getBuilder(event.getSubject(), event.getStartDateTime())
            .endDateTime(event.getEndDateTime())
            .description(event.getDescription())
            .location(event.getLocation())
            .status(event.getStatus())
            .weekdays(weekdays)
            .occurrences(n)
            .build();

    // use the series to generate all events with proper seriesId
    List<Event> eventsToCreate = series.generateSeriesEvents();

    // checking for duplicates and create events
    for (Event newEvent : eventsToCreate) {
      if (isDuplicate(newEvent)) {
        throw new IllegalArgumentException("This would create a duplication");
      }
      eventsByStartDate.computeIfAbsent(
              newEvent.getStartDateTime(), k -> new ArrayList<>()).add(newEvent);
    }
  }


  @Override
  public void createEventSeriesUntil(Event event, String weekdays, String until)
          throws IllegalArgumentException {
    LocalDateTime untilDate = LocalDateTime.parse(until + "T00:00");
    EventSeries series = EventSeries.getBuilder(event.getSubject(), event.getStartDateTime())
            .endDateTime(event.getEndDateTime())
            .description(event.getDescription())
            .location(event.getLocation())
            .status(event.getStatus())
            .weekdays(weekdays)
            .seriesEndDate(untilDate)
            .build();

    // use the EventSeries to generate all events with the same seriesId
    List<Event> eventsToCreate = series.generateSeriesEvents();

    // checking for duplicates before creating any events
    for (Event newEvent : eventsToCreate) {
      if (isDuplicate(newEvent)) {
        throw new IllegalArgumentException("This would create a duplication");
      }
    }

    // if no duplicates found, create all events
    for (Event e : eventsToCreate) {
      eventsByStartDate.computeIfAbsent(
              e.getStartDateTime(), k -> new ArrayList<>()).add(e);
    }

  }

  @Override
  public void editEvent(String property, String subject, String startDateTime,
                        String endDateTime, String newValue) throws IllegalArgumentException {
    LocalDateTime start = LocalDateTime.parse(startDateTime);
    LocalDateTime end = LocalDateTime.parse(endDateTime);
    Property propertyName = Property.fromString(property);

    // unable to find event to edit

    Event oldEvent = null;

    List<Event> eventsAtStart = eventsByStartDate.get(start);
    if (eventsAtStart != null) {
      for (Event event : eventsAtStart) {
        if (event.getSubject().equals(subject) && event.getEndDateTime().equals(end)) {
          oldEvent = event;
        }
      }
    }

    if (oldEvent == null) {
      throw new IllegalArgumentException("Event not found");
    }

    // makes a new event to replace the old event
    Event newEvent = createUpdatedEvent(oldEvent, propertyName, newValue);
    replaceEvent(oldEvent, newEvent);
  }


  private Event findEvent(String subject, LocalDateTime start) {
    List<Event> eventsAtStart = eventsByStartDate.get(start);
    if (eventsAtStart != null) {
      for (Event event : eventsAtStart) {
        if (event.getSubject().equals(subject)) {
          return event;
        }
      }
    }
    return null;
  }


  @Override
  public void editEvents(String property, String subject, String startDateTime, String newValue)
          throws IllegalArgumentException {
    Property propertyToEdit = Property.fromString(property);
    LocalDateTime start = LocalDateTime.parse(startDateTime);


    Event targetEvent = findEvent(subject, start);
    if (targetEvent == null) {
      throw new IllegalArgumentException("No matching events found");
    }

    if (targetEvent.isPartOfSeries()) {
      // all events that need to be updated if it is in a series
      List<Event> eventsToUpdate = new ArrayList<>();
      String seriesId = targetEvent.getSeriesId();

      // gets all the matching series
      for (Map.Entry<LocalDateTime, List<Event>> entry : eventsByStartDate.entrySet()) {
        if (!entry.getKey().isBefore(start)) {
          for (Event event : entry.getValue()) {
            if (event.getSeriesId() != null && event.getSeriesId().equals(seriesId)) {
              eventsToUpdate.add(event);
            }
          }
        }
      }

      // replaces old events
      for (Event event : eventsToUpdate) {
        Event newEvent = createUpdatedEvent(event, propertyToEdit, newValue);
        replaceEvent(event, newEvent);
      }
    }
    // treat like single event
    else {
      Event newEvent = createUpdatedEvent(targetEvent, propertyToEdit, newValue);
      replaceEvent(targetEvent, newEvent);
    }


  }

  @Override
  public void editEventSeries(String property, String subject, String startDateTime,
                              String newValue) {
    Property propertyToEdit = Property.fromString(property);
    LocalDateTime start = LocalDateTime.parse(startDateTime);


    // finds the target event to get the series ID
    Event targetEvent = null;
    List<Event> eventsAtStart = eventsByStartDate.get(start);
    if (eventsAtStart != null) {
      for (Event event : eventsAtStart) {
        if (event.getSubject().equals(subject)) {
          targetEvent = event;
          break;
        }
      }
    }

    if (targetEvent == null) {
      throw new IllegalArgumentException("Event not found");
    }

    if (targetEvent.isPartOfSeries()) {
      // for the series events, find all events with the same series ID
      List<Event> eventsToUpdate = new ArrayList<>();
      String seriesId = targetEvent.getSeriesId();

      // collects all events in the series
      for (List<Event> events : eventsByStartDate.values()) {
        for (Event event : events) {
          if (event.getSeriesId() != null && event.getSeriesId().equals(seriesId)) {
            eventsToUpdate.add(event);
          }
        }
      }

      // updates all events in the series
      for (Event event : eventsToUpdate) {
        Event newEvent = createUpdatedEvent(event, propertyToEdit, newValue);
        replaceEvent(event, newEvent);
      }
    } else {
      // for non-series events, just update the single event
      Event newEvent = createUpdatedEvent(targetEvent, propertyToEdit, newValue);
      replaceEvent(targetEvent, newEvent);
    }

  }


  @Override
  public String daySchedule(String date) {
    LocalDateTime startOfDay = LocalDateTime.parse(date + "T00:00");
    LocalDateTime endOfDay = LocalDateTime.parse(date + "T23:59:59.999999999");

    List<Event> events = findEventsInRange(startOfDay, endOfDay);
    return formatSchedule(events);
  }

  @Override
  public String rangeSchedule(String time1, String time2) {
    LocalDateTime start = LocalDateTime.parse(time1);
    LocalDateTime end = LocalDateTime.parse(time2);

    List<Event> events = findEventsInRange(start, end);
    return formatSchedule(events);
  }

  @Override
  public String isFree(String date) {
    LocalDateTime targetTime = LocalDateTime.parse(date);
    List<Event> events = findEventsInRange(targetTime, targetTime);

    if (events.isEmpty()) {
      return "Available";
    } else {
      return "Busy";
    }
  }


  /**
   * checks if an event with the same name and time already exists.
   *
   * @param event the vent to be checked
   * @return true if the event exists
   */
  private boolean isDuplicate(Event event) {
    List<Event> eventsAtStart = eventsByStartDate.get(event.getStartDateTime());
    if (eventsAtStart != null) {
      for (Event existingEvent : eventsAtStart) {
        if (existingEvent.getSubject().equals(event.getSubject())
                && existingEvent.getEndDateTime().equals(event.getEndDateTime())) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * Finds all events within a given time range.
   *
   * @param start the start time of the time range
   * @param end   the end time of the time range
   * @return a list of events within the given time range, empty list if no events are found
   */
  List<Event> findEventsInRange(LocalDateTime start, LocalDateTime end) {
    List<Event> events = new ArrayList<>();
    for (Map.Entry<LocalDateTime, List<Event>> entry : eventsByStartDate.entrySet()) {
      for (Event event : entry.getValue()) {
        if (!(event.getEndDateTime().isBefore(start) || event.getStartDateTime().isAfter(end))) {
          events.add(event);
        }
      }
    }
    return events;
  }

  /**
   * Creates a new event based on an old event and a property and a new value.
   *
   * @param oldEvent the event to be used as a base for the new event
   * @param property the property to be updated
   * @param newValue the new value for the property
   * @return the new event
   */
  private Event createUpdatedEvent(Event oldEvent, Property property, String newValue) {
    Event.EventBuilder builder = Event.getBuilder(oldEvent.getSubject(),
                    oldEvent.getStartDateTime())
            .endDateTime(oldEvent.getEndDateTime())
            .description(oldEvent.getDescription())
            .location(oldEvent.getLocation())
            .status(oldEvent.getStatus())
            .seriesId(oldEvent.getSeriesId());

    switch (property) {
      case SUBJECT:
        builder = Event.getBuilder(newValue, oldEvent.getStartDateTime());
        builder.endDateTime(oldEvent.getEndDateTime())
                .description(oldEvent.getDescription())
                .location(oldEvent.getLocation())
                .status(oldEvent.getStatus())
                .seriesId(oldEvent.getSeriesId());
        break;

      case START:
        LocalDateTime newStart = LocalDateTime.parse(newValue);
        if (newStart.isAfter(oldEvent.getEndDateTime())) {
          throw new IllegalArgumentException("New end time would be after original end time");
        }
        builder = Event.getBuilder(oldEvent.getSubject(), newStart)
                .endDateTime(oldEvent.getEndDateTime())
                .description(oldEvent.getDescription())
                .location(oldEvent.getLocation())
                .status(oldEvent.getStatus())
                .seriesId(oldEvent.getSeriesId());
        break;


      case END:
        LocalDateTime newEnd = LocalDateTime.parse(newValue);
        if (newEnd.isBefore(oldEvent.getStartDateTime())) {
          throw new IllegalArgumentException("End time cannot be before start time");
        }
        builder.endDateTime(newEnd);
        break;
      case DESCRIPTION:
        builder.description(newValue);
        break;
      case LOCATION:
        builder.location(newValue);
        break;
      case STATUS:
        if (!Property.isValidStatus(newValue)) {
          throw new IllegalArgumentException("Invalid status");
        }
        builder.status(newValue);
        break;
      default:
        throw new IllegalArgumentException("Invalid property");
    }

    return builder.build();
  }

  /**
   * Replaces an event with a new event.
   *
   * @param oldEvent the event to be replaced
   * @param newEvent the new event
   */
  private void replaceEvent(Event oldEvent, Event newEvent) {
    removeEvent(oldEvent);
    createEvent(newEvent);
  }

  /**
   * Removes an event from the list of events.
   *
   * @param event the event to be removed
   */
  private void removeEvent(Event event) {
    List<Event> eventsAtStart = eventsByStartDate.get(event.getStartDateTime());
    if (eventsAtStart != null) {
      eventsAtStart.remove(event);
      if (eventsAtStart.isEmpty()) {
        eventsByStartDate.remove(event.getStartDateTime());
      }
    }
  }

  /**
   * Formats schedule output.
   *
   * @param events the list of events to be formatted
   * @return the formatted schedule, empty string if no events are found
   */
  public String formatSchedule(List<Event> events) {
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    if (events.isEmpty()) {
      return "";
    }

    StringBuilder schedule = new StringBuilder();
    events.sort(Comparator.comparing(Event::getStartDateTime));

    for (Event event : events) {
      schedule.append("• ").append(event.getSubject()).append(" (");

      // format start time
      schedule.append(event.getStartDateTime().format(dateFormatter))
              .append(" ")
              .append(event.getStartDateTime().format(timeFormatter))
              .append(" - ");

      // include end date if different from start date
      if (!event.getStartDateTime().toLocalDate().equals(event.getEndDateTime().toLocalDate())) {
        schedule.append(event.getEndDateTime().format(dateFormatter))
                .append(" ");
      }
      schedule.append(event.getEndDateTime().format(timeFormatter))
              .append(")");

      // add location if present
      if (!event.getLocation().isEmpty()) {
        schedule.append(" @ ").append(event.getLocation());
      }
      schedule.append("\n");
    }

    return schedule.toString();
  }
}