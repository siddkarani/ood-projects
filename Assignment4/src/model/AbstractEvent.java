package model;

import java.time.LocalDateTime;

/**
 * Represents an AbstractEvent. Has all the properties of an event used in a calendar.
 */
public abstract class AbstractEvent implements IEvent {
  protected final String subject;
  protected final LocalDateTime startDateTime;
  protected final LocalDateTime endDateTime;
  protected final String description;
  protected final String location;
  protected final String status;
  protected String seriesId;

  protected AbstractEvent(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime,
                 String description, String location, String status, String seriesId) {
    this.subject = subject;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.description = description;
    this.location = location;
    this.status = status;
    this.seriesId = seriesId;
  }

  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }

  public String getStatus() {
    return status;
  }

  /**
   * Gets the series ID of an event in a series.
   * @return a string of the ID.
   */
  @Override
  public String getSeriesId() {
    return seriesId;
  }

  /**
   * Checks if the event is part of a series.
   * @return true if the event has a non-null and non-empty series ID, false otherwise
   */
  @Override
  public boolean isPartOfSeries() {
    return seriesId != null && !seriesId.isEmpty();
  }


}
