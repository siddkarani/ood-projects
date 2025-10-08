package betterburrito;

import burrito.PortionSize;
import burrito.Protein;
import burrito.Size;
import burrito.Topping;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract builder class for creating burritos.
 *
 * @param <T> the type of builder
 */
public abstract class BurritoBuilder<T extends BurritoBuilder<T>> {
  protected Size size;
  protected final Map<Protein, PortionSize> proteins = new HashMap<>();
  protected final Map<Topping, PortionSize> toppings = new HashMap<>();

  /**
   * Set the size of the burrito.
   *
   * @param size the size of the burrito
   * @return this builder
   */
  public T size(Size size) {
    this.size = size;
    return returnBuilder();
  }

  /**
   * Add a protein to the burrito.
   *
   * @param protein the protein to add
   * @param size    the portion size of the protein
   * @return this builder
   */
  public T addProtein(Protein protein, PortionSize size) {
    this.proteins.put(protein, size);
    return returnBuilder();
  }

  /**
   * Add a topping to the burrito.
   *
   * @param topping the topping to add
   * @param size    the portion size of the topping
   * @return this builder
   */
  public T addTopping(Topping topping, PortionSize size) {
    this.toppings.put(topping, size);
    return returnBuilder();
  }

  /**
   * Returns the appropriate builder instance for method chaining.
   *
   * @return the builder instance
   */
  protected abstract T returnBuilder();

  /**
   * Build the burrito.
   *
   * @return a new burrito
   */
  public abstract ObservableBurrito build();
}