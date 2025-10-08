package initial;

import java.util.Map;

/**
 * This interface represents all operations for a grade schema. A grade schema
 * is a description of all graded items in a course, along with the weight
 * as a percentage in the course.
 */

public interface GradeSchema {
  /**
   * Adds a new gradable item into this grading schema.
   *
   * @param name   the unique name of the gradable item to be added
   * @param weight the weight of the gradable item, between 0 and 100%
   * @throws IllegalArgumentException
   *         if the name of the item is null, if an item with the specified name already exists in the schema,
   *         or if the specified weight is not between 0 and 100
   */
  void addGradeableItem(String name, double weight) throws IllegalArgumentException;
  /**
   * Get the weight of an existing gradable item in this gradable schema.
   *
   * @param name the name of this existing item
   * @return the weight of this item
   * @throws IllegalArgumentException
   *         if the argument is null, or if no item with the specified name exists
   */
  double getWeight(String name) throws IllegalArgumentException;
  /**
   * Computes and returns the weighted total grade of a student based on
   * this grading schema. The weighted total grade of the student is computed
   * by the sum of the products of the percentage score on an item and its weight.
   * @param score
   *        the scores by a student, as a map of (name, percentage-score).
   *        The percentage scores are numbers between 0 and 100
   * @return the current total grade as a number between 0 and 100.
   *         this may not represent the final grade of the student if the weights
   *         of all items do not add to 100
   * @throws IllegalArgumentException
   *         if the argument is null, or there is no score for an item that exists in the grading schema
   */
  double getWeightedTotal(Map<String,Double> score) throws IllegalArgumentException;
  /**
   * Get the sum of all weights in this schema.
   * It is possible that this does not add up to 100
   * @return the sum of all weights in this schema
   */
  double getSumOfWeights();
}
