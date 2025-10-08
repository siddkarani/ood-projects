package initial;

import java.util.HashMap;
import java.util.Map;

/**
 * A flexible implementation of GradeSchema that supports both individual items
 * and groups of equally-weighted items. This makes it easier for professors to
 * create grading schemas without manually calculating individual weights.
 */
public class FlexibleGradeSchema implements GradeSchema {
  private Map<String, Double> weights;

  public FlexibleGradeSchema() {
    weights = new HashMap<String, Double>();
  }

  /**
   * Adds a group of equally-weighted gradeable items with a combined weight.
   * Items will be named as groupName + " " + number (1-indexed).
   *
   * @param groupName the base name for all items in the group
   * @param count the number of items in the group
   * @param combinedWeight the total weight to be distributed equally among all items
   * @throws IllegalArgumentException if groupName is null, count <= 0,
   *         combinedWeight is not between 0 and 100, or if any generated name
   *         already exists in the schema
   */
  public void addGradeableGroup(String groupName, int count, double combinedWeight)
          throws IllegalArgumentException {
    if (groupName == null) {
      throw new IllegalArgumentException("Group name cannot be null");
    }
    if (count <= 0) {
      throw new IllegalArgumentException("Count must be positive");
    }
    if ((combinedWeight < 0) || (combinedWeight > 100)) {
      throw new IllegalArgumentException("Invalid combined weight, must be between 0 and 100");
    }

    // Check if any of the generated names would conflict
    for (int i = 1; i <= count; i++) {
      String itemName = groupName + " " + i;
      if (weights.containsKey(itemName)) {
        throw new IllegalArgumentException(
                "Item with name: " + itemName + " already exists in schema.");
      }
    }

    // Calculate individual weight for each item
    double individualWeight = combinedWeight / count;

    // Add all items in the group
    for (int i = 1; i <= count; i++) {
      String itemName = groupName + " " + i;
      weights.put(itemName, individualWeight);
    }
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
      totalWeight += item.getValue();
    }
    return totalWeight;
  }

  /**
   * Gets the number of items currently in the schema.
   * Useful for verification and testing.
   *
   * @return the total number of gradeable items in the schema
   */
  public int getItemCount() {
    return weights.size();
  }

  /**
   * Checks if a specific item exists in the schema.
   * Useful for verification and testing.
   *
   * @param name the name of the item to check
   * @return true if the item exists, false otherwise
   */
  public boolean hasItem(String name) {
    return weights.containsKey(name);
  }
}