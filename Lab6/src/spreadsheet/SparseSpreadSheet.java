package spreadsheet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a sparse spreadsheet. A sparse spreadsheet is a spreadsheet
 * with a large number of empty cells. It represents this efficiently using a hash map.
 */
public class SparseSpreadSheet implements SpreadSheet {
  private final Map<CellPosition, Double> sheet;
  private int width;
  private int height;

  /**
   * Create an empty spreadsheet.
   */
  public SparseSpreadSheet() {
    this.sheet = new HashMap<CellPosition, Double>();
    this.width = 0;
    this.height = 0;
  }

  @Override
  public double get(int row, int col) throws IllegalArgumentException {
    if ((row < 0) || (col < 0)) {
      throw new IllegalArgumentException("Row or column cannot be negative");
    }
    return this.sheet.getOrDefault(new CellPosition(row, col), 0.0);
  }

  @Override
  public void set(int row, int col, double value) throws IllegalArgumentException {
    if ((row < 0) || (col < 0)) {
      throw new IllegalArgumentException("Row or column cannot be negative");
    }
    this.sheet.put(new CellPosition(row, col), value);
    if ((row + 1) > height) {
      height = row + 1;
    }

    if ((col + 1) > width) {
      width = col + 1;
    }
  }

  @Override
  public boolean isEmpty(int row, int col) throws IllegalArgumentException {
    if ((row < 0) || (col < 0)) {
      throw new IllegalArgumentException("Row or column cannot be negative");
    }
    return !this.sheet.containsKey(new CellPosition(row, col));
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  private static class CellPosition {
    private final int row;
    private final int column;

    private CellPosition(int row, int column) {
      this.row = row;
      this.column = column;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof CellPosition)) {
        return false;
      }
      CellPosition other = (CellPosition) o;
      return this.row == other.row && this.column == other.column;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.row, this.column);
    }
  }
}
