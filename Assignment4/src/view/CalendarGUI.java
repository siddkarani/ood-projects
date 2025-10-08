package view;

import controller.GUIController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.awt.event.ActionListener;
import java.util.TimeZone;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Calendars;
import model.MultipleCalendars;
import model.Event;

/**
 * GUI for the Calendar application using Swing.
 */
public class CalendarGUI extends JFrame {
  private static final Color BACKGROUND_COLOR = new Color(200, 220, 250);

  private GUIController controller;
  private JTextArea scheduleArea;
  private JComboBox<String> calendarSelector;
  private JComboBox<String> monthBox;
  private JComboBox<Integer> dayBox;
  private JComboBox<Integer> yearBox;

  public CalendarGUI() {
    // Initialize model and controller
    Calendars calendars = new MultipleCalendars(new SwingView());
    controller = new GUIController(calendars);

    // Create default calendar
    String result = controller.createCalendar("Default", TimeZone.getDefault());
    if (!result.startsWith("Error")) {
      controller.useCalendar("Default");
    }

    initializeGUI();
  }

  /**
   * Initializes the GUI components.
   */
  private void initializeGUI() {
    setTitle("Calendar Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 600);
    setLayout(new BorderLayout());

    // Create main panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(BACKGROUND_COLOR);

    // Add panels
    mainPanel.add(createCalendarControlPanel());
    add(mainPanel, BorderLayout.NORTH);
    createCenterPanel();
    createBottomPanel();

    // Show initial schedule
    updateScheduleView();
  }

  // Helper method for making the date time drop downs
  private Object[][] createDateTimeDropdowns() {
    String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    Integer[] days = new Integer[31];
    for (int i = 0; i < 31; i++) {
      days[i] = i + 1;
    }
    Integer[] years = new Integer[10];
    int currentYear = LocalDateTime.now().getYear();
    for (int i = 0; i < 10; i++) {
      years[i] = currentYear + i;
    }
    String[] hours = new String[24];
    for (int i = 0; i < 24; i++) {
      hours[i] = String.format("%02d", i);
    }
    String[] minutes = new String[60];
    for (int i = 0; i < 60; i++) {
      minutes[i] = String.format("%02d", i);
    }
    return new Object[][]{months, days, years, hours, minutes};
  }

  // Helper method to set current date/time in dropdowns
  private void setCurrentDateTime(JComboBox<String> monthBox, JComboBox<Integer> dayBox,
                                  JComboBox<Integer> yearBox, JComboBox<String> hourBox, JComboBox<String> minBox) {
    LocalDateTime now = LocalDateTime.now();
    monthBox.setSelectedIndex(now.getMonthValue() - 1);
    dayBox.setSelectedItem(now.getDayOfMonth());
    yearBox.setSelectedItem(now.getYear());
    hourBox.setSelectedItem(String.format("%02d", now.getHour()));
    minBox.setSelectedItem(String.format("%02d", now.getMinute()));
  }

  // Helper method to create a date and time panel
  private JPanel createDateTimePanel(JComboBox<String> monthBox, JComboBox<Integer> dayBox,
                                     JComboBox<Integer> yearBox, JComboBox<String> hourBox, JComboBox<String> minBox) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setBackground(BACKGROUND_COLOR);
    panel.add(monthBox);
    panel.add(dayBox);
    panel.add(yearBox);
    panel.add(new JLabel(" Time:"));
    panel.add(hourBox);
    panel.add(new JLabel(":"));
    panel.add(minBox);
    return panel;
  }

  // Helper method for timed dialog
  private JDialog showTimedDialog(String title) {
    JDialog dialog = new JDialog(this, title, true);
    dialog.setLayout(new GridLayout(0, 2, 5, 5));
    dialog.getContentPane().setBackground(BACKGROUND_COLOR);
    return dialog;
  }

  private JPanel createCalendarControlPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND_COLOR);

    // Left side panel for calendar controls
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftPanel.setBackground(BACKGROUND_COLOR);
    calendarSelector = new JComboBox<>();
    updateCalendarList();
    JButton addCalendarButton = new JButton("New Calendar");
    addCalendarButton.addActionListener(e -> showAddCalendarDialog());

    leftPanel.add(new JLabel(" Calendar:"));
    leftPanel.add(calendarSelector);
    leftPanel.add(addCalendarButton);

    // Right side panel for event buttons
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightPanel.setBackground(BACKGROUND_COLOR);
    JButton addEventButton = new JButton("Add Event");
    addEventButton.addActionListener(e -> showAddEventDialog());
    JButton editEventButton = new JButton("Edit Event");
    editEventButton.addActionListener(e -> showEditEventDialog());
    JButton editTimesButton = new JButton("Edit Event Times");
    editTimesButton.addActionListener(e -> showEditEventTimesDialog());

    rightPanel.add(addEventButton);
    rightPanel.add(editEventButton);
    rightPanel.add(editTimesButton);

    // Put these panels onto the main panel
    panel.add(leftPanel, BorderLayout.WEST);
    panel.add(rightPanel, BorderLayout.EAST);

    calendarSelector.addActionListener(e -> {
      String selected = (String)calendarSelector.getSelectedItem();
      if (selected != null) {
        String result = controller.useCalendar(selected);
        if (result.startsWith("Error")) {
          JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
          updateScheduleView();
        }
      }
    });

    return panel;
  }

  /**
   * Updates the calendar list dropdown.
   */
  public void updateCalendarList() {
    calendarSelector.removeAllItems();
    String calendarList = controller.getCalendars();
    if (!calendarList.startsWith("Error")) {
      String[] calendars = calendarList.split("\n");
      for (String calendar : calendars) {
        if (!calendar.equals("No calendars")) {
          calendarSelector.addItem(calendar);
        }
      }
    }
  }

  private void showAddCalendarDialog() {
    JDialog dialog = new JDialog(this, "Add Calendar", true);
    dialog.setLayout(new GridLayout(0, 2, 5, 5));
    dialog.getContentPane().setBackground(BACKGROUND_COLOR);

    JTextField nameField = new JTextField();
    JComboBox<String> timezoneBox = new JComboBox<>(TimeZone.getAvailableIDs());

    dialog.add(new JLabel(" Calendar Name:"));
    dialog.add(nameField);
    dialog.add(new JLabel(" Timezone:"));
    dialog.add(timezoneBox);

    JButton addButton = new JButton("Add");
    addButton.addActionListener(e -> {
      String result = controller.createCalendar(nameField.getText(),
              TimeZone.getTimeZone((String) timezoneBox.getSelectedItem()));

      if (result.startsWith("Error")) {
        JOptionPane.showMessageDialog(dialog, result, "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        updateCalendarList();
        JOptionPane.showMessageDialog(dialog, result, "Success", JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
      }
    });

    dialog.add(addButton);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  private void createCenterPanel() {
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setBackground(BACKGROUND_COLOR);

    // Make date selection panel
    JPanel dateSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    dateSelectionPanel.setBackground(BACKGROUND_COLOR);

    // Get arrays from helper method
    Object[][] dropdowns = createDateTimeDropdowns();
    String[] months = (String[]) dropdowns[0];
    Integer[] days = (Integer[]) dropdowns[1];
    Integer[] years = (Integer[]) dropdowns[2];

    // Make dropdown menus and store references
    monthBox = new JComboBox<>(months);
    dayBox = new JComboBox<>(days);
    yearBox = new JComboBox<>(years);

    // Set current date as default
    LocalDateTime now = LocalDateTime.now();
    monthBox.setSelectedIndex(now.getMonthValue() - 1);
    dayBox.setSelectedItem(now.getDayOfMonth());
    yearBox.setSelectedItem(now.getYear());

    // Add change listeners to update schedule when selections change
    ActionListener dateChangeListener = e -> updateScheduleView();

    // Add listeners to all dropdowns
    monthBox.addActionListener(dateChangeListener);
    dayBox.addActionListener(dateChangeListener);
    yearBox.addActionListener(dateChangeListener);

    // Put components to date selection panel
    dateSelectionPanel.add(new JLabel("Month:"));
    dateSelectionPanel.add(monthBox);
    dateSelectionPanel.add(new JLabel("Day:"));
    dateSelectionPanel.add(dayBox);
    dateSelectionPanel.add(new JLabel("Year:"));
    dateSelectionPanel.add(yearBox);

    // Make schedule area
    scheduleArea = new JTextArea(10, 40);
    scheduleArea.setEditable(false);
    scheduleArea.setBackground(new Color(222, 238, 252));
    JScrollPane scrollPane = new JScrollPane(scheduleArea);

    // Add components to center
    centerPanel.add(dateSelectionPanel, BorderLayout.NORTH);
    centerPanel.add(scrollPane, BorderLayout.CENTER);

    add(centerPanel, BorderLayout.CENTER);
  }

  /**
   * Updates the schedule display.
   */
  public void updateScheduleView() {
    try {
      String date = getCurrentSelectedDate();
      if (date != null) {
        String schedule = controller.getSchedule(date);
        if (schedule.startsWith("Error")) {
          scheduleArea.setText(schedule);
        } else {
          scheduleArea.setText(schedule.isEmpty() ? "No events scheduled" : schedule);
        }
      }
    } catch (Exception e) {
      scheduleArea.setText("Error: " + e.getMessage());
    }
  }

  private String getCurrentSelectedDate() {
    if (yearBox == null || monthBox == null || dayBox == null) {
      return null;
    }

    try {
      return String.format("%04d-%02d-%02d",
              (Integer) yearBox.getSelectedItem(),
              monthBox.getSelectedIndex() + 1,
              (Integer) dayBox.getSelectedItem());
    } catch (Exception e) {
      return null;
    }
  }

  private void createBottomPanel() {
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.setBackground(BACKGROUND_COLOR);
    JButton prevButton = new JButton("Previous Day");
    JButton nextButton = new JButton("Next Day");

    prevButton.addActionListener(e -> {
      int day = (Integer) dayBox.getSelectedItem();
      if (day > 1) {
        dayBox.setSelectedItem(day - 1);
      } else {
        int month = monthBox.getSelectedIndex();
        if (month > 0) {
          monthBox.setSelectedIndex(month - 1);
          dayBox.setSelectedItem(31);
        }
      }
      updateScheduleView();
    });

    nextButton.addActionListener(e -> {
      int day = (Integer) dayBox.getSelectedItem();
      if (day < 31) {
        dayBox.setSelectedItem(day + 1);
      } else {
        int month = monthBox.getSelectedIndex();
        if (month < 11) {
          monthBox.setSelectedIndex(month + 1);
          dayBox.setSelectedItem(1);
        }
      }
      updateScheduleView();
    });

    bottomPanel.add(prevButton);
    bottomPanel.add(nextButton);
    add(bottomPanel, BorderLayout.SOUTH);
  }

  private void showAddEventDialog() {
    JDialog dialog = showTimedDialog("Add Event");

    // Subject field
    JTextField subjectField = new JTextField(20);
    dialog.add(new JLabel(" Event Subject:"));
    dialog.add(subjectField);

    Object[][] dropdowns = createDateTimeDropdowns();
    String[] months = (String[]) dropdowns[0];
    Integer[] days = (Integer[]) dropdowns[1];
    Integer[] years = (Integer[]) dropdowns[2];
    String[] hours = (String[]) dropdowns[3];
    String[] minutes = (String[]) dropdowns[4];

    String[] statuses = new String[3];
    statuses[0] = " ";
    statuses[1] = "public";
    statuses[2] = "private";

    // Dropdown menus for start date/time
    JComboBox<String> startMonthBox = new JComboBox<>(months);
    JComboBox<Integer> startDayBox = new JComboBox<>(days);
    JComboBox<Integer> startYearBox = new JComboBox<>(years);
    JComboBox<String> startHourBox = new JComboBox<>(hours);
    JComboBox<String> startMinBox = new JComboBox<>(minutes);

    // Dropdown menus for end date/time
    JComboBox<String> endMonthBox = new JComboBox<>(months);
    JComboBox<Integer> endDayBox = new JComboBox<>(days);
    JComboBox<Integer> endYearBox = new JComboBox<>(years);
    JComboBox<String> endHourBox = new JComboBox<>(hours);
    JComboBox<String> endMinBox = new JComboBox<>(minutes);

    // Set current date/time as default
    setCurrentDateTime(startMonthBox, startDayBox, startYearBox, startHourBox, startMinBox);
    LocalDateTime now = LocalDateTime.now();
    endMonthBox.setSelectedIndex(now.getMonthValue() - 1);
    endDayBox.setSelectedItem(now.getDayOfMonth());
    endYearBox.setSelectedItem(now.getYear());
    endHourBox.setSelectedItem(String.format("%02d", now.getHour() + 1));
    endMinBox.setSelectedItem(String.format("%02d", now.getMinute()));

    // Create panels for start and end date/time
    JPanel startDatePanel = createDateTimePanel(startMonthBox, startDayBox, startYearBox,
            startHourBox, startMinBox);

    JPanel endDatePanel = createDateTimePanel(endMonthBox, endDayBox, endYearBox,
            endHourBox, endMinBox);

    dialog.add(new JLabel(" Start:"));
    dialog.add(startDatePanel);
    dialog.add(new JLabel(" End:"));
    dialog.add(endDatePanel);

    // Status dropdown
    JComboBox<String> statusBox = new JComboBox<>(statuses);
    dialog.add(new JLabel(" Status (optional):"));
    dialog.add(statusBox);

    // Location field (optional)
    JTextField locationField = new JTextField();
    dialog.add(new JLabel(" Location (optional):"));
    dialog.add(locationField);

    // Description field (optional)
    JTextField descriptionField = new JTextField();
    dialog.add(new JLabel(" Description (optional):"));
    dialog.add(descriptionField);

    // Add button
    JButton addButton = new JButton("Add Event");
    addButton.addActionListener(e -> {
      try {
        // Format the datetime strings
        LocalDateTime startDateTime = LocalDateTime.of(
                (Integer) startYearBox.getSelectedItem(),
                startMonthBox.getSelectedIndex() + 1,
                (Integer) startDayBox.getSelectedItem(),
                Integer.parseInt((String) startHourBox.getSelectedItem()),
                Integer.parseInt((String) startMinBox.getSelectedItem())
        );

        LocalDateTime endDateTime = LocalDateTime.of(
                (Integer) endYearBox.getSelectedItem(),
                endMonthBox.getSelectedIndex() + 1,
                (Integer) endDayBox.getSelectedItem(),
                Integer.parseInt((String) endHourBox.getSelectedItem()),
                Integer.parseInt((String) endMinBox.getSelectedItem())
        );

        // Make sure that end time is after start time
        if (endDateTime.isBefore(startDateTime)) {
          throw new IllegalArgumentException("End time must be after start time");
        }

        String subject = subjectField.getText();
        if (subject.trim().isEmpty()) {
          throw new IllegalArgumentException("Subject cannot be empty");
        }

        Event event = Event.getBuilder(subject, startDateTime)
                .endDateTime(endDateTime)
                .location(locationField.getText())
                .description(descriptionField.getText())
                .status((String) statusBox.getSelectedItem())
                .build();

        String result = controller.createEvent(event);
        if (result.startsWith("Error")) {
          JOptionPane.showMessageDialog(dialog, result, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
          dialog.dispose();
          updateScheduleView();
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dialog,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    });

    JPanel spacingPanel = new JPanel();
    spacingPanel.setBackground(BACKGROUND_COLOR);
    spacingPanel.add(new JLabel(" "));
    dialog.add(spacingPanel);
    dialog.add(addButton);

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  private void showEditEventDialog() {
    JDialog dialog = showTimedDialog("Edit Event");

    // Subject field
    JTextField subjectField = new JTextField();
    dialog.add(new JLabel(" Event Subject:"));
    dialog.add(subjectField);

    Object[][] dropdowns = createDateTimeDropdowns();
    String[] months = (String[]) dropdowns[0];
    Integer[] days = (Integer[]) dropdowns[1];
    Integer[] years = (Integer[]) dropdowns[2];
    String[] hours = (String[]) dropdowns[3];
    String[] minutes = (String[]) dropdowns[4];

    // Start date/time dropdowns
    JComboBox<String> startMonthBox = new JComboBox<>(months);
    JComboBox<Integer> startDayBox = new JComboBox<>(days);
    JComboBox<Integer> startYearBox = new JComboBox<>(years);
    JComboBox<String> startHourBox = new JComboBox<>(hours);
    JComboBox<String> startMinBox = new JComboBox<>(minutes);

    // End date/time dropdowns
    JComboBox<String> endMonthBox = new JComboBox<>(months);
    JComboBox<Integer> endDayBox = new JComboBox<>(days);
    JComboBox<Integer> endYearBox = new JComboBox<>(years);
    JComboBox<String> endHourBox = new JComboBox<>(hours);
    JComboBox<String> endMinBox = new JComboBox<>(minutes);

    // Set current date/time as default
    LocalDateTime now = LocalDateTime.now();
    setCurrentDateTime(startMonthBox, startDayBox, startYearBox, startHourBox, startMinBox);
    setCurrentDateTime(endMonthBox, endDayBox, endYearBox, endHourBox, endMinBox);

    // Create date panels
    JPanel startDatePanel = createDateTimePanel(startMonthBox, startDayBox, startYearBox,
            startHourBox, startMinBox);
    JPanel endDatePanel = createDateTimePanel(endMonthBox, endDayBox, endYearBox,
            endHourBox, endMinBox);

    dialog.add(new JLabel(" Event Start:"));
    dialog.add(startDatePanel);
    dialog.add(new JLabel(" Event End:"));
    dialog.add(endDatePanel);

    // Select property
    String[] properties = {"subject", "location", "description", "status", "start", "end"};
    JComboBox<String> propertyBox = new JComboBox<>(properties);
    JTextField newValueField = new JTextField();

    dialog.add(new JLabel(" Property to Edit:"));
    dialog.add(propertyBox);
    dialog.add(new JLabel(" New Value:"));
    dialog.add(newValueField);

    // Edit event button
    JButton editButton = new JButton("Edit Event");
    editButton.addActionListener(e -> {
      try {
        // Format the datetime strings
        LocalDateTime startDateTime = LocalDateTime.of(
                (Integer) startYearBox.getSelectedItem(),
                startMonthBox.getSelectedIndex() + 1,
                (Integer) startDayBox.getSelectedItem(),
                Integer.parseInt((String) startHourBox.getSelectedItem()),
                Integer.parseInt((String) startMinBox.getSelectedItem())
        );

        LocalDateTime endDateTime = LocalDateTime.of(
                (Integer) endYearBox.getSelectedItem(),
                endMonthBox.getSelectedIndex() + 1,
                (Integer) endDayBox.getSelectedItem(),
                Integer.parseInt((String) endHourBox.getSelectedItem()),
                Integer.parseInt((String) endMinBox.getSelectedItem())
        );

        // Check that end time is after start time
        if (endDateTime.isBefore(startDateTime)) {
          throw new IllegalArgumentException("End time must be after start time");
        }

        String subject = subjectField.getText();
        String property = (String) propertyBox.getSelectedItem();
        String newValue = newValueField.getText();

        String result = controller.editEvent(property, subject, startDateTime.toString(),
                endDateTime.toString(), newValue);

        if (result.startsWith("Error")) {
          JOptionPane.showMessageDialog(dialog, result, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
          dialog.dispose();
          updateScheduleView();
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dialog,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    });

    JPanel spacingPanel = new JPanel();
    spacingPanel.setBackground(BACKGROUND_COLOR);
    spacingPanel.add(new JLabel(" "));
    dialog.add(spacingPanel);
    dialog.add(editButton);

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  private void showEditEventTimesDialog() {
    // Similar implementation to your existing one but using controller
    JOptionPane.showMessageDialog(this, "Edit Event Times feature coming soon!",
            "Info", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new CalendarGUI().setVisible(true);
    });
  }
}