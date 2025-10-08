import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import controller.GUIController;
import model.Calendars;
import model.MultipleCalendars;
import model.Event;
import view.ViewForConsole;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Comprehensive tests for the GUI Controller.
 */
public class TestGUIController {
  private GUIController controller;
  private Calendars calendars;

  @Before
  public void setup() {
    calendars = new MultipleCalendars(new ViewForConsole());
    controller = new GUIController(calendars);
  }

  // Constructor tests
  @Test
  public void testConstructorNullCalendars() {
    try {
      new GUIController(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Calendars cannot be null", e.getMessage());
    }
  }

  @Test
  public void testConstructorValid() {
    GUIController validController = new GUIController(calendars);
    assertNotNull(validController);
  }

  // Create calendar tests
  @Test
  public void testCreateCalendar() {
    String result = controller.createCalendar("Work", TimeZone.getTimeZone("America/New_York"));
    assertEquals("Calendar 'Work' created successfully", result);
  }

  @Test
  public void testCreateCalendarDuplicate() {
    controller.createCalendar("Work", TimeZone.getTimeZone("America/New_York"));
    String duplicateResult = controller.createCalendar("Work", TimeZone.getTimeZone("America/Los_Angeles"));
    assertTrue(duplicateResult.startsWith("Error:"));
    assertTrue(duplicateResult.contains("already exists"));
  }

  @Test
  public void testCreateCalendarNullName() {
    String result = controller.createCalendar(null, TimeZone.getTimeZone("America/New_York"));
    assertEquals("Error: Calendar name cannot be empty", result);
  }

  @Test
  public void testCreateCalendarEmptyName() {
    String result = controller.createCalendar("", TimeZone.getTimeZone("America/New_York"));
    assertEquals("Error: Calendar name cannot be empty", result);
  }

  @Test
  public void testCreateCalendarWhitespaceName() {
    String result = controller.createCalendar("   ", TimeZone.getTimeZone("America/New_York"));
    assertEquals("Error: Calendar name cannot be empty", result);
  }

  @Test
  public void testCreateCalendarInvalidTimezone() {
    String result = controller.createCalendar("Test", TimeZone.getTimeZone("Invalid/Timezone"));
    assertTrue(result.startsWith("Error:"));
  }

  // Use calendar tests
  @Test
  public void testUseCalendar() {
    controller.createCalendar("Personal", TimeZone.getTimeZone("America/New_York"));
    String result = controller.useCalendar("Personal");
    assertEquals("Now using calendar: Personal", result);
  }

  @Test
  public void testUseCalendarNonExistent() {
    String errorResult = controller.useCalendar("NonExistent");
    assertTrue(errorResult.startsWith("Error:"));
    assertTrue(errorResult.contains("Calendar not found"));
  }

  @Test
  public void testUseCalendarNullName() {
    String result = controller.useCalendar(null);
    assertEquals("Error: Calendar name cannot be empty", result);
  }

  @Test
  public void testUseCalendarEmptyName() {
    String result = controller.useCalendar("");
    assertEquals("Error: Calendar name cannot be empty", result);
  }

  @Test
  public void testUseCalendarWhitespaceName() {
    String result = controller.useCalendar("   ");
    assertEquals("Error: Calendar name cannot be empty", result);
  }

  // Create event tests
  @Test
  public void testCreateEvent() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();

    String result = controller.createEvent(event);
    assertEquals("Event created successfully", result);
  }

  @Test
  public void testCreateEventNull() {
    String result = controller.createEvent(null);
    assertEquals("Error: Event cannot be null", result);
  }

  @Test
  public void testCreateEventWithoutCalendar() {
    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();

    String result = controller.createEvent(event);
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("No calendar") && result.contains("selected"));
  }

  @Test
  public void testCreateEventWithAllFields() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .description("Important meeting")
            .location("Conference Room")
            .status("public")
            .build();

    String result = controller.createEvent(event);
    assertEquals("Event created successfully", result);
  }

  // Create event series tests
  @Test
  public void testCreateEventSeriesNTimes() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Daily Standup", LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T09:30"))
            .build();

    String result = controller.createEventSeriesNTimes(event, "MWF", 5);
    assertEquals("Event series created successfully", result);
  }

  @Test
  public void testCreateEventSeriesNTimesNullEvent() {
    String result = controller.createEventSeriesNTimes(null, "MWF", 5);
    assertEquals("Error: Event cannot be null", result);
  }

  @Test
  public void testCreateEventSeriesNTimesNullWeekdays() {
    Event event = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T09:00")).build();
    String result = controller.createEventSeriesNTimes(event, null, 5);
    assertEquals("Error: Weekdays cannot be empty", result);
  }

  @Test
  public void testCreateEventSeriesNTimesEmptyWeekdays() {
    Event event = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T09:00")).build();
    String result = controller.createEventSeriesNTimes(event, "", 5);
    assertEquals("Error: Weekdays cannot be empty", result);
  }

  @Test
  public void testCreateEventSeriesNTimesInvalidTimes() {
    Event event = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T09:00")).build();
    String result = controller.createEventSeriesNTimes(event, "MWF", 0);
    assertEquals("Error: Number of times must be positive", result);
  }

  @Test
  public void testCreateEventSeriesNTimesNegativeTimes() {
    Event event = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T09:00")).build();
    String result = controller.createEventSeriesNTimes(event, "MWF", -1);
    assertEquals("Error: Number of times must be positive", result);
  }

  @Test
  public void testCreateEventSeriesUntil() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Weekly Meeting", LocalDateTime.parse("2025-01-01T14:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T15:00"))
            .build();

    String result = controller.createEventSeriesUntil(event, "W", "2025-02-01");
    assertEquals("Event series created successfully", result);
  }

  @Test
  public void testCreateEventSeriesUntilNullEvent() {
    String result = controller.createEventSeriesUntil(null, "W", "2025-02-01");
    assertEquals("Error: Event cannot be null", result);
  }

  @Test
  public void testCreateEventSeriesUntilNullWeekdays() {
    Event event = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T09:00")).build();
    String result = controller.createEventSeriesUntil(event, null, "2025-02-01");
    assertEquals("Error: Weekdays cannot be empty", result);
  }

  @Test
  public void testCreateEventSeriesUntilNullUntil() {
    Event event = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T09:00")).build();
    String result = controller.createEventSeriesUntil(event, "W", null);
    assertEquals("Error: Until date cannot be empty", result);
  }

  @Test
  public void testCreateEventSeriesUntilEmptyUntil() {
    Event event = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T09:00")).build();
    String result = controller.createEventSeriesUntil(event, "W", "");
    assertEquals("Error: Until date cannot be empty", result);
  }

  // Edit event tests
  @Test
  public void testEditEvent() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();
    controller.createEvent(event);

    String result = controller.editEvent("subject", "Meeting", "2025-01-01T10:00",
            "2025-01-01T11:00", "Updated Meeting");
    assertEquals("Event edited successfully", result);
  }

  @Test
  public void testEditEventNullProperty() {
    String result = controller.editEvent(null, "Meeting", "2025-01-01T10:00",
            "2025-01-01T11:00", "Updated Meeting");
    assertEquals("Error: Property cannot be empty", result);
  }

  @Test
  public void testEditEventEmptyProperty() {
    String result = controller.editEvent("", "Meeting", "2025-01-01T10:00",
            "2025-01-01T11:00", "Updated Meeting");
    assertEquals("Error: Property cannot be empty", result);
  }

  @Test
  public void testEditEventNullSubject() {
    String result = controller.editEvent("subject", null, "2025-01-01T10:00",
            "2025-01-01T11:00", "Updated Meeting");
    assertEquals("Error: Subject cannot be empty", result);
  }

  @Test
  public void testEditEventEmptySubject() {
    String result = controller.editEvent("subject", "", "2025-01-01T10:00",
            "2025-01-01T11:00", "Updated Meeting");
    assertEquals("Error: Subject cannot be empty", result);
  }

  @Test
  public void testEditEventNullStartDateTime() {
    String result = controller.editEvent("subject", "Meeting", null,
            "2025-01-01T11:00", "Updated Meeting");
    assertEquals("Error: Start date time cannot be empty", result);
  }

  @Test
  public void testEditEventEmptyStartDateTime() {
    String result = controller.editEvent("subject", "Meeting", "",
            "2025-01-01T11:00", "Updated Meeting");
    assertEquals("Error: Start date time cannot be empty", result);
  }

  @Test
  public void testEditEventNonExistent() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    String result = controller.editEvent("subject", "NonExistent", "2025-01-01T10:00",
            "2025-01-01T11:00", "Updated");
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("Event not found"));
  }

  // Edit events tests
  @Test
  public void testEditEvents() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();
    controller.createEvent(event);

    String result = controller.editEvents("location", "Meeting", "2025-01-01T10:00", "New Location");
    assertEquals("Events edited successfully", result);
  }

  @Test
  public void testEditEventsNullProperty() {
    String result = controller.editEvents(null, "Meeting", "2025-01-01T10:00", "New Location");
    assertEquals("Error: Property cannot be empty", result);
  }

  // Edit event series tests
  @Test
  public void testEditEventSeries() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Daily Standup", LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T09:30"))
            .build();
    controller.createEventSeriesNTimes(event, "MWF", 3);

    String result = controller.editEventSeries("location", "Daily Standup", "2025-01-01T09:00", "New Room");
    assertEquals("Event series edited successfully", result);
  }

  @Test
  public void testEditEventSeriesNullProperty() {
    String result = controller.editEventSeries(null, "Meeting", "2025-01-01T10:00", "New Value");
    assertEquals("Error: Property cannot be empty", result);
  }

  // Get schedule tests
  @Test
  public void testGetScheduleEmpty() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    String result = controller.getSchedule("2025-01-01");
    assertFalse(result.startsWith("Error:"));
  }

  @Test
  public void testGetScheduleWithEvents() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();
    controller.createEvent(event);

    String result = controller.getSchedule("2025-01-01");
    assertTrue(result.contains("Meeting"));
  }

  @Test
  public void testGetScheduleNullDate() {
    String result = controller.getSchedule(null);
    assertEquals("Error: Date cannot be empty", result);
  }

  @Test
  public void testGetScheduleEmptyDate() {
    String result = controller.getSchedule("");
    assertEquals("Error: Date cannot be empty", result);
  }

  @Test
  public void testGetScheduleWithoutCalendar() {
    String result = controller.getSchedule("2025-01-01");
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("No calendar") && result.contains("selected"));
  }

  // Get schedule range tests
  @Test
  public void testGetScheduleRange() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    String result = controller.getScheduleRange("2025-01-01T00:00", "2025-01-02T00:00");
    assertFalse(result.startsWith("Error:"));
  }

  @Test
  public void testGetScheduleRangeNullStartDate() {
    String result = controller.getScheduleRange(null, "2025-01-02T00:00");
    assertEquals("Error: Start date cannot be empty", result);
  }

  @Test
  public void testGetScheduleRangeNullEndDate() {
    String result = controller.getScheduleRange("2025-01-01T00:00", null);
    assertEquals("Error: End date cannot be empty", result);
  }

  // Check availability tests
  @Test
  public void testCheckAvailabilityFree() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    String result = controller.checkAvailability("2025-01-01T10:00");
    assertEquals("Available", result);
  }

  @Test
  public void testCheckAvailabilityBusy() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();
    controller.createEvent(event);

    String result = controller.checkAvailability("2025-01-01T10:30");
    assertEquals("Busy", result);
  }

  @Test
  public void testCheckAvailabilityNullDateTime() {
    String result = controller.checkAvailability(null);
    assertEquals("Error: Date time cannot be empty", result);
  }

  @Test
  public void testCheckAvailabilityEmptyDateTime() {
    String result = controller.checkAvailability("");
    assertEquals("Error: Date time cannot be empty", result);
  }

  // Get calendars tests
  @Test
  public void testGetCalendarsEmpty() {
    String result = controller.getCalendars();
    assertEquals("No calendars", result);
  }

  @Test
  public void testGetCalendarsWithCalendars() {
    controller.createCalendar("Work", TimeZone.getTimeZone("America/New_York"));
    controller.createCalendar("Personal", TimeZone.getTimeZone("America/Los_Angeles"));

    String result = controller.getCalendars();
    assertTrue(result.contains("Work"));
    assertTrue(result.contains("Personal"));
  }

  // Edit calendar tests
  @Test
  public void testEditCalendar() {
    controller.createCalendar("Work", TimeZone.getTimeZone("America/New_York"));
    String result = controller.editCalendar("Work", "name", "WorkCalendar");
    assertEquals("Calendar edited successfully", result);
  }

  @Test
  public void testEditCalendarNullName() {
    String result = controller.editCalendar(null, "name", "NewName");
    assertEquals("Error: Calendar name cannot be empty", result);
  }

  @Test
  public void testEditCalendarNullProperty() {
    String result = controller.editCalendar("Work", null, "NewValue");
    assertEquals("Error: Property cannot be empty", result);
  }

  @Test
  public void testEditCalendarNonExistent() {
    String result = controller.editCalendar("NonExistent", "name", "NewName");
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("Calendar not found"));
  }

  // Copy event tests
  @Test
  public void testCopyEvent() {
    controller.createCalendar("Source", TimeZone.getTimeZone("America/New_York"));
    controller.createCalendar("Target", TimeZone.getTimeZone("America/Los_Angeles"));
    controller.useCalendar("Source");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();
    controller.createEvent(event);

    String result = controller.copyEvent("Meeting", "2025-01-01T10:00", "Target", "2025-01-02T10:00");
    assertEquals("Event copied successfully", result);
  }

  @Test
  public void testCopyEventNullEventName() {
    String result = controller.copyEvent(null, "2025-01-01T10:00", "Target", "2025-01-02T10:00");
    assertEquals("Error: Event name cannot be empty", result);
  }

  @Test
  public void testCopyEventNullOriginalDate() {
    String result = controller.copyEvent("Meeting", null, "Target", "2025-01-02T10:00");
    assertEquals("Error: Original date cannot be empty", result);
  }

  @Test
  public void testCopyEventNullTargetCalendar() {
    String result = controller.copyEvent("Meeting", "2025-01-01T10:00", null, "2025-01-02T10:00");
    assertEquals("Error: Target calendar cannot be empty", result);
  }

  @Test
  public void testCopyEventNullNewDate() {
    String result = controller.copyEvent("Meeting", "2025-01-01T10:00", "Target", null);
    assertEquals("Error: New date cannot be empty", result);
  }

  @Test
  public void testCopyEventEmptyEventName() {
    String result = controller.copyEvent("", "2025-01-01T10:00", "Target", "2025-01-02T10:00");
    assertEquals("Error: Event name cannot be empty", result);
  }

  @Test
  public void testCopyEventEmptyOriginalDate() {
    String result = controller.copyEvent("Meeting", "", "Target", "2025-01-02T10:00");
    assertEquals("Error: Original date cannot be empty", result);
  }

  @Test
  public void testCopyEventEmptyTargetCalendar() {
    String result = controller.copyEvent("Meeting", "2025-01-01T10:00", "", "2025-01-02T10:00");
    assertEquals("Error: Target calendar cannot be empty", result);
  }

  @Test
  public void testCopyEventEmptyNewDate() {
    String result = controller.copyEvent("Meeting", "2025-01-01T10:00", "Target", "");
    assertEquals("Error: New date cannot be empty", result);
  }

  @Test
  public void testCopyEventNonExistentSource() {
    controller.createCalendar("Source", TimeZone.getTimeZone("America/New_York"));
    controller.createCalendar("Target", TimeZone.getTimeZone("America/Los_Angeles"));
    controller.useCalendar("Source");

    String result = controller.copyEvent("NonExistent", "2025-01-01T10:00", "Target", "2025-01-02T10:00");
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("Event not found"));
  }

  @Test
  public void testCopyEventNonExistentTarget() {
    controller.createCalendar("Source", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Source");

    Event event = Event.getBuilder("Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00"))
            .build();
    controller.createEvent(event);

    String result = controller.copyEvent("Meeting", "2025-01-01T10:00", "NonExistent", "2025-01-02T10:00");
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("calendar") && (result.contains("not found") || result.contains("No calendar found")));
  }

  // Integration tests
  @Test
  public void testFullWorkflow() {
    // Create multiple calendars
    String createResult1 = controller.createCalendar("Work", TimeZone.getTimeZone("America/New_York"));
    assertEquals("Calendar 'Work' created successfully", createResult1);

    String createResult2 = controller.createCalendar("Personal", TimeZone.getTimeZone("America/Los_Angeles"));
    assertEquals("Calendar 'Personal' created successfully", createResult2);

    // Use work calendar
    String useResult = controller.useCalendar("Work");
    assertEquals("Now using calendar: Work", useResult);

    // Create an event
    Event workEvent = Event.getBuilder("Team Meeting", LocalDateTime.parse("2025-01-15T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-15T11:00"))
            .location("Conference Room A")
            .description("Weekly team sync")
            .status("public")
            .build();

    String eventResult = controller.createEvent(workEvent);
    assertEquals("Event created successfully", eventResult);

    // Check schedule
    String schedule = controller.getSchedule("2025-01-15");
    assertTrue(schedule.contains("Team Meeting"));
    assertTrue(schedule.contains("Conference Room A"));

    // Edit the event
    String editResult = controller.editEvent("location", "Team Meeting", "2025-01-15T10:00",
            "2025-01-15T11:00", "Conference Room B");
    assertEquals("Event edited successfully", editResult);

    // Verify edit
    String updatedSchedule = controller.getSchedule("2025-01-15");
    assertTrue(updatedSchedule.contains("Conference Room B"));
    assertFalse(updatedSchedule.contains("Conference Room A"));

    // Copy event to personal calendar
    String copyResult = controller.copyEvent("Team Meeting", "2025-01-15T10:00",
            "Personal", "2025-01-16T10:00");
    assertEquals("Event copied successfully", copyResult);

    // Switch to personal calendar and verify
    controller.useCalendar("Personal");
    String personalSchedule = controller.getSchedule("2025-01-16");
    assertTrue(personalSchedule.contains("Team Meeting"));

    // Check availability
    String availability1 = controller.checkAvailability("2025-01-16T10:30");
    assertEquals("Busy", availability1);

    String availability2 = controller.checkAvailability("2025-01-16T14:00");
    assertEquals("Available", availability2);

    // Verify calendar list
    String calendarList = controller.getCalendars();
    assertTrue(calendarList.contains("Work"));
    assertTrue(calendarList.contains("Personal"));
  }

  @Test
  public void testEventSeriesWorkflow() {
    // Setup calendar
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    // Create event series
    Event seriesEvent = Event.getBuilder("Daily Standup", LocalDateTime.parse("2025-01-06T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-06T09:30"))
            .location("Main Conference Room")
            .build();

    String seriesResult = controller.createEventSeriesNTimes(seriesEvent, "MTWRF", 5);
    assertEquals("Event series created successfully", seriesResult);

    // Check schedule for multiple days
    String mondaySchedule = controller.getSchedule("2025-01-06");
    assertTrue(mondaySchedule.contains("Daily Standup"));

    String tuesdaySchedule = controller.getSchedule("2025-01-07");
    assertTrue(tuesdaySchedule.contains("Daily Standup"));

    String saturdaySchedule = controller.getSchedule("2025-01-11");
    assertFalse(saturdaySchedule.contains("Daily Standup")); // Saturday not included

    // Edit entire series
    String editSeriesResult = controller.editEventSeries("location", "Daily Standup",
            "2025-01-06T09:00", "Small Conference Room");
    assertEquals("Event series edited successfully", editSeriesResult);

    // Verify series edit
    String updatedMondaySchedule = controller.getSchedule("2025-01-06");
    assertTrue(updatedMondaySchedule.contains("Small Conference Room"));

    String updatedTuesdaySchedule = controller.getSchedule("2025-01-07");
    assertTrue(updatedTuesdaySchedule.contains("Small Conference Room"));
  }

  @Test
  public void testErrorHandlingWorkflow() {
    // Try operations without calendar
    String noCalendarEvent = controller.createEvent(
            Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T10:00")).build());
    assertTrue(noCalendarEvent.startsWith("Error:"));

    String noCalendarSchedule = controller.getSchedule("2025-01-01");
    assertTrue(noCalendarSchedule.startsWith("Error:"));

    // Create calendar and try invalid operations
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    // Try to edit non-existent event
    String editNonExistent = controller.editEvent("subject", "NonExistent",
            "2025-01-01T10:00", "2025-01-01T11:00", "New Name");
    assertTrue(editNonExistent.startsWith("Error:"));

    // Try invalid event series
    Event invalidSeriesEvent = Event.getBuilder("Test", LocalDateTime.parse("2025-01-01T10:00")).build();
    String invalidSeries = controller.createEventSeriesNTimes(invalidSeriesEvent, "XYZ", 3);
    assertTrue(invalidSeries.startsWith("Error:"));

    // Try copy to non-existent calendar
    Event testEvent = Event.getBuilder("Test Event", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T11:00")).build();
    controller.createEvent(testEvent);

    String invalidCopy = controller.copyEvent("Test Event", "2025-01-01T10:00",
            "NonExistentCalendar", "2025-01-02T10:00");
    assertTrue(invalidCopy.startsWith("Error:"));
    assertTrue(invalidCopy.contains("calendar") && (invalidCopy.contains("not found") || invalidCopy.contains("No calendar found")));
  }

  @Test
  public void testBoundaryConditions() {
    controller.createCalendar("Test", TimeZone.getTimeZone("America/New_York"));
    controller.useCalendar("Test");

    // Test with whitespace-only inputs
    String whitespaceProperty = controller.editEvent("   ", "Meeting", "2025-01-01T10:00",
            "2025-01-01T11:00", "New Value");
    assertEquals("Error: Property cannot be empty", whitespaceProperty);

    String whitespaceSubject = controller.editEvent("subject", "   ", "2025-01-01T10:00",
            "2025-01-01T11:00", "New Value");
    assertEquals("Error: Subject cannot be empty", whitespaceSubject);

    // Test with valid minimal event
    Event minimalEvent = Event.getBuilder("A", LocalDateTime.parse("2025-01-01T00:00")).build();
    String minimalResult = controller.createEvent(minimalEvent);
    assertEquals("Event created successfully", minimalResult);

    // Test event series with single occurrence
    Event singleEvent = Event.getBuilder("Single", LocalDateTime.parse("2025-01-02T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-02T11:00")).build();
    String singleSeriesResult = controller.createEventSeriesNTimes(singleEvent, "M", 1);
    assertEquals("Event series created successfully", singleSeriesResult);
  }
}