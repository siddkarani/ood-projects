package spreadsheet;

/**
 * A macro that assigns a specific value to an entire range of cells.
 * This macro implements the bulk-assign-value functionality.
 */
public class BulkAssignMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final double value;

  /**
   * Creates a new BulkAssignMacro.
   * @param fromRow the starting row (0-indexed)
   * @param fromCol the starting column (0-indexed)
   * @param toRow the ending row (0-indexed)
   * @param toCol the ending column (0-indexed)
   * @param value the value to assign to all cells in the range
   * @throws IllegalArgumentException if any coordinate is negative or if the range is invalid
   */
  public BulkAssignMacro(int fromRow, int fromCol, int toRow,
                         int toCol, double value) {
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0) {
      throw new IllegalArgumentException("Row and column indices cannot be negative");
    }
    if (fromRow > toRow || fromCol > toCol) {
      throw new IllegalArgumentException(
              "Invalid range: from coordinates must be <= to coordinates");
    }

    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.value = value;
  }

  @Override
  public void execute(SpreadSheet spreadSheet) throws IllegalArgumentException {
    if (spreadSheet == null) {
      throw new IllegalArgumentException("SpreadSheet cannot be null");
    }

    for (int row = fromRow; row <= toRow; row++) {
      for (int col = fromCol; col <= toCol; col++) {
        spreadSheet.set(row, col, value);
      }
    }
  }
}