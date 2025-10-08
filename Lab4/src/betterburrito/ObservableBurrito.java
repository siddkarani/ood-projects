package betterburrito;

import burrito.PortionSize;
import burrito.Protein;
import burrito.Topping;

/**
 * This interface represents all the non-mutating operations offered by a burrito.
 */
public interface ObservableBurrito {
  /**
   * Calculate and return the cost of this burrito using its ingredients.
   *
   * @return the cost of this burrito in USD, MM.XX format
   */
  double cost();

  /**
   * Determines if the specified topping is on this burrito and if so, return its portion.
   *
   * @param name the name of the topping
   * @return the portion of this topping on this burrito, or null if the given topping is not
   *          on this burrito
   */
  PortionSize hasTopping(Topping name);

  /**
   * Determines if the specified protein is on this burrito and if so, return its portion.
   *
   * @param name the name of the protein
   * @return the portion of this protein on this burrito, or null if the given protein is not
   *          on this pizza
   */
  PortionSize hasProtein(Protein name);
}