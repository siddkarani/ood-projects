package initial;

import java.util.List;
import java.util.Map;

/**
 * Represents a course with students, graded work, and grade management capabilities.
 *
 * This interface provides comprehensive functionality for managing a course:
 * - Student enrollment (register/withdraw)
 * - Adding graded work items as the semester progresses
 * - Entering and managing student scores
 * - Calculating current grades and statistics
 *
 * DESIGN ASSUMPTIONS:
 * - Students are identified by unique string IDs
 * - Graded work items have unique names within the course
 * - Scores are percentages between 0.0 and 100.0
 * - Current grade is calculated as weighted average of completed work only
 * - All operations use defensive programming with appropriate exceptions
 */
public interface Course {

  // ========== STUDENT MANAGEMENT ==========

  /**
   * Registers a new student in the course.
   * The student will initially have no scores for any existing graded work.
   *
   * @param studentId unique identifier for the student
   * @param studentName display name for the student
   * @throws IllegalArgumentException if studentId is null/empty, already registered,
   *                                  or if studentName is null/empty
   */
  void registerStudent(String studentId, String studentName) throws IllegalArgumentException;

  /**
   * Withdraws a student from the course.
   * All of the student's scores are permanently removed.
   *
   * @param studentId the student to withdraw
   * @throws IllegalArgumentException if studentId is null/empty or not registered
   */
  void withdrawStudent(String studentId) throws IllegalArgumentException;

  /**
   * Checks if a student is currently registered in the course.
   *
   * @param studentId the student ID to check
   * @return true if the student is registered, false otherwise
   * @throws IllegalArgumentException if studentId is null or empty
   */
  boolean isStudentRegistered(String studentId) throws IllegalArgumentException;

  /**
   * Gets the display name for a registered student.
   *
   * @param studentId the student ID
   * @return the student's display name
   * @throws IllegalArgumentException if studentId is null/empty or not registered
   */
  String getStudentName(String studentId) throws IllegalArgumentException;

  /**
   * Gets a list of all currently registered student IDs.
   *
   * @return list of student IDs (copy - modifications won't affect course)
   */
  List<String> getRegisteredStudents();

  /**
   * Gets the current number of registered students.
   *
   * @return number of students (>= 0)
   */
  int getStudentCount();

  // ========== GRADED WORK MANAGEMENT ==========

  /**
   * Adds a new graded work item to the course.
   * All currently registered students will have no score for this item initially.
   *
   * @param workName unique name for the graded work
   * @param weight percentage weight in final grade (0.0 to 100.0)
   * @throws IllegalArgumentException if workName is null/empty/already exists,
   *                                  or if weight is not between 0.0 and 100.0
   */
  void addGradedWork(String workName, double weight) throws IllegalArgumentException;

  /**
   * Removes a graded work item from the course.
   * All student scores for this item are permanently deleted.
   *
   * @param workName the graded work to remove
   * @throws IllegalArgumentException if workName is null/empty or doesn't exist
   */
  void removeGradedWork(String workName) throws IllegalArgumentException;

  /**
   * Checks if a graded work item exists in the course.
   *
   * @param workName the name to check
   * @return true if the work exists, false otherwise
   * @throws IllegalArgumentException if workName is null or empty
   */
  boolean hasGradedWork(String workName) throws IllegalArgumentException;

  /**
   * Gets the weight of a specific graded work item.
   *
   * @param workName the name of the graded work
   * @return the percentage weight
   * @throws IllegalArgumentException if workName is null/empty or doesn't exist
   */
  double getWorkWeight(String workName) throws IllegalArgumentException;

  /**
   * Gets a list of all graded work names in the course.
   *
   * @return list of work names (copy - modifications won't affect course)
   */
  List<String> getGradedWorkList();

  /**
   * Gets the total weight of all graded work in the course.
   * This may be less than 100.0 if the course is incomplete.
   *
   * @return sum of all graded work weights
   */
  double getTotalWeight();

  // ========== SCORE MANAGEMENT ==========

  /**
   * Enters a score for a student on specific graded work.
   * Overwrites any existing score for the same student and work.
   *
   * @param studentId the student ID
   * @param workName the graded work name
   * @param score the percentage score (0.0 to 100.0)
   * @throws IllegalArgumentException if studentId is null/empty/not registered,
   *                                  workName is null/empty/doesn't exist,
   *                                  or score is not between 0.0 and 100.0
   */
  void enterScore(String studentId, String workName, double score) throws IllegalArgumentException;

  /**
   * Gets the score for a specific student and graded work.
   *
   * @param studentId the student ID
   * @param workName the graded work name
   * @return the percentage score
   * @throws IllegalArgumentException if studentId is null/empty/not registered,
   *                                  or workName is null/empty/doesn't exist
   * @throws IllegalStateException if no score has been entered
   */
  double getScore(String studentId, String workName) throws IllegalArgumentException, IllegalStateException;

  /**
   * Checks if a score exists for a student and graded work.
   *
   * @param studentId the student ID
   * @param workName the graded work name
   * @return true if a score exists, false otherwise
   * @throws IllegalArgumentException if studentId is null/empty/not registered,
   *                                  or workName is null/empty/doesn't exist
   */
  boolean hasScore(String studentId, String workName) throws IllegalArgumentException;

  /**
   * Gets all scores for a specific student.
   * Only includes graded work for which scores have been entered.
   *
   * @param studentId the student ID
   * @return map of work names to scores (copy - modifications won't affect course)
   * @throws IllegalArgumentException if studentId is null/empty or not registered
   */
  Map<String, Double> getStudentScores(String studentId) throws IllegalArgumentException;

  /**
   * Removes a specific score for a student.
   *
   * @param studentId the student ID
   * @param workName the graded work name
   * @throws IllegalArgumentException if studentId is null/empty/not registered,
   *                                  or workName is null/empty/doesn't exist
   * @throws IllegalStateException if no score exists to remove
   */
  void removeScore(String studentId, String workName) throws IllegalArgumentException, IllegalStateException;

  // ========== GRADE CALCULATION ==========

  /**
   * Calculates the current numeric grade for a student.
   * Computed as weighted total of scored work divided by sum of weights for scored work.
   * If student has no scores, returns 0.0.
   * Only includes graded work for which the student has scores.
   *
   * @param studentId the student ID
   * @return current grade as percentage (0.0 to potentially > 100.0)
   * @throws IllegalArgumentException if studentId is null/empty or not registered
   */
  double getCurrentGrade(String studentId) throws IllegalArgumentException;

  /**
   * Gets the weighted total of a student's scores (numerator for grade calculation).
   * Only includes graded work for which the student has scores.
   *
   * @param studentId the student ID
   * @return weighted total of student's scores
   * @throws IllegalArgumentException if studentId is null/empty or not registered
   */
  double getWeightedTotal(String studentId) throws IllegalArgumentException;

  /**
   * Gets the sum of weights for graded work where student has scores.
   * This is the denominator for current grade calculation.
   *
   * @param studentId the student ID
   * @return sum of weights for work with scores
   * @throws IllegalArgumentException if studentId is null/empty or not registered
   */
  double getScoredWeight(String studentId) throws IllegalArgumentException;

  // ========== REPORTING ==========

  /**
   * Gets basic statistics for a specific graded work item across all students.
   * Only includes students who have scores for this work.
   * Returns array: [minScore, maxScore, averageScore, studentCount]
   *
   * @param workName the graded work name
   * @return array with [min, max, average, count]
   * @throws IllegalArgumentException if workName is null/empty or doesn't exist
   * @throws IllegalStateException if no students have scores for this work
   */
  double[] getWorkStatistics(String workName) throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets a summary of which students are missing scores for which graded work.
   *
   * @return map from student IDs to lists of missing work names
   */
  Map<String, List<String>> getMissingScores();
}

// No helper class needed - interface is self-contained