import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import allocator.BeachLockerAllocator;
import java.util.ArrayList;
import java.util.List;

public class OutOfCommissionTest {

  private BeachLockerAllocator allocator;

  @Before
  public void setUp() {
    // 1. Create a locker allocator with locker IDs 2000-2010 (inclusive)
    allocator = new BeachLockerAllocator(2000, 2010);
  }

  @Test
  public void testOutOfCommissionFunctionality() {
    // 2. Rent out 5 lockers and verify their IDs
    List<Integer> firstRentals = new ArrayList<Integer>();
    for (int i = 0; i < 5; i++) {
      firstRentals.add(allocator.rent());
    }
    Assert.assertEquals(5, firstRentals.size());
    // Should get 2000, 2001, 2002, 2003, 2004 (closest to counter first)
    for (int i = 0; i < 5; i++) {
      Assert.assertEquals(Integer.valueOf(2000 + i), firstRentals.get(i));
    }

    // 3. Flag lockers with IDs 2005, 2007, 2009 as "out of commission"
    allocator.markOutOfCommission(2005);
    allocator.markOutOfCommission(2007);
    allocator.markOutOfCommission(2009);

    // 4. Rent out one more locker and verify its ID
    int nextRental = allocator.rent();
    Assert.assertEquals(2006, nextRental); // Should skip 2005 since it's out of commission

    // 5. Free locker with ID 2000
    allocator.free(2000);

    // 6. Mark locker with ID 2005 as "operational" again
    allocator.markOperational(2005);

    // 7. Rent out two more lockers and verify their IDs
    int rental1 = allocator.rent();
    int rental2 = allocator.rent();

    // Should get 2000 (closest, now free) and 2005 (now operational)
    Assert.assertEquals(2000, rental1);
    Assert.assertEquals(2005, rental2);
  }
}