package allocator;

import java.util.ArrayList;

/**
 * Extended BeachLockerAllocator with out-of-commission functionality
 */
public class BeachLockerAllocator implements LockerAllocator<Bag> {

  // Existing fields from previous implementation
  private final int minLockerNumber;
  private final int maxLockerNumber;
  private final boolean[] isAllocated;
  private final Bag[] lockerContents;
  private int nextAvailableLocker;

  // New field to track out-of-commission lockers
  private final boolean[] isOutOfCommission;

  /**
   * Constructor - same as previous implementation
   */
  public BeachLockerAllocator(int minLockerNumber, int maxLockerNumber) throws IllegalArgumentException {
    // Validation logic exactly as written in previous question
    if (maxLockerNumber <= minLockerNumber) {
      throw new IllegalArgumentException("Max locker number must be greater than min locker number");
    }
    if (minLockerNumber <= 0 || maxLockerNumber <= 0) {
      throw new IllegalArgumentException("All locker numbers must be positive");
    }

    this.minLockerNumber = minLockerNumber;
    this.maxLockerNumber = maxLockerNumber;

    int rangeSize = maxLockerNumber - minLockerNumber + 1;
    this.isAllocated = new boolean[rangeSize];
    this.lockerContents = new Bag[rangeSize];
    this.isOutOfCommission = new boolean[rangeSize]; // Initialize all as operational (false)

    this.nextAvailableLocker = minLockerNumber;
  }

  @Override
  public int rent() throws IllegalStateException {
    // Modified to check both allocation status AND commission status
    for (int lockerId = nextAvailableLocker; lockerId <= maxLockerNumber; lockerId++) {
      if (!isAllocated(lockerId) && !isOutOfCommission(lockerId)) {
        setAllocated(lockerId, true);
        updateNextAvailableLocker(lockerId + 1);
        return lockerId;
      }
    }

    // Search from beginning if nothing found from nextAvailableLocker onwards
    for (int lockerId = minLockerNumber; lockerId < nextAvailableLocker; lockerId++) {
      if (!isAllocated(lockerId) && !isOutOfCommission(lockerId)) {
        setAllocated(lockerId, true);
        updateNextAvailableLocker(lockerId + 1);
        return lockerId;
      }
    }

    throw new IllegalStateException("No available lockers to rent");
  }

  /**
   * Mark a locker as out of commission (cannot be rented)
   * @param id the locker ID to mark as out of commission
   * @throws IllegalArgumentException if the ID is invalid
   */
  public void markOutOfCommission(int id) throws IllegalArgumentException {
    validateLockerId(id);
    int arrayIndex = id - minLockerNumber;
    isOutOfCommission[arrayIndex] = true;
  }

  /**
   * Mark a locker as operational (can be rented again)
   * @param id the locker ID to mark as operational
   * @throws IllegalArgumentException if the ID is invalid
   */
  public void markOperational(int id) throws IllegalArgumentException {
    validateLockerId(id);
    int arrayIndex = id - minLockerNumber;
    isOutOfCommission[arrayIndex] = false;
  }

  // Helper methods
  private boolean isAllocated(int lockerId) {
    int arrayIndex = lockerId - minLockerNumber;
    return isAllocated[arrayIndex];
  }

  private void setAllocated(int lockerId, boolean allocated) {
    int arrayIndex = lockerId - minLockerNumber;
    isAllocated[arrayIndex] = allocated;
  }

  private boolean isOutOfCommission(int lockerId) {
    int arrayIndex = lockerId - minLockerNumber;
    return isOutOfCommission[arrayIndex];
  }

  private void updateNextAvailableLocker(int startFrom) {
    for (int lockerId = startFrom; lockerId <= maxLockerNumber; lockerId++) {
      if (!isAllocated(lockerId) && !isOutOfCommission(lockerId)) {
        nextAvailableLocker = lockerId;
        return;
      }
    }
    nextAvailableLocker = maxLockerNumber + 1;
  }

  private void validateLockerId(int id) throws IllegalArgumentException {
    if (id < minLockerNumber || id > maxLockerNumber) {
      throw new IllegalArgumentException("Invalid locker ID: " + id);
    }
  }

  @Override
  public void free(int id) throws IllegalArgumentException {
    // Implementation exactly as given to you or as implemented in previous question
    validateLockerId(id);
    if (!isAllocated(id)) {
      throw new IllegalArgumentException("Locker " + id + " is already free");
    }
    setAllocated(id, false);
    lockerContents[id - minLockerNumber] = null;
    if (id < nextAvailableLocker) {
      nextAvailableLocker = id;
    }
  }

  @Override
  public void deposit(int id, Bag equipment) throws IllegalArgumentException {
    // Implementation exactly as given to you or as implemented in previous question
    validateLockerId(id);
    if (!isAllocated(id)) {
      throw new IllegalArgumentException("Locker " + id + " is not allocated");
    }
    lockerContents[id - minLockerNumber] = equipment;
  }

  @Override
  public Bag get(int id) throws IllegalArgumentException {
    // Implementation exactly as given to you or as implemented in previous question
    validateLockerId(id);
    if (!isAllocated(id)) {
      throw new IllegalArgumentException("Locker " + id + " is not allocated");
    }
    return lockerContents[id - minLockerNumber];
  }
}