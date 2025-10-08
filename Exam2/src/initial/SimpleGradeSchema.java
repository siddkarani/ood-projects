package initial;

import java.util.HashMap;
import java.util.Map;

/**
 * A straightforward implementation of a {@link GradeSchema} that uses a
 * tabular representation of the items and weights.
 * Each assignment has a weight (percentage between 0.0 and 100.0)
 * and each assignment contributes to the grade.
 */
public class SimpleGradeSchema implements GradeSchema {
  private Map<String, Double> weights;

  public SimpleGradeSchema() { weights = new HashMap<String, Double>(); }

  @Override
  public void addGradeableItem(String name, double weight) throws IllegalArgumentException {
    if (name==null) {
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
    if (name==null) {
      throw new IllegalArgumentException("Name of item cannot be null");
    }
    if (weights.containsKey(name)) {
      return weights.get(name);
    }
    throw new IllegalArgumentException("Schema contains no item with name: " + name);
  }

  @Override
  public double getWeightedTotal(Map<String, Double> score) throws IllegalArgumentException { //\label{line:getWeightedTotal}*/
    double weightedTotal = 0;

    if (score==null) {
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
}
