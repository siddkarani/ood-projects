import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import model.Calendars;
import model.Event;
import model.MultipleCalendars;
import view.View;
import view.ViewForConsole;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Tests the multipleCalendars class.
 */

public class TestMultipleCalendars {
  Calendars calendars;
  Event flight;
  Event dentist;
  Event doctor;
  Event convention;
  Event classes;
  View view;

  @Before
  public void setup() {
    view = new ViewForConsole();
    calendars = new MultipleCalendars(view);
    flight = Event.getBuilder("Flight", LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T12:00"))
            .build();
    dentist = Event.getBuilder("Dentist Appointment", LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T10:00"))
            .build();
    doctor = Event.getBuilder("Doctors Appointment", LocalDateTime.parse("2025-02-01T12:00"))
            .endDateTime(LocalDateTime.parse("2025-02-01T13:00"))
            .build();
    convention = Event.getBuilder("Convention", LocalDateTime.parse("2025-01-03T11:00"))
            .build();
    classes = Event.getBuilder("Classes", LocalDateTime.parse("2025-02-01T15:00"))
            .endDateTime(LocalDateTime.parse("2025-02-01T16:00"))
            .build();

    calendars.createCalendar("California", TimeZone.getTimeZone("America/Los_Angeles"));
    calendars.createCalendar("New York", TimeZone.getTimeZone("America/New_York"));
    calendars.createCalendar("Tokyo", TimeZone.getTimeZone("Asia/Tokyo"));

  }

  @Test
  public void testCreateCalendar() {
    setup();
    // trying to add a duplicate name calendar
    try {
      calendars.createCalendar("California", TimeZone.getTimeZone("America/Los_Angeles"));
    } catch (IllegalArgumentException e) {
      assertEquals("A calendar with this name already exists", e.getMessage());
    }
    // trying to use a non-existent timezone
    try {
      calendars.createCalendar("Boston", TimeZone.getTimeZone("America/Boston"));
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid timezone", e.getMessage());
    }
    // adding a valid calendar
    calendars.createCalendar("London", TimeZone.getTimeZone("Europe/London"));
    assertEquals("California\n" + "London\n" + "New York\n" + "Tokyo", calendars.getCalendars());
  }

  @Test
  public void testEditCalendar() {
    // editing a name
    assertEquals("California\n" + "New York\n" + "Tokyo", calendars.getCalendars());
    calendars.editCalendar("California", "name", "New California");
    assertEquals("New California\n" + "New York\n" + "Tokyo", calendars.getCalendars());

    // trying editing non existent calendar
    try {
      calendars.editCalendar("California", "name", "No California");
    } catch (IllegalArgumentException e) {
      assertEquals("Calendar not found", e.getMessage());
    }

    // editing to a calendar with the same name already
    try {
      calendars.editCalendar("Tokyo", "name", "New California");
    } catch (IllegalArgumentException e) {
      assertEquals("A calendar with this name already exists", e.getMessage());
    }

    assertEquals("New California\n" + "New York\n" + "Tokyo", calendars.getCalendars());

    // trying editing invalid property
    try {
      calendars.editCalendar("Tokyo", "foo", "New California");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid property", e.getMessage());
    }
    // using invalid timezone when editing
    try {
      calendars.editCalendar("Tokyo", "timezone", "Foo/Fee");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid timezone", e.getMessage());
    }
  }

  @Test
  public void testEditCalendarTimezone() {
    // editing a timezone
    // validating that the events in that calendar change accordingly
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    calendars.editCalendar("California", "timezone", "America/New_York");
    calendars.editCalendar("California", "name", "CaliToNY");
    assertEquals("CaliToNY\n" + "New York\n" + "Tokyo", calendars.getCalendars());
    assertEquals("• Flight (2025-01-01 12:00 - 15:00)\n", calendars.daySchedule("2025-01-01"));

    // editing a calendar that isnt in use
    calendars.editCalendar("Tokyo", "name", "New Tokyo");
    assertEquals("CaliToNY\n" + "New Tokyo\n" + "New York", calendars.getCalendars());
  }


  @Test
  public void testUseCalendar() {
    // trying to use a non-existent calendar
    try {
      calendars.useCalendar("Florida");
    } catch (IllegalArgumentException e) {
      assertEquals("Calendar not found", e.getMessage());
    }
    // use twice in a row

    calendars.useCalendar("California");
    calendars.createEvent(flight);
    assertEquals("• Flight (2025-01-01 09:00 - 12:00)\n", calendars.daySchedule("2025-01-01"));
    calendars.useCalendar("Tokyo");
    calendars.createEvent(doctor);
    assertEquals("", calendars.daySchedule("2025-01-01"));
    assertEquals("California\n" + "New York\n" + "Tokyo", calendars.getCalendars());
  }

  @Test
  public void testCopyEvent() {
    // no calendar selected  
    try {
      calendars.copyEvent("Flight", "2025-01-01T09:00", "Tokyo",
              "2025-01-01T09:00");
    } catch (IllegalStateException e) {
      assertEquals("No calendar currently in use", e.getMessage());
    }

    // no target calendar
    calendars.useCalendar("Tokyo");
    try {
      calendars.copyEvent("Flight", "2025-01-01T09:00", "Florida",
              "2025-01-01T09:00");
    } catch (IllegalArgumentException e) {
      assertEquals("No calendar found with that name", e.getMessage());
    }

    // can't find event
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    try {
      calendars.copyEvent("Meeting", "2025-01-01T09:00", "Tokyo",
              "2025-01-01T09:00");
    } catch (IllegalArgumentException e) {
      assertEquals("Event not found", e.getMessage());
    }

    setup();
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    // valid example
    calendars.copyEvent("Flight", "2025-01-01T09:00", "Tokyo",
            "2025-02-01T09:00");
    calendars.useCalendar("Tokyo");
    assertEquals("• Flight (2025-02-01 09:00 - 12:00)\n", calendars.daySchedule("2025-02-01"));

    calendars.useCalendar("California");
    calendars.copyEvent("Flight", "2025-01-01T09:00", "Tokyo",
            "2025-02-01T10:00");
    calendars.useCalendar("Tokyo");
    assertEquals("• Flight (2025-02-01 09:00 - 12:00)\n" + "• Flight (2025-02-01 10:00 - 13:00)\n",
            calendars.daySchedule("2025-02-01"));

  }

  @Test
  public void testCopyEventsOn() {
    // no calendar selected
    try {
      calendars.copyEventsOn("2025-01-01", "Tokyo", "2025-01-01");
    } catch (IllegalStateException e) {
      assertEquals("No calendar currently in use", e.getMessage());
    }

    // no target calendar
    calendars.useCalendar("Tokyo");
    try {
      calendars.copyEventsOn("2025-01-01", "Florida", "2025-01-01");
    } catch (IllegalArgumentException e) {
      assertEquals("No calendar found with that name", e.getMessage());
    }

    // no events on that day
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    try {
      calendars.copyEventsOn("2024-01-01", "New York", "2025-01-01");
    } catch (IllegalArgumentException e) {
      assertEquals("No events found on this day", e.getMessage());
    }
    //valid examples
    setup();
    calendars.useCalendar("New York");
    calendars.createEvent(flight);
    calendars.copyEventsOn("2025-01-01", "California", "2025-02-01");
    calendars.useCalendar("California");
    assertEquals("• Flight (2025-02-01 06:00 - 09:00)\n",
            calendars.daySchedule("2025-02-01"));

    // because of time conversion, goes into next day
    setup();
    calendars.useCalendar("New York");
    Event meeting = Event.getBuilder("Team Meeting", LocalDateTime.parse("2025-01-01T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T12:00"))
            .build();
    Event interview = Event.getBuilder("Job Interview", LocalDateTime.parse("2025-01-01T11:30"))
            .endDateTime(LocalDateTime.parse("2025-01-01T13:00"))
            .build();
    calendars.createEvent(meeting);
    calendars.createEvent(interview);
    calendars.copyEventsOn("2025-01-01", "Tokyo", "2025-02-01");
    calendars.useCalendar("Tokyo");
    String dayScheduleTokyo = calendars.daySchedule("2025-02-02");
    assertEquals("• Team Meeting (2025-02-02 00:00 - 02:00)\n" +
                    "• Job Interview (2025-02-02 01:30 - 03:00)\n",
            dayScheduleTokyo);
  }

  @Test
  public void testCopyEventsBetweenWithSeries() {
    // verifies that event series are still series
    calendars.useCalendar("New York");
    Event lecture = Event.getBuilder("Daily Lecture", LocalDateTime.parse("2025-01-06T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-06T11:00"))
            .seriesId("lecture123")
            .build();
    calendars.createEventSeriesNTimes(lecture, "MTWR", 4);

    calendars.copyEventsBetween("2025-01-06", "2025-01-09", "California",
            "2025-02-03");
    calendars.useCalendar("California");
    assertEquals("• Daily Lecture (2025-02-03 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-03"));
    assertEquals("• Daily Lecture (2025-02-04 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-04"));
    assertEquals("• Daily Lecture (2025-02-05 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-05"));
    assertEquals("• Daily Lecture (2025-02-06 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-06"));

    calendars.editEventSeries("subject", "Daily Lecture",
            "2025-02-03T07:00", "OOD Lecture");
    assertEquals("• OOD Lecture (2025-02-03 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-03"));
    assertEquals("• OOD Lecture (2025-02-04 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-04"));
    assertEquals("• OOD Lecture (2025-02-05 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-05"));
    assertEquals("• OOD Lecture (2025-02-06 07:00 - 08:00)\n",
            calendars.daySchedule("2025-02-06"));
  }


  @Test
  public void testCopyEventsBetween() {

    // no calendar selected
    try {
      calendars.copyEventsBetween("2025-01-01T00:00", "2025-01-02T00:00",
              "Tokyo", "2025-01-01T00:00");
    } catch (IllegalStateException e) {
      assertEquals("No calendar currently in use", e.getMessage());
    }

    // no target calendar
    calendars.useCalendar("Tokyo");
    try {
      calendars.copyEventsBetween("2025-01-01T00:00", "2025-01-01T00:00",
              "Florida", "2025-01-01T00:00");
    } catch (IllegalArgumentException e) {
      assertEquals("No calendar found with that name", e.getMessage());
    }

    // no events on that day
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    try {
      calendars.copyEventsBetween("2024-01-01", "2024-01-02",
              "New York", "2025-01-01");
    } catch (IllegalArgumentException e) {
      assertEquals("No events found in this range of times", e.getMessage());
    }
    // valid example
    calendars.useCalendar("New York");
    Event meeting = Event.getBuilder("Team Meeting", LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T10:00"))
            .build();
    Event interview = Event.getBuilder("Job Interview", LocalDateTime.parse("2025-01-02T11:30"))
            .endDateTime(LocalDateTime.parse("2025-01-02T13:00"))
            .seriesId("rec123")
            .build();

    calendars.createEvent(meeting);
    calendars.createEvent(interview);
    calendars.copyEventsBetween("2025-01-01", "2025-01-02",
            "Tokyo", "2025-02-01");
    calendars.useCalendar("Tokyo");
    String day1Schedule = calendars.daySchedule("2025-02-01");
    String day2Schedule = calendars.daySchedule("2025-02-02");

    assertEquals("• Team Meeting (2025-02-01 23:00 - 2025-02-02 00:00)\n", day1Schedule);
    assertEquals("• Team Meeting (2025-02-01 23:00 - 2025-02-02 00:00)\n", day2Schedule);
  }

  @Test
  public void testCreateEvent() {
    try {
      calendars.createEvent(flight);
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }
  }

  @Test
  public void testCreateEventSeriesNTimes() {
    try {
      calendars.createEventSeriesNTimes(flight, "MWF", 3);
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }

    calendars.useCalendar("California");
    calendars.createEventSeriesNTimes(flight, "MWF", 3);
    assertEquals("• Flight (2025-01-01 09:00 - 12:00)\n" +
                    "• Flight (2025-01-03 09:00 - 12:00)\n" +
                    "• Flight (2025-01-06 09:00 - 12:00)\n",
            calendars.rangeSchedule("2025-01-01T08:00", "2025-01-07T13:00"));
  }

  @Test
  public void testCreateEventSeriesUntil() {
    try {
      calendars.createEventSeriesUntil(flight, "MWF", "2025-01-10T23:59");
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }

    calendars.useCalendar("California");
    calendars.createEventSeriesUntil(flight, "MWF", "2025-01-10");
    assertEquals("• Flight (2025-01-01 09:00 - 12:00)\n" +
                    "• Flight (2025-01-03 09:00 - 12:00)\n" +
                    "• Flight (2025-01-06 09:00 - 12:00)\n" +
                    "• Flight (2025-01-08 09:00 - 12:00)\n",
            calendars.rangeSchedule("2025-01-01T00:00", "2025-01-11T00:00"));
  }

  @Test
  public void testEditEvent() {
    try {
      calendars.editEvent("subject", "Flight", "2025-01-01T09:00",
              "2025-01-01T10:00", "New Flight");
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }
  }

  @Test
  public void testEditEvents() {
    try {
      calendars.editEvents("subject", "Flight", "2025-01-01T09:00",
              "New Flight");
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }

    calendars.useCalendar("California");
    calendars.createEvent(flight);
    calendars.editEvents("subject", "Flight", "2025-01-01T09:00",
            "Updated Flight");
    assertEquals("• Updated Flight (2025-01-01 09:00 - 12:00)\n",
            calendars.daySchedule("2025-01-01"));

    calendars.editEvents("start", "Updated Flight", "2025-01-01T09:00",
            "2025-01-01T10:00");
    assertEquals("• Updated Flight (2025-01-01 10:00 - 12:00)\n",
            calendars.daySchedule("2025-01-01"));
  }

  @Test
  public void testEditEventSeries() {
    try {
      calendars.editEventSeries("subject", "Flight", "2025-01-01T09:00",
              "New Flight");
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }
    calendars.useCalendar("California");
    Event lecture = Event.getBuilder("Daily Lecture", LocalDateTime.parse("2025-01-06T10:00"))
            .endDateTime(LocalDateTime.parse("2025-01-06T11:00"))
            .seriesId("lecture123")
            .build();
    calendars.createEventSeriesNTimes(lecture, "MWF", 3);
    calendars.editEventSeries("subject", "Daily Lecture", "2025-01-06T10:00",
            "Updated Lecture");
    assertEquals("• Updated Lecture (2025-01-06 10:00 - 11:00)\n" +
                    "• Updated Lecture (2025-01-08 10:00 - 11:00)\n" +
                    "• Updated Lecture (2025-01-10 10:00 - 11:00)\n",
            calendars.rangeSchedule("2025-01-06T00:00", "2025-01-11T00:00"));
  }

  @Test
  public void testDaySchedule() {
    try {
      calendars.daySchedule("2025-01-01");
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    calendars.createEvent(dentist);
    assertEquals("• Flight (2025-01-01 09:00 - 12:00)\n" +
                    "• Dentist Appointment (2025-01-01 09:00 - 10:00)\n",
            calendars.daySchedule("2025-01-01"));
    assertEquals("", calendars.daySchedule("2025-01-02"));
  }

  @Test
  public void testRangeSchedule() {
    try {
      calendars.rangeSchedule("2025-01-01T00:00", "2025-01-04T00:00");
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    calendars.createEvent(convention);
    calendars.createEvent(doctor);
    assertEquals("• Flight (2025-01-01 09:00 - 12:00)\n" +
                    "• Convention (2025-01-03 08:00 - 17:00)\n",
            calendars.rangeSchedule("2025-01-01T00:00", "2025-01-04T00:00"));
    assertEquals("• Doctors Appointment (2025-02-01 12:00 - 13:00)\n",
            calendars.rangeSchedule("2025-02-01T00:00", "2025-02-02T00:00"));
    assertEquals("", calendars.rangeSchedule("2024-01-01T00:00", "2024-01-02T00:00"));
  }

  @Test
  public void testIsFree() {
    try {
      calendars.isFree("2025-01-01");
    } catch (IllegalStateException e) {
      assertEquals("No calendar selected", e.getMessage());
    }
    calendars.useCalendar("California");
    calendars.createEvent(flight);
    calendars.createEvent(dentist);
    calendars.createEvent(classes);
    assertEquals("Busy", calendars.isFree("2025-01-01T09:00"));
    assertEquals("Available", calendars.isFree("2023-01-01T09:00"));
  }
}

