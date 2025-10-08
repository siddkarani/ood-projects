package initial;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of GradeSchema that supports extra credit assignments.
 * Any gradeable item containing "Extra" (case sensitive) is treated as extra credit.
 * Extra credit items contribute to the weighted total but not to the sum of weights.
 */
public class ExtraCreditGradeSchema implements GradeSchema {
  private Map<String, Double> weights;

  public ExtraCreditGradeSchema() {
    weights = new HashMap<String, Double>();
  }

  /**
   * Helper method to determine if an item is extra credit.
   * @param name the name of the gradeable item
   * @return true if the item contains "Extra" (case sensitive)
   */
  private boolean isExtraCredit(String name) {
    return name.contains("Extra");
  }

  @Override
  public void addGradeableItem(String name, double weight) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name of gradeable item cannot be null");
    }
    if (weights.containsKey(name)) {
      throw new IllegalArgumentException(
              "Item with name: " + name + " already exists in schema.");
    }
    if ((weight < 0) || (weight > 100)) {
      throw new IllegalArgumentException("Invalid weight, must be between 0 and 100)");
    }
    weights.put(name, weight);
  }

  @Override
  public double getWeight(String name) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name of item cannot be null");
    }
    if (weights.containsKey(name)) {
      return weights.get(name);
    }
    throw new IllegalArgumentException("Schema contains no item with name: " + name);
  }

  @Override
  public double getWeightedTotal(Map<String, Double> score) throws IllegalArgumentException {
    double weightedTotal = 0;

    if (score == null) {
      throw new IllegalArgumentException("The provided scores map is null");
    }

    for (Map.Entry<String, Double> item : weights.entrySet()) {
      if (!score.containsKey(item.getKey())) {
        throw new IllegalArgumentException(
                "No entry in student score for item " + item.getKey());
      }
      // Include ALL items (regular and extra credit) in weighted total calculation
      weightedTotal += item.getValue() * score.get(item.getKey());
    }

    /* Scores are percentages between 0 and 100, and so are item weights.
       So their products are between 0 and 10,000, and we need to divide by 100
       to get the products (and their final sum) to be a meaningful value. */
    return weightedTotal / 100.0;
  }

  @Override
  public double getSumOfWeights() {
    double totalWeight = 0;

    for (Map.Entry<String, Double> item : weights.entrySet()) {
      // Only include non-extra credit items in sum of weights
      if (!isExtraCredit(item.getKey())) {
        totalWeight += item.getValue();
      }
    }
    return totalWeight;
  }
}