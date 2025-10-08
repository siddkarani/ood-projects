/**
 * Represents a Queen chess piece.
 * A Queen combines the movement capabilities of a Rook and a Bishop, allowing it to move
 * horizontally, vertically, or diagonally across any number of unoccupied squares.
 */
public class Queen extends AbstractChessPiece {

  /**
   * Constructs a Queen at the specified position with the given color.
   *
   * @param row   the row position of the Queen (0-based index)
   * @param col   the column position of the Queen (0-based index)
   * @param color the color of the Queen (WHITE or BLACK)
   * @throws IllegalArgumentException if the position is invalid (negative row or column)
   */
  public Queen(int row, int col, Color color) throws IllegalArgumentException {
    super(row, col, color);
  }

  /**
   * Determines if this Queen can move to the specified position.
   * A Queen can move horizontally, vertically, or diagonally, which means either
   * the row or column must remain the same, or the absolute difference between
   * the row and column distances must be equal.
   *
   * @param row the row to which this Queen can be moved
   * @param col the col to which this Queen can be moved
   * @return true if the Queen can move to the specified position, false otherwise
   */
  @Override
  public boolean canMove(int row, int col) {
    if (!isValidPosition(row, col)) {
      return false;
    }
    return (this.getRow() == row) || (this.getColumn() == col)
            || (Math.abs(this.getRow() - row) == Math.abs(this.getColumn() - col));
  }
}