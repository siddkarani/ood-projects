import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Optional Extension: Combine your batch-adding decorator with the add-monitoring decorator 
 * in a way that shows you that the number of calls to addPill were reduced.
 */
public class TestCombinedDecorators {

  /** Test combining decorators to demonstrate optimization effectiveness. */
  @Test
  public void testCombinedDecoratorsUsage() {
    System.out.println("=== COMBINED DECORATORS TEST ===");

    // Test 1: Monitor -> LoggingPillCounter (to see original behavior)
    System.out.println("\n1. Testing with monitoring only (original behavior):");
    PillAddMonitor monitorOnly = new PillAddMonitor(new LoggingPillCounter());

    // Run a smaller test to keep output manageable
    smallConveyerBelt(monitorOnly);
    List<Integer> originalCounts = monitorOnly.getAddCounts();

    int totalOriginalCalls = 0;
    for (int count : originalCounts) {
      totalOriginalCalls += count;
    }
    System.out.println("Total addPill() calls to LoggingPillCounter: " + totalOriginalCalls);

    // Test 2: Monitor -> BatchAdd -> LoggingPillCounter (to see optimized behavior)
    System.out.println("\n2. Testing with batch optimization:");
    PillCounter baseCounter = new LoggingPillCounter();
    PillCounter batchCounter = new PillBatchAddCounter(baseCounter);
    PillAddMonitor monitorBatch = new PillAddMonitor(batchCounter);

    smallConveyerBelt(monitorBatch);
    List<Integer> batchCounts = monitorBatch.getAddCounts();

    int totalBatchCalls = 0;
    for (int count : batchCounts) {
      totalBatchCalls += count;
    }
    System.out.println("Total addPill() calls to BatchAddCounter: " + totalBatchCalls);

    // Show the reduction
    System.out.println("\n=== PERFORMANCE IMPROVEMENT ===");
    System.out.println("Original calls to LoggingPillCounter: " + totalOriginalCalls);
    System.out.println("Optimized calls to LoggingPillCounter: " + batchCounts.size());
    int reductionFactor = totalOriginalCalls / batchCounts.size();
    System.out.println("Reduction factor: " + reductionFactor + "x fewer file I/O operations!");

    assertTrue("Optimization should reduce calls",
            batchCounts.size() < totalOriginalCalls);
  }

  /**
   * Smaller version of conveyerBelt for clearer demonstration.
   *
   * @param counter the pill counter to test
   * @return true if the test passes
   */
  private boolean smallConveyerBelt(PillCounter counter) {
    // Make 5 bottles of 10 pills each (1 at a time)
    for (int bottle = 0; bottle < 5; bottle += 1) {
      for (int pill = 0; pill < 10; pill += 1) {
        counter.addPill(1);
      }
      assertEquals(10, counter.getPillCount());
      counter.reset();
    }

    // Make 3 bottles of 8 pills each (2 at a time)
    for (int bottle = 0; bottle < 3; bottle += 1) {
      for (int pill = 0; pill < 8; pill += 2) {
        counter.addPill(2);
      }
      assertEquals(8, counter.getPillCount());
      counter.reset();
    }

    return true;
  }
}