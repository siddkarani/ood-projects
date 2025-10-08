package betterburrito;

import burrito.PortionSize;
import burrito.Protein;
import burrito.Size;
import burrito.Topping;

import java.util.Map;

/**
 * This class represents a veggie burrito. A veggie burrito has black beans,
 * medium salsa, cheese, lettuce, and guacamole, all in the regular portions.
 */
public class VeggieBurrito extends CustomBurrito {

  /**
   * Private constructor for VeggieBurrito, to be used by the builder.
   *
   * @param size     the size of this burrito
   * @param proteins the proteins in this burrito
   * @param toppings the toppings in this burrito
   */
  private VeggieBurrito(Size size, Map<Protein, PortionSize> proteins, Map<Topping,
          PortionSize> toppings) {
    super(size, proteins, toppings);
  }

  /**
   * Builder for veggie burritos.
   */
  public static class VeggieBurritoBuilder
          extends BurritoBuilder<VeggieBurritoBuilder> {

    /**
     * Create a VeggieBurritoBuilder with default veggie burrito ingredients.
     */
    public VeggieBurritoBuilder() {
      // Default ingredients for a veggie burrito
      addProtein(Protein.BlackBeans, PortionSize.Normal);
      addTopping(Topping.MediumSalsa, PortionSize.Normal);
      addTopping(Topping.Cheese, PortionSize.Normal);
      addTopping(Topping.Lettuce, PortionSize.Normal);
      addTopping(Topping.Guacamole, PortionSize.Normal);
      // Default size is Normal - still set a default size for VeggieBurrito
      size(Size.Normal);
    }

    @Override
    protected VeggieBurritoBuilder returnBuilder() {
      return this;
    }

    /**
     * Remove cheese from this veggie burrito.
     *
     * @return this builder
     */
    public VeggieBurritoBuilder noCheese() {
      toppings.remove(Topping.Cheese);
      return this;
    }

    /**
     * Remove black beans from this veggie burrito.
     *
     * @return this builder
     */
    public VeggieBurritoBuilder noBlackBeans() {
      proteins.remove(Protein.BlackBeans);
      return this;
    }

    /**
     * Remove medium salsa from this veggie burrito.
     *
     * @return this builder
     */
    public VeggieBurritoBuilder noMediumSalsa() {
      toppings.remove(Topping.MediumSalsa);
      return this;
    }

    /**
     * Remove lettuce from this veggie burrito.
     *
     * @return this builder
     */
    public VeggieBurritoBuilder noLettuce() {
      toppings.remove(Topping.Lettuce);
      return this;
    }

    /**
     * Remove guacamole from this veggie burrito.
     *
     * @return this builder
     */
    public VeggieBurritoBuilder noGuacamole() {
      toppings.remove(Topping.Guacamole);
      return this;
    }

    @Override
    public ObservableBurrito build() {
      if (size == null) {
        throw new IllegalStateException("Burrito size must be set");
      }
      return new VeggieBurrito(size, proteins, toppings);
    }
  }
}