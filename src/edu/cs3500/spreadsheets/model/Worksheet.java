package edu.cs3500.spreadsheets.model;


import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * the interface for represent worksheet, the index biased-1 and all coord is positive
 *
 * @param <T> the datatype of the cell
 * @throw IllegalArgumentException if coord less than 1
 */
public interface Worksheet<T> {

  /**
   * Returns the value stored at the given coordinate.
   *
   * @param coord the cell coordinate
   * @return the value stored at the given coordinate, or {@code null} if no value has been stored
   * there.
   * @throws IllegalArgumentException if coord less than 1
   */
  T getCell(Coord coord);

  /**
   * set the given content at the give coordinate, even if the given coordinate is empty will set
   *
   * <p>
   * {@code T}:
   * </p>
   * <li>
   * No formula is permitted to refer to itself, though, either directly or indirectly
   * </li>
   * <li>any formula that depends on one of these erroneous formulas must itself be in error.</li>
   * <li>“Let A1 equal A1 plus 1”, “Let A1 equal B1, and let B1 equal A1 plus 1”, error</li>
   *
   * <P>
   * {@code formula}
   * <p>SExp it could be SExp or integer direct</p>
   *   <ul>
   *     <LI>(SUM sexp...)</LI>
   *     <li>(SUB sexp sexp)</li>
   *     <li>(PRODUCT sexp...)</li>
   *     <li>(SQRT Sexp)</li>
   *     <li>(< Sexp Sexp) returns whether the first is less than the second.</li>
   *     <li>(AVG  Sexp...) take the average </li>
   *     <li>for A1:A2 this mean the same column just(A1-> A2), B2:C5 this mean all column from B2=38 to C5=67 w/ all there cell</li>
   *     <li>(i.e. C42:D53 A1:A4) reference it should be to right or below</li>
   *     <li>a Reference like A1</li>
   *   </ul>
   * </P>
   * <h2>Notes:</h2>
   * <P> if Formula takes n args then gives it group of n args it will works normal as gives it args been expanded. </P>
   * <p>exclude from any grouping: `<` </p>
   * @param coord   the coordinate
   * @param content the value
   * @throws IllegalArgumentException if the content refer to itself (i.e. A1 = (SUM A1 + 1) or A1 =
   *                                  B1, B1=(SUM A1 1) ) ,
   *                                  <p>if any Sexp To formal not lead to number or value is
   *                                  missing,</p>
   *                                  <P>if second reference not to right or below the first (i.e.
   *                                  B1:A1 A2:A1)</P>
   *                                  <p> if {@code coord} is invalid</p>
   *                                  <p>any Invalid symbol Syntax like (A, HI)</p>
   */
  void set(Coord coord, String content);

  /**
   * get the max rows exits at the worksheet
   *
   * @return the max rows
   */
  int getMaxRows();

  /**
   * get the max columns exits at the worksheet
   *
   * @return the max columns
   */
  int getMaxColumns();

  /**
   * the rows max length of the given column.
   *
   * @param column the column inquare about, 1-based index
   * @return the length of the column
   * @throws IllegalArgumentException if the column  doesnt exist
   */
  int getColumnMaxSize(int column);

  /**
   * get the value that exist at this coordinate cell, return null if cell not set.
   *
   * @param coord the coordinate
   * @return the raw value without evaluate , {@code null} if no value has been stored there.
   * @throws IllegalArgumentException if {@code coord} is invalid
   */
  String getRawValue(Coord coord);


  /**
   * remove the given coord from the sheet,
   * that mean it will set to null.
   * @param coord
   */
  void remove(Coord coord);


  /**
   * get all row in that rows a cross all column.
   * @param row
   * @return all row in that rows a cross all column.
   */
  HashMap<Coord, T> getCellsInRow(int row);


  /**
   * get all rows in that column.
   * @param column
   * @return all row in that rows a cross all column.
   */
  HashMap<Coord, T> getCellsInColumn(int column);

  /**
   * get all rows in that column.
   * @param column
   * @return all row in that rows a cross all column w/o evaluate.
   */
 // HashMap<Coord, String> getCellsInColumnRaw(int column);

  /**
   * Set the visitor will be used for evaluate.
   * @param visitor
   */
  void setVisitor(SexpVisitor<Sexp> visitor);

  void set(Coord coord, String content, SexpVisitor<Void> visitor);

  /**
   * check if the work sheet has any reference to the given sheet.
   * @param SheetName
   * @return true if it has otherwise false.
   */
  boolean containsRefSheet(String SheetName);
}
