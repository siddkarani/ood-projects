import java.util.ArrayList;
import java.util.List;

/**
 * Part 2: Extend the above decorator in a new class called PillAddMonitor.
 *
 * <p>This class will monitor the number of times pills are added to a pill counter
 * before it is reset (monitoring how many times the pill counter is used while
 * filling one bottle).
 *
 * <p>The same pill counter may be reset many times, so this decorator saves all add counts.
 * Each time the pill counter is reset, this new class will start a new count.
 */
public class PillAddMonitor extends PillCounterDecorator {
  private List<Integer> addCounts;
  private int currentAddCount;

  /**
   * Constructor that takes a PillCounter delegate.
   * @param delegate the PillCounter to decorate
   */
  public PillAddMonitor(PillCounter delegate) {
    super(delegate);
    this.addCounts = new ArrayList<>();
    this.currentAddCount = 0;
  }

  @Override
  public void addPill(int count) {
    super.addPill(count);
    // Count the number of times addPill is called (not the number of pills)
    currentAddCount++;
  }

  @Override
  public void reset() {
    super.reset();
    // Each time the pill counter is reset, start a new count
    addCounts.add(currentAddCount);
    currentAddCount = 0;
  }

  /**
   * Add a new method to this class that returns the add counts in a list.
   * @return list of add counts for each reset cycle
   */
  public List<Integer> getAddCounts() {
    return new ArrayList<>(addCounts);
  }
}