import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Queen chess piece.
 */
public class QueenTest extends AbstractChessPieceTest {

  /**
   * Creates a Queen piece for testing.
   *
   * @param row   the row position
   * @param col   the column position
   * @param color the piece color
   * @return a new Queen piece
   */
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Queen(row, col, color);
  }

  /**
   * Sets up the expected valid moves for a Queen at the given position.
   * This includes both horizontal/vertical moves (like Rook) and diagonal moves (like Bishop).
   *
   * @param row the row of the Queen
   * @param col the column of the Queen
   */
  @Override
  protected void setupResults(int row, int col) {
    // Set up horizontal and vertical moves (like Rook)
    for (int i = 0; i < 8; i++) {
      results[i][col] = true;  // Vertical moves
      results[row][i] = true;  // Horizontal moves
    }

    // Set up diagonal moves (like Bishop)
    for (int i = 0; i < 8; i++) {
      if ((row + i) < 8) {
        if ((col + i) < 8) {
          results[row + i][col + i] = true;
        }
        if (col >= i) {
          results[row + i][col - i] = true;
        }
      }

      if (row >= i) {
        if ((col + i) < 8) {
          results[row - i][col + i] = true;
        }
        if (col >= i) {
          results[row - i][col - i] = true;
        }
      }
    }
  }

  /**
   * Tests that the Queen can move correctly.
   */
  @Test(timeout = 500)
  public void testQueenMoves() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Queen(3, 3, Color.BLACK);

    // Test horizontal movement
    Assert.assertTrue("Queen should be able to move horizontally.", piece.canMove(3, 5));

    // Test vertical movement
    Assert.assertTrue("Queen should be able to move vertically.", piece.canMove(5, 3));

    // Test diagonal movement
    Assert.assertTrue("Queen should be able to move diagonally.", piece.canMove(5, 5));

    // Test invalid movement
    Assert.assertFalse("Queen should not be able to move like a knight.", piece.canMove(5, 4));

    // Run the complete test suite
    testMoves();
  }

  /**
   * Tests that the Queen can kill correctly.
   */
  @Test(timeout = 500)
  public void testQueenKills() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Queen(3, 3, Color.BLACK);
    ChessPiece target1 = new Queen(3, 5, Color.WHITE);
    ChessPiece target2 = new Queen(5, 5, Color.WHITE);

    Assert.assertTrue("Queen should be able to kill horizontally.", piece.canKill(target1));
    Assert.assertTrue("Queen should be able to kill diagonally.", piece.canKill(target2));

    // Run the complete test suite
    testKills();
  }
}