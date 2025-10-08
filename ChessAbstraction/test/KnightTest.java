import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Knight chess piece.
 */
public class KnightTest extends AbstractChessPieceTest {

  /**
   * Creates a Knight piece for testing.
   *
   * @param row   the row position
   * @param col   the column position
   * @param color the piece color
   * @return a new Knight piece
   */
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Knight(row, col, color);
  }

  /**
   * Sets up the expected valid L-shaped moves for a Knight at the given position.
   *
   * @param row the row of the Knight
   * @param col the column of the Knight
   */
  @Override
  protected void setupResults(int row, int col) {
    // Set up L-shaped moves
    // 2 up, 1 right
    if (row >= 2 && col < 7) {
      results[row - 2][col + 1] = true;
    }

    // 2 up, 1 left
    if (row >= 2 && col > 0) {
      results[row - 2][col - 1] = true;
    }

    // 2 down, 1 right
    if (row < 6 && col < 7) {
      results[row + 2][col + 1] = true;
    }

    // 2 down, 1 left
    if (row < 6 && col > 0) {
      results[row + 2][col - 1] = true;
    }

    // 1 up, 2 right
    if (row > 0 && col < 6) {
      results[row - 1][col + 2] = true;
    }

    // 1 up, 2 left
    if (row > 0 && col >= 2) {
      results[row - 1][col - 2] = true;
    }

    // 1 down, 2 right
    if (row < 7 && col < 6) {
      results[row + 1][col + 2] = true;
    }

    // 1 down, 2 left
    if (row < 7 && col >= 2) {
      results[row + 1][col - 2] = true;
    }
  }

  /**
   * Tests that the Knight can move correctly.
   */
  @Test(timeout = 500)
  public void testKnightMoves() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Knight(3, 3, Color.BLACK);

    // Test L-shaped movement
    Assert.assertTrue("Knight should be able to move in L-shape (2 up, 1 right).",
            piece.canMove(1, 4));
    Assert.assertTrue("Knight should be able to move in L-shape (1 up, 2 right).",
            piece.canMove(2, 5));

    // Test invalid movements
    Assert.assertFalse("Knight should not be able to move horizontally.",
            piece.canMove(3, 5));
    Assert.assertFalse("Knight should not be able to move diagonally.",
            piece.canMove(5, 5));

    // Run the complete test suite
    testMoves();
  }

  /**
   * Tests that the Knight can kill correctly.
   */
  @Test(timeout = 500)
  public void testKnightKills() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Knight(3, 3, Color.BLACK);
    ChessPiece target = new Knight(1, 4, Color.WHITE);

    Assert.assertTrue("Knight should be able to kill in L-shape pattern.",
            piece.canKill(target));

    // Run the complete test suite
    testKills();
  }
}