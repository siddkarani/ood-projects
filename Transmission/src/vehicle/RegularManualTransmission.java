package vehicle;

/**
 * Implementation of the ManualTransmission interface representing a standard
 * manual transmission system with 5 gears.
 * This transmission adjusts speed by 1 unit at a time and manages appropriate
 * gear-speed relationships.
 */
public class RegularManualTransmission implements ManualTransmission {

  // Speed ranges for each gear
  private final int[] lowerLimits;
  private final int[] upperLimits;

  // Current state
  private int currentSpeed;
  private int currentGear;
  private String currentStatus;

  /**
   * Constructs a new RegularManualTransmission with specified speed ranges for each gear.
   *
   * @param l1 lower speed limit for gear 1
   * @param h1 upper speed limit for gear 1
   * @param l2 lower speed limit for gear 2
   * @param h2 upper speed limit for gear 2
   * @param l3 lower speed limit for gear 3
   * @param h3 upper speed limit for gear 3
   * @param l4 lower speed limit for gear 4
   * @param h4 upper speed limit for gear 4
   * @param l5 lower speed limit for gear 5
   * @param h5 upper speed limit for gear 5
   * @throws IllegalArgumentException if speed ranges are invalid
   */
  public RegularManualTransmission(int l1, int h1, int l2, int h2, int l3, int h3,
                                   int l4, int h4, int l5, int h5) {
    // Validate input parameters
    validateSpeedRanges(l1, h1, l2, h2, l3, h3, l4, h4, l5, h5);

    // Initialize speed ranges
    lowerLimits = new int[]{l1, l2, l3, l4, l5};
    upperLimits = new int[]{h1, h2, h3, h4, h5};

    // Set initial state
    currentSpeed = 0;
    currentGear = 1;
    currentStatus = "OK: everything is OK.";
  }

  /**
   * Validates that speed ranges for all gears are consistent and appropriate.
   */
  private void validateSpeedRanges(int l1, int h1, int l2, int h2, int l3, int h3,
                                   int l4, int h4, int l5, int h5) {
    // Check each gear's lower limit is less than or equal to its upper limit
    if (l1 > h1 || l2 > h2 || l3 > h3 || l4 > h4 || l5 > h5) {
      throw new IllegalArgumentException(
              "Lower limit of a gear cannot be greater than its upper limit");
    }

    // Check first gear starts at 0
    if (l1 != 0) {
      throw new IllegalArgumentException("The lower limit of gear 1 should be 0");
    }

    if (l1 >= l2 || l2 >= l3 || l3 >= l4 || l4 >= l5) {
    // Check gear ranges are in ascending order
      throw new IllegalArgumentException("Lower limits should be in ascending order");
    }

    // Check for no gaps between gears
    if (h1 < l2 || h2 < l3 || h3 < l4 || h4 < l5) {
      throw new IllegalArgumentException("There should be no gaps between adjacent gear ranges");
    }
  }

  @Override
  public String getStatus() {
    return currentStatus;
  }

  @Override
  public int getSpeed() {
    return currentSpeed;
  }

  @Override
  public int getGear() {
    return currentGear;
  }

  @Override
  public ManualTransmission increaseSpeed() {
    int targetSpeed = currentSpeed + 1;

    // Check if speed can be increased beyond vehicle's limit
    if (targetSpeed > upperLimits[4]) {
      currentStatus = "Cannot increase speed. Reached maximum speed.";
      return this;
    }

    // Check if speed can be increased within current gear
    if (targetSpeed > upperLimits[currentGear - 1]) {
      currentStatus = "Cannot increase speed, increase gear first.";
      return this;
    }

    // Increase speed
    currentSpeed = targetSpeed;

    // Check if we can suggest increasing gear
    if (currentGear < 5 && currentSpeed >= lowerLimits[currentGear]) {
      currentStatus = "OK: you may increase the gear.";
    } else {
      currentStatus = "OK: everything is OK.";
    }

    return this;
  }

  @Override
  public ManualTransmission decreaseSpeed() {
    int targetSpeed = currentSpeed - 1;

    // Check if speed can be decreased below 0
    if (targetSpeed < 0) {
      currentStatus = "Cannot decrease speed. Reached minimum speed.";
      return this;
    }

    // Check if speed can be decreased within current gear
    if (targetSpeed < lowerLimits[currentGear - 1]) {
      currentStatus = "Cannot decrease speed, decrease gear first.";
      return this;
    }

    // Decrease speed
    currentSpeed = targetSpeed;

    // Check if we can suggest decreasing gear
    if (currentGear > 1 && currentSpeed <= upperLimits[currentGear - 2]) {
      currentStatus = "OK: you may decrease the gear.";
    } else {
      currentStatus = "OK: everything is OK.";
    }

    return this;
  }

  @Override
  public ManualTransmission increaseGear() {
    // Check if already at maximum gear
    if (currentGear == 5) {
      currentStatus = "Cannot increase gear. Reached maximum gear.";
      return this;
    }

    // Check if speed is sufficient for next gear
    if (currentSpeed < lowerLimits[currentGear]) {
      currentStatus = "Cannot increase gear, increase speed first.";
      return this;
    }

    // Increase gear
    currentGear++;
    currentStatus = "OK: everything is OK.";

    return this;
  }

  @Override
  public ManualTransmission decreaseGear() {
    // Check if already at minimum gear
    if (currentGear == 1) {
      currentStatus = "Cannot decrease gear. Reached minimum gear.";
      return this;
    }

    // Check if speed is low enough for previous gear
    if (currentSpeed > upperLimits[currentGear - 2]) {
      currentStatus = "Cannot decrease gear, decrease speed first.";
      return this;
    }

    // Decrease gear
    currentGear--;
    currentStatus = "OK: everything is OK.";

    return this;
  }
}