/**
 * Basic decorator for PillCounter that implements the decorator pattern.
 *
 * <p>This decorator acts as a pass-through, delegating all calls to the wrapped PillCounter.
 * Part 1: Write a decorator for a pill counter that implements the PillCounter interface
 * and is composed by an object of it.
 */
public class PillCounterDecorator implements PillCounter {
  protected PillCounter delegate;

  /**
   * Constructor that takes a PillCounter delegate.
   * @param delegate the PillCounter to decorate
   */
  public PillCounterDecorator(PillCounter delegate) {
    this.delegate = delegate;
  }

  /**
   * Implement all required methods by calling to the corresponding methods in the delegate.
   */
  @Override
  public void addPill(int count) {
    delegate.addPill(count);
  }

  @Override
  public void removePill() {
    delegate.removePill();
  }

  @Override
  public void reset() {
    delegate.reset();
  }

  @Override
  public int getPillCount() {
    return delegate.getPillCount();
  }
}