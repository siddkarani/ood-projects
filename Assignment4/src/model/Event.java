package model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents an Event in a calendar with a subject, start date and time, end date and time,
 * description, location and status.
 */
public class Event extends AbstractEvent {

  private Event(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime,
                String description, String location, String status, String seriesId) {
    super(subject, startDateTime, endDateTime, description, location, status, seriesId);
  }

  /**
   * Gets a builder for creating events.
   * Follows the Builder pattern for flexible object construction.
   *
   * @param subject       the subject of the event
   * @param startDateTime the start date and time
   * @return a new EventBuilder
   */
  public static EventBuilder getBuilder(String subject, LocalDateTime startDateTime) {
    return new EventBuilder(subject, startDateTime);
  }

  /**
   * Creates a copy of this event with new start and end times.
   *
   * @param start the start time that it should be changed to
   * @param end   the end time that it should be changed to
   * @return a new Event object with changed start and end times
   */
  public Event copyEventToNewDate(LocalDateTime start, LocalDateTime end) {
    return new Event(this.subject, start, end, this.description,
            this.location, this.status, this.seriesId);
  }

  /**
   * Builder class for creating Event objects.
   */
  public static class EventBuilder {
    private final String subject;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String description = "";
    private String location = "";
    private String status = "";
    private String seriesId;

    /**
     * Creates an event builder with the required fields.
     *
     * @param subject       subject of the event as a String
     * @param startDateTime the start date time of the event
     */
    public EventBuilder(String subject, LocalDateTime startDateTime) {
      if (subject == null || subject.trim().isEmpty()) {
        throw new IllegalArgumentException("Subject cannot be null or empty");
      }
      if (startDateTime == null) {
        throw new IllegalArgumentException("Start date time cannot be null");
      }
      this.subject = subject;
      this.startDateTime = startDateTime;
    }

    /**
     * Sets the end date time for the event.
     *
     * @param endDateTime the end date time of an event
     * @return this builder for method chaining
     */
    public EventBuilder endDateTime(LocalDateTime endDateTime) {
      this.endDateTime = endDateTime;
      return this;
    }

    /**
     * Sets the description for the event.
     *
     * @param description the description for an event
     * @return this builder for method chaining
     */
    public EventBuilder description(String description) {
      this.description = description == null ? "" : description;
      return this;
    }

    /**
     * Sets the location for the event.
     *
     * @param location the location for an event
     * @return this builder for method chaining
     */
    public EventBuilder location(String location) {
      this.location = location == null ? "" : location;
      return this;
    }

    /**
     * Sets the status of the event.
     *
     * @param status either public or private
     * @return this builder for method chaining
     */
    public EventBuilder status(String status) {
      this.status = status == null ? "" : status;
      return this;
    }

    /**
     * Sets the series ID for the event.
     * @param seriesId series id for an event
     * @return this builder for method chaining
     */
    public EventBuilder seriesId(String seriesId) {
      this.seriesId = seriesId;
      return this;
    }


    /**
     * Builds an event with the specified properties.
     * Applies default all-day event logic if no end time is provided.
     *
     * @return an Event instance
     * @throws IllegalArgumentException if start time is after end time
     */
    public Event build() throws IllegalArgumentException {
      // if no end time is provided, create an all-day event (8am-5pm)
      if (this.endDateTime == null) {
        this.endDateTime = LocalDateTime.of(this.startDateTime.toLocalDate(),
                LocalTime.of(17, 0));
        this.startDateTime = LocalDateTime.of(this.startDateTime.toLocalDate(),
                LocalTime.of(8, 0));
      }

      // Validate that end time is not before start time
      if (this.endDateTime.isBefore(this.startDateTime)) {
        throw new IllegalArgumentException("Start time cannot be after end time");
      }

      return new Event(subject, startDateTime, this.endDateTime, description,
              location, status, seriesId);
    }
  }

  /**
   * Overrides default equals method.
   * @param o object to compare
   * @return true if equal
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Event)) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(subject, event.getSubject()) &&
            Objects.equals(startDateTime, event.getStartDateTime()) &&
            Objects.equals(endDateTime, event.getEndDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, startDateTime, endDateTime);
  }

  @Override
  public String toString() {
    return String.format("Event{subject='%s', start=%s, end=%s}",
            subject, startDateTime, endDateTime);
  }

}