import initial.FlexibleGradeSchema;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.HashMap;
import java.util.Map;

/**
 * Comprehensive tests for FlexibleGradeSchema to verify both individual item
 * functionality and group functionality, including the specific CS 3500 example.
 */
public class FlexibleGradeSchemaTest {

  private FlexibleGradeSchema schema;
  private static final double DELTA = 0.001; // For floating point comparisons

  @Before
  public void setUp() {
    schema = new FlexibleGradeSchema();
  }

  // ========== CS 3500 Specific Test (Required by Problem Statement) ==========

  @Test
  public void testCS3500GradingSchema() {
    // Create the CS 3500 schema as specified:
    // - Assignment 1 with weight of 5%
    // - 5 other assignments collectively named "Group" with combined weight of 55%
    // - Exam 1 worth 15%
    // Note: weights do not add to 100

    schema.addGradeableItem("Assignment 1", 5.0);
    schema.addGradeableGroup("Group", 5, 55.0);
    schema.addGradeableItem("Exam 1", 15.0);

    // Verify the schema was successfully created

    // Check that Assignment 1 exists with correct weight
    assertTrue("Assignment 1 should exist", schema.hasItem("Assignment 1"));
    assertEquals("Assignment 1 should have weight 5.0", 5.0, schema.getWeight("Assignment 1"), DELTA);

    // Check that all 5 group items exist with correct individual weights
    for (int i = 1; i <= 5; i++) {
      String groupItemName = "Group " + i;
      assertTrue("Group item " + i + " should exist", schema.hasItem(groupItemName));
      assertEquals("Each group item should have weight 11.0 (55/5)",
              11.0, schema.getWeight(groupItemName), DELTA);
    }

    // Check that Exam 1 exists with correct weight
    assertTrue("Exam 1 should exist", schema.hasItem("Exam 1"));
    assertEquals("Exam 1 should have weight 15.0", 15.0, schema.getWeight("Exam 1"), DELTA);

    // Verify total number of items (1 + 5 + 1 = 7)
    assertEquals("Should have 7 total items", 7, schema.getItemCount());

    // Verify sum of weights (5 + 55 + 15 = 75, not 100 as specified)
    assertEquals("Sum of weights should be 75.0", 75.0, schema.getSumOfWeights(), DELTA);

    // Test that the schema functions correctly for grade calculations
    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Assignment 1", 90.0);
    scores.put("Group 1", 85.0);
    scores.put("Group 2", 80.0);
    scores.put("Group 3", 88.0);
    scores.put("Group 4", 92.0);
    scores.put("Group 5", 87.0);
    scores.put("Exam 1", 85.0);

    // Calculate expected weighted total
    // Assignment 1: 5 * 90 = 450
    // Group items: 11 * (85 + 80 + 88 + 92 + 87) = 11 * 432 = 4752
    // Exam 1: 15 * 85 = 1275
    // Total: (450 + 4752 + 1275) / 100 = 6477 / 100 = 64.77
    double expectedWeightedTotal = (5.0 * 90.0 + 11.0 * (85.0 + 80.0 + 88.0 + 92.0 + 87.0) + 15.0 * 85.0) / 100.0;
    assertEquals("Weighted total should be calculated correctly",
            expectedWeightedTotal, schema.getWeightedTotal(scores), DELTA);
  }

  // ========== Individual Item Functionality Tests ==========

  @Test
  public void testAddIndividualItem() {
    schema.addGradeableItem("Quiz 1", 10.0);

    assertTrue("Quiz 1 should exist", schema.hasItem("Quiz 1"));
    assertEquals("Quiz 1 should have weight 10.0", 10.0, schema.getWeight("Quiz 1"), DELTA);
    assertEquals("Should have 1 item", 1, schema.getItemCount());
    assertEquals("Sum of weights should be 10.0", 10.0, schema.getSumOfWeights(), DELTA);
  }

  @Test
  public void testMultipleIndividualItems() {
    schema.addGradeableItem("Quiz 1", 10.0);
    schema.addGradeableItem("Midterm", 25.0);
    schema.addGradeableItem("Final", 30.0);

    assertEquals("Should have 3 items", 3, schema.getItemCount());
    assertEquals("Sum of weights should be 65.0", 65.0, schema.getSumOfWeights(), DELTA);

    assertEquals(10.0, schema.getWeight("Quiz 1"), DELTA);
    assertEquals(25.0, schema.getWeight("Midterm"), DELTA);
    assertEquals(30.0, schema.getWeight("Final"), DELTA);
  }

  // ========== Group Functionality Tests ==========

  @Test
  public void testAddSingleGroup() {
    schema.addGradeableGroup("Homework", 4, 40.0);

    assertEquals("Should have 4 items", 4, schema.getItemCount());
    assertEquals("Sum of weights should be 40.0", 40.0, schema.getSumOfWeights(), DELTA);

    // Each homework should have weight 10.0 (40/4)
    for (int i = 1; i <= 4; i++) {
      String homeworkName = "Homework " + i;
      assertTrue(homeworkName + " should exist", schema.hasItem(homeworkName));
      assertEquals(homeworkName + " should have weight 10.0",
              10.0, schema.getWeight(homeworkName), DELTA);
    }
  }

  @Test
  public void testAddMultipleGroups() {
    schema.addGradeableGroup("Lab", 3, 30.0);
    schema.addGradeableGroup("Project", 2, 20.0);

    assertEquals("Should have 5 items total", 5, schema.getItemCount());
    assertEquals("Sum of weights should be 50.0", 50.0, schema.getSumOfWeights(), DELTA);

    // Labs should each have weight 10.0 (30/3)
    for (int i = 1; i <= 3; i++) {
      assertEquals("Lab " + i + " should have weight 10.0",
              10.0, schema.getWeight("Lab " + i), DELTA);
    }

    // Projects should each have weight 10.0 (20/2)
    for (int i = 1; i <= 2; i++) {
      assertEquals("Project " + i + " should have weight 10.0",
              10.0, schema.getWeight("Project " + i), DELTA);
    }
  }

  @Test
  public void testMixedIndividualAndGroups() {
    schema.addGradeableItem("Participation", 5.0);
    schema.addGradeableGroup("Assignment", 6, 60.0);
    schema.addGradeableItem("Final Exam", 35.0);

    assertEquals("Should have 8 items total", 8, schema.getItemCount());
    assertEquals("Sum of weights should be 100.0", 100.0, schema.getSumOfWeights(), DELTA);

    assertEquals(5.0, schema.getWeight("Participation"), DELTA);
    assertEquals(35.0, schema.getWeight("Final Exam"), DELTA);

    // Each assignment should have weight 10.0 (60/6)
    for (int i = 1; i <= 6; i++) {
      assertEquals("Assignment " + i + " should have weight 10.0",
              10.0, schema.getWeight("Assignment " + i), DELTA);
    }
  }

  // ========== Edge Cases and Boundary Tests ==========

  @Test
  public void testGroupWithOneItem() {
    schema.addGradeableGroup("Solo", 1, 25.0);

    assertEquals("Should have 1 item", 1, schema.getItemCount());
    assertTrue("Solo 1 should exist", schema.hasItem("Solo 1"));
    assertEquals("Solo 1 should have weight 25.0", 25.0, schema.getWeight("Solo 1"), DELTA);
  }

  @Test
  public void testGroupWithUnevenDivision() {
    // 100 / 3 = 33.333...
    schema.addGradeableGroup("Test", 3, 100.0);

    double expectedWeight = 100.0 / 3.0;
    for (int i = 1; i <= 3; i++) {
      assertEquals("Test " + i + " should have weight 33.333...",
              expectedWeight, schema.getWeight("Test " + i), DELTA);
    }

    // Sum should still be approximately 100
    assertEquals("Sum should be approximately 100.0",
            100.0, schema.getSumOfWeights(), DELTA);
  }

  @Test
  public void testZeroWeights() {
    schema.addGradeableItem("Zero Item", 0.0);
    schema.addGradeableGroup("Zero Group", 2, 0.0);

    assertEquals("Should have 3 items", 3, schema.getItemCount());
    assertEquals("Sum should be 0.0", 0.0, schema.getSumOfWeights(), DELTA);

    assertEquals(0.0, schema.getWeight("Zero Item"), DELTA);
    assertEquals(0.0, schema.getWeight("Zero Group 1"), DELTA);
    assertEquals(0.0, schema.getWeight("Zero Group 2"), DELTA);
  }

  @Test
  public void testMaximumWeights() {
    schema.addGradeableItem("Max Item", 100.0);
    schema.addGradeableGroup("Max Group", 1, 100.0);

    assertEquals(100.0, schema.getWeight("Max Item"), DELTA);
    assertEquals(100.0, schema.getWeight("Max Group 1"), DELTA);
    assertEquals(200.0, schema.getSumOfWeights(), DELTA);
  }

  // ========== Weighted Total Calculation Tests ==========

  @Test
  public void testWeightedTotalWithGroups() {
    schema.addGradeableItem("Quiz", 20.0);
    schema.addGradeableGroup("Homework", 2, 30.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Quiz", 80.0);
    scores.put("Homework 1", 90.0);
    scores.put("Homework 2", 85.0);

    // Expected: (20*80 + 15*90 + 15*85) / 100 = (1600 + 1350 + 1275) / 100 = 42.25
    double expected = (20.0 * 80.0 + 15.0 * 90.0 + 15.0 * 85.0) / 100.0;
    assertEquals("Weighted total should be calculated correctly",
            expected, schema.getWeightedTotal(scores), DELTA);
  }

  @Test
  public void testComplexWeightedTotal() {
    schema.addGradeableGroup("Assignment", 8, 60.0); // Each worth 7.5%
    schema.addGradeableItem("Midterm", 20.0);
    schema.addGradeableItem("Final", 20.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    // All assignments score 90%
    for (int i = 1; i <= 8; i++) {
      scores.put("Assignment " + i, 90.0);
    }
    scores.put("Midterm", 85.0);
    scores.put("Final", 88.0);

    // Expected: (8 * 7.5 * 90 + 20 * 85 + 20 * 88) / 100 = (5400 + 1700 + 1760) / 100 = 88.6
    double expected = (8 * 7.5 * 90.0 + 20.0 * 85.0 + 20.0 * 88.0) / 100.0;
    assertEquals("Complex weighted total should be calculated correctly",
            expected, schema.getWeightedTotal(scores), DELTA);
  }

  // ========== Error Handling Tests ==========

  @Test(expected = IllegalArgumentException.class)
  public void testAddGroupNullName() {
    schema.addGradeableGroup(null, 3, 30.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddGroupZeroCount() {
    schema.addGradeableGroup("Test", 0, 30.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddGroupNegativeCount() {
    schema.addGradeableGroup("Test", -1, 30.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddGroupNegativeWeight() {
    schema.addGradeableGroup("Test", 3, -10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddGroupWeightOver100() {
    schema.addGradeableGroup("Test", 3, 150.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddGroupNameConflictWithExisting() {
    schema.addGradeableItem("Test 1", 10.0);
    schema.addGradeableGroup("Test", 3, 30.0); // Should conflict with "Test 1"
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddGroupNameConflictWithinGroup() {
    schema.addGradeableGroup("Assignment", 3, 30.0);
    schema.addGradeableItem("Assignment 2", 10.0); // Should conflict
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddIndividualItemNullName() {
    schema.addGradeableItem(null, 10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddIndividualItemDuplicateName() {
    schema.addGradeableItem("Test", 10.0);
    schema.addGradeableItem("Test", 15.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddIndividualItemNegativeWeight() {
    schema.addGradeableItem("Test", -5.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddIndividualItemWeightOver100() {
    schema.addGradeableItem("Test", 105.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWeightNullName() {
    schema.getWeight(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWeightNonexistentItem() {
    schema.addGradeableItem("Test", 10.0);
    schema.getWeight("Nonexistent");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeightedTotalNullScores() {
    schema.addGradeableItem("Test", 10.0);
    schema.getWeightedTotal(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeightedTotalMissingScore() {
    schema.addGradeableItem("Test 1", 10.0);
    schema.addGradeableGroup("Test Group", 2, 20.0);

    Map<String, Double> scores = new HashMap<String, Double>();
    scores.put("Test 1", 80.0);
    scores.put("Test Group 1", 85.0);
    // Missing "Test Group 2"

    schema.getWeightedTotal(scores);
  }

  // ========== Utility Method Tests ==========

  @Test
  public void testHasItemMethod() {
    assertFalse("Should not have nonexistent item", schema.hasItem("Nonexistent"));

    schema.addGradeableItem("Individual", 10.0);
    schema.addGradeableGroup("Group", 2, 20.0);

    assertTrue("Should have Individual", schema.hasItem("Individual"));
    assertTrue("Should have Group 1", schema.hasItem("Group 1"));
    assertTrue("Should have Group 2", schema.hasItem("Group 2"));
    assertFalse("Should not have Group 3", schema.hasItem("Group 3"));
  }

  @Test
  public void testGetItemCountMethod() {
    assertEquals("Empty schema should have 0 items", 0, schema.getItemCount());

    schema.addGradeableItem("Individual", 10.0);
    assertEquals("Should have 1 item", 1, schema.getItemCount());

    schema.addGradeableGroup("Group", 3, 30.0);
    assertEquals("Should have 4 items total", 4, schema.getItemCount());
  }

  // ========== Real-world Scenario Tests ==========

  @Test
  public void testFallSpringCS3500Scenario() {
    // Fall/Spring: 8 assignments worth 60% total (7.5% each)
    schema.addGradeableGroup("Assignment", 8, 60.0);
    schema.addGradeableItem("Midterm", 20.0);
    schema.addGradeableItem("Final", 20.0);

    assertEquals("Should have 10 items", 10, schema.getItemCount());
    assertEquals("Sum should be 100.0", 100.0, schema.getSumOfWeights(), DELTA);

    for (int i = 1; i <= 8; i++) {
      assertEquals("Assignment " + i + " should have weight 7.5",
              7.5, schema.getWeight("Assignment " + i), DELTA);
    }
  }

  @Test
  public void testSummer1CS3500Scenario() {
    // Summer 1: 7 assignments worth 60% total (8.57% each approximately)
    schema.addGradeableGroup("Assignment", 7, 60.0);
    schema.addGradeableItem("Midterm", 20.0);
    schema.addGradeableItem("Final", 20.0);

    assertEquals("Should have 9 items", 9, schema.getItemCount());
    assertEquals("Sum should be 100.0", 100.0, schema.getSumOfWeights(), DELTA);

    double expectedWeight = 60.0 / 7.0; // approximately 8.571428...
    for (int i = 1; i <= 7; i++) {
      assertEquals("Assignment " + i + " should have weight ~8.57",
              expectedWeight, schema.getWeight("Assignment " + i), DELTA);
    }
  }
}