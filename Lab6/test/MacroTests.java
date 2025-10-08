import org.junit.Before;
import org.junit.Test;
import spreadsheet.SpreadSheet;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;
import spreadsheet.BulkAssignMacro;
import spreadsheet.AverageMacro;
import spreadsheet.RangeAssignMacro;
import spreadsheet.MacroSpreadSheetController;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for all macro functionality including edge cases and error conditions.
 */
public class MacroTests {
  private SpreadSheet sheet;
  private SpreadSheetWithMacro macroSheet;

  /**
   * Set up test fixtures.
   */
  @Before
  public void setup() {
    sheet = new SparseSpreadSheet();
    macroSheet = new SpreadSheetWithMacroImpl(sheet);
  }

  // Tests for SpreadSheetWithMacroImpl
  /**
   * Test macro sheet construction.
   */
  @Test
  public void testMacroSheetConstruction() {
    SpreadSheet testSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro wrapper = new SpreadSheetWithMacroImpl(testSheet);

    testSheet.set(2, 3, 7.5);
    assertEquals(7.5, wrapper.get(2, 3), 0.001);
    assertEquals(testSheet.getWidth(), wrapper.getWidth());
    assertEquals(testSheet.getHeight(), wrapper.getHeight());
  }

  /**
   * Test macro sheet construction with null input throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMacroSheetNullConstruction() {
    new SpreadSheetWithMacroImpl(null);
  }

  /**
   * Test macro sheet execution with null macro throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMacroSheetNullMacroExecution() {
    macroSheet.execute(null);
  }

  /**
   * Test macro sheet executing a valid macro.
   */
  @Test
  public void testMacroSheetExecuteValidMacro() {
    BulkAssignMacro macro = new BulkAssignMacro(0, 0, 1, 1, 42.0);
    macroSheet.execute(macro);

    assertEquals(42.0, macroSheet.get(0, 0), 0.001);
    assertEquals(42.0, macroSheet.get(0, 1), 0.001);
    assertEquals(42.0, macroSheet.get(1, 0), 0.001);
    assertEquals(42.0, macroSheet.get(1, 1), 0.001);
  }

  /**
   * Test all delegated methods to ensure full coverage.
   */
  @Test
  public void testMacroSheetAllDelegatedMethods() {
    macroSheet.set(5, 10, 99.9);
    assertEquals(99.9, macroSheet.get(5, 10), 0.001);
    assertFalse(macroSheet.isEmpty(5, 10));
    assertEquals(11, macroSheet.getWidth());
    assertEquals(6, macroSheet.getHeight());

    assertTrue(macroSheet.isEmpty(0, 0));
    assertEquals(0.0, macroSheet.get(0, 0), 0.001);
  }

  // BulkAssignMacro tests
  /**
   * Test bulk assign macro with basic range.
   */
  @Test
  public void testBulkAssignBasicRange() {
    BulkAssignMacro macro = new BulkAssignMacro(1, 1, 3, 2, 15.5);
    macro.execute(sheet);

    assertEquals(15.5, sheet.get(1, 1), 0.001);
    assertEquals(15.5, sheet.get(2, 2), 0.001);
    assertEquals(15.5, sheet.get(3, 1), 0.001);
    assertTrue(sheet.isEmpty(0, 0));
    assertTrue(sheet.isEmpty(4, 1));
  }

  /**
   * Test bulk assign macro with single cell.
   */
  @Test
  public void testBulkAssignSingleCell() {
    BulkAssignMacro macro = new BulkAssignMacro(5, 7, 5, 7, -42.3);
    macro.execute(sheet);

    assertEquals(-42.3, sheet.get(5, 7), 0.001);
    assertFalse(sheet.isEmpty(5, 7));
  }

  /**
   * Test bulk assign macro with zero and negative values.
   */
  @Test
  public void testBulkAssignZeroAndNegativeValues() {
    BulkAssignMacro macro1 = new BulkAssignMacro(0, 0, 1, 1, 0.0);
    macro1.execute(sheet);
    assertEquals(0.0, sheet.get(0, 0), 0.001);

    BulkAssignMacro macro2 = new BulkAssignMacro(2, 2, 3, 3, -999.99);
    macro2.execute(sheet);
    assertEquals(-999.99, sheet.get(2, 2), 0.001);
  }

  /**
   * Test bulk assign macro with negative coordinates throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBulkAssignNegativeCoordinates() {
    new BulkAssignMacro(-1, 0, 2, 2, 1.0);
  }

  /**
   * Test bulk assign macro with invalid range throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBulkAssignInvalidRange() {
    new BulkAssignMacro(5, 3, 2, 8, 1.0);
  }

  /**
   * Test bulk assign macro with null sheet throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBulkAssignNullSheet() {
    BulkAssignMacro macro = new BulkAssignMacro(0, 0, 1, 1, 5.0);
    macro.execute(null);
  }

  // AverageMacro tests
  /**
   * Test average macro with basic calculation.
   */
  @Test
  public void testAverageBasicCalculation() {
    sheet.set(1, 1, 10.0);
    sheet.set(1, 2, 20.0);
    sheet.set(2, 1, 30.0);
    sheet.set(2, 2, 40.0);

    AverageMacro macro = new AverageMacro(1, 1, 2, 2, 5, 5);
    macro.execute(sheet);

    assertEquals(25.0, sheet.get(5, 5), 0.001);
  }

  /**
   * Test average macro with empty cells.
   */
  @Test
  public void testAverageWithEmptyCells() {
    sheet.set(0, 0, 50.0);
    sheet.set(0, 2, 150.0);

    AverageMacro macro = new AverageMacro(0, 0, 0, 2, 3, 3);
    macro.execute(sheet);

    assertEquals(66.666, sheet.get(3, 3), 0.01);
  }

  /**
   * Test average macro with single cell.
   */
  @Test
  public void testAverageSingleCell() {
    sheet.set(3, 4, 77.7);
    AverageMacro macro = new AverageMacro(3, 4, 3, 4, 8, 9);
    macro.execute(sheet);

    assertEquals(77.7, sheet.get(8, 9), 0.001);
  }

  /**
   * Test average macro overwriting destination.
   */
  @Test
  public void testAverageOverwriteDestination() {
    sheet.set(1, 1, 100.0);
    sheet.set(9, 9, 999.0);

    AverageMacro macro = new AverageMacro(1, 1, 1, 1, 9, 9);
    macro.execute(sheet);

    assertEquals(100.0, sheet.get(9, 9), 0.001);
  }

  /**
   * Test average macro with invalid coordinates throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAverageInvalidCoordinates() {
    new AverageMacro(0, -5, 1, 1, 2, 2);
  }

  /**
   * Test average macro with null sheet throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAverageNullSheet() {
    AverageMacro macro = new AverageMacro(0, 0, 1, 1, 2, 2);
    macro.execute(null);
  }

  // RangeAssignMacro tests
  /**
   * Test range assign macro for a row.
   */
  @Test
  public void testRangeAssignRow() {
    RangeAssignMacro macro = new RangeAssignMacro(2, 1, 2, 4, 100.0, 50.0);
    macro.execute(sheet);

    assertEquals(100.0, sheet.get(2, 1), 0.001);
    assertEquals(150.0, sheet.get(2, 2), 0.001);
    assertEquals(200.0, sheet.get(2, 3), 0.001);
    assertEquals(250.0, sheet.get(2, 4), 0.001);
  }

  /**
   * Test range assign macro for a column.
   */
  @Test
  public void testRangeAssignColumn() {
    RangeAssignMacro macro = new RangeAssignMacro(0, 5, 3, 5, 1.0, 0.5);
    macro.execute(sheet);

    assertEquals(1.0, sheet.get(0, 5), 0.001);
    assertEquals(1.5, sheet.get(1, 5), 0.001);
    assertEquals(2.0, sheet.get(2, 5), 0.001);
    assertEquals(2.5, sheet.get(3, 5), 0.001);
  }

  /**
   * Test range assign macro with negative increment.
   */
  @Test
  public void testRangeAssignNegativeIncrement() {
    RangeAssignMacro macro = new RangeAssignMacro(1, 0, 1, 2, 10.0, -3.0);
    macro.execute(sheet);

    assertEquals(10.0, sheet.get(1, 0), 0.001);
    assertEquals(7.0, sheet.get(1, 1), 0.001);
    assertEquals(4.0, sheet.get(1, 2), 0.001);
  }

  /**
   * Test range assign macro with zero increment.
   */
  @Test
  public void testRangeAssignZeroIncrement() {
    RangeAssignMacro macro = new RangeAssignMacro(0, 0, 2, 0, 88.8, 0.0);
    macro.execute(sheet);

    assertEquals(88.8, sheet.get(0, 0), 0.001);
    assertEquals(88.8, sheet.get(1, 0), 0.001);
    assertEquals(88.8, sheet.get(2, 0), 0.001);
  }

  /**
   * Test range assign macro with 2D range throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRangeAssign2DRange() {
    new RangeAssignMacro(0, 0, 2, 2, 1.0, 1.0);
  }

  /**
   * Test range assign macro with invalid range throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRangeAssignInvalidRange() {
    new RangeAssignMacro(3, 0, 1, 0, 1.0, 1.0);
  }

  /**
   * Test range assign macro with null sheet throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRangeAssignNullSheet() {
    RangeAssignMacro macro = new RangeAssignMacro(0, 0, 0, 2, 1.0, 1.0);
    macro.execute(null);
  }

  // Controller integration tests
  /**
   * Test bulk assign command through controller.
   */
  @Test
  public void testBulkAssignCommand() {
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value B 2 C 3 25.5").append(System.lineSeparator());
    input.append("print-value B 2").append(System.lineSeparator());
    input.append("print-value C 3").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    String result = output.toString();
    assertTrue(result.contains("Value: 25.5"));
  }

  /**
   * Test range assign command through controller.
   */
  @Test
  public void testRangeAssignCommand() {
    StringBuilder input = new StringBuilder();
    input.append("range-assign A 1 A 4 5 3").append(System.lineSeparator());
    input.append("print-value A 1").append(System.lineSeparator());
    input.append("print-value A 4").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    String result = output.toString();
    assertTrue(result.contains("Value: 5.0"));
    assertTrue(result.contains("Value: 14.0"));
  }

  /**
   * Test average command through controller.
   */
  @Test
  public void testAverageCommand() {
    StringBuilder input = new StringBuilder();
    input.append("assign-value A 1 8").append(System.lineSeparator());
    input.append("assign-value A 2 12").append(System.lineSeparator());
    input.append("assign-value B 1 16").append(System.lineSeparator());
    input.append("assign-value B 2 20").append(System.lineSeparator());
    input.append("average A 1 B 2 C 1").append(System.lineSeparator());
    input.append("print-value C 1").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    assertTrue(output.toString().contains("Value: 14.0"));
  }

  /**
   * Test updated menu display.
   */
  @Test
  public void testUpdatedMenu() {
    StringBuilder input = new StringBuilder();
    input.append("menu").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    String result = output.toString();
    assertTrue(result.contains("bulk-assign-value"));
    assertTrue(result.contains("range-assign"));
    assertTrue(result.contains("average"));
    assertTrue(result.contains("assign-value"));
    assertTrue(result.contains("print-value"));
  }

  /**
   * Test error handling for invalid commands.
   */
  @Test
  public void testErrorHandling() {
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value A 5 A 1 10").append(System.lineSeparator()); // Invalid range
    input.append("range-assign A 1 B 2 1 1").append(System.lineSeparator()); // 2D range
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    String result = output.toString();
    assertTrue(result.contains("Error:"));
  }

  /**
   * Test combined operations working together.
   */
  @Test
  public void testCombinedOperations() {
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value A 1 A 2 10").append(System.lineSeparator());
    input.append("range-assign B 1 B 2 20 5").append(System.lineSeparator());
    input.append("average A 1 B 2 C 1").append(System.lineSeparator());
    input.append("print-value C 1").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    // Average of A1=10, A2=10, B1=20, B2=25 = 65/4 = 16.25
    assertTrue(output.toString().contains("Value: 16.25"));
  }

  /**
   * Test inherited functionality still works.
   */
  @Test
  public void testInheritedFunctionality() {
    StringBuilder input = new StringBuilder();
    input.append("assign-value Z 10 99.9").append(System.lineSeparator());
    input.append("print-value Z 10").append(System.lineSeparator());
    input.append("bulk-assign-value A 1 A 1 50").append(System.lineSeparator());
    input.append("print-value A 1").append(System.lineSeparator());
    input.append("q").append(System.lineSeparator());

    StringBuilder output = new StringBuilder();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(
            new SparseSpreadSheet(), new StringReader(input.toString()), output);

    controller.control();
    String result = output.toString();
    assertTrue(result.contains("Value: 99.9"));
    assertTrue(result.contains("Value: 50.0"));
  }
}