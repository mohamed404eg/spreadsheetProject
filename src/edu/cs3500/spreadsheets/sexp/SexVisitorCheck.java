package edu.cs3500.spreadsheets.sexp;


import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import java.util.ArrayList;
import java.util.List;

public class SexVisitorCheck implements SexpVisitor<Void> {

  List<Sexp> rootList;
  Worksheet<Sexp> model;

  public SexVisitorCheck(List<Sexp> list, Worksheet<Sexp> model) {
    this();
    this.rootList = new ArrayList<>(list);
    this.model = model;
  }

  public SexVisitorCheck() {
    this.rootList = new ArrayList<>();
  }

  @Override
  public Void visitBoolean(boolean b) {
    return null;
  }

  @Override
  public Void visitNumber(double d) {
    return null;
  }

  @Override
  public Void visitSList(List<Sexp> l) {
    List<Sexp> expanded = new ArrayList<Sexp>(l);
    // rule for :
    for (int i = 0; i < l.size(); i++) {
      if(!isSList(l.get(i))){
      if (l.get(i).toString().contains(":")) {
        String[] arr = l.get(1).toString().split(":");
        String col1 = arr[1].replaceAll("\\d", "");
        String col2 = arr[0].replaceAll("\\d", "");
        int second = Integer.parseInt(arr[0].replaceAll("\\D", ""));
        int first = Integer.parseInt(arr[1].replaceAll("\\D", ""));
        if (col1.equals(col2)) { // A == A Same Column
          if (first <= second) {
            throw new IllegalArgumentException("Wrong Direction: " + l.get(1).toString());
          }
        } else {  // B1:A1 2:1 diff column
          if (Coord.colNameToIndex(col2) >= Coord.colNameToIndex(col1)) {
            throw new IllegalArgumentException("Wrong Direction: " + l.get(1).toString());
          }
        }

        // expand // add here for efficiency
        expanded = new ArrayList<>(l.subList(0, i));
        List<Sexp> expand = expandGroup(l.get(i));
        expanded.addAll(expand);
        expanded.addAll(l.subList(i + 1, l.size()));

      }
    }
    }

//    System.out.println("root: "+rootList);
//    System.out.println("exp: "+ expanded);
    for (int j = 1; j < expanded.size(); j++) {
      expanded.get(j).accept(new SexVisitorCheck(rootList, model));
    }

    String opt = l.get(0).toString();

    // sqrt
    if (opt.equalsIgnoreCase("SQRT")) {
      if (l.size() < 2) {
        throw new IllegalArgumentException("Missing argument need one: (SQRT a b)");
      }
    }

    if (opt.equalsIgnoreCase("SUB")) {
      if (evaluator.containGroup(l)) {
        throw new IllegalArgumentException("SUB Can't take group.");
      }
      // just take tow arg + the sign
      if (expanded.size() > 3) {
        throw new IllegalArgumentException("SUB Can't take more 2 arg.");
      }
      if (expanded.size() < 3) {
        throw new IllegalArgumentException("SUB Can't take less than 2 arg.");
      }
    }

    if (opt.equalsIgnoreCase("AVG")) {
      avg(expanded.subList(1, expanded.size()));
    }

    return null;
  }

  /**
   * check the rules for the average operations, look at the interface to knew more.
   *
   * @param l the avg args
   */
  private void avg(List<Sexp> l) {
    double count = l.size();
    boolean Numerical = false;
    for (int i = 0; i < l.size(); i++) {
      if (l.get(i) != null) {
        Sexp se = model.getCell(Coord.NameToCoord(l.get(i).toString()));
        if (se != null) {
          if (UtilVisitor.isNumber(se)) {
            Numerical = true;
          }
        }
      }

    }
    if (count < 1 || !Numerical) {
      throw new ArithmeticException("can't have divisor zero");
    }

  }

  @Override
  public Void visitSymbol(String s) {
    int row = Integer.parseInt(String.valueOf(s.charAt(1)));
    int col = Coord.colNameToIndex(String.valueOf(s.charAt(0)));
    String v = model.getRawValue(new Coord(col, row));

    for (int i = 0; i < rootList.size(); i++) {
      if (new SSymbol(s).equals(rootList.get(i))) { // self reference
        throw new IllegalArgumentException("Self Reference: " + s );
      }
      if (v == null) {
        return null;
      }
    }

    // recursively go through the formula
    List<Sexp> newPath = new ArrayList<>(rootList);
    newPath.add(new SSymbol(s));
    Parser.parse(v).accept(new SexVisitorCheck(newPath, model));

    return null;
  }

  @Override
  public Void visitString(String s) {
    return null;
  }

  /**
   * expand the give sexp list if its has group, no evaluated just add the symbol
   *
   * @param sexp
   * @return the expanded list
   */
  private List<Sexp> expandGroup(Sexp sexp) {
    String[] split = sexp.toString().split(":");

    int colStart = Coord.colNameToIndex(String.valueOf(split[0].replaceAll("\\d", "")));
    int colEnd = Coord.colNameToIndex(String.valueOf(split[1].replaceAll("\\d", "")));

    int rowStart = Integer.parseInt(String.valueOf(split[0].replaceAll("\\D", "")));
    int rowEnd = Integer.parseInt(String.valueOf(split[1].replaceAll("\\D", "")));

    List<Sexp> sexpList = new ArrayList<>();
    for (int i = colStart; i <= colEnd; i++) {
      for (int j = rowStart; j <= rowEnd; j++) {
        sexpList.add(new SSymbol(Coord.colIndexToName(i) + j));
      }
    }

    return sexpList;
  }

  /**
   * @param sexp
   * @return true if sexp is SList
   */
  private boolean isSList(Sexp sexp){
    return sexp instanceof SList;
  }

}
