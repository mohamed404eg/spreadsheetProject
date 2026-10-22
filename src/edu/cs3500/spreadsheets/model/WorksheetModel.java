package edu.cs3500.spreadsheets.model;

import static edu.cs3500.spreadsheets.sexp.UtilVisitor.expandGroup;
import static edu.cs3500.spreadsheets.sexp.UtilVisitor.isSList;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.SexVisitorCheck;
import edu.cs3500.spreadsheets.sexp.evaluator;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorksheetModel implements Worksheet<Sexp> {

  HashMap<Coord, Sexp> sheet;
  SexpVisitor<Sexp> visitor;

  public WorksheetModel(SexpVisitor<Sexp> visitor) {
    this();
    this.visitor = visitor;
  }

  public WorksheetModel() {
    sheet = new HashMap<>();

    // the visitor need the model to evaluate later
    this.visitor = new evaluator(this);
  }


  @Override
  public Sexp getCell(Coord coord) {

    Sexp sexp = sheet.get(coord);
    if (sexp == null) {
      return null;
    }

    return sexp.accept(visitor);
  }


  @Override
  public void set(Coord coord, String content) {

    // check fr self reference or violate any rules // look at the doc interface
    checkViolate(coord, content);

    // otherwise set the Sexp
    sheet.put(coord,  Parser.parse(content));
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
    List<Sexp> root = new ArrayList<>();
    root.add(new SSymbol(coord.toString()));
    c.accept(new SexVisitorCheck(root, this));
  }

  // !!!
  @Override
  public int getMaxRows() {
    int max = 0;
    for (Coord  coord : sheet.keySet()) {
     max =  Math.max(max, coord.row);
    }
    return max;
  }

  // !!!
  @Override
  public int getMaxColumns() {
    int max = 0;
    for (Coord  coord : sheet.keySet()) {
    max =  Math.max(max, coord.col);
    }
    return max;
  }

  // !!! , we could return the max length of the column.
  @Override
  public int getColumnMaxSize(int column) {
    int size = 0;
    for(Coord  coord : sheet.keySet()) {
      if(coord.col == column) {
        size = Math.max(size, coord.row);
      }
    }
    return  size;
  }

  @Override
  public String getRawValue(Coord coord) {
    Sexp res = this.sheet.get(coord);

    if (res == null) {
      return null;
    } else {
      return res.toString();
    }
  }

  @Override
  public void remove(Coord coord) {
   // checkForResize(coord);
    this.sheet.remove(coord);
  }

  @Override
  public HashMap<Coord, Sexp> getCellsInRow(int row) {
    HashMap<Coord, Sexp> cells = new HashMap<>();

    for (Coord  coord : sheet.keySet()) {
      if(coord.row == row) {
        cells.put(coord, sheet.get(coord));
      }
    }

    return cells;
  }

  @Override
  public HashMap<Coord, Sexp> getCellsInColumn(int column) {
    HashMap<Coord, Sexp> cells = new HashMap<>();

    for (Coord  coord : sheet.keySet()) {
      if(coord.col == column) {
        cells.put(coord, sheet.get(coord));
      }
    }

    return cells;
  }
  @Override
  public void setVisitor(SexpVisitor<Sexp> visitor){
    this.visitor = visitor;
  }

  @Override
  public void set(Coord coord, String content, SexpVisitor<Void> checkVisitor) {
    Sexp c = Parser.parse(content);
    c.accept(checkVisitor);

    sheet.put(coord,  Parser.parse(content));
  }

  @Override
  public boolean containsRefSheet(String SheetName) {

    for (Sexp sexp : sheet.values()) {

      if (sexp.toString().contains("!") && ! isSList(sexp)) {
        String[] split = sexp.toString().split("!");
        if (split[0].equals(SheetName)) {
          return true;
        }
      }
      if(isSList(sexp)) {
        List<Sexp> expendList = expandGroup(sexp, this, null);
        for (Sexp expend : expendList) {
          if (expend.toString().contains("!") && ! isSList(expend)) {
            String[] split = expend.toString().split("!");
            if (split[0].equals(SheetName)) {
              return true;
            }
          }
        }
      }
    }


    return false;
  }

}
