package spreadsheet;

/**
 * A macro that assigns a range of values to a row or column of cells.
 * This macro implements the range-assign functionality.
 */
public class RangeAssignMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final double startValue;
  private final double increment;

  /**
   * Creates a new RangeAssignMacro.
   * @param fromRow the starting row (0-indexed)
   * @param fromCol the starting column (0-indexed)
   * @param toRow the ending row (0-indexed)
   * @param toCol the ending column (0-indexed)
   * @param startValue the starting value for the sequence
   * @param increment the increment between consecutive values
   * @throws IllegalArgumentException if any coordinate is negative, if the range is invalid,
   *                                  or if the range is not a single row or column
   */
  public RangeAssignMacro(int fromRow, int fromCol, int toRow, int toCol,
                          double startValue, double increment) {
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0) {
      throw new IllegalArgumentException("Row and column indices cannot be negative");
    }
    if (fromRow > toRow || fromCol > toCol) {
      throw new IllegalArgumentException(
              "Invalid range: from coordinates must be <= to coordinates");
    }

    // Check that the range is either a single row or a single column
    if (fromRow != toRow && fromCol != toCol) {
      throw new IllegalArgumentException("Range must be either a single row or a single column");
    }

    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.startValue = startValue;
    this.increment = increment;
  }

  @Override
  public void execute(SpreadSheet spreadSheet) throws IllegalArgumentException {
    if (spreadSheet == null) {
      throw new IllegalArgumentException("SpreadSheet cannot be null");
    }

    double currentValue = startValue;

    for (int row = fromRow; row <= toRow; row++) {
      for (int col = fromCol; col <= toCol; col++) {
        spreadSheet.set(row, col, currentValue);
        currentValue += increment;
      }
    }
  }
}