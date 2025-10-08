import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the MyDate implementation.
 * This class contains unit tests that verify the functionality of the MyDate class,
 * including date validation, leapYear calculations, and date advancement operations.
 */
public class MyDateTest {

  /**
   * Tests that valid dates can be constructed correctly.
   */
  @Test
  public void testValidDateConstruction() {
    // Test valid dates
    MyDate date1 = new MyDate(1, 1, 2000);
    Assert.assertEquals("2000-01-01", date1.toString());

    MyDate date2 = new MyDate(31, 12, 2020);
    Assert.assertEquals("2020-12-31", date2.toString());

    // Test leap year February 29
    MyDate leapDate = new MyDate(29, 2, 2020);
    Assert.assertEquals("2020-02-29", leapDate.toString());
  }

  /**
   * Tests that an exception is thrown when a day is too large for the month.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDay_TooLarge() {
    // Day too large for month
    new MyDate(32, 1, 2000);
  }

  /**
   * Tests that an exception is thrown when day is zero.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDay_Zero() {
    // Day zero is invalid
    new MyDate(0, 1, 2000);
  }

  /**
   * Tests that an exception is thrown when month is too large.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMonth_TooLarge() {
    // Month too large
    new MyDate(1, 13, 2000);
  }

  /**
   * Tests that an exception is thrown when month is zero.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMonth_Zero() {
    // Month zero is invalid
    new MyDate(1, 0, 2000);
  }

  /**
   * Tests that an exception is thrown when year is negative.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidYear_Negative() {
    // Negative year is invalid
    new MyDate(1, 1, -1);
  }

  /**
   * Tests that February 29th is invalid in non-leap years.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDayInFebruary_NonLeapYear() {
    // February 29 in non-leap year
    new MyDate(29, 2, 2019);
  }

  /**
   * Tests leap year validation for various scenarios.
   */
  @Test
  public void testLeapYearValidation() {
    // Valid leap years
    MyDate leapDate1 = new MyDate(29, 2, 2020); // Divisible by 4 but not by 100
    Assert.assertEquals("2020-02-29", leapDate1.toString());

    MyDate leapDate2 = new MyDate(29, 2, 2000); // Divisible by 400
    Assert.assertEquals("2000-02-29", leapDate2.toString());

    // Test all month lengths
    MyDate jan = new MyDate(31, 1, 2020);
    Assert.assertEquals("2020-01-31", jan.toString());

    MyDate feb = new MyDate(29, 2, 2020);
    Assert.assertEquals("2020-02-29", feb.toString());

    MyDate mar = new MyDate(31, 3, 2020);
    Assert.assertEquals("2020-03-31", mar.toString());

    MyDate apr = new MyDate(30, 4, 2020);
    Assert.assertEquals("2020-04-30", apr.toString());

    MyDate may = new MyDate(31, 5, 2020);
    Assert.assertEquals("2020-05-31", may.toString());

    MyDate jun = new MyDate(30, 6, 2020);
    Assert.assertEquals("2020-06-30", jun.toString());

    MyDate jul = new MyDate(31, 7, 2020);
    Assert.assertEquals("2020-07-31", jul.toString());

    MyDate aug = new MyDate(31, 8, 2020);
    Assert.assertEquals("2020-08-31", aug.toString());

    MyDate sep = new MyDate(30, 9, 2020);
    Assert.assertEquals("2020-09-30", sep.toString());

    MyDate oct = new MyDate(31, 10, 2020);
    Assert.assertEquals("2020-10-31", oct.toString());

    MyDate nov = new MyDate(30, 11, 2020);
    Assert.assertEquals("2020-11-30", nov.toString());

    MyDate dec = new MyDate(31, 12, 2020);
    Assert.assertEquals("2020-12-31", dec.toString());
  }

  /**
   * Tests that February 29th is invalid in century years that are not divisible by 400.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonLeapYear() {
    // Feb 29 in non-leap year (divisible by 100 but not by 400)
    new MyDate(29, 2, 1900);
  }

  /**
   * Tests advancing a date within the same month.
   */
  @Test
  public void testAdvanceSameMonth() {
    // Advance within same month
    MyDate date = new MyDate(15, 5, 2020);
    date.advance(5);
    Assert.assertEquals("2020-05-20", date.toString());
  }

  /**
   * Tests advancing a date to the next month.
   */
  @Test
  public void testAdvanceNextMonth() {
    // Advance to next month
    MyDate date = new MyDate(28, 2, 2020);
    date.advance(5);
    Assert.assertEquals("2020-03-04", date.toString());
  }

  /**
   * Tests advancing a date to the next year.
   */
  @Test
  public void testAdvanceNextYear() {
    // Advance to next year
    MyDate date = new MyDate(30, 12, 2020);
    date.advance(3);
    Assert.assertEquals("2021-01-02", date.toString());
  }

  /**
   * Tests advancing a date multiple years.
   */
  @Test
  public void testAdvanceMultipleYears() {
    // Advance multiple years
    MyDate date = new MyDate(1, 1, 2020);
    date.advance(365 + 366); // 2020 (leap) + 2021 (non-leap)
    Assert.assertEquals("2022-01-01", date.toString());
  }

  /**
   * Tests receding a date within the same month.
   */
  @Test
  public void testRecedeWithinMonth() {
    // Recede within same month
    MyDate date = new MyDate(15, 5, 2020);
    date.advance(-5);
    Assert.assertEquals("2020-05-10", date.toString());
  }

  /**
   * Tests receding a date to the previous month.
   */
  @Test
  public void testRecedeToPreviousMonth() {
    // Recede to previous month
    MyDate date = new MyDate(3, 3, 2020);
    date.advance(-5);
    Assert.assertEquals("2020-02-27", date.toString());
  }

  /**
   * Tests receding a date to the previous year.
   */
  @Test
  public void testRecedeToPreviousYear() {
    // Recede to previous year
    MyDate date = new MyDate(2, 1, 2020);
    date.advance(-3);
    Assert.assertEquals("2019-12-30", date.toString());
  }

  /**
   * Tests receding a date multiple years.
   */
  @Test
  public void testRecedeMultipleYears() {
    // Recede multiple years
    MyDate date = new MyDate(1, 1, 2022);
    date.advance(-(365 + 366)); // 2021 (non-leap) + 2020 (leap)
    Assert.assertEquals("2020-01-01", date.toString());
  }

  /**
   * Tests that advancing by zero days makes no change to the date.
   */
  @Test
  public void testAdvanceZeroDays() {
    // Advance zero days (no change)
    MyDate date = new MyDate(15, 6, 2020);
    date.advance(0);
    Assert.assertEquals("2020-06-15", date.toString());
  }

  /**
   * Tests the earliest possible date (January 1, 0000).
   */
  @Test
  public void testEarliestDate() {
    // Test earliest possible date (1 Jan 0000)
    MyDate date = new MyDate(1, 1, 0);
    Assert.assertEquals("0000-01-01", date.toString());
  }

  /**
   * Tests that attempting to recede before the earliest date clamps to January 1, 0000.
   */
  @Test
  public void testClampToEarliestDate() {
    // Test that receding past the earliest date clamps to 1 Jan 0000
    MyDate date = new MyDate(3, 1, 0);
    date.advance(-5); // Should clamp to 1 Jan 0000
    Assert.assertEquals("0000-01-01", date.toString());
  }

  /**
   * Tests proper zero-padding in the toString method.
   */
  @Test
  public void testToStringFormat() {
    // Test proper zero-padding in toString
    MyDate date1 = new MyDate(1, 1, 1);
    Assert.assertEquals("0001-01-01", date1.toString());

    MyDate date2 = new MyDate(9, 9, 9);
    Assert.assertEquals("0009-09-09", date2.toString());
  }
}