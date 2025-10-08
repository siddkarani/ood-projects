import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Part 2: Write a test that uses this decorator in the same scenario as the given test.
 *
 * <p>However this test should also print the list of add counts in the end.
 * While printing is not part of testing, this will give you a quick view of what is happening.
 * Note that this test will take a bit longer than the original test, because in addition to
 * bad performance, your test is also printing to console!
 */
public class TestPillAddMonitor {

  @Test
  public void testPillAddMonitorUsage() {
    // Use the monitoring decorator in the same scenario as the given test
    PillAddMonitor monitor = new PillAddMonitor(new LoggingPillCounter());
    boolean result = conveyerBelt(monitor);
    assertTrue(result);

    // Print the list of add counts in the end
    List<Integer> addCounts = monitor.getAddCounts();
    System.out.println("\n=== ADD COUNTS ANALYSIS ===");
    System.out.println("Total bottles processed: " + addCounts.size());

    // Show some sample add counts to understand the pattern
    System.out.println("\nFirst 10 bottles (100 pills each, 1 at a time):");
    for (int i = 0; i < Math.min(10, addCounts.size()); i++) {
      System.out.println("Bottle " + (i + 1) + ": " + addCounts.get(i) + " addPill() calls");
    }

    if (addCounts.size() > 110) {
      System.out.println("\nBottles 101-110 (20 pills each, 4 at a time):");
      for (int i = 100; i < Math.min(110, addCounts.size()); i++) {
        System.out.println("Bottle " + (i + 1) + ": " + addCounts.get(i) + " addPill() calls");
      }
    }

    if (addCounts.size() > 1110) {
      System.out.println("\nBottles 1101-1110 (200 pills each, 2 at a time):");
      for (int i = 1100; i < Math.min(1110, addCounts.size()); i++) {
        System.out.println("Bottle " + (i + 1) + ": " + addCounts.get(i) + " addPill() calls");
      }
    }

    // Calculate total addPill calls
    int totalCalls = 0;
    for (int count : addCounts) {
      totalCalls += count;
    }
    System.out.println("\nTotal addPill() calls made: " + totalCalls);
    System.out.println("This explains the performance issue!");
  }

  /**
   * Same conveyerBelt method as the original test.
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