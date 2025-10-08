import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class represents the system that is using the pill counter.
 * What this class does is illustrated in a test only so
 * that you can run this. In reality, this usage is
 * embedded deep into a larger system, interfacing with actual
 * conveyer belts that are bottling pills.
 */
public class PillCounterBeginTest {

  /**
   * You may think of this as representative of the client
   * of a pill counter. It represents a heavy usage of the
   * pill counter, responding to an actual conveyer belt
   * that is bottling a large quantity of pill bottles of varying capacities.
   */

  @Test
  public void usage() {
    PillCounter counter = new LoggingPillCounter();
    boolean result = conveyerBelt(counter);
    assertTrue(result);
  }

  private boolean conveyerBelt(PillCounter counter) {
    //make 100 bottles of 100 pills each
    for (int bottle = 0; bottle < 100; bottle += 1) {
      for (int pill = 0; pill < 100; pill += 1) {
        counter.addPill(1); //1 pill at a time
      }
      assertEquals(100, counter.getPillCount());
      counter.reset(); //for the next bottle
    }

    //make 1000 bottles of 20 pills each
    for (int bottle = 0; bottle < 1000; bottle += 1) {
      for (int pill = 0; pill < 20; pill += 4) {
        counter.addPill(4); //4 pills at a time (newer machine)
      }
      assertEquals(20, counter.getPillCount());
      counter.reset(); //for the next bottle
    }

    //make 500 bottles of 200 pills each
    for (int bottle = 0; bottle < 500; bottle += 1) {
      for (int pill = 0; pill < 200; pill += 2) {
        counter.addPill(2); //2 pills at a time (third machine)
      }
      assertEquals(200, counter.getPillCount());
      counter.reset(); //for the next bottle
    }
    return true;
  }
}