package burrito;

/**
 * This enumerated type represents the portion size of a protein, sauce or topping.
 */
public enum PortionSize {
  Less(0.9),
  Normal(1.0),
  Extra(1.25);

  private final double costMultipler;

  public double getCostMultipler() {
    return this.costMultipler;
  }

  PortionSize(double multipler) {
    this.costMultipler = multipler;
  }
}
