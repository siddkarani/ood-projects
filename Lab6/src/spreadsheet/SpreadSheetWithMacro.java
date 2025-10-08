package spreadsheet;

/**
 * This interface extends the basic SpreadSheet interface to support macro execution.
 * It adds the ability to accept and execute macro commands on the spreadsheet.
 */
public interface SpreadSheetWithMacro extends SpreadSheet {
  /**
   * Executes the given macro on this spreadsheet.
   * @param macro the macro to execute
   * @throws IllegalArgumentException if the macro is null or cannot be executed
   */
  void execute(SpreadSheetMacro macro) throws IllegalArgumentException;
}