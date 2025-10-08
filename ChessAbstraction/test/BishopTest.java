import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Bishop chess piece.
 */
public class BishopTest extends AbstractChessPieceTest {

  /**
   * Creates a Bishop piece for testing.
   *
   * @param row   the row position
   * @param col   the column position
   * @param color the piece color
   * @return a new Bishop piece
   */
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Bishop(row, col, color);
  }

  /**
   * Sets up the expected valid diagonal moves for a Bishop at the given position.
   *
   * @param row the row of the Bishop
   * @param col the column of the Bishop
   */
  @Override
  protected void setupResults(int row, int col) {
    // Set up diagonal moves
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
   * Tests that the Bishop can move correctly.
   */
  @Test(timeout = 500)
  public void testBishopMoves() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Bishop(3, 3, Color.BLACK);

    // Test diagonal movement
    Assert.assertTrue("Bishop should be able to move diagonally.", piece.canMove(5, 5));

    // Test non-diagonal movement
    Assert.assertFalse("Bishop should not be able to move horizontally.", piece.canMove(3, 5));

    // Run the complete test suite
    testMoves();
  }

  /**
   * Tests that the Bishop can kill correctly.
   */
  @Test(timeout = 500)
  public void testBishopKills() {
    // Test a specific case with explicit assertions
    ChessPiece piece = new Bishop(3, 3, Color.BLACK);
    ChessPiece target = new Bishop(5, 5, Color.WHITE);

    Assert.assertTrue("Bishop should be able to kill diagonally.", piece.canKill(target));

    // Run the complete test suite
    testKills();
  }
}