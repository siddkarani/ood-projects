/**
 * Represents a Bishop chess piece.
 * A Bishop can move diagonally in any direction across any number of unoccupied squares.
 */
public class Bishop extends AbstractChessPiece {

  /**
   * Constructs a Bishop at the specified position with the given color.
   *
   * @param row   the row position of the Bishop (0-based index)
   * @param col   the column position of the Bishop (0-based index)
   * @param color the color of the Bishop (WHITE or BLACK)
   * @throws IllegalArgumentException if the position is invalid (negative row or column)
   */
  public Bishop(int row, int col, Color color) throws IllegalArgumentException {
    super(row, col, color);
  }

  /**
   * Determines if this Bishop can move to the specified position.
   * A Bishop can only move diagonally, which means the absolute difference
   * between the row and column distances must be equal.
   *
   * @param row the row to which this Bishop can be moved
   * @param col the col to which this Bishop can be moved
   * @return true if the Bishop can move to the specified position, false otherwise
   */
  @Override
  public boolean canMove(int row, int col) {
    if (!isValidPosition(row, col)) {
      return false;
    }
    return Math.abs(this.getRow() - row) == Math.abs(this.getColumn() - col);
  }
}