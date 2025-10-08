package spreadsheet;

/**
 * This interface represents a macro (command) that can be executed on a spreadsheet.
 * This follows the Command design pattern.
 */
public interface SpreadSheetMacro {
  /**
   * Executes this macro on the given spreadsheet.
   * @param spreadSheet the spreadsheet to execute this macro on
   * @throws IllegalArgumentException if the spreadsheet is null or if the macro
   *                                  cannot be executed due to invalid parameters
   */
  void execute(SpreadSheet spreadSheet) throws IllegalArgumentException;
}