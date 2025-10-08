import org.junit.Before;
import org.junit.Test;

import vehicle.ManualTransmission;
import vehicle.RegularManualTransmission;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the RegularManualTransmission class.
 * Tests all the functionality as specified in the assignment.
 */
public class RegularManualTransmissionTest {

  private ManualTransmission transmission;

  /**
   * Sets up a standard test transmission before each test.
   * Using speed ranges:
   * Gear 1: 0-10
   * Gear 2: 5-20
   * Gear 3: 15-30
   * Gear 4: 25-40
   * Gear 5: 35-50
   */
  @Before
  public void setUp() {
    // Initialize with valid speed ranges for 5 gears
    transmission = new RegularManualTransmission(0, 10, 5, 20, 15, 30,
            25, 40, 35, 50);
  }

  /**
   * Tests that the constructor properly validates its input parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidConstructorLowerGreaterThanUpper() {
    // Lower limit greater than upper limit for gear 3
    new RegularManualTransmission(0, 10, 5, 20, 35, 30, 25,
            40, 35, 50);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidConstructorFirstGearNotStartAtZero() {
    // First gear doesn't start at 0
    new RegularManualTransmission(1, 10, 5, 20, 15, 30, 25, 40,
            35, 50);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidConstructorLowerLimitsNotAscending() {
    // Lower limits not in ascending order (l3 < l2)
    new RegularManualTransmission(0, 10, 15, 20, 10, 30, 25,
            40, 35, 50);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidConstructorGapsInGearRanges() {
    // Gap between gear 2 and gear 3 (h2 < l3)
    new RegularManualTransmission(0, 10, 5, 14, 15, 30, 25, 40,
            35, 50);
  }

  /**
   * Tests the initial state of the transmission.
   */
  @Test
  public void testInitialState() {
    assertEquals("Initial speed should be 0", 0, transmission.getSpeed());
    assertEquals("Initial gear should be 1", 1, transmission.getGear());
    assertEquals("Initial status should be OK", "OK: everything is OK.",
            transmission.getStatus());
  }

  /**
   * Tests increasing speed within the same gear.
   */
  @Test
  public void testIncreaseSpeedSameGear() {
    transmission.increaseSpeed();
    assertEquals("Speed should be 1", 1, transmission.getSpeed());
    assertEquals("Gear should remain 1", 1, transmission.getGear());
    assertEquals("Status should be OK", "OK: everything is OK.",
            transmission.getStatus());
  }

  /**
   * Tests increasing speed to reach a point where increasing gear is suggested.
   */
  @Test
  public void testIncreaseSpeedSuggestGearIncrease() {
    // Increase speed to 5 (on boundary of gear 1 and 2)
    for (int i = 0; i < 5; i++) {
      transmission.increaseSpeed();
    }
    assertEquals("Speed should be 5", 5, transmission.getSpeed());
    assertEquals("Status should suggest gear increase",
            "OK: you may increase the gear.", transmission.getStatus());
  }

  /**
   * Tests increasing speed to reach the current gear's limit.
   */
  @Test
  public void testIncreaseSpeedToLimit() {
    // Increase speed to 10 (max for gear 1)
    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    assertEquals("Speed should be 10", 10, transmission.getSpeed());

    // Try to increase beyond the limit
    transmission.increaseSpeed();
    assertEquals("Speed should still be 10", 10, transmission.getSpeed());
    assertEquals("Status should indicate need to increase gear",
            "Cannot increase speed, increase gear first.", transmission.getStatus());
  }

  /**
   * Tests increasing gear when appropriate.
   */
  @Test
  public void testIncreaseGear() {
    // Increase speed to 5 (valid for gear 2)
    for (int i = 0; i < 5; i++) {
      transmission.increaseSpeed();
    }

    // Increase gear
    transmission.increaseGear();
    assertEquals("Gear should be 2", 2, transmission.getGear());
    assertEquals("Status should be OK", "OK: everything is OK.",
            transmission.getStatus());
  }

  /**
   * Tests attempting to increase gear when speed is too low.
   */
  @Test
  public void testIncreaseGearSpeedTooLow() {
    // Speed is still 0, cannot increase to gear 2
    transmission.increaseGear();
    assertEquals("Gear should still be 1", 1, transmission.getGear());
    assertEquals("Status should indicate need to increase speed",
            "Cannot increase gear, increase speed first.", transmission.getStatus());
  }

  /**
   * Tests attempting to increase gear when already at maximum.
   */
  @Test
  public void testIncreaseGearAtMaximum() {
    // Gear 1: increase to max speed (10)
    for (int i = 0; i < 10; i++) {
      transmission = transmission.increaseSpeed();
    }
    assertEquals(10, transmission.getSpeed());
    assertEquals(1, transmission.getGear());

    // Shift to gear 2
    transmission = transmission.increaseGear();
    assertEquals(2, transmission.getGear());

    // Gear 2: increase to upper range (20)
    for (int i = 0; i < 10; i++) {
      transmission = transmission.increaseSpeed();
    }
    assertEquals(20, transmission.getSpeed());

    // Shift to gear 3
    transmission = transmission.increaseGear();
    assertEquals(3, transmission.getGear());

    // Gear 3: increase to upper range (30)
    for (int i = 0; i < 10; i++) {
      transmission = transmission.increaseSpeed();
    }
    assertEquals(30, transmission.getSpeed());

    // Shift to gear 4
    transmission = transmission.increaseGear();
    assertEquals(4, transmission.getGear());

    // Gear 4: increase to sufficient speed for gear 5 (at least 35)
    for (int i = 0; i < 5; i++) {
      transmission = transmission.increaseSpeed();
    }
    assertEquals(35, transmission.getSpeed());

    // Shift to gear 5
    transmission = transmission.increaseGear();
    assertEquals(5, transmission.getGear());

    // Try to shift beyond gear 5
    transmission = transmission.increaseGear();
    assertEquals("Gear should still be 5", 5, transmission.getGear());
    assertEquals("Status should indicate maximum gear reached",
            "Cannot increase gear. Reached maximum gear.", transmission.getStatus());
  }

  /**
   * Tests decreasing speed within the same gear.
   */
  @Test
  public void testDecreaseSpeed() {
    // Increase speed first
    transmission.increaseSpeed().increaseSpeed();
    assertEquals("Speed should be 2", 2, transmission.getSpeed());

    // Decrease speed
    transmission.decreaseSpeed();
    assertEquals("Speed should be 1", 1, transmission.getSpeed());
    assertEquals("Gear should remain 1", 1, transmission.getGear());
    assertEquals("Status should be OK", "OK: everything is OK.",
            transmission.getStatus());
  }

  /**
   * Tests decreasing speed to minimum limit.
   */
  @Test
  public void testDecreaseSpeedToMinimum() {
    // Speed is already at minimum (0)
    transmission.decreaseSpeed();
    assertEquals("Speed should still be 0", 0, transmission.getSpeed());
    assertEquals("Status should indicate minimum speed",
            "Cannot decrease speed. Reached minimum speed.", transmission.getStatus());
  }

  /**
   * Tests decreasing speed to suggest gear decrease.
   */
  @Test
  public void testDecreaseSpeedSuggestGearDecrease() {
    // Set up to get to gear 2 with speed 10
    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    transmission.increaseGear();

    // Decrease speed to 5 (could go back to gear 1)
    for (int i = 0; i < 5; i++) {
      transmission.decreaseSpeed();
    }
    assertEquals("Speed should be 5", 5, transmission.getSpeed());
    assertEquals("Status should suggest gear decrease",
            "OK: you may decrease the gear.", transmission.getStatus());
  }

  /**
   * Tests decreasing gear when appropriate.
   */
  @Test
  public void testDecreaseGear() {
    // Set up to get to gear 2 with speed 10
    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    transmission.increaseGear();

    // Decrease speed to 10 and gear to 1
    transmission.decreaseGear();
    assertEquals("Gear should be 1", 1, transmission.getGear());
    assertEquals("Status should be OK", "OK: everything is OK.",
            transmission.getStatus());
  }

  /**
   * Tests attempting to decrease gear when speed is too high.
   */
  @Test
  public void testDecreaseGearSpeedTooHigh() {
    // Set up to get to gear 2 with speed 15
    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    transmission.increaseGear(); // Now in gear 2

    // Increase speed to 15 in gear 2
    for (int i = 0; i < 5; i++) {
      transmission.increaseSpeed();
    }

    // Try to decrease gear when speed is too high
    transmission = transmission.decreaseGear();
    assertEquals("Gear should still be 2", 2, transmission.getGear());
    assertEquals("Status should indicate need to decrease speed",
            "Cannot decrease gear, decrease speed first.", transmission.getStatus());
  }

  /**
   * Tests attempting to decrease gear when already at minimum.
   */
  @Test
  public void testDecreaseGearAtMinimum() {
    // Gear is already at minimum (1)
    transmission.decreaseGear();
    assertEquals("Gear should still be 1", 1, transmission.getGear());
    assertEquals("Status should indicate minimum gear reached",
            "Cannot decrease gear. Reached minimum gear.", transmission.getStatus());
  }

  /**
   * Tests increasing to maximum speed.
   */
  @Test
  public void testMaximumSpeed() {
    // Set up to reach maximum speed (50) in gear 5
    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    transmission.increaseGear(); // Gear 2

    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    transmission.increaseGear(); // Gear 3

    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    transmission.increaseGear(); // Gear 4

    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }
    transmission.increaseGear(); // Gear 5

    for (int i = 0; i < 10; i++) {
      transmission.increaseSpeed();
    }

    assertEquals("Speed should be 50", 50, transmission.getSpeed());

    // Try to exceed maximum speed
    transmission.increaseSpeed();
    assertEquals("Speed should still be 50", 50, transmission.getSpeed());
    assertEquals("Status should indicate maximum speed",
            "Cannot increase speed. Reached maximum speed.", transmission.getStatus());
  }

  /**
   * Tests the fluent interface by chaining method calls.
   */
  @Test
  public void testFluentInterface() {
    // Increase speed to 5
    for (int i = 0; i < 5; i++) {
      transmission = transmission.increaseSpeed();
    }

    // Increase gear to 2
    transmission = transmission.increaseGear();
    assertEquals("Gear should be 2 after increase", 2, transmission.getGear());

    // Increase and decrease speed once each
    transmission = transmission.increaseSpeed().decreaseSpeed();

    assertEquals("Speed should be 5", 5, transmission.getSpeed());
    assertEquals("Gear should be 2", 2, transmission.getGear());
  }
}