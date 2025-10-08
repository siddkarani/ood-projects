package burrito;

/**
 * This enumerated type represents the size of a burrito.
 */
public enum Size {

  Normal(1.0),
  Jumbo(1.2);

  private final double cost;

  public double getBaseCost() {
    return cost;
  }

  Size(double cost) {
    this.cost = cost;
  }

}
