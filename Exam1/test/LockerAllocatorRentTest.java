import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import allocator.BeachLockerAllocator;
import allocator.Bag;
import java.util.HashSet;
import java.util.Set;

public class LockerAllocatorRentTest {

  private BeachLockerAllocator allocator;

  @Before
  public void setUp() {
    // Create allocator with 50 lockers (IDs 1 to 50)
    allocator = new BeachLockerAllocator(1, 50);
  }

  @Test
  public void testRentReturnsUniqueIds() {
    Set<Integer> rentedIds = new HashSet<Integer>();

    // Rent 30 lockers
    for (int i = 0; i < 30; i++) {
      int id = allocator.rent();
      Assert.assertTrue("rent() returned duplicate ID: " + id, rentedIds.add(id));
    }

    Assert.assertEquals(30, rentedIds.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testRentThrowsExceptionWhenFull() {
    // Rent all available lockers (50 total)
    for (int i = 0; i < 50; i++) {
      allocator.rent();
    }

    // Next rent() should throw IllegalStateException
    allocator.rent();
  }

  @Test
  public void testRentedLockersHaveValidIds() {
    // Rent several lockers
    int id1 = allocator.rent();
    int id2 = allocator.rent();
    int id3 = allocator.rent();

    // Verify the IDs are valid and unique
    Assert.assertTrue("ID should be positive", id1 > 0);
    Assert.assertTrue("ID should be positive", id2 > 0);
    Assert.assertTrue("ID should be positive", id3 > 0);
    Assert.assertTrue("IDs should be different", id1 != id2);
    Assert.assertTrue("IDs should be different", id2 != id3);
    Assert.assertTrue("IDs should be different", id1 != id3);

    // Verify IDs are within expected range (1-50)
    Assert.assertTrue("ID should be in range", id1 >= 1 && id1 <= 50);
    Assert.assertTrue("ID should be in range", id2 >= 1 && id2 <= 50);
    Assert.assertTrue("ID should be in range", id3 >= 1 && id3 <= 50);
  }
}