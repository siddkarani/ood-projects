/**
 * Represents a single date with day, month, and year.
 * This class validates dates according to calendar rules including leap years.
 */
public class MyDate {
  private int day;
  private int month;
  private int year;

  // Constants to help with date calculations
  private static final int[] DAYS_IN_MONTH = [0,31,28,31,30,31,30,31,31,30,31,30,31];
  private static final int MIN_YEAR = 0;
  private static final int MIN_MONTH = 1;
  private static final int MAX_MONTH = 12;
  private static final int MIN_DAY = 1;

  /**
   * Constructs a MyDate object if date is valid.
   *
   * @param day   the day of the month
   * @param month the month (1-12)
   * @param year  the year (non-negative)
   * @throws IllegalArgumentException if the date is invalid
   */
  public MyDate(int day, int month, int year) throws IllegalArgumentException {
    // Validate year
    if (year < MIN_YEAR) {
      throw new IllegalArgumentException("Year cannot be negative");
    }

    // Validate month
    if (month < MIN_MONTH || month > MAX_MONTH) {
      throw new IllegalArgumentException("Month must be between 1 and 12");
    }

    // Validate day
    int maxDaysInMonth = getDaysInMonth(month, year);
    if (day < MIN_DAY || day > maxDaysInMonth) {
      throw new IllegalArgumentException("Day must be between 1 and " + maxDaysInMonth + " for month " + month);
    }

    this.day = day;
    this.month = month;
    this.year = year;
  }

  /**
   * Adds or removes days by the given amount.
   * The lowest possible date is Jan 1 0000.
   *
   * @param days positive to add days, negative to remove days
   */
  public void advance(int days) {
    // Handle advancing (days > 0) or receding (days < 0)
    while (days != 0) {
      if (days > 0) {
        // Advance one day at a time
        advanceOneDay();
        days--;
      } else {
        // Recede one day at a time
        if (isFirstDay()) {
          // Cannot go before 1 Jan 0000
          break;
        }
        recedeOneDay();
        days++;
      }
    }
  }

  /**
   * Checks if the current date is the earliest valid date (1 Jan 0000).
   *
   * @return true if it is the earliest date, false otherwise
   */
  private boolean isFirstDay() {
    return day == 1 && month == 1 && year == 0;
  }

  /**
   * Adds one day to the current date.
   */
  private void advanceOneDay() {
    int maxDaysInMonth = getDaysInMonth(month, year);

    if (day < maxDaysInMonth) {
      // Simply increment the day
      day++;
    } else {
      // End of month, reset day and increment month
      day = 1;
      if (month < MAX_MONTH) {
        // Simply increment the month
        month++;
      } else {
        // End of year, reset month and increment year
        month = 1;
        year++;
      }
    }
  }

  /**
   * Removes one day from the current date.
   */
  private void recedeOneDay() {
    if (day > 1) {
      // Simply decrement the day
      day--;
    } else {
      // Beginning of month, go to end of previous month
      if (month > 1) {
        month--;
        day = getDaysInMonth(month, year);
      } else {
        // Beginning of year, go to end of previous year
        if (year > 0) {
          year--;
          month = MAX_MONTH;
          day = getDaysInMonth(month, year);
        }
      }
    }
  }

  /**
   * Determines if the specified year is a leap year.
   * A year is a leap year if it is divisible by 4 and either:
   * not divisible by 100, or divisible by both 100 and 400.
   *
   * @param year the year to check
   * @return true if it is a leap year, false otherwise
   */
  private boolean isLeapYear(int year) {
    return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
  }

  /**
   * Returns the number of days in the given month and year.
   *
   * @param month the month to check
   * @param year  the year to check
   * @return the number of days in the month
   */
  private int getDaysInMonth(int month, int year) {
    if (month == 2 && isLeapYear(year)) {
      return 29;
    }
    return DAYS_IN_MONTH[month];
  }

  /**
   * Returns a YYYY-MM-DD formatted string representation of this date.
   *
   * @return the formatted string
   */
  @Override
  public String toString() {
    // Format with %04d for year to ensure 4 digits with leading zeros
    return String.format("%04d-%02d-%02d", year, month, day);
  }
}