package edu.cs3500.spreadsheets.model;


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
   *     <LI>(SUM number or cell or groub of cell like A1, A1:B1, B2:C5)</LI>
   *     <li>(SUB sexp sexp)</li>
   *     <li>(PRODUCT number or cell or groub of cell like A1, A1:B1, B2:C5)</li>
   *     <li>(SQRT Sexp)</li>
   *     <li>(< Sexp Sexp) returns whether the first is less than the second.</li>
   *     <li>(AVG  number or cell or groub of cell like A1, A1:B1, B2:C5) take the average </li>
   *     <li>for A1:A2 this mean the same column just(A1-> A2), B2:C5 this mean all column from B2=38 to C5=67 w/ all there cell</li>
   *     <li>(i.e. C42:D53 A1:A4) reference it should be to right or below</li>
   *     <li>a Reference like A1</li>
   *   </ul>
   * </P>
   *
   * @param coord   the coordinate
   * @param content the value
   * @throws IllegalArgumentException if the content refer to itself (i.e. A1 = (SUM A1 + 1) or A1 =
   *                                  B1, B1=(SUM A1 1) ) ,
   *                                  <p>if any Sexp To formal not lead to number or value is
   *                                  missing,</p>
   *                                  <P>if second reference not to right or below the first (i.e.
   *                                  B1:A1 A2:A1)</P>
   *                                  <p> if {@code coord} is invalid</p>
   */
  void set(Coord coord, String content);

  /**
   * get the max rows exits at the worksheet
   *
   * @return the max rows
   */
  int getRows();

  /**
   * get the max columns exits at the worksheet
   *
   * @return the max columns
   */
  int getColumns();

  /**
   * the rows length of the given column
   *
   * @param column the column inquare about, 1-based index
   * @return the length of the column
   * @throws IllegalArgumentException if the column  doesnt exist
   */
  int getColumnSize(int column);

  /**
   * get the value that exist at this coordinate cell, return null if cell not set.
   *
   * @param coord the coordinate
   * @return the raw value without evaluate , {@code null} if no value has been stored there.
   * @throws IllegalArgumentException if {@code coord} is invalid
   */
  String getRawValue(Coord coord);
}
