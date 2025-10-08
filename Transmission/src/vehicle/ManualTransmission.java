package vehicle;

/**
 * Interface representing a manual transmission system in a vehicle.
 * Provides methods to control and monitor the transmission's gear and speed.
 */
public interface ManualTransmission {

  /**
   * Returns the current status of the transmission as a string.
   * The status reflects the current state and the result of the most recent operation.
   *
   * @return a String describing the current status of the transmission
   */
  String getStatus();

  /**
   * Returns the current speed of the vehicle.
   *
   * @return the current speed as a whole number
   */
  int getSpeed();

  /**
   * Returns the current gear of the vehicle.
   *
   * @return the current gear as a whole number
   */
  int getGear();

  /**
   * Increases the speed by a fixed amount without changing gears.
   * If the speed cannot be increased, the speed remains unchanged.
   *
   * @return the resulting ManualTransmission object
   */
  ManualTransmission increaseSpeed();

  /**
   * Decreases the speed by a fixed amount without changing gears.
   * If the speed cannot be decreased, the speed remains unchanged.
   *
   * @return the resulting ManualTransmission object
   */
  ManualTransmission decreaseSpeed();

  /**
   * Increases the gear by one without changing speed.
   * If the gear cannot be increased, the gear remains unchanged.
   *
   * @return the resulting ManualTransmission object
   */
  ManualTransmission increaseGear();

  /**
   * Decreases the gear by one without changing speed.
   * If the gear cannot be decreased, the gear remains unchanged.
   *
   * @return the resulting ManualTransmission object
   */
  ManualTransmission decreaseGear();
}