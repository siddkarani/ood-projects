package burrito;

/**
 * This enumerated type represents a topping on the burrito.
 */
public enum Topping {
  Guacamole(2.00),
  Lettuce(0.5),
  Cheese(1.00),
  Corn(1.00),
  Onion(0.5),
  Cilantro(0.5),
  SourCream(1.00),
  Jalapeno(0.75),
  MildSalsa(1.00),
  MediumSalsa(1.00),
  HotSalsa(1.00),
  Queso(1.5);

  private final double cost;

  public double getCost() {
    return this.cost;
  }

  Topping(double cost) {
    this.cost = cost;
  }
}
