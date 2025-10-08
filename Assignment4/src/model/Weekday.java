package model;

import java.time.DayOfWeek;

/**
 * Represents the days of a week with their character representations.
 */
public enum Weekday {
  M("Monday", DayOfWeek.MONDAY),
  T("Tuesday", DayOfWeek.TUESDAY),
  W("Wednesday", DayOfWeek.WEDNESDAY),
  R("Thursday", DayOfWeek.THURSDAY),
  F("Friday", DayOfWeek.FRIDAY),
  S("Saturday", DayOfWeek.SATURDAY),
  U("Sunday", DayOfWeek.SUNDAY);

  private final String name;
  private final DayOfWeek javaDay;

  /**
   * Constructs a Weekday with its name and corresponding Java DayOfWeek.
   *
   * @param name the full name of the weekday
   * @param javaDay the corresponding Java DayOfWeek enum value
   */
  Weekday(String name, DayOfWeek javaDay) {
    this.name = name;
    this.javaDay = javaDay;
  }

  /**
   * Converts this weekday to its corresponding Java DayOfWeek value.
   *
   * @return the equivalent DayOfWeek constant
   */
  public DayOfWeek toJavaDayOfWeek() {
    return this.javaDay;
  }

  /**
   * Gets the full name of this weekday.
   *
   * @return the weekday name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Converts a character to its weekday representation.
   * Follows the Open/Closed Principle - new weekdays could be added
   * without modifying existing code.
   *
   * @param c the character to be converted
   * @return the corresponding weekday
   * @throws IllegalArgumentException if the character is not a valid weekday
   */
  public static Weekday fromChar(char c) throws IllegalArgumentException {
    switch (c) {
      case 'M': return M;
      case 'T': return T;
      case 'W': return W;
      case 'R': return R;
      case 'F': return F;
      case 'S': return S;
      case 'U': return U;
      default: throw new IllegalArgumentException("Invalid weekday");
    }
  }

  /**
   * Validates that all characters in a weekday string are valid.
   *
   * @param weekdays the string of weekday characters to validate
   * @throws IllegalArgumentException if any character is invalid
   */
  public static void validateWeekdays(String weekdays) throws IllegalArgumentException {
    if (weekdays == null || weekdays.trim().isEmpty()) {
      throw new IllegalArgumentException("Weekdays cannot be null or empty");
    }

    for (char c : weekdays.toCharArray()) {
      fromChar(c); // This will throw IllegalArgumentException if invalid
    }
  }

  /**
   * Converts this weekday to its corresponding string representation.
   * @return the weekday name as used in the calendar interface
   */
  @Override
  public String toString() {
    return name;
  }
}