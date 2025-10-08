package spreadsheet;

/**
 * A macro that computes the average of a range of cells and stores it in a destination cell.
 * This macro implements the average functionality.
 */
public class AverageMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final int destRow;
  private final int destCol;

  /**
   * Creates a new AverageMacro.
   * @param fromRow the starting row of the range to average (0-indexed)
   * @param fromCol the starting column of the range to average (0-indexed)
   * @param toRow the ending row of the range to average (0-indexed)
   * @param toCol the ending column of the range to average (0-indexed)
   * @param destRow the destination row to store the result (0-indexed)
   * @param destCol the destination column to store the result (0-indexed)
   * @throws IllegalArgumentException if any coordinate is negative or if the range is invalid
   */
  public AverageMacro(int fromRow, int fromCol, int toRow, int toCol,
                      int destRow, int destCol) {
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0 || destRow < 0 || destCol < 0) {
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
    this.destRow = destRow;
    this.destCol = destCol;
  }

  @Override
  public void execute(SpreadSheet spreadSheet) throws IllegalArgumentException {
    if (spreadSheet == null) {
      throw new IllegalArgumentException("SpreadSheet cannot be null");
    }

    double sum = 0.0;
    int count = 0;

    for (int row = fromRow; row <= toRow; row++) {
      for (int col = fromCol; col <= toCol; col++) {
        sum += spreadSheet.get(row, col);
        count++;
      }
    }

    double average = (count > 0) ? sum / count : 0.0;
    spreadSheet.set(destRow, destCol, average);
  }
}