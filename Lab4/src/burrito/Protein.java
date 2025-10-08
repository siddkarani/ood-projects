package burrito;

/**
 * This enumerated type represents a protein that can be added to a burrito.
 */
public enum Protein {
  Chicken("Chicken", 2.00),
  Carnitas("Carnitas", 2.00),
  Fish("Fish", 2.5),
  Tofu("Tofu", 1.75),
  BlackBeans("Black Beans", 1.5);

  private final String descriptor;
  private final double cost;

  public String getDescriptor() {
    return this.descriptor;
  }

  public double getCost() {
    return this.cost;
  }

  Protein(String d, double cost) {
    this.descriptor = d;
    this.cost = cost;
  }
}
