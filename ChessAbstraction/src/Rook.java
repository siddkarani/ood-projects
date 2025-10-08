/**
 * Represents a Rook chess piece.
 * A Rook can move horizontally or vertically across any number of unoccupied squares.
 */
public class Rook extends AbstractChessPiece {

  /**
   * Constructs a Rook at the specified position with the given color.
   *
   * @param row   the row position of the Rook (0-based index)
   * @param col   the column position of the Rook (0-based index)
   * @param color the color of the Rook (WHITE or BLACK)
   * @throws IllegalArgumentException if the position is invalid (negative row or column)
   */
  public Rook(int row, int col, Color color) throws IllegalArgumentException {
    super(row, col, color);
  }

  /**
   * Determines if this Rook can move to the specified position.
   * A Rook can only move horizontally or vertically, which means either
   * the row or column must remain the same.
   *
   * @param row the row to which this Rook can be moved
   * @param col the col to which this Rook can be moved
   * @return true if the Rook can move to the specified position, false otherwise
   */
  @Override
  public boolean canMove(int row, int col) {
    if (!isValidPosition(row, col)) {
      return false;
    }
    return (this.getRow() == row) || (this.getColumn() == col);
  }
}