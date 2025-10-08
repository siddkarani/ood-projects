import initial.Course;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * Tests for the Course interface design.
 * This verifies the interface structure, method signatures, and helper classes
 * without requiring any implementation.
 */
public class CourseTest {

  // ========== INTERFACE STRUCTURE TESTS ==========

  @Test
  public void testCourseIsInterface() {
    assertTrue("Course should be an interface", Course.class.isInterface());
    assertTrue("Course should be public", Modifier.isPublic(Course.class.getModifiers()));
  }

  @Test
  public void testCourseHasAllRequiredMethods() throws NoSuchMethodException {
    // Student management methods
    assertMethodExists("registerStudent", String.class, String.class);
    assertMethodExists("withdrawStudent", String.class);
    assertMethodExists("isStudentRegistered", String.class);
    assertMethodExists("getStudentName", String.class);
    assertMethodExists("getRegisteredStudents");
    assertMethodExists("getStudentCount");

    // Graded work management methods
    assertMethodExists("addGradedWork", String.class, double.class);
    assertMethodExists("removeGradedWork", String.class);
    assertMethodExists("hasGradedWork", String.class);
    assertMethodExists("getWorkWeight", String.class);
    assertMethodExists("getGradedWorkList");
    assertMethodExists("getTotalWeight");

    // Score management methods
    assertMethodExists("enterScore", String.class, String.class, double.class);
    assertMethodExists("getScore", String.class, String.class);
    assertMethodExists("hasScore", String.class, String.class);
    assertMethodExists("getStudentScores", String.class);
    assertMethodExists("removeScore", String.class, String.class);

    // Grade calculation methods
    assertMethodExists("getCurrentGrade", String.class);
    assertMethodExists("getWeightedTotal", String.class);
    assertMethodExists("getScoredWeight", String.class);

    // Reporting methods
    assertMethodExists("getWorkStatistics", String.class);
    assertMethodExists("getMissingScores");
  }

  // ========== METHOD SIGNATURE TESTS ==========

  @Test
  public void testReturnTypes() throws NoSuchMethodException {
    // Boolean return types
    assertReturnType("isStudentRegistered", boolean.class, String.class);
    assertReturnType("hasGradedWork", boolean.class, String.class);
    assertReturnType("hasScore", boolean.class, String.class, String.class);

    // String return types
    assertReturnType("getStudentName", String.class, String.class);

    // List return types
    assertReturnType("getRegisteredStudents", List.class);
    assertReturnType("getGradedWorkList", List.class);

    // Map return types
    assertReturnType("getStudentScores", Map.class, String.class);
    assertReturnType("getMissingScores", Map.class);

    // int return types
    assertReturnType("getStudentCount", int.class);

    // double return types
    assertReturnType("getWorkWeight", double.class, String.class);
    assertReturnType("getTotalWeight", double.class);
    assertReturnType("getScore", double.class, String.class, String.class);
    assertReturnType("getCurrentGrade", double.class, String.class);
    assertReturnType("getWeightedTotal", double.class, String.class);
    assertReturnType("getScoredWeight", double.class, String.class);

    // double[] return type for statistics
    assertReturnType("getWorkStatistics", double[].class, String.class);
  }

  @Test
  public void testExceptionDeclarations() throws NoSuchMethodException {
    // Methods that should throw IllegalArgumentException
    assertThrows("registerStudent", IllegalArgumentException.class, String.class, String.class);
    assertThrows("withdrawStudent", IllegalArgumentException.class, String.class);
    assertThrows("getStudentName", IllegalArgumentException.class, String.class);
    assertThrows("addGradedWork", IllegalArgumentException.class, String.class, double.class);
    assertThrows("enterScore", IllegalArgumentException.class, String.class, String.class, double.class);

    // Methods that should throw both IllegalArgumentException and IllegalStateException
    assertThrows("getScore", IllegalArgumentException.class, String.class, String.class);
    assertThrows("getScore", IllegalStateException.class, String.class, String.class);
    assertThrows("removeScore", IllegalArgumentException.class, String.class, String.class);
    assertThrows("removeScore", IllegalStateException.class, String.class, String.class);
    assertThrows("getWorkStatistics", IllegalStateException.class, String.class);
  }

  // ========== STATISTICS METHOD TESTS ==========

  @Test
  public void testStatisticalMethods() throws NoSuchMethodException {
    // Test that statistical methods exist
    assertMethodExists("getWorkStatistics", String.class);
    assertMethodExists("getMissingScores");

    // Test return types
    assertReturnType("getWorkStatistics", double[].class, String.class);
    assertReturnType("getMissingScores", Map.class);

    // Test exception declarations for getWorkStatistics
    Method method = Course.class.getMethod("getWorkStatistics", String.class);
    Class<?>[] exceptions = method.getExceptionTypes();
    boolean hasIllegalArg = false;
    boolean hasIllegalState = false;

    for (Class<?> exception : exceptions) {
      if (exception.equals(IllegalArgumentException.class)) hasIllegalArg = true;
      if (exception.equals(IllegalStateException.class)) hasIllegalState = true;
    }

    assertTrue("getWorkStatistics should throw IllegalArgumentException", hasIllegalArg);
    assertTrue("getWorkStatistics should throw IllegalStateException", hasIllegalState);
  }

  // ========== INTERFACE DESIGN QUALITY TESTS ==========

  @Test
  public void testMethodGroupCompleteness() throws NoSuchMethodException {
    // Instead of counting methods, just verify the key methods exist

    // Student management methods
    assertMethodExists("registerStudent", String.class, String.class);
    assertMethodExists("withdrawStudent", String.class);
    assertMethodExists("isStudentRegistered", String.class);
    assertMethodExists("getStudentName", String.class);

    // Graded work methods
    assertMethodExists("addGradedWork", String.class, double.class);
    assertMethodExists("removeGradedWork", String.class);
    assertMethodExists("hasGradedWork", String.class);
    assertMethodExists("getWorkWeight", String.class);

    // Score management methods
    assertMethodExists("enterScore", String.class, String.class, double.class);
    assertMethodExists("getScore", String.class, String.class);
    assertMethodExists("hasScore", String.class, String.class);
    assertMethodExists("getStudentScores", String.class);

    // Grade calculation methods - these are the core ones we need
    assertMethodExists("getCurrentGrade", String.class);
    assertMethodExists("getWeightedTotal", String.class);
    assertMethodExists("getScoredWeight", String.class);

    // If we get here, all the key methods exist
    assertTrue("All required method groups are present", true);
  }

  @Test
  public void testMethodNamingConsistency() {
    Method[] methods = Course.class.getDeclaredMethods();

    boolean hasGetters = false;
    boolean hasCheckers = false;
    boolean hasActions = false;

    for (Method method : methods) {
      String name = method.getName();
      if (name.startsWith("get")) hasGetters = true;
      if (name.startsWith("has") || name.startsWith("is")) hasCheckers = true;
      if (name.startsWith("add") || name.startsWith("remove") || name.startsWith("enter")) hasActions = true;
    }

    assertTrue("Interface should have getter methods", hasGetters);
    assertTrue("Interface should have checker methods (has/is)", hasCheckers);
    assertTrue("Interface should have action methods (add/remove/enter)", hasActions);
  }

  @Test
  public void testInterfaceComprehensiveness() {
    Method[] methods = Course.class.getDeclaredMethods();

    // Should have a reasonable number of methods for a comprehensive interface
    assertTrue("Interface should be comprehensive (15+ methods)", methods.length >= 15);
    assertTrue("Interface should not be overwhelming (< 30 methods)", methods.length < 30);
  }

  @Test
  public void testRequiredCapabilitiesCovered() throws NoSuchMethodException {
    // Requirement 1: Register and withdraw students
    assertMethodExists("registerStudent", String.class, String.class);
    assertMethodExists("withdrawStudent", String.class);

    // Requirement 2: Add graded work as course progresses
    assertMethodExists("addGradedWork", String.class, double.class);

    // Requirement 3: Enter scores for students
    assertMethodExists("enterScore", String.class, String.class, double.class);

    // Requirement 4: Query current numeric grade
    assertMethodExists("getCurrentGrade", String.class);
  }

  // ========== UTILITY METHODS ==========

  private void assertMethodExists(String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
    Method method = Course.class.getMethod(methodName, parameterTypes);
    assertNotNull("Method " + methodName + " should exist", method);
  }

  private void assertReturnType(String methodName, Class<?> expectedReturnType, Class<?>... parameterTypes) throws NoSuchMethodException {
    Method method = Course.class.getMethod(methodName, parameterTypes);
    assertEquals("Method " + methodName + " should return " + expectedReturnType.getSimpleName(),
            expectedReturnType, method.getReturnType());
  }

  private void assertThrows(String methodName, Class<? extends Exception> exceptionType, Class<?>... parameterTypes) throws NoSuchMethodException {
    Method method = Course.class.getMethod(methodName, parameterTypes);
    Class<?>[] exceptions = method.getExceptionTypes();

    boolean found = false;
    for (Class<?> exception : exceptions) {
      if (exception.equals(exceptionType)) {
        found = true;
        break;
      }
    }

    assertTrue("Method " + methodName + " should declare " + exceptionType.getSimpleName(), found);
  }
}