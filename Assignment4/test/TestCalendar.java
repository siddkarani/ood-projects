import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.TimeZone;

import model.Calendar;
import model.Event;
import view.View;
import view.ViewForConsole;
import model.TimezoneCalendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for CalendarAppModel.
 */
public class TestCalendar {
  Event dentist;
  Event doctor;
  Event convention;
  Event classes;
  Event discount;
  Event birthday;
  Event meeting;
  Event meeting2;
  Event dentist2;
  Event holiday;
  Calendar calendar;
  View view;

  @Before
  public void setup() {
    view = new ViewForConsole();

    dentist = Event.getBuilder("Dentist Appointment", LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T10:00"))
            .build();
    doctor = Event.getBuilder("Doctors Appointment", LocalDateTime.parse("2025-01-01T12:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T13:00"))
            .build();
    convention = Event.getBuilder("Convention", LocalDateTime.parse("2025-01-03T11:00"))
            .build();
    classes = Event.getBuilder("Classes", LocalDateTime.parse("2025-02-01T15:00"))
            .endDateTime(LocalDateTime.parse("2025-02-01T16:00"))
            .build();
    discount = Event.getBuilder("Discount", LocalDateTime.parse("2020-10-09T13:00"))
            .endDateTime(LocalDateTime.parse("2020-10-09T19:00"))
            .build();
    birthday = Event.getBuilder("Birthday", LocalDateTime.parse("2020-10-09T13:00"))
            .endDateTime(LocalDateTime.parse("2020-10-09T19:00"))
            .build();
    meeting = Event.getBuilder("Meeting", LocalDateTime.parse("2020-10-09T14:00"))
            .endDateTime(LocalDateTime.parse("2020-10-09T20:00"))
            .build();

    meeting2 = Event.getBuilder("Meeting", LocalDateTime.parse("2020-10-09T14:00"))
            .endDateTime(LocalDateTime.parse("2020-10-09T21:00"))
            .build();

    dentist2 = Event.getBuilder("Dentist Appointment", LocalDateTime.parse("2025-01-01T09:00"))
            .endDateTime(LocalDateTime.parse("2025-01-01T10:00"))
            .build();

    holiday = Event.getBuilder("Holiday", LocalDateTime.parse("2025-12-20T09:00"))
            .endDateTime(LocalDateTime.parse("2025-12-31T09:00"))
            .build();

    calendar = new TimezoneCalendar(view, TimeZone.getTimeZone("America/New_York"));

    calendar.createEvent(this.dentist);
    calendar.createEvent(this.doctor);
    calendar.createEvent(this.convention);
    calendar.createEvent(this.classes);
    calendar.createEvent(this.discount);
    calendar.createEvent(this.birthday);
    calendar.createEvent(this.meeting);
    calendar.createEvent(this.meeting2);
  }

  @Test
  public void testCreateEvent() {
    setup();
    // create two events that are the same (error)
    try {
      calendar.createEvent(this.dentist2);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("An event with the same name and time already exists"));
    }
    // create an event with no end time
    assertEquals("2025-01-03T17:00", convention.getEndDateTime().toString());
    assertEquals("2025-01-03T08:00", convention.getStartDateTime().toString());
    // create an event longer than a day
    calendar.createEvent(this.holiday);
    assertEquals("• Holiday (2025-12-20 09:00 - 2025-12-31 09:00)\n",
            calendar.rangeSchedule("2025-12-20T00:00", "2026-01-01T00:00"));
    // create an event with the same subject as another event
    assertNotEquals(meeting, meeting2);
    assertEquals(meeting.getSubject(), meeting.getSubject());
  }

  @Test
  public void testCreateEventSeriesNTimes() {
    setup();
    // create series with same information (error)
    try {
      // creating discount twice on friday
      calendar.createEventSeriesNTimes(discount, "MWF", 2);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("This would create a duplication"));
    }

    // trying to make the same series twice
    calendar.createEventSeriesNTimes(classes, "MWF", 3);
    try {
      calendar.createEventSeriesNTimes(classes, "MWF", 3);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("This would create a duplication"));
    }

    //create recurring event that is more than a day long (error)
    try {
      calendar.createEventSeriesNTimes(holiday, "M", 2);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Recurring events cannot be longer than one day"));
    }

    // use an invalid number of days (error)
    try {
      calendar.createEventSeriesNTimes(classes, "MWF", -1);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Either end date or number of occurrences must be specified"));
    }

    // use an invalid weekday combination (error)
    try {
      calendar.createEventSeriesNTimes(classes, "MXT", 2);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Invalid weekday"));
    }
    setup();
    calendar.createEventSeriesNTimes(classes, "MW", 3);

    // make sure repeats 3 times on the right day

    assertEquals("• Classes (2025-02-01 15:00 - 16:00)\n" +
                    "• Classes (2025-02-03 15:00 - 16:00)\n" +
                    "• Classes (2025-02-05 15:00 - 16:00)\n" +
                    "• Classes (2025-02-10 15:00 - 16:00)\n",
            calendar.rangeSchedule("2025-02-01T00:00", "2025-02-15T00:00"));


  }

  @Test
  public void testCreateEventSeriesUntil() {
    setup();
    // create series with same information (error)
    try {
      calendar.createEventSeriesUntil(discount, "MWF", "2020-12-31");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("This would create a duplication"));
    }
    // creating a series with an event longer than a day (error)
    try {
      calendar.createEventSeriesUntil(holiday, "M", "2026-01-01");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Recurring events cannot be longer than one day"));
    }
    // putting the end date the same as start date (error)
    try {
      calendar.createEventSeriesUntil(classes, "MWF", "2024-02-01");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("End date must be after start date"));
    }

    // using an invalid weekday (error)
    try {
      calendar.createEventSeriesUntil(classes, "XYZ", "2025-03-01");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Invalid weekday"));
    }

    // creating a series that is same as another event (dentist)
    try {
      calendar.createEventSeriesUntil(dentist2, "W", "2025-02-01");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("This would create a duplication"));
    }

    // all valid
    calendar.createEventSeriesUntil(classes, "MW", "2025-03-01");
    // ensures that things that partially overlap with range are also included
    assertEquals("• Classes (2025-02-01 15:00 - 16:00)\n" +
                    "• Classes (2025-02-03 15:00 - 16:00)\n" +
                    "• Classes (2025-02-05 15:00 - 16:00)\n" +
                    "• Classes (2025-02-10 15:00 - 16:00)\n" +
                    "• Classes (2025-02-12 15:00 - 16:00)\n" +
                    "• Classes (2025-02-17 15:00 - 16:00)\n" +
                    "• Classes (2025-02-19 15:00 - 16:00)\n" +
                    "• Classes (2025-02-24 15:00 - 16:00)\n" +
                    "• Classes (2025-02-26 15:00 - 16:00)\n",
            calendar.rangeSchedule("2025-02-01T00:00", "2025-03-01T00:00"));

    // wont make it because the end date is before monday
    try {
      calendar.createEventSeriesUntil(classes, "M", "2025-02-01");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("End date must be after start date"));
    }


  }

  @Test
  public void testEditEvent() {
    setup();
    // edit an event with no end time
    calendar.editEvent("end", "Convention",
            "2025-01-03T08:00",
            "2025-01-03T17:00", "2025-01-03T18:00");
    assertEquals("• Convention (2025-01-03 08:00 - 18:00)\n", calendar.daySchedule("2025-01-03"));

    // edit an event longer than a day
    calendar.createEvent(this.holiday);
    calendar.editEvent("end", "Holiday",
            "2025-12-20T09:00", "2025-12-31T09:00", "2025-12-25T09:00");

    // edit an event with the same subject as another event
    // edit an event with the same subject as another event
    calendar.editEvent("subject", "Meeting", "2020-10-09T14:00",
            "2020-10-09T20:00", "Team Meeting");
    assertEquals("• Discount (2020-10-09 13:00 - 19:00)\n" +
                    "• Birthday (2020-10-09 13:00 - 19:00)\n" +
                    "• Meeting (2020-10-09 14:00 - 21:00)\n" +
                    "• Team Meeting (2020-10-09 14:00 - 20:00)\n",
            calendar.daySchedule("2020-10-09"));
    // edit an event that is part of a series
    calendar.createEventSeriesNTimes(classes, "MWF", 3);
    calendar.editEvent("location", "Classes", "2025-02-01T15:00", "2025-02-01T16:00", "Room 100");
    assertEquals("• Classes (2025-02-01 15:00 - 16:00) @ Room 100\n" +
                    "• Classes (2025-02-03 15:00 - 16:00)\n" +
                    "• Classes (2025-02-05 15:00 - 16:00)\n" +
                    "• Classes (2025-02-07 15:00 - 16:00)\n",
            calendar.rangeSchedule("2025-02-01T00:00", "2025-02-08T00:00"));

    // edit event that would make start time after end time (error)
    try {
      calendar.editEvent("start", "Dentist Appointment", "2025-01-01T09:00",
              "2025-01-01T10:00", "2025-01-01T10:30");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("New end time would be after original end time"));
    }

    // edit an event to an invalid status
    try {
      calendar.editEvent("status", "Doctors Appointment", "2025-01-01T12:00",
              "2025-01-01T13:00", "invalid");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Invalid status"));
    }

    // try to find an event that doesn't exist - FIXED ERROR MESSAGE
    try {
      calendar.editEvent("subject", "Non-existent", "2025-01-01T09:00",
              "2025-01-01T10:00", "New Subject");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Event not found"));
    }
  }

  @Test
  public void testEditEvents() {
    setup();
    calendar.createEventSeriesNTimes(classes, "MWF", 3);

    // edit location for all classes
    calendar.editEvents("location", "Classes", "2025-02-01T15:00",
            "Room 101");
    // the first event wasnt part of the series
    assertEquals("• Classes (2025-02-01 15:00 - 16:00) @ Room 101\n" +
                    "• Classes (2025-02-03 15:00 - 16:00)\n" +
                    "• Classes (2025-02-05 15:00 - 16:00)\n" +
                    "• Classes (2025-02-07 15:00 - 16:00)\n",
            calendar.rangeSchedule("2025-02-01T00:00", "2025-02-08T00:00"));

    calendar.editEvents("location", "Classes", "2025-02-03T15:00",
            "Room 101");
    assertEquals("• Classes (2025-02-03 15:00 - 16:00) @ Room 101\n" +
                    "• Classes (2025-02-05 15:00 - 16:00) @ Room 101\n" +
                    "• Classes (2025-02-07 15:00 - 16:00) @ Room 101\n",
            calendar.rangeSchedule("2025-02-03T00:00", "2025-02-08T00:00"));

    try {
      calendar.editEvents("invalid", "Classes", "2025-02-01T15:00",
              "value");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Invalid property"));
    }

    // test editing something that doesnt exist
    try {
      calendar.editEvents("subject", "Non-existent", "2025-01-01T09:00",
              "New Subject");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("No matching events found"));
    }
    // editing a single event
    calendar.editEvents("location", "Dentist Appointment", "2025-01-01T09:00",
            "123 Leonard St");
    assertEquals("• Dentist Appointment (2025-01-01 09:00 - 10:00) @ 123 Leonard St\n" +
                    "• Doctors Appointment (2025-01-01 12:00 - 13:00)\n",
            calendar.daySchedule("2025-01-01"));
  }

  @Test
  public void testEditEventSeries() {
    setup();
    calendar.createEventSeriesNTimes(classes, "MWF", 3);
    String initialSchedule = calendar.rangeSchedule("2025-02-03T00:00", "2025-02-08T00:00");
    assertEquals("• Classes (2025-02-03 15:00 - 16:00)\n" +
                    "• Classes (2025-02-05 15:00 - 16:00)\n" +
                    "• Classes (2025-02-07 15:00 - 16:00)\n",
            initialSchedule);
    calendar.editEventSeries("location", "Classes", "2025-02-05T15:00", "Room 101");
    String updatedSchedule = calendar.rangeSchedule("2025-02-03T00:00", "2025-02-08T00:00");
    assertEquals("• Classes (2025-02-03 15:00 - 16:00) @ Room 101\n" +
                    "• Classes (2025-02-05 15:00 - 16:00) @ Room 101\n" +
                    "• Classes (2025-02-07 15:00 - 16:00) @ Room 101\n",
            updatedSchedule);
    try {
      calendar.editEventSeries("subject", "Non-existent", "2025-01-01T09:00",
              "New Subject");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is("Event not found"));
    }

    calendar.editEventSeries("location", "Dentist Appointment", "2025-01-01T09:00",
            "New Location");
  }

  @Test
  public void testDaySchedule() {
    setup();
    // empty day
    assertEquals("", calendar.daySchedule("2024-01-01"));

    // multiple events
    assertEquals("• Dentist Appointment (2025-01-01 09:00 - 10:00)\n" +
                    "• Doctors Appointment (2025-01-01 12:00 - 13:00)\n",
            calendar.daySchedule("2025-01-01"));

    // event that has location
    calendar.editEvent("location", "Dentist Appointment",
            "2025-01-01T09:00",
            "2025-01-01T10:00", "Dental Office");
    assertEquals("• Dentist Appointment (2025-01-01 09:00 - 10:00) @ Dental Office\n" +
                    "• Doctors Appointment (2025-01-01 12:00 - 13:00)\n",
            calendar.daySchedule("2025-01-01"));

    // day with multi-day event
    calendar.createEvent(holiday);
    assertEquals("• Holiday (2025-12-20 09:00 - 2025-12-31 09:00)\n",
            calendar.daySchedule("2025-12-25"));
  }

  @Test
  public void testRangeSchedule() {
    setup();
    // empty range
    assertEquals("", calendar.rangeSchedule("2024-01-01T00:00", "2024-12-31T23:59"));

    // single event
    assertEquals("• Classes (2025-02-01 15:00 - 16:00)\n",
            calendar.rangeSchedule("2025-02-01T00:00", "2025-02-01T23:59"));

    // multiple events on same day
    assertEquals("• Discount (2020-10-09 13:00 - 19:00)\n" +
                    "• Birthday (2020-10-09 13:00 - 19:00)\n" +
                    "• Meeting (2020-10-09 14:00 - 20:00)\n" +
                    "• Meeting (2020-10-09 14:00 - 21:00)\n",
            calendar.rangeSchedule("2020-10-09T00:00", "2020-10-09T23:59"));

    // multiday event
    calendar.createEvent(holiday);
    assertEquals("• Holiday (2025-12-20 09:00 - 2025-12-31 09:00)\n",
            calendar.rangeSchedule("2025-12-20T00:00", "2025-12-31T23:59"));

    // partial overlap with event
    assertEquals("• Dentist Appointment (2025-01-01 09:00 - 10:00)\n" +
                    "• Doctors Appointment (2025-01-01 12:00 - 13:00)\n" +
                    "• Convention (2025-01-03 08:00 - 17:00)\n",
            calendar.rangeSchedule("2025-01-01T00:00", "2025-01-05T23:59"));
  }

  @Test
  public void testIsFree() {
    setup();
    assertEquals("Available", calendar.isFree("2000-01-01T09:00"));
    assertEquals("Available", calendar.isFree("2000-01-01T14:00"));

    assertEquals("Busy", calendar.isFree("2025-02-01T15:30"));
    assertEquals("Busy", calendar.isFree("2025-01-01T09:30"));

    try {
      calendar.isFree("xxx");
    } catch (java.time.format.DateTimeParseException e) {
      assertThat(e.getMessage(), is("Text 'xxx' could not be parsed at index 0"));
    }
  }
}