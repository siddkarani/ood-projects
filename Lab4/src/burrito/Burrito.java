package burrito;

import betterburrito.ObservableBurrito;

/**
 * This interface represents all the operations offered by a burrito.
 */
public interface Burrito extends ObservableBurrito {
  /**
   * Add the specified protein in the specified portion size to this burrito.
   *
   * @param p    the protein to be added
   * @param size the portion size of this protein
   */
  void addProtein(Protein p, PortionSize size);

  /**
   * Add a specific topping in the specified size to this burrito.
   *
   * @param t    the topping to be added
   * @param size the portion size of this topping
   */
  void addTopping(Topping t, PortionSize size);

  /**
   * Set the size of this burrito.
   *
   * @param size the size of this burrito
   */
  void setSize(Size size);
}