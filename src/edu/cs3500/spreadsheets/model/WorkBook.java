package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.HashMap;
import java.util.List;

/**
 * @implNote {name} => "sheetName!cellName".
 * <p> no exist sheet name @throws IllegalArgumentException. </p>
 * @param <T>
 */
public interface WorkBook<T> {

  /**
   * get the cell evaluated.
   * @param name the cell name plus worksheet name.
   * @return the value of the evaluated cell, null for none set cell.
   */
  T get(String name);

  /**
   * set cell content.
   * @param name the cell name plus worksheet name.
   * @param content the content {@linkplain Worksheet#set(Coord, String)}
   */
  void set(String name, String content);

  /**
   * Resolve the sheet and cell, then return the unevaluated content
   *
   * @param name the coordinate
   * @return the raw value without evaluate , {@code null} if no value has been stored there.
   * @throws IllegalArgumentException if {@code coord} is invalid
   */
  String getRawValue(String name);

  /**
   * remove the given name from the sheet {@linkplain Worksheet#remove(Coord)}
   * @param name the name of Sheet + the cell.
   * @throws IllegalArgumentException if sheet not exist.
   */
  void removeCell(String name);

  /**
   * get all row in that rows a cross all column.
   * @param row
   * @param sheetName
   * @return all row in that rows a cross all column.
   */
  HashMap<Coord, T> getCellsInRow(int row, String sheetName);


  /**
   * get all rows in that column.
   * @param column
   * @param sheetName
   * @return all row in that rows a cross all column.
   */
  HashMap<Coord, T> getCellsInColumn(int column, String sheetName);


  /**
   * add the given sheet w/ given name.
   * @param sheet
   * @param sheetName
   * @throws IllegalArgumentException if there's sheet w/ the same name.
   */
  void add(Worksheet<T> sheet, String sheetName);

  /**
   * remove the given sheetName.
   * @param sheetName
   * @throws IllegalArgumentException if the sheet been use in other sheet.
   */
  void remove(String sheetName);


  /**
   * get all sheet name.
   * @return the sheets name.
   */
  List<String> getSheetNames();


}
