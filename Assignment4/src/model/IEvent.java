package model;

import java.time.LocalDateTime;

/**
 * Represents an event that can get its properties and be copied.
 */
public interface IEvent {
  /**
   * Gets the subject/title of the event.
   * @return the subject
   */
  String getSubject();

  /**
   * Gets the start date and time of the event.
   * @return the start date and time
   */
  LocalDateTime getStartDateTime();

  /**
   * Gets the end date and time of the event.
   * @return the end date and time
   */
  LocalDateTime getEndDateTime();

  /**
   * Gets the description of the event.
   * @return the description
   */
  String getDescription();

  /**
   * Gets the location of the event.
   * @return the location
   */
  String getLocation();

  /**
   * Gets the status of the event.
   * @return the status
   */
  String getStatus();

  /**
   * Creates a copy of this event with new start and end times.
   * @param start the new start time
   * @param end the new end time
   * @return a new Event object with the specified times
   */
  Event copyEventToNewDate(LocalDateTime start, LocalDateTime end);

  /**
   * Gets the series ID if the event is part of a series.
   * @return the series ID or null if not part of a series
   */
  String getSeriesId();

  /**
   * Checks if the event is part of a series.
   * @return true if the event is part of a series, false otherwise
   */
  boolean isPartOfSeries();

}