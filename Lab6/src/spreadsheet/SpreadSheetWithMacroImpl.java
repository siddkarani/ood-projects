package spreadsheet;

/**
 * Implementation of SpreadSheetWithMacro that reuses the existing SparseSpreadSheet.
 * This class uses the decorator pattern to add macro functionality to any existing
 * SpreadSheet implementation.
 */
public class SpreadSheetWithMacroImpl implements SpreadSheetWithMacro {
  private final SpreadSheet delegate;

  /**
   * Creates a new SpreadSheetWithMacroImpl with a default SparseSpreadSheet.
   */
  public SpreadSheetWithMacroImpl() {
    this.delegate = new SparseSpreadSheet();
  }

  /**
   * Creates a new SpreadSheetWithMacroImpl that wraps the given spreadsheet.
   * @param spreadSheet the spreadsheet to wrap and add macro functionality to
   * @throws IllegalArgumentException if spreadSheet is null
   */
  public SpreadSheetWithMacroImpl(SpreadSheet spreadSheet) {
    if (spreadSheet == null) {
      throw new IllegalArgumentException("SpreadSheet cannot be null");
    }
    this.delegate = spreadSheet;
  }

  @Override
  public void execute(SpreadSheetMacro macro) throws IllegalArgumentException {
    if (macro == null) {
      throw new IllegalArgumentException("Macro cannot be null");
    }
    macro.execute(this);
  }

  @Override
  public double get(int row, int col) throws IllegalArgumentException {
    return delegate.get(row, col);
  }

  @Override
  public void set(int row, int col, double value) throws IllegalArgumentException {
    delegate.set(row, col, value);
  }

  @Override
  public boolean isEmpty(int row, int col) throws IllegalArgumentException {
    return delegate.isEmpty(row, col);
  }

  @Override
  public int getWidth() {
    return delegate.getWidth();
  }

  @Override
  public int getHeight() {
    return delegate.getHeight();
  }
}