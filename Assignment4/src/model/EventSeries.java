package model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an eventSeries, where each has a unique ID. Used for Calendars or Calendar.
 */
public class EventSeries extends AbstractEvent {
  private final String weekdays;
  private final LocalDateTime seriesEndDate;
  private final int occurrences;

  private EventSeries(EventSeriesBuilder builder) {
    super(builder.subject,
            builder.startDateTime,
            builder.endDateTime,
            builder.description,
            builder.location,
            builder.status,
            null);
    this.weekdays = builder.weekdays;
    this.seriesEndDate = builder.seriesEndDate;
    this.occurrences = builder.occurrences;
    this.seriesId = generateSeriesId();
  }

  /**
   * Generates a unique ID for a series.
   * @return unique String id
   */
  private String generateSeriesId() {
    return String.format("%s-%s-%s",
            getSubject(),
            getStartDateTime().toString(),
            UUID.randomUUID().toString().substring(0, 12));
  }

  /**
   * Generates a list of events based on a recurring series configuration determined by start date, 
   * end date, weekday matches, and occurrence restrictions. The method creates and adds events
   * to the list until either the series end date is reached or the maximum occurrences is met.
   *
   * @return a list of Event objects representing the scheduled events for the series
   */
  public List<Event> generateSeriesEvents() {
    List<Event> events = new ArrayList<>();
    LocalDateTime currentStart = getStartDateTime();
    LocalDateTime currentEnd = getEndDateTime();
    int count = 0;

    // keep adding the event to list until done
    while ((seriesEndDate == null || !currentStart.isAfter(seriesEndDate))
            && (occurrences == -1 || count < occurrences)) {

      if (isWeekdayMatch(currentStart.getDayOfWeek())) {
        Event event = Event.getBuilder(getSubject(), currentStart)
                .endDateTime(currentEnd)
                .description(getDescription())
                .location(getLocation())
                .status(getStatus())
                .seriesId(this.seriesId)
                .build();
        events.add(event);
        count++;
      }

      currentStart = currentStart.plusDays(1);
      currentEnd = currentEnd.plusDays(1);
    }

    return events;
  }

  private boolean isWeekdayMatch(DayOfWeek day) {
    return weekdays.chars()
            .mapToObj(c -> Weekday.fromChar((char) c))
            .anyMatch(w -> w.toJavaDayOfWeek() == day);
  }

  @Override
  public Event copyEventToNewDate(LocalDateTime start, LocalDateTime end) {
    return Event.getBuilder(getSubject(), start)
            .endDateTime(end)
            .description(getDescription())
            .location(getLocation())
            .status(getStatus())
            .seriesId(getSeriesId())
            .build();
  }

  /**
   * Builds a EventSeries.
   * @param subject subject of the series
   * @param startDateTime start date time of the series
   * @return the EventSeriesBuilder instance for building an EventSeries
   */
  public static EventSeriesBuilder getBuilder(String subject, LocalDateTime startDateTime) {
    return new EventSeriesBuilder(subject, startDateTime);
  }

  /**
   * Builder class for creating EventSeries instances with configurable properties.
   * Uses the builder pattern to construct EventSeries objects step by step.
   */
  public static class EventSeriesBuilder {
    private final String subject;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String description = "";
    private String location = "";
    private String status = "CONFIRMED";
    private String weekdays;
    private LocalDateTime seriesEndDate;
    private int occurrences = -1;

    /**
     * Creates a new EventSeriesBuilder with required subject and start date/time.
     *
     * @param subject the subject/title of the event series (must not be null or empty)
     * @param startDateTime the start date and time of the event series (must not be null)
     * @throws IllegalArgumentException if subject is null/empty or if startDateTime is null
     */
    public EventSeriesBuilder(String subject, LocalDateTime startDateTime) {
      if (subject == null || subject.trim().isEmpty()) {
        throw new IllegalArgumentException("Subject cannot be null or empty");
      }
      if (startDateTime == null) {
        throw new IllegalArgumentException("Start date time cannot be null");
      }
      this.subject = subject;
      this.startDateTime = startDateTime;
    }

    public EventSeriesBuilder endDateTime(LocalDateTime endDateTime) {
      this.endDateTime = endDateTime;
      return this;
    }

    public EventSeriesBuilder description(String description) {
      this.description = description;
      return this;
    }

    public EventSeriesBuilder location(String location) {
      this.location = location;
      return this;
    }

    public EventSeriesBuilder status(String status) {
      this.status = status;
      return this;
    }

    /**
     * Sets the weekdays on which the events in the series occur.
     *
     * @param weekdays string representing weekdays for the series
     * @return this builder for method chaining
     */
    public EventSeriesBuilder weekdays(String weekdays) {
      this.weekdays = weekdays;
      return this;
    }

    /**
     * Sets the end date for the entire event series.
     *
     * @param seriesEndDate the date and time when the series ends
     * @return this builder for method chaining
     */
    public EventSeriesBuilder seriesEndDate(LocalDateTime seriesEndDate) {
      this.seriesEndDate = seriesEndDate;
      return this;
    }

    /**
     * Sets the number of occurrences for the event series.
     *
     * @param occurrences number of times the event should repeat
     * @return this builder for method chaining
     */
    public EventSeriesBuilder occurrences(int occurrences) {
      this.occurrences = occurrences;
      return this;
    }

    /**
     * Builds and validates an EventSeries instance.
     *
     * @return new EventSeries instance
     * @throws IllegalArgumentException if validation fails
     */
    public EventSeries build() {
      // make sure weekdays are valid
      if (weekdays == null || weekdays.isEmpty()) {
        throw new IllegalArgumentException("Weekdays must be specified for series");
      }
      Weekday.validateWeekdays(weekdays);

      // make sure at least end date or n is inputted
      if (seriesEndDate == null && occurrences == -1) {
        throw new IllegalArgumentException("Either end date or number of " +
                "occurrences must be specified");
      }
      if (seriesEndDate != null && seriesEndDate.isBefore(startDateTime)) {
        throw new IllegalArgumentException("End date must be after start date");
      }
      if (occurrences != -1 && occurrences <= 0) {
        throw new IllegalArgumentException("Number of occurrences must be positive");
      }

      if (endDateTime == null) {
        this.endDateTime = LocalDateTime.of(startDateTime.toLocalDate(),
                LocalTime.of(17, 0));
        this.startDateTime = LocalDateTime.of(startDateTime.toLocalDate(),
                LocalTime.of(8, 0));

        // make sure start and end date are valid
        if (endDateTime.isBefore(startDateTime)) {
          throw new IllegalArgumentException("End time cannot be before start time");
        }
        if (!startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
          throw new IllegalArgumentException("Series events must start and end on the same day");
        }
      }
      return new EventSeries(this);
    }
  }
}