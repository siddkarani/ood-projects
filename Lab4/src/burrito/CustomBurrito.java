package burrito;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a custom burrito that can have an arbitrary number
 * of proteins and toppings, both of arbitrary portion sizes.
 */
public class CustomBurrito implements Burrito {
  protected Size size;
  protected final Map<Protein,PortionSize> proteins;
  protected final Map<Topping,PortionSize> toppings;

  /**
   * Create a custom burrito of the specified size.
   * @param size the size of this burrito
   */
  public CustomBurrito(Size size) {
    this.size = size;
    this.proteins = new HashMap<Protein,PortionSize>();
    this.toppings = new HashMap<Topping,PortionSize>();

  }

  @Override
  public void addProtein(Protein p, PortionSize size) {
    this.proteins.put(p, size);
  }

  @Override
  public void addTopping(Topping t, PortionSize size) {
    this.toppings.put(t, size);
  }

  @Override
  public void setSize(Size size) {
    this.size = size;
  }

  @Override
  public PortionSize hasTopping(Topping name) {
    return this.toppings.getOrDefault(name,null);
  }

  @Override
  public PortionSize hasProtein(Protein name) {
    return this.proteins.getOrDefault(name,null);
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


}
