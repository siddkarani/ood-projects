import org.junit.Before;
import org.junit.Test;

import burrito.Burrito;
import burrito.CustomBurrito;
import burrito.PortionSize;
import burrito.Protein;
import burrito.Size;
import burrito.Topping;
import burrito.VeggieBurrito;

import static org.junit.Assert.assertEquals;

/**
 * This is the test class for burritos.
 */
public class BurritoTest {
  private Burrito alacarteBurrito;
  private Burrito veggieBurritoJumboSize;
  private Burrito veggieBurritoLessCheese;


  @Before
  public void setup() {
    alacarteBurrito = new CustomBurrito(Size.Normal);
    alacarteBurrito.addProtein(Protein.Tofu, PortionSize.Normal);
    alacarteBurrito.addTopping(Topping.Cheese, PortionSize.Normal);
    alacarteBurrito.addTopping(Topping.MediumSalsa,PortionSize.Less);
    alacarteBurrito.addTopping(Topping.SourCream,PortionSize.Extra);

    veggieBurritoJumboSize = new VeggieBurrito(Size.Jumbo);

    veggieBurritoLessCheese = new VeggieBurrito(Size.Normal);
    //put less cheese
    veggieBurritoLessCheese.addTopping(Topping.Cheese, PortionSize.Less);
  }

  @Test
  public void testCost() {
    assertEquals(5.9,alacarteBurrito.cost(),0.01);
    assertEquals(7.2, veggieBurritoJumboSize.cost(),0.01);
    assertEquals(6.9,veggieBurritoLessCheese.cost(),0.01);

  }
}