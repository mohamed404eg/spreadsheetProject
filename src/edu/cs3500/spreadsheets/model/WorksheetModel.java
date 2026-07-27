package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.SexVisitorCheck;
import edu.cs3500.spreadsheets.sexp.evaluator;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.ArrayList;
import java.util.List;

public class WorksheetModel implements Worksheet<Sexp> {

  ArrayList<ArrayList<Sexp>> sheet;
  private int maxColumns = 10;
  private int maxRows = 5;
  SexpVisitor<Sexp> visitor;

  public WorksheetModel(SexpVisitor<Sexp> visitor) {
    this();
    this.visitor = visitor;
  }

  public WorksheetModel() {
    sheet = new ArrayList<>();

    for (int i = 0; i < maxColumns; i++) {
      ArrayList<Sexp> row = new ArrayList<>();
      for (int j = 0; j < maxRows; j++) {
        row.add(null);
      }
      sheet.add(row);
    }
    // the visitor need the model to evaluate later
    this.visitor = new evaluator(this);
  }


  @Override
  public Sexp getCell(Coord coord) {

    if (coord.row > maxRows || coord.col > maxColumns) {
      return null;
    }
    Sexp sexp = sheet.get(coord.col - 1).get(coord.row - 1);
    if (sexp == null) {
      return null;
    }
    return sexp.accept(visitor);
  }

  @Override
  public void set(Coord coord, String content) {

    if (coord.row > maxRows || coord.col > maxColumns) {
      int[] size = detSize(coord.row, coord.col);
      resize(size[0], size[1]);
    }

    // check fr self reference or violate any rules // look at the doc interface
    checkViolate(coord, content);

    // otherwise set the Sexp
    sheet.get(coord.col - 1).set(coord.row - 1, Parser.parse(content));
  }


  /**
   * determent the new size of the sheet
   *
   * @param maxRows
   * @param maxColumns
   * @return array size 2 , 0 = newMaxRows, 1 = newMaxColumns
   */
  int[] detSize(int maxRows, int maxColumns) {
    int[] size = new int[2];
    size[0] = maxRows * 2;
    size[1] = maxColumns * 2;
    return size;
  }

  /**
   * resize the inner data store
   * <p>EFFECT: update maxRows, maxColumns, sheet</p>
   *
   * @param maxRows    the new max size
   * @param maxColumns the new max size
   */
  void resize(int maxRows, int maxColumns) {
    ArrayList<ArrayList<Sexp>> resizeSheet = new ArrayList<>();

    for (int i = 0; i < maxColumns; i++) {
      ArrayList<Sexp> row = new ArrayList<>();
      if (i < sheet.size()) {
        for (int r = 0; r < maxRows; r++) {
          if (r < sheet.get(i).size()) { // copy every row
            row.add(sheet.get(i).get(r));
          } else { // add new cell
            row.add(null);
          }
        }
      } else {
        for (int r = 0; r < maxRows; r++) {
          row.add(null);
        }
      }
      resizeSheet.add(row);
    }

    sheet = new ArrayList<>(resizeSheet);
    this.maxRows = maxRows;
    this.maxColumns = maxColumns;
  }

  /**
   * check if the content violate any rules
   *
   * @param coord
   * @param content
   * @throws IllegalArgumentException if any violate found
   */
  void checkViolate(Coord coord, String content) {
    Sexp c = Parser.parse(content);
    List<Sexp> list = new ArrayList<>();
    list.add(new SSymbol(coord.toString()));
    c.accept(new SexVisitorCheck(list, this));
  }

  @Override
  public int getRows() {
    return this.maxRows;
  }

  @Override
  public int getColumns() {
    return this.maxColumns;
  }

  @Override
  public int getColumnSize(int column) {
    return this.sheet.get(column - 1).size();
  }

  @Override
  public String getRawValue(Coord coord) {
    if (coord.row > maxRows || coord.col > maxColumns) {
      return null;
    }
    Sexp res = this.sheet.get(coord.col - 1).get(coord.row - 1);
    if (res == null) {
      return null;
    } else {
      return res.toString();
    }
  }


}
