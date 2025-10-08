import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Rook chess piece.
 */
public class RookTest extends AbstractChessPieceTest {

  /**
   * Creates a Rook piece for testing.
   *
   * @param row   the row position
   * @param col   the column position
   * @param color the piece color
   * @return a new Rook piece
   */
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Rook(row, col, color);
  }

  /**
   * Sets up the expected valid horizontal and vertical moves for a Rook at the given position.
   *
   * @param row the row of the Rook
   * @param col the column of the Rook
   */
  @Override
  protected void setupResults(int row, int col) {
    // Set up horizontal and vertical moves
    for (int i = 0; i < 8; i++) {
      results[i][col] = true;  // Vertical moves
      results[row][i] = true;  // Horizontal moves
    }
  }

  /**
   * Tests that the Rook can move correctly.
   */
  @Test(timeout = 500)
  public void testRookMoves() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Rook(3, 3, Color.BLACK);

    // Test horizontal movement
    Assert.assertTrue("Rook should be able to move horizontally.",
            piece.canMove(3, 5));

    // Test vertical movement
    Assert.assertTrue("Rook should be able to move vertically.",
            piece.canMove(5, 3));

    // Test diagonal movement
    Assert.assertFalse("Rook should not be able to move diagonally.",
            piece.canMove(5, 5));

    // Run the complete test suite
    testMoves();
  }

  /**
   * Tests that the Rook can kill correctly.
   */
  @Test(timeout = 500)
  public void testRookKills() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Rook(3, 3, Color.BLACK);
    ChessPiece target = new Rook(3, 5, Color.WHITE);

    Assert.assertTrue("Rook should be able to kill horizontally.",
            piece.canKill(target));

    // Run the complete test suite
    testKills();
  }
}