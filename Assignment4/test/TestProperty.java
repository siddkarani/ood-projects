import model.Property;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test class for Property class.
 */
public class TestProperty {

  @Test
  public void testPropertyFromString() {
    assertEquals(Property.SUBJECT, Property.fromString("subject"));
    assertEquals(Property.START, Property.fromString("start"));
    assertEquals(Property.END, Property.fromString("end"));
    assertEquals(Property.DESCRIPTION, Property.fromString("description"));
    assertEquals(Property.LOCATION, Property.fromString("location"));
    assertEquals(Property.STATUS, Property.fromString("status"));
  }

  @Test
  public void testPropertyFromStringCaseInsensitive() {
    assertEquals(Property.SUBJECT, Property.fromString("SUBJECT"));
    assertEquals(Property.START, Property.fromString("Start"));
    assertEquals(Property.END, Property.fromString("eNd"));
    assertEquals(Property.DESCRIPTION, Property.fromString("DESCRIPTION"));
    assertEquals(Property.LOCATION, Property.fromString("Location"));
    assertEquals(Property.STATUS, Property.fromString("STATUS"));
  }

  @Test
  public void testPropertyFromStringWithWhitespace() {
    assertEquals(Property.SUBJECT, Property.fromString(" subject "));
    assertEquals(Property.START, Property.fromString("  start  "));
    assertEquals(Property.END, Property.fromString("\tend\t"));
    assertEquals(Property.DESCRIPTION, Property.fromString(" DESCRIPTION "));
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
    assertEquals("subject", Property.SUBJECT.getName());
    assertEquals(String.class, Property.SUBJECT.getType());
    assertEquals("start", Property.START.getName());
    assertEquals(String.class, Property.START.getType());
    assertEquals("end", Property.END.getName());
    assertEquals(String.class, Property.END.getType());
    assertEquals("description", Property.DESCRIPTION.getName());
    assertEquals(String.class, Property.DESCRIPTION.getType());
    assertEquals("location", Property.LOCATION.getName());
    assertEquals(String.class, Property.LOCATION.getType());
    assertEquals("status", Property.STATUS.getName());
    assertEquals(String.class, Property.STATUS.getType());
  }

  @Test
  public void testToString() {
    assertEquals("subject", Property.SUBJECT.toString());
    assertEquals("start", Property.START.toString());
    assertEquals("end", Property.END.toString());
    assertEquals("description", Property.DESCRIPTION.toString());
    assertEquals("location", Property.LOCATION.toString());
    assertEquals("status", Property.STATUS.toString());
  }

  @Test
  public void testIsValidStatus() {
    // Test valid statuses
    assertTrue(Property.isValidStatus(null));
    assertTrue(Property.isValidStatus(""));
    assertTrue(Property.isValidStatus("public"));
    assertTrue(Property.isValidStatus("private"));
    assertTrue(Property.isValidStatus("PUBLIC"));
    assertTrue(Property.isValidStatus("PRIVATE"));
    assertTrue(Property.isValidStatus("Public"));
    assertTrue(Property.isValidStatus("Private"));

    // tests invalid statuses
    assertFalse(Property.isValidStatus("invalid"));
    assertFalse(Property.isValidStatus("secret"));
    assertFalse(Property.isValidStatus("confidential"));
    assertFalse(Property.isValidStatus("123"));
  }

  @Test
  public void testAllProperties() {
    Property[] allProperties = Property.values();
    assertEquals(6, allProperties.length);

    // make sure that all expected properties exist
    boolean hasSubject = false;
    boolean hasStart = false;
    boolean hasEnd = false;
    boolean hasDescription = false;
    boolean hasLocation = false;
    boolean hasStatus = false;

    for (Property prop : allProperties) {
      switch (prop) {
        case SUBJECT:
          hasSubject = true;
          break;
        case START:
          hasStart = true;
          break;
        case END:
          hasEnd = true;
          break;
        case DESCRIPTION:
          hasDescription = true;
          break;
        case LOCATION:
          hasLocation = true;
          break;
        case STATUS:
          hasStatus = true;
          break;
        default:
          throw new IllegalStateException("Unexpected property: " + prop);

      }

    }

    assertTrue(hasSubject);
    assertTrue(hasStart);
    assertTrue(hasEnd);
    assertTrue(hasDescription);
    assertTrue(hasLocation);
    assertTrue(hasStatus);
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
  public void testMultipleInvalidProperties() {
    String[] invalidProperties = {"xyz", "time", "date", "name", "title", "invalid123", "random"};

    for (String invalid : invalidProperties) {
      try {
        Property.fromString(invalid);
        fail("Expected IllegalArgumentException for: " + invalid);
      } catch (IllegalArgumentException e) {
        assertEquals("Invalid property", e.getMessage());
      }
    }
  }

  @Test
  public void testPropertyEnumConstruction() {
    // tests that each property has correct name and type
    for (Property prop : Property.values()) {
      assertEquals(String.class, prop.getType());
      assertTrue(!prop.getName().isEmpty());
      assertEquals(prop.getName(), prop.toString());
    }
  }
}