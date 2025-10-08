import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import model.Event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test class for Event class.
 */
public class TestEvent {
  Event dentist;
  Event doctor;
  Event convention;

  @Before
  public void setup() {
    dentist = Event.getBuilder("Dentist Appointment",
                    LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T10:00"))
            .build();
    doctor = Event.getBuilder("Doctors Appointment",
                    LocalDateTime.parse("2025-02-24T12:00"))
            .endDateTime(LocalDateTime.parse("2025-02-24T13:00"))
            .build();
    convention = Event.getBuilder("Convention",
                    LocalDateTime.parse("2025-01-03T11:00"))
            .build();
  }

  @Test
  public void testBuilderFields() {
    // correct subject, start time, end time
    assertEquals("Dentist Appointment", dentist.getSubject());
    assertEquals(dentist.getStartDateTime(),
            LocalDateTime.parse("2025-01-01T09:00"));
    assertEquals(dentist.getEndDateTime(),
            LocalDateTime.parse("2025-01-01T10:00"));
    assertEquals("", dentist.getLocation());
    assertEquals("", dentist.getDescription());
    assertEquals("", dentist.getStatus());
    assertNull(dentist.getSeriesId());

    // make sure no end time means all day event
    assertEquals(LocalDateTime.parse("2025-01-03T08:00"),
            convention.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-01-03T17:00"),
            convention.getEndDateTime());
  }

  @Test
  public void testUsingBuilder() {
    setup();
    assertEquals("Doctors Appointment", doctor.getSubject());
    doctor = Event.getBuilder("Doctors Appointment",
                    LocalDateTime.parse("2025-02-24T12:00"))
            .endDateTime(LocalDateTime.parse("2025-02-24T13:00"))
            .description("Yearly checkup")
            .status("private")
            .location("123 Huntington Ave")
            .build();
    assertEquals("Yearly checkup", doctor.getDescription());
    assertEquals("private", doctor.getStatus());
    assertEquals("123 Huntington Ave", doctor.getLocation());
  }

  @Test
  public void testBuilderWithNullFields() {
    Event event = Event.getBuilder("Test Event",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .description(null)
            .location(null)
            .status(null)
            .build();

    assertEquals("", event.getDescription());
    assertEquals("", event.getLocation());
    assertEquals("", event.getStatus());
    assertEquals("Test Event", event.getSubject());
    assertNull(event.getSeriesId());
  }

  @Test
  public void testBuilderNullSubject() {
    try {
      Event.getBuilder(null, LocalDateTime.parse("2025-01-01T10:00"));
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Subject cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testBuilderEmptySubject() {
    try {
      Event.getBuilder("", LocalDateTime.parse("2025-01-01T10:00"));
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Subject cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testBuilderWhitespaceOnlySubject() {
    try {
      Event.getBuilder("   ", LocalDateTime.parse("2025-01-01T10:00"));
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Subject cannot be null or empty", e.getMessage());
    }
  }

  @Test
  public void testBuilderNullStartDateTime() {
    try {
      Event.getBuilder("Test Event", null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Start date time cannot be null", e.getMessage());
    }
  }

  @Test
  public void testInvalidEndTimeBeforeStartTime() {
    try {
      Event.getBuilder("Test Event",
                      LocalDateTime.parse("2025-01-01T10:00"))
              .endDateTime(LocalDateTime.parse("2025-01-01T09:00"))
              .build();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Start time cannot be after end time", e.getMessage());
    }
  }

  @Test
  public void testSameStartAndEndTime() {
    LocalDateTime sameTime = LocalDateTime.parse("2025-01-01T10:00");
    Event event = Event.getBuilder("Test Event", sameTime)
            .endDateTime(sameTime)
            .build();

    assertEquals(sameTime, event.getStartDateTime());
    assertEquals(sameTime, event.getEndDateTime());
  }

  @Test
  public void testCopyEventToNewDate() {
    LocalDateTime newStart = LocalDateTime.parse("2025-02-01T14:00");
    LocalDateTime newEnd = LocalDateTime.parse("2025-02-01T15:00");

    Event copiedEvent = dentist.copyEventToNewDate(newStart, newEnd);

    assertEquals("Dentist Appointment", copiedEvent.getSubject());
    assertEquals(newStart, copiedEvent.getStartDateTime());
    assertEquals(newEnd, copiedEvent.getEndDateTime());
    assertEquals(dentist.getDescription(), copiedEvent.getDescription());
    assertEquals(dentist.getLocation(), copiedEvent.getLocation());
    assertEquals(dentist.getStatus(), copiedEvent.getStatus());
  }

  @Test
  public void testAllDayEventCreation() {
    Event allDayEvent = Event.getBuilder("All Day Meeting",
                    LocalDateTime.parse("2025-01-01T00:00"))
            .build();

    assertEquals(LocalDateTime.parse("2025-01-01T08:00"),
            allDayEvent.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-01-01T17:00"),
            allDayEvent.getEndDateTime());
  }

  @Test
  public void testBuilderMethodChaining() {
    Event event = Event.getBuilder("Chained Event",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .description("Test description")
            .location("Test location")
            .status("public")
            .build();

    assertEquals("Chained Event", event.getSubject());
    assertEquals("Test description", event.getDescription());
    assertEquals("Test location", event.getLocation());
    assertEquals("public", event.getStatus());
  }

  @Test
  public void testToString() {
    String expected = "Event{subject='Dentist Appointment', " +
            "start=2025-01-01T09:00, end=2025-01-01T10:00}";
    assertEquals(expected, dentist.toString());
  }

  @Test
  public void testEquals() {
    Event lecture = Event.getBuilder("OOD Lecture",
                    LocalDateTime.parse("2025-06-03T09:00"))
            .endDateTime(LocalDateTime.parse("2025-06-03T10:00"))
            .description("Daily lecture")
            .status("public")
            .location("NEU")
            .build();

    Event lecture2 = Event.getBuilder("OOD Lecture",
                    LocalDateTime.parse("2025-06-03T09:00"))
            .endDateTime(LocalDateTime.parse("2025-06-03T10:00"))
            .description("Daily lecture")
            .status("public")
            .location("NEU")
            .build();

    Event otherLecture = Event.getBuilder("OOD Lecture",
                    LocalDateTime.parse("2025-06-03T09:00"))
            .endDateTime(LocalDateTime.parse("2025-06-04T10:00"))
            .build();

    assertEquals(lecture, lecture2);
    assertEquals(lecture, lecture);
    assertNotEquals(lecture, otherLecture);
    assertNotEquals(lecture, null);
    assertNotEquals(lecture, "not event");
  }

  @Test
  public void testEqualsDifferentSubject() {
    Event event1 = Event.getBuilder("Event 1",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();

    Event event2 = Event.getBuilder("Event 2",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();

    assertNotEquals(event1, event2);
  }

  @Test
  public void testEqualsDifferentStartTime() {
    Event event1 = Event.getBuilder("Same Event",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();

    Event event2 = Event.getBuilder("Same Event",
                    LocalDateTime.parse("2025-01-01T10:30"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();

    assertNotEquals(event1, event2);
  }

  @Test
  public void testEqualsDifferentEndTime() {
    Event event1 = Event.getBuilder("Same Event",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();

    Event event2 = Event.getBuilder("Same Event",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:30"))
            .build();

    assertNotEquals(event1, event2);
  }

  @Test
  public void testHashCode() {
    Event lecture = Event.getBuilder("OOD Lecture",
                    LocalDateTime.parse("2025-06-03T09:00"))
            .endDateTime(LocalDateTime.parse("2025-06-03T10:00"))
            .description("Daily lecture")
            .status("public")
            .location("NEU")
            .build();

    Event lecture2 = Event.getBuilder("OOD Lecture",
                    LocalDateTime.parse("2025-06-03T09:00"))
            .endDateTime(LocalDateTime.parse("2025-06-03T10:00"))
            .description("Daily lecture")
            .status("public")
            .location("NEU")
            .build();

    Event otherLecture = Event.getBuilder("OOD Lecture",
                    LocalDateTime.parse("2025-06-03T09:00"))
            .endDateTime(LocalDateTime.parse("2025-06-04T10:00"))
            .build();

    assertEquals(lecture.hashCode(), lecture2.hashCode());
    assertEquals(lecture.hashCode(), lecture.hashCode());
    assertNotEquals(lecture.hashCode(), otherLecture.hashCode());
  }

  @Test
  public void testHashCodeConsistency() {
    int hashCode1 = dentist.hashCode();
    int hashCode2 = dentist.hashCode();
    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testGetterMethods() {
    Event event = Event.getBuilder("Test Event",
                    LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .description("Test Description")
            .location("Test Location")
            .status("private")
            .build();

    assertEquals("Test Event", event.getSubject());
    assertEquals(LocalDateTime.parse("2025-01-01T10:00"),
            event.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-01-01T11:00"),
            event.getEndDateTime());
    assertEquals("Test Description", event.getDescription());
    assertEquals("Test Location", event.getLocation());
    assertEquals("private", event.getStatus());
  }
}