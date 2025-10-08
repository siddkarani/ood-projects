package allocator;

/**
 * This interface represents the operations for a locker manager.
 *
 * A single locker is represented by a unique integer ID (a locker number).
 * To use an allocated locker its ID must be presented.
 * Before being used, a locker must be allocated, and after it is
 * no longer needed it must be deallocated (for possible future use).
 *
 * A free locker cannot be used to deposit or retrieve contents
 *
 * @param <T> an object that represents the equipment to be kept
 *           in the locker. One locker may store only one T-type object.
 */
public interface LockerAllocator<T> {
    /**
     * Allocate an available locker.
     * @return the unique integer ID of the locker.
     *         All subsequent uses of this locker must present this ID.
     * @throws IllegalStateException if there are no more lockers
     *                               left to allocate
     */
    int rent() throws IllegalStateException;

    /**
     * Free a previously allocated locker, possibly to be re-allocated
     * for use later.
     *
     * @param id the unique integer ID of the locker to be freed
     * @throws IllegalArgumentException if the id represents an already free
     *                                  locker, or is an invalid number.
     *                                  Invalidity conditions depend on implementation
     */
    void free(int id) throws IllegalArgumentException;

    /**
     * Store equipment in the given locker.
     * This operation may overwrite the current contents of the locker.
     *
     * @param id the unique integer ID of the locker to be used
     * @param equipment the equipment object to be stored in this locker
     * @throws IllegalArgumentException if the ID is invalid or is free
     */
    void deposit(int id,T equipment) throws IllegalArgumentException;

    /**
     * Retrieved the equipment stored at the given locker
     * @param id the unique integer ID of the locker to be used
     * @return the equipment object stored in the specified locker
     * @throws IllegalArgumentException if the ID is invalid or is free
     */
    T get(int id) throws IllegalArgumentException;
}
