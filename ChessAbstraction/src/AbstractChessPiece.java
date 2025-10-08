/**
 * Abstract base class for chess pieces that implements common functionality.
 * This class provides implementations for methods that are common across all chess pieces,
 * while leaving piece-specific movement logic to be implemented by subclasses.
 */
public abstract class AbstractChessPiece implements ChessPiece {
  /**
   * The row position of the chess piece (0-based index).
   */
  private final int row;

  /**
   * The column position of the chess piece (0-based index).
   */
  private final int col;

  /**
   * The color of the chess piece (WHITE or BLACK).
   */
  private final Color color;

  /**
   * The maximum size of the chess board.
   */
  protected static final int BOARD_SIZE = 8;

  /**
   * Constructs a chess piece at the specified position with the given color.
   *
   * @param row   the row position of the piece (0-based index)
   * @param col   the column position of the piece (0-based index)
   * @param color the color of the piece (WHITE or BLACK)
   * @throws IllegalArgumentException if the position is invalid (negative row or column)
   */
  public AbstractChessPiece(int row, int col, Color color) throws IllegalArgumentException {
    if ((row < 0) || (col < 0)) {
      throw new IllegalArgumentException("Illegal position");
    }
    this.row = row;
    this.col = col;
    this.color = color;
  }

  /**
   * Checks if the position is within the bounds of the chess board.
   *
   * @param row the row to check
   * @param col the column to check
   * @return true if the position is valid, false otherwise
   */
  protected boolean isValidPosition(int row, int col) {
    return (row >= 0) && (col >= 0) && (row < BOARD_SIZE) && (col < BOARD_SIZE);
  }

  @Override
  public int getRow() {
    return row;
  }

  @Override
  public int getColumn() {
    return col;
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public boolean canKill(ChessPiece piece) {
    return (this.getColor() != piece.getColor()) && canMove(piece.getRow(), piece.getColumn());
  }

  /**
   * Abstract method that determines if this chess piece can move to a specified position.
   * Each specific chess piece must implement this method according to its movement rules.
   *
   * @param row the row to which this chess piece can be moved
   * @param col the col to which this chess piece can be moved
   * @return true if the piece can move to the specified position, false otherwise
   */
  @Override
  public abstract boolean canMove(int row, int col);
}