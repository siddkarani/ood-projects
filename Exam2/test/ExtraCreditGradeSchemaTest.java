import initial.ExtraCreditGradeSchema;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.HashMap;
import java.util.Map;

/**
 * Comprehensive tests for ExtraCreditGradeSchema to verify extra credit functionality
 * and backward compatibility with regular grade schema operations.
 */
public class ExtraCreditGradeSchemaTest {

  private ExtraCreditGradeSchema schema;
  private static final double DELTA = 0.001; // For floating point comparisons

  @Before
  public void setUp() {
    schema = new ExtraCreditGradeSchema();
  }

  // ========== Basic Functionality Tests ==========

  @Test
  public void testAddRegularItem() {
    schema.addGradeableItem("Assignment 1", 15.0);
    assertEquals(15.0, schema.getWeight("Assignment 1"), DELTA);
  }

  @Test
  public void testAddExtraCreditItem() {
    schema.addGradeableItem("Extra Credit 1", 10.0);
    assertEquals(10.0, schema.getWeight("Extra Credit 1"), DELTA);
  }

  @Test
  public void testMixedItems() {
    schema.addGradeableItem("Assignment 1", 20.0);
    schema.addGradeableItem("Extra Credit", 5.0);
    schema.addGradeableItem("Exam", 30.0);

    assertEquals(20.0, schema.getWeight("Assignment 1"), DELTA);
    assertEquals(5.0, schema.getWeight("Extra Credit"), DELTA);
    assertEquals(30.0, schema.getWeight("Exam"), DELTA);
  }

  // ========== Sum of Weights Tests ==========

  @Test
  public void testSumOfWeightsRegularOnly() {
    schema.addGradeableItem("Assignment 1", 15.0);
    schema.addGradeableItem("Assignment 2", 20.0);
    schema.addGradeableItem("Exam", 25.0);

    assertEquals(60.0, schema.getSumOfWeights(), DELTA);
  }

  @Test
  public void testSumOfWeightsExtraCreditOnly() {
    schema.addGradeableItem("Extra Credit 1", 5.0);
    schema.addGradeableItem("Extra Credit 2", 10.0);

    assertEquals(0.0, schema.getSumOfWeights(), DELTA);
  }

  @Test
  public void testSumOfWeightsMixed() {
    schema.addGradeableItem("Assignment 1", 15.0);
    schema.addGradeableItem("Extra Credit 1", 5.0);
    schema.addGradeableItem("Exam", 25.0);
    schema.addGradeableItem("Extra Credit 2", 10.0);

    // Should only count Assignment 1 (15) + Exam (25) = 40
    assertEquals(40.0, schema.getSumOfWeights(), DELTA);
  }

  @Test
  public void testSumOfWeightsEmpty() {
    assertEquals(0.0, schema.getSumOfWeights(), DELTA);
  }

  // ========== Weighted Total Tests ==========

  @Test
  public void testWeightedTotalRegularOnly() {
    schema.addGradeableItem("Assignment 1", 20.0);
    schema.addGradeableItem("Exam", 30.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 80.0); // 80% score
    scores.put("Exam", 90.0); // 90% score

    // Expected: (20 * 80 + 30 * 90) / 100 = (1600 + 2700) / 100 = 43.0
    assertEquals(43.0, schema.getWeightedTotal(scores), DELTA);
  }

  @Test
  public void testWeightedTotalExtraCreditOnly() {
    schema.addGradeableItem("Extra Credit 1", 5.0);
    schema.addGradeableItem("Extra Credit 2", 10.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Extra Credit 1", 100.0);
    scores.put("Extra Credit 2", 80.0);

    // Expected: (5 * 100 + 10 * 80) / 100 = (500 + 800) / 100 = 13.0
    assertEquals(13.0, schema.getWeightedTotal(scores), DELTA);
  }

  @Test
  public void testWeightedTotalMixed() {
    schema.addGradeableItem("Assignment 1", 15.0);
    schema.addGradeableItem("Assignment 2", 15.0);
    schema.addGradeableItem("Exam", 25.0);
    schema.addGradeableItem("Extra Credit 1", 5.0);
    schema.addGradeableItem("Extra Credit 2", 10.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 50.0);
    scores.put("Assignment 2", 50.0);
    scores.put("Exam", 50.0);
    scores.put("Extra Credit 1", 50.0);
    scores.put("Extra Credit 2", 50.0);

    // Expected: (15*50 + 15*50 + 25*50 + 5*50 + 10*50) / 100 = 3500 / 100 = 35.0
    assertEquals(35.0, schema.getWeightedTotal(scores), DELTA);
  }

  // ========== Example from Problem Statement ==========

  @Test
  public void testProblemStatementExample() {
    // Set up the exact example from the problem
    schema.addGradeableItem("Assignment 1", 15.0);
    schema.addGradeableItem("Assignment 2", 15.0);
    schema.addGradeableItem("Assignment 3", 15.0);
    schema.addGradeableItem("Exam 1", 15.0);
    schema.addGradeableItem("Exam 2", 25.0);
    schema.addGradeableItem("Extra Credit 1", 5.0);
    schema.addGradeableItem("Extra Credit 2", 10.0);

    // Verify sum of weights (should be 85, not including extra credit)
    assertEquals(85.0, schema.getSumOfWeights(), DELTA);

    // Student scores 50% on everything
    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 50.0);
    scores.put("Assignment 2", 50.0);
    scores.put("Assignment 3", 50.0);
    scores.put("Exam 1", 50.0);
    scores.put("Exam 2", 50.0);
    scores.put("Extra Credit 1", 50.0);
    scores.put("Extra Credit 2", 50.0);

    // Expected weighted total: 50.0 (as stated in problem)
    // Regular: (15+15+15+15+25) * 0.5 = 85 * 0.5 = 42.5
    // Extra: (5+10) * 0.5 = 15 * 0.5 = 7.5
    // Total: 42.5 + 7.5 = 50.0
    assertEquals(50.0, schema.getWeightedTotal(scores), DELTA);
  }

  // ========== Edge Cases for Extra Credit Detection ==========

  @Test
  public void testExtraCreditCaseSensitive() {
    schema.addGradeableItem("extra credit", 5.0); // lowercase - should be regular
    schema.addGradeableItem("EXTRA CREDIT", 5.0); // uppercase - should be regular (no "Extra")
    schema.addGradeableItem("Extra Credit", 5.0); // proper case - should be extra credit
    schema.addGradeableItem("ExtraCredit", 5.0);  // no space - should be extra credit

    // Only items containing "Extra" (case sensitive) should be excluded from sum
    // "extra credit" and "EXTRA CREDIT" both count as regular (10.0 total)
    assertEquals(10.0, schema.getSumOfWeights(), DELTA);
  }

  @Test
  public void testExtraInMiddleOfName() {
    schema.addGradeableItem("Assignment Extra Problem", 10.0);
    schema.addGradeableItem("Regular Assignment", 15.0);

    // "Assignment Extra Problem" contains "Extra" so should be extra credit
    assertEquals(15.0, schema.getSumOfWeights(), DELTA);
  }

  @Test
  public void testExtraAtEnd() {
    schema.addGradeableItem("Homework Extra", 10.0);
    schema.addGradeableItem("Regular Homework", 20.0);

    assertEquals(20.0, schema.getSumOfWeights(), DELTA);
  }

  // ========== Error Handling Tests ==========

  @Test(expected = IllegalArgumentException.class)
  public void testAddNullName() {
    schema.addGradeableItem(null, 10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDuplicateName() {
    schema.addGradeableItem("Assignment 1", 10.0);
    schema.addGradeableItem("Assignment 1", 15.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNegativeWeight() {
    schema.addGradeableItem("Assignment 1", -5.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddWeightOver100() {
    schema.addGradeableItem("Assignment 1", 105.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWeightNullName() {
    schema.getWeight(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWeightNonexistentItem() {
    schema.addGradeableItem("Assignment 1", 10.0);
    schema.getWeight("Assignment 2");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeightedTotalNullScores() {
    schema.addGradeableItem("Assignment 1", 10.0);
    schema.getWeightedTotal(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeightedTotalMissingScore() {
    schema.addGradeableItem("Assignment 1", 10.0);
    schema.addGradeableItem("Assignment 2", 15.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 80.0);
    // Missing Assignment 2 score

    schema.getWeightedTotal(scores);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeightedTotalMissingExtraCreditScore() {
    schema.addGradeableItem("Assignment 1", 10.0);
    schema.addGradeableItem("Extra Credit", 5.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 80.0);
    // Missing Extra Credit score - should still throw exception

    schema.getWeightedTotal(scores);
  }

  // ========== Boundary Value Tests ==========

  @Test
  public void testZeroWeights() {
    schema.addGradeableItem("Assignment 1", 0.0);
    schema.addGradeableItem("Extra Credit", 0.0);

    assertEquals(0.0, schema.getSumOfWeights(), DELTA);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 100.0);
    scores.put("Extra Credit", 100.0);

    assertEquals(0.0, schema.getWeightedTotal(scores), DELTA);
  }

  @Test
  public void testMaxWeights() {
    schema.addGradeableItem("Assignment 1", 100.0);
    schema.addGradeableItem("Extra Credit", 100.0);

    assertEquals(100.0, schema.getSumOfWeights(), DELTA);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 50.0);
    scores.put("Extra Credit", 75.0);

    // (100 * 50 + 100 * 75) / 100 = 12500 / 100 = 125.0
    assertEquals(125.0, schema.getWeightedTotal(scores), DELTA);
  }

  @Test
  public void testZeroScores() {
    schema.addGradeableItem("Assignment 1", 20.0);
    schema.addGradeableItem("Extra Credit", 10.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 0.0);
    scores.put("Extra Credit", 0.0);

    assertEquals(0.0, schema.getWeightedTotal(scores), DELTA);
  }

  @Test
  public void testMaxScores() {
    schema.addGradeableItem("Assignment 1", 20.0);
    schema.addGradeableItem("Extra Credit", 10.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 100.0);
    scores.put("Extra Credit", 100.0);

    // (20 * 100 + 10 * 100) / 100 = 3000 / 100 = 30.0
    assertEquals(30.0, schema.getWeightedTotal(scores), DELTA);
  }

  // ========== Current Numeric Grade Calculation Test ==========

  @Test
  public void testCurrentNumericGradeCalculation() {
    // This tests the typical use case: weightedTotal / sumOfWeights
    schema.addGradeableItem("Assignment 1", 25.0);
    schema.addGradeableItem("Assignment 2", 25.0);
    schema.addGradeableItem("Extra Credit", 10.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 80.0);
    scores.put("Assignment 2", 90.0);
    scores.put("Extra Credit", 100.0);

    double weightedTotal = schema.getWeightedTotal(scores);
    double sumOfWeights = schema.getSumOfWeights();

    // weightedTotal = (25*80 + 25*90 + 10*100)/100 = (2000+2250+1000)/100 = 52.5
    // sumOfWeights = 25 + 25 = 50 (extra credit not counted)
    // Current grade = 52.5 / 50 = 1.05 (105%)

    assertEquals(52.5, weightedTotal, DELTA);
    assertEquals(50.0, sumOfWeights, DELTA);
    assertEquals(1.05, weightedTotal / sumOfWeights, DELTA);
  }
}