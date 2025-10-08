package betterburrito;

import burrito.PortionSize;
import burrito.Protein;
import burrito.Size;
import burrito.Topping;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an immutable custom burrito that can have an arbitrary number
 * of proteins and toppings, both of arbitrary portion sizes.
 */
public class CustomBurrito implements ObservableBurrito {
  private final Size size;
  private final Map<Protein, PortionSize> proteins;
  private final Map<Topping, PortionSize> toppings;

  /**
   * Create a custom burrito with the given size, proteins, and toppings.
   * Protected constructor used by the builder.
   *
   * @param size     the size of this burrito
   * @param proteins the proteins in this burrito
   * @param toppings the toppings in this burrito
   */
  protected CustomBurrito(Size size, Map<Protein, PortionSize> proteins,
                          Map<Topping,
          PortionSize> toppings) {
    this.size = size;
    this.proteins = new HashMap<>(proteins);
    this.toppings = new HashMap<>(toppings);
  }

  @Override
  public PortionSize hasTopping(Topping name) {
    return this.toppings.getOrDefault(name, null);
  }

  @Override
  public PortionSize hasProtein(Protein name) {
    return this.proteins.getOrDefault(name, null);
  }

  @Override
  public double cost() {
    double cost = 0.0;
    for (Map.Entry<Protein, PortionSize> item : this.proteins.entrySet()) {
      cost += item.getKey().getCost() * item.getValue().getCostMultipler();
    }

    for (Map.Entry<Topping, PortionSize> item : this.toppings.entrySet()) {
      cost += item.getKey().getCost() * item.getValue().getCostMultipler();
    }
    return cost + this.size.getBaseCost();
  }

  /**
   * Builder for custom burritos.
   */
  public static class CustomBurritoBuilder
          extends BurritoBuilder<CustomBurritoBuilder> {

    /**
     * Create a new CustomBurritoBuilder.
     * Note: We're NOT setting a default size anymore
     */
    public CustomBurritoBuilder() {
      // No default size - will require explicit setting
    }

    @Override
    protected CustomBurritoBuilder returnBuilder() {
      return this;
    }

    @Override
    public ObservableBurrito build() {
      if (size == null) {
        throw new IllegalStateException("Burrito size must be set");
      }
      return new CustomBurrito(size, proteins, toppings);
    }
  }
}