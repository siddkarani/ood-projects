/**
 * Represents a Knight chess piece.
 * A Knight moves in an L-shape pattern: two squares horizontally followed by one square vertically,
 * or two squares vertically followed by one square horizontally.
 */
public class Knight extends AbstractChessPiece {

  /**
   * Constructs a Knight at the specified position with the given color.
   *
   * @param row   the row position of the Knight (0-based index)
   * @param col   the column position of the Knight (0-based index)
   * @param color the color of the Knight (WHITE or BLACK)
   * @throws IllegalArgumentException if the position is invalid (negative row or column)
   */
  public Knight(int row, int col, Color color) throws IllegalArgumentException {
    super(row, col, color);
  }

  /**
   * Determines if this Knight can move to the specified position.
   * A Knight moves in an L-shape pattern, which means:
   * - Move two squares horizontally and one square vertically, or
   * - Move two squares vertically and one square horizontally.
   *
   * @param row the row to which this Knight can be moved
   * @param col the col to which this Knight can be moved
   * @return true if the Knight can move to the specified position, false otherwise
   */
  @Override
  public boolean canMove(int row, int col) {
    if (!isValidPosition(row, col)) {
      return false;
    }

    int rowDiff = Math.abs(this.getRow() - row);
    int colDiff = Math.abs(this.getColumn() - col);

    // Knight moves in an L-shape: (2,1) or (1,2)
    return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
  }
}