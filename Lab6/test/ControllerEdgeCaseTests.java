import org.junit.Test;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.MacroSpreadSheetController;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;
import spreadsheet.BulkAssignMacro;
import spreadsheet.AverageMacro;
import java.io.StringReader;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Additional tests focusing on controller edge cases and boundary conditions.
 */
public class ControllerEdgeCaseTests {

  /**
   * Test controller with null arguments.
   */
  @Test
  public void testControllerWithNullArguments() {
    try {
      new MacroSpreadSheetController(null, new StringReader(""), new StringBuilder());
    } catch (IllegalArgumentException e) {
      assertTrue("Should reject null sheet", e.getMessage().contains("null"));
    }
  }

  /**
   * Test controller with complex row letters.
   */
  @Test
  public void testComplexRowLetters() {
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value AA 1 AB 2 5").append(System.lineSeparator());
    input.append("print-value AA 1").append(System.lineSeparator());
    input.append("print-value AB 2").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    assertTrue("Should handle multi-letter rows", output
            .toString().contains("Value: 5.0"));
  }

  /**
   * Test controller with large coordinates.
   */
  @Test
  public void testLargeCoordinates() {
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value A 100 A 100 123").append(System.lineSeparator());
    input.append("print-value A 100").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    assertTrue("Should handle large coordinates", output
            .toString().contains("Value: 123.0"));
  }

  /**
   * Test controller with decimal values.
   */
  @Test
  public void testDecimalValues() {
    StringBuilder input = new StringBuilder();
    input.append("range-assign A 1 A 3 1.5 0.25").append(System.lineSeparator());
    input.append("print-value A 1").append(System.lineSeparator());
    input.append("print-value A 3").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    assertTrue("Should handle decimals", output.toString().contains("Value: 1.5"));
    assertTrue("Should handle decimals", output.toString().contains("Value: 2.0"));
  }

  /**
   * Test controller with mixed command types.
   */
  @Test
  public void testMixedCommandTypes() {
    StringBuilder input = new StringBuilder();
    input.append("assign-value A 1 10").append(System.lineSeparator());
    input.append("bulk-assign-value B 1 B 2 20").append(System.lineSeparator());
    input.append("range-assign C 1 C 2 30 5").append(System.lineSeparator());
    input.append("average A 1 C 2 D 1").append(System.lineSeparator());
    input.append("print-value D 1").append(System.lineSeparator());
    input.append("menu").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    String result = output.toString();
    assertTrue("Should execute mixed commands", result.contains("Value:"));
    assertTrue("Should show menu", result.contains("bulk-assign-value"));
  }

  /**
   * Test invalid commands handling.
   */
  @Test
  public void testInvalidCommandsHandling() {
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign").append(System.lineSeparator());
    input.append("range-wrong A 1 A 2 1 1").append(System.lineSeparator());
    input.append("unknown-command").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    String result = output.toString();
    assertTrue("Should handle invalid commands",
            result.contains("Undefined instruction"));
  }

  /**
   * Test empty range operations.
   */
  @Test
  public void testEmptyRangeOperations() {
    StringBuilder input = new StringBuilder();
    input.append("average A 1 A 3 B 1").append(System.lineSeparator()); // All empty cells
    input.append("print-value B 1").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    assertTrue("Should handle empty ranges", output.toString().contains("Value: 0.0"));
  }

  /**
   * Test overwriting values.
   */
  @Test
  public void testOverwritingValues() {
    StringBuilder input = new StringBuilder();
    input.append("assign-value A 1 100").append(System.lineSeparator());
    input.append("bulk-assign-value A 1 A 1 200").append(System.lineSeparator());
    input.append("range-assign A 1 A 1 300 0").append(System.lineSeparator());
    input.append("print-value A 1").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    assertTrue("Should show final overwritten value",
            output.toString().contains("Value: 300.0"));
  }

  /**
   * Test SpreadSheetWithMacro interface methods.
   */
  @Test
  public void testSpreadSheetWithMacroInterface() {
    // Create a macro-enabled spreadsheet to test interface methods
    SparseSpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro macroSheet = new SpreadSheetWithMacroImpl(baseSheet);

    // Test execute method with actual macro
    BulkAssignMacro macro = new BulkAssignMacro(0, 0,
            2, 2, 15.0);
    macroSheet.execute(macro);

    // Verify the macro worked through the interface
    assertEquals(15.0, macroSheet.get(0, 0), 0.001);
    assertEquals(15.0, macroSheet.get(2, 2), 0.001);
    assertFalse(macroSheet.isEmpty(1, 1));
  }

  /**
   * Test all SpreadSheetWithMacroImpl methods.
   */
  @Test
  public void testAllSpreadSheetWithMacroImplMethods() {
    SparseSpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro wrapper = new SpreadSheetWithMacroImpl(baseSheet);

    // Test all inherited methods
    wrapper.set(3, 4, 77.5);
    assertEquals(77.5, wrapper.get(3, 4), 0.001);
    assertFalse(wrapper.isEmpty(3, 4));
    assertTrue(wrapper.isEmpty(0, 0));
    assertEquals(5, wrapper.getWidth());
    assertEquals(4, wrapper.getHeight());

    // Test execute method
    AverageMacro avgMacro = new AverageMacro(3, 4, 3,
            4, 5, 5);
    wrapper.execute(avgMacro);
    assertEquals(77.5, wrapper.get(5, 5), 0.001);
  }
}