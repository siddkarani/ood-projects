package model;

/**
 * Represents the properties of an event that can be edited.
 */
public enum Property {
  SUBJECT("subject", String.class),
  START("start", String.class),
  END("end", String.class),
  DESCRIPTION("description", String.class),
  LOCATION("location", String.class),
  STATUS("status", String.class);

  private final String name;
  private final Class<?> type;

  /**
   * Constructs a Property with its name and expected value type.
   *
   * @param name the property name as used in commands
   * @param type the expected Java type for this property's values
   */
  Property(String name, Class<?> type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Gets the property name as used in the calendar interface.
   * @return the property name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the expected type of the property value.
   * @return the Class object representing the value type
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Converts a string to the corresponding Property enum value.
   * Case-insensitive matching for user convenience.
   *
   * @param propertyName the name of the property
   * @return the corresponding Property enum value
   * @throws IllegalArgumentException if the property name is invalid
   */
  public static Property fromString(String propertyName) {
    if (propertyName == null || propertyName.trim().isEmpty()) {
      throw new IllegalArgumentException("Property name cannot be null or empty");
    }

    for (Property property : Property.values()) {
      if (property.getName().equalsIgnoreCase(propertyName.trim())) {
        return property;
      }
    }
    throw new IllegalArgumentException("Invalid property");
  }

  /**
   * Validates that a status value is acceptable.
   *
   * @param status the status value to validate
   * @return true if the status is valid
   */
  public static boolean isValidStatus(String status) {
    return status == null || status.isEmpty() ||
            status.equalsIgnoreCase("public") ||
            status.equalsIgnoreCase("private");
  }

  /**
   * Converts this property to its corresponding string representation.
   * @return the property name as used in the calendar interface
   */
  @Override
  public String toString() {
    return name;
  }
}