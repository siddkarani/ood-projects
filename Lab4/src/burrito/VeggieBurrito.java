package burrito;

/**
 * This class represents a veggie burrito. A veggie burrito has black beans,
 * medium salsa, cheese, lettuce, and guacamole, all in the regular portions.
 */
public class VeggieBurrito extends CustomBurrito {

  /**
   * Create a veggie burrito using the specified size.
   * @param size the size of this burrito
   */
  public VeggieBurrito(Size size) {
    super(size);

    this.addProtein(Protein.BlackBeans, PortionSize.Normal);
    this.addTopping(Topping.MediumSalsa, PortionSize.Normal);
    this.addTopping(Topping.Cheese, PortionSize.Normal);
    this.addTopping(Topping.Lettuce, PortionSize.Normal);
    this.addTopping(Topping.Guacamole, PortionSize.Normal);
  }
}
