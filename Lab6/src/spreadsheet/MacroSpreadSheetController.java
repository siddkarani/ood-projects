package spreadsheet;

import java.util.Scanner;

/**
 * An enhanced spreadsheet controller that supports macro operations.
 * This controller extends the basic SpreadSheetController to add support for
 * bulk-assign-value, range-assign, and average commands.
 */
public class MacroSpreadSheetController extends SpreadSheetController {
  private SpreadSheetWithMacro macroSheet;

  /**
   * Create a controller to work with the specified sheet (model),
   * readable (to take inputs) and appendable (to transmit output).
   * @param sheet the sheet to work with (the model)
   * @param readable the Readable object for inputs
   * @param appendable the Appendable objects to transmit any output
   */
  public MacroSpreadSheetController(SpreadSheet sheet,
                                    Readable readable, Appendable appendable) {
    super(sheet, readable, appendable);
    this.macroSheet = new SpreadSheetWithMacroImpl(sheet);
  }

  @Override
  protected void processCommand(String userInstruction, Scanner sc, SpreadSheet sheet) {
    switch (userInstruction) {
      case "bulk-assign-value":
        processBulkAssignValue(sc);
        break;
      case "range-assign":
        processRangeAssign(sc);
        break;
      case "average":
        processAverage(sc);
        break;
      default:
        super.processCommand(userInstruction, sc, sheet);
        break;
    }
  }

  /**
   * Processes the bulk-assign-value command.
   * Syntax: bulk-assign-value from-row-num from-col-num to-row-num to-col-num value
   */
  private void processBulkAssignValue(Scanner sc) {
    try {
      int fromRow = getRowNum(sc.next());
      int fromCol = sc.nextInt() - 1; // Convert from 1-indexed to 0-indexed
      int toRow = getRowNum(sc.next());
      int toCol = sc.nextInt() - 1; // Convert from 1-indexed to 0-indexed
      double value = sc.nextDouble();

      BulkAssignMacro macro = new BulkAssignMacro(fromRow, fromCol, toRow, toCol, value);
      macroSheet.execute(macro);
    } catch (IllegalArgumentException e) {
      writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Processes the range-assign command.
   * Syntax: range-assign from-row-num from-col-num to-row-num to-col-num start-value increment
   */
  private void processRangeAssign(Scanner sc) {
    try {
      int fromRow = getRowNum(sc.next());
      int fromCol = sc.nextInt() - 1; // Convert from 1-indexed to 0-indexed
      int toRow = getRowNum(sc.next());
      int toCol = sc.nextInt() - 1; // Convert from 1-indexed to 0-indexed
      double startValue = sc.nextDouble();
      double increment = sc.nextDouble();

      RangeAssignMacro macro = new RangeAssignMacro(fromRow, fromCol, toRow, toCol,
              startValue, increment);
      macroSheet.execute(macro);
    } catch (IllegalArgumentException e) {
      writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Processes the average command.
   * Syntax: average from-row-num from-col-num to-row-num to-col-num dest-row-num dest-col-num
   */
  private void processAverage(Scanner sc) {
    try {
      int fromRow = getRowNum(sc.next());
      int fromCol = sc.nextInt() - 1; // Convert from 1-indexed to 0-indexed
      int toRow = getRowNum(sc.next());
      int toCol = sc.nextInt() - 1; // Convert from 1-indexed to 0-indexed
      int destRow = getRowNum(sc.next());
      int destCol = sc.nextInt() - 1; // Convert from 1-indexed to 0-indexed

      AverageMacro macro = new AverageMacro(fromRow, fromCol, toRow, toCol, destRow, destCol);
      macroSheet.execute(macro);
    } catch (IllegalArgumentException e) {
      writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  @Override
  protected void printMenu() throws IllegalStateException {
    writeMessage("Supported user instructions are: " + System.lineSeparator());
    writeMessage("assign-value row-num col-num value (set a cell to a value)"
            + System.lineSeparator());
    writeMessage("print-value row-num col-num (print the value at a given cell)"
            + System.lineSeparator());
    writeMessage("bulk-assign-value from-row-num from-col-num to-row-num to-col-num value "
            + "(set a range of cells to a value)" + System.lineSeparator());
    writeMessage("range-assign from-row-num from-col-num to-row-num to-col-num start-value "
            + "increment (set a row or column of cells to a range of values starting at "
            + "the given value and advancing by the increment)" + System.lineSeparator());
    writeMessage("average from-row-num from-col-num to-row-num to-col-num dest-row-num "
            + "dest-col-num (compute the average of a range of cells and put it at the "
            + "given location)" + System.lineSeparator());
    writeMessage("menu (Print supported instruction list)" + System.lineSeparator());
    writeMessage("q or quit (quit the program) " + System.lineSeparator());
  }
}