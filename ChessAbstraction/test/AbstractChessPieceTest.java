import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Abstract test class for chess pieces that provides common test functionality.
 * This class contains test methods and utility methods that can be used by all chess piece tests.
 */
public abstract class AbstractChessPieceTest {
  /** Matrix to track expected valid moves for a piece. */
  protected boolean[][] results;

  /**
   * Creates a new chess piece of the appropriate type for testing.
   *
   * @param row   the row position
   * @param col   the column position
   * @param color the piece color
   * @return a new chess piece
   */
  protected abstract ChessPiece createPiece(int row, int col, Color color);

  /**
   * Sets up the expected valid moves for a piece at the given position.
   *
   * @param row the row of the piece
   * @param col the column of the piece
   */
  protected abstract void setupResults(int row, int col);

  /**
   * Initializes the test by creating a new results matrix.
   */
  @Before
  public void setup() {
    results = new boolean[8][8];
  }

  /**
   * Verifies that canMove returns the expected results for all board positions.
   *
   * @param piece the chess piece to test
   */
  protected void verifyMoveResults(ChessPiece piece) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if ((i == piece.getRow()) && (j == piece.getColumn())) {
          continue;
        }

        Assert.assertEquals("Piece at :" + piece.getRow() + "," + piece.getColumn() +
                        ", Unexpected canMove result "
                        + "for "
                        + "i=" + i + " j=" +
                        j + "",
                results[i][j], piece.canMove(i, j));
      }
    }
  }

  /**
   * Verifies that canKill returns the expected results for all board positions.
   *
   * @param piece the chess piece to test
   */
  protected void verifyKillResults(ChessPiece piece) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if ((i == piece.getRow()) && (j == piece.getColumn())) {
          continue;
        }

        // Create a piece of the opposite color at position i,j
        ChessPiece another = createPiece(i, j,
                Color.values()[(piece.getColor().ordinal() + 1)
                        % Color.values().length]);

        Assert.assertEquals("Unexpected canKill result for "
                        + "i=" + i + " j=" +
                        j + "",
                results[i][j], piece.canKill(another));
      }
    }
  }

  /**
   * Tests the getter methods of a chess piece.
   */
  @Test(timeout = 500)
  public void testGetters() {
    ChessPiece piece;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        for (Color c : Color.values()) {
          piece = createPiece(row, col, c);

          Assert.assertEquals("Row number does not match what was initialized.", row,
                  piece.getRow());
          Assert.assertEquals("Column number does not match what was initialized.",
                  col, piece.getColumn());
          Assert.assertEquals("Color does not match what was initialized.",
                  c, piece.getColor());
        }
      }
    }
  }

  /**
   * Tests that the constructor properly validates positions.
   */
  @Test(timeout = 500)
  public void testInvalidConstructions() {
    for (Color c : Color.values()) {
      for (int i = 0; i < 8; i++) {
        try {
          createPiece(i, -1, c);
          Assert.fail("Did not throw an exception when piece is created with invalid row.");
        } catch (IllegalArgumentException e) {
          // passes
        }

        try {
          createPiece(-1, i, c);
          Assert.fail("Did not throw an exception when piece is created with invalid column.");
        } catch (IllegalArgumentException e) {
          // passes
        }
      }
    }
  }

  /**
   * Tests that the piece can move correctly.
   */
  protected void testMoves() {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        initializeResults();
        ChessPiece piece = createPiece(row, col, Color.BLACK);

        setupResults(row, col);
        verifyMoveResults(piece);
      }
    }
  }

  /**
   * Tests that the piece can kill correctly.
   */
  protected void testKills() {
    for (Color c : Color.values()) {
      for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
          initializeResults();
          ChessPiece piece = createPiece(row, col, c);

          setupResults(row, col);
          verifyKillResults(piece);
        }
      }
    }
  }

  /**
   * Initializes the results matrix to all false.
   */
  protected void initializeResults() {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        results[row][col] = false;
      }
    }
  }
}