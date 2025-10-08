import model.Weekday;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;

/**
 * Test class for Weekday enum class.
 */
public class TestWeekday {

  @Test
  public void testWeekdayFromChar() {
    assertEquals(Weekday.M, Weekday.fromChar('M'));
    assertEquals(Weekday.T, Weekday.fromChar('T'));
    assertEquals(Weekday.W, Weekday.fromChar('W'));
    assertEquals(Weekday.R, Weekday.fromChar('R'));
    assertEquals(Weekday.F, Weekday.fromChar('F'));
    assertEquals(Weekday.S, Weekday.fromChar('S'));
    assertEquals(Weekday.U, Weekday.fromChar('U'));
  }

  @Test
  public void testInvalidWeekdayChar() {
    try {
      Weekday.fromChar('X');
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid weekday", e.getMessage());
    }
  }

  @Test
  public void testToJavaDayOfWeek() {
    assertEquals(DayOfWeek.MONDAY, Weekday.M.toJavaDayOfWeek());
    assertEquals(DayOfWeek.TUESDAY, Weekday.T.toJavaDayOfWeek());
    assertEquals(DayOfWeek.WEDNESDAY, Weekday.W.toJavaDayOfWeek());
    assertEquals(DayOfWeek.THURSDAY, Weekday.R.toJavaDayOfWeek());
    assertEquals(DayOfWeek.FRIDAY, Weekday.F.toJavaDayOfWeek());
    assertEquals(DayOfWeek.SATURDAY, Weekday.S.toJavaDayOfWeek());
    assertEquals(DayOfWeek.SUNDAY, Weekday.U.toJavaDayOfWeek());
  }

  @Test
  public void testGetName() {
    assertEquals("Monday", Weekday.M.getName());
    assertEquals("Tuesday", Weekday.T.getName());
    assertEquals("Wednesday", Weekday.W.getName());
    assertEquals("Thursday", Weekday.R.getName());
    assertEquals("Friday", Weekday.F.getName());
    assertEquals("Saturday", Weekday.S.getName());
    assertEquals("Sunday", Weekday.U.getName());
  }

  @Test
  public void testToString() {
    assertEquals("Monday", Weekday.M.toString());
    assertEquals("Tuesday", Weekday.T.toString());
    assertEquals("Wednesday", Weekday.W.toString());
    assertEquals("Thursday", Weekday.R.toString());
    assertEquals("Friday", Weekday.F.toString());
    assertEquals("Saturday", Weekday.S.toString());
    assertEquals("Sunday", Weekday.U.toString());
  }


  @Test
  public void testValidateWeekdaysWithNull() {
    try {
      Weekday.validateWeekdays(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Weekdays cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testValidateWeekdaysWithEmptyString() {
    try {
      Weekday.validateWeekdays("");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Weekdays cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testValidateWeekdaysWithWhitespaceOnly() {
    try {
      Weekday.validateWeekdays("   ");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Weekdays cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testValidateWeekdaysWithInvalidChar() {
    try {
      Weekday.validateWeekdays("MXF");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid weekday", e.getMessage());
    }
  }

  @Test
  public void testAllWeekdaysConversion() {
    char[] weekdayChars = {
      'M', 'T', 'W', 'R', 'F', 'S', 'U'
    };
    DayOfWeek[] javaDays = {
      DayOfWeek.MONDAY,
      DayOfWeek.TUESDAY,
      DayOfWeek.WEDNESDAY,
      DayOfWeek.THURSDAY,
      DayOfWeek.FRIDAY,
      DayOfWeek.SATURDAY,
      DayOfWeek.SUNDAY
    };


    for (int i = 0; i < weekdayChars.length; i++) {
      Weekday weekday = Weekday.fromChar(weekdayChars[i]);
      assertEquals(javaDays[i], weekday.toJavaDayOfWeek());
    }
  }

  @Test
  public void testAllWeekdayEnumValues() {
    Weekday[] allWeekdays = Weekday.values();
    assertEquals(7, allWeekdays.length);

    // Verify all expected weekdays exist
    boolean hasM = false;
    boolean hasT = false;
    boolean hasW = false;
    boolean hasR = false;
    boolean hasF = false;
    boolean hasS = false;
    boolean hasU = false;

    for (Weekday day : allWeekdays) {
      switch (day) {
        case M:
          hasM = true;
          break;
        case T:
          hasT = true;
          break;
        case W:
          hasW = true;
          break;
        case R:
          hasR = true;
          break;
        case F:
          hasF = true;
          break;
        case S:
          hasS = true;
          break;
        case U:
          hasU = true;
          break;
        default:
          throw new IllegalStateException("Unexpected weekday: " + day);
      }
    }

    assertTrue(hasM);
    assertTrue(hasT);
    assertTrue(hasW);
    assertTrue(hasR);
    assertTrue(hasF);
    assertTrue(hasS);
    assertTrue(hasU);
  }

  @Test
  public void testInvalidWeekdayChars() {
    char[] invalidChars = {
      'A', 'B', 'C', 'D', 'E', 'G', 'H', 'I', 'J', 'K', 'L',
      'N', 'O', 'P', 'Q', 'V', 'X', 'Y', 'Z'
    };

    for (char c : invalidChars) {
      try {
        Weekday.fromChar(c);
        fail("Expected IllegalArgumentException for char: " + c);
      } catch (IllegalArgumentException e) {
        assertEquals("Invalid weekday", e.getMessage());
      }
    }
  }

  @Test
  public void testLowercaseChars() {
    // tests  that lowercase letters throw exceptions (since the enum expects uppercase)
    char[] lowercaseChars = {'m', 't', 'w', 'r', 'f', 's', 'u'};

    for (char c : lowercaseChars) {
      try {
        Weekday.fromChar(c);
        fail("Expected IllegalArgumentException for lowercase char: " + c);
      } catch (IllegalArgumentException e) {
        assertEquals("Invalid weekday", e.getMessage());
      }
    }
  }
}