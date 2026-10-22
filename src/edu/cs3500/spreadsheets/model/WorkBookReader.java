package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkBookReader {
  /**
   * A builder pattern for producing WorksBooks.
   *
   * @param <T> the type of WorkBook to produce
   */
  public interface WorkbookBuilder<T> {

    /**
     * Creates a new cell at the given coordinates and fills in its raw contents.
     *
     * @param col      the column of the new cell (1-indexed)
     * @param row      the row of the new cell (1-indexed)
     * @param sheetName the sheet name of the cell.
     * @param contents the raw contents of the new cell: may be {@code null}, or any string. Strings
     *                 beginning with an {@code =} character should be treated as formulas; all
     *                 other strings should be treated as number or boolean values if possible, and
     *                 string values otherwise.
     * @return this {@link WorkBookReader.WorkbookBuilder}
     */
    WorkBookReader.WorkbookBuilder<T> createCell(String sheetName,int col, int row, String contents);

    /**
     * Finalizes the construction of the WorkBook and returns it.
     *
     * @return the fully-filled-in WorkBook
     */
    WorkBook<T> createWorkbook();

    /**
     * create sheet inside the workbook
     * @param sheetName the name of the sheet.
     */
    void createSheetName(String sheetName);
  }

  /**
   * <p>A factory for producing WorkBook.  The file format is</p>
   * <pre>
   *   &lt;SheetName!cell coordinates, in A# format&gt; &lt;the raw contents of the cell&gt;
   *   ...
   * </pre>
   * <p>e.g.</p>
   * <pre>
   *   A!A1 5
   *   A!A2 6
   *   B!A3 (SUM A!A1 A!A2:A2)
   * </pre>
   * <p>you have to creat sheet before assign to it by: </p
   * <pre>? SheetName </pre>
   * <p>Line-comments are indicated by <code>#</code>, and are allowed either at the start
   * of a line or between the cell coordinate and its contents.</p>
   * <p>There is no requirement that cells are filled in in order of their dependencies,
   * since no cell evaluation occurs during this creation process.</p>
   *
   * @param builder  The source of the new WorkBook object
   * @param readable the input source for the contents of this WorkBook
   * @param <T>      the type of WorkBook to produce
   * @return the fully-filled-in WorkBook
   */
  // !!! need to understand the T here clearly
  public static <T> WorkBook<T> read(WorkBookReader.WorkbookBuilder<T> builder, Readable readable) {
    Scanner scan = new Scanner(readable);
    final Pattern cellRef = Pattern.compile("([A-Za-z]+)!([A-Za-z]+)([1-9][0-9]*)");
    final Pattern sheetDeclaration =
        Pattern.compile("\\?([A-Za-z]+)");
    scan.useDelimiter("\\s+");
    while (scan.hasNext()) {
      int col;
      int row;
      String sheetName;
      while (scan.hasNext("#.*")) {
        scan.nextLine();
        scan.skip("\\s*");
      }

      String next = scan.next();

      Matcher m = cellRef.matcher(next);

      if (next.equals("?")) {
        sheetName = scan.next();
        builder.createSheetName(sheetName);
        scan.skip("\\s*");
      }else if (m.matches()) {
         sheetName = m.group(1);
         col = Coord.colNameToIndex(m.group(2));
         row = Integer.parseInt(m.group(3));

        scan.skip("\\s*");
        while (scan.hasNext("#.*")) {
          scan.nextLine();
          scan.skip("\\s*");
        }

        String contents = scan.nextLine();
        builder = builder.createCell(sheetName,col, row, contents);
      }else {
        throw new IllegalStateException("Expected cell ref or sheet name");
      }

    }

    return builder.createWorkbook();
  }
}


