import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Part 3: Write a test identical to the one provided to you.
 *
 * <p>However this test should use the original pill counter decorated with the batch-adding
 * decorator. This test should pass.
 */
public class TestPillBatchAddCounter {

  @Test
  public void testBatchAddCounterUsage() {
    // Use the original pill counter decorated with the batch-adding decorator
    PillCounter counter = new PillBatchAddCounter(new LoggingPillCounter());

    // Time the execution to compare performance
    long startTime = System.currentTimeMillis();
    boolean result = conveyerBelt(counter);
    long endTime = System.currentTimeMillis();

    assertTrue(result);

    System.out.println("Batch add counter test completed in: " + (endTime - startTime) + " ms");
    System.out.println("This should be significantly faster than the original test!");
  }

  /**
   * Test identical to the one provided (same as PillCounterBeginTest).
   */
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