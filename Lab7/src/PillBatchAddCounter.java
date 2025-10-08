/**
 * Part 3: Write another new class called PillBatchAddCounter that extends the original decorator.
 *
 * <p>This class "lazy-adds" pills: pills must be added only sometime before the pill count is
 * retrieved, or the pill count is reset.
 *
 * <p>This decorator will simply record how many pills are added when addPill is called,
 * but not forward it to its delegate. When getPillCount() is called, first call the delegate's
 * addPill method with the cumulative number of pills added, and then reset its pill add count.
 * The reset() method should work in the same way.
 */
public class PillBatchAddCounter extends PillCounterDecorator {
  private int pendingPills;

  /**
   * Constructor that takes a PillCounter delegate.
   * @param delegate the PillCounter to decorate
   */
  public PillBatchAddCounter(PillCounter delegate) {
    super(delegate);
    this.pendingPills = 0;
  }

  @Override
  public void addPill(int count) {
    // Simply record how many pills are added when addPill is called,
    // but not forward it to the delegate
    if (count > 0) {
      pendingPills += count;
    }
  }

  @Override
  public void removePill() {
    // Need to flush pending adds before removing
    flushPendingAdds();
    super.removePill();
  }

  @Override
  public void reset() {
    // When reset() is called, first call the delegate's addPill method
    // with the cumulative number of pills added, then reset pill add count
    flushPendingAdds();
    super.reset();
  }

  @Override
  public int getPillCount() {
    // When getPillCount() is called, first call the delegate's addPill method
    // with the cumulative number of pills added, then reset pill add count
    flushPendingAdds();
    return super.getPillCount();
  }

  /**
   * Helper method to flush pending pill additions to the delegate.
   */
  private void flushPendingAdds() {
    if (pendingPills > 0) {
      super.addPill(pendingPills);
      pendingPills = 0;
    }
  }
}