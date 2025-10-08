import org.junit.Test;

import model.CalendarsProperty;
import model.Property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the property enum.
 */
public class TestCalendarsProperty {
  @Test
  public void testPropertyFromString() {
    assertEquals(CalendarsProperty.NAME, CalendarsProperty.fromString("name"));
    assertEquals(CalendarsProperty.TIMEZONE, CalendarsProperty.fromString("timezone"));
  }

  @Test
  public void testPropertyFromStringCaseInsensitive() {
    assertEquals(CalendarsProperty.NAME, CalendarsProperty.fromString("NAME"));
    assertEquals(CalendarsProperty.TIMEZONE, CalendarsProperty.fromString("TIMEZONE"));
  }

  @Test
  public void testPropertyFromStringWithWhitespace() {
    assertEquals(CalendarsProperty.NAME, CalendarsProperty.fromString(" name "));
    assertEquals(CalendarsProperty.TIMEZONE, CalendarsProperty.fromString(" timezone "));
  }

  @Test
  public void testInvalidProperty() {
    try {
      Property.fromString("invalid");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid property", e.getMessage());
    }
  }

  @Test
  public void testPropertyGetters() {
    assertEquals("name", CalendarsProperty.NAME.getName());
    assertEquals(String.class, CalendarsProperty.NAME.getType());
    assertEquals("timezone", CalendarsProperty.TIMEZONE.getName());
    assertEquals(String.class, CalendarsProperty.TIMEZONE.getType());
  }

  @Test
  public void testToString() {
    assertEquals("name", CalendarsProperty.NAME.toString());
    assertEquals("timezone", CalendarsProperty.TIMEZONE.toString());
  }


  @Test
  public void testAllProperties() {
    CalendarsProperty[] allProperties = CalendarsProperty.values();
    assertEquals(2, allProperties.length);

    // make sure that all expected properties exist
    boolean hasName = false;
    boolean hasTimezone = false;

    for (CalendarsProperty prop : allProperties) {
      switch (prop) {
        case NAME:
          hasName = true;
          break;
        case TIMEZONE:
          hasTimezone = true;
          break;
        default:
          throw new IllegalStateException("Unexpected property: " + prop);
      }
    }

    assertTrue(hasName);
    assertTrue(hasTimezone);
  }

  @Test
  public void testEmptyStringProperty() {
    try {
      Property.fromString("");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Property name cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testNullStringProperty() {
    try {
      Property.fromString(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Property name cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testWhitespaceOnlyProperty() {
    try {
      Property.fromString("   ");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Property name cannot be null or empty", e.getMessage());
    }
  }


  @Test
  public void testPropertyEnumConstruction() {
    // tests that each property has correct name and type
    for (CalendarsProperty prop : CalendarsProperty.values()) {
      assertEquals(String.class, prop.getType());
      assertTrue(!prop.getName().isEmpty());
      assertEquals(prop.getName(), prop.toString());
    }
  }
  
  
}
