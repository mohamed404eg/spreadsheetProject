package edu.cs3500.spreadsheets.sexp;


import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkBook;
import edu.cs3500.spreadsheets.model.Worksheet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SexVisitorCheck implements SexpVisitor<Void> {

  List<Sexp> rootList;
  Worksheet<Sexp> model;
  WorkBook<Sexp> workbook;

  public SexVisitorCheck(List<Sexp> list, Worksheet<Sexp> model) {
    this();
    this.rootList = new ArrayList<>(list);
    this.model = model;
  }

  public SexVisitorCheck(Worksheet<Sexp> worksheetModel, WorkBook<Sexp> workbook, List<Sexp> list) {
    this.model = worksheetModel;
    this.workbook = workbook;
    this.rootList = new ArrayList<>(list);
  }

  public SexVisitorCheck(WorkBook<Sexp> workbook, List<Sexp> list) {
    this.workbook = workbook;
    this.rootList = new ArrayList<>(list);
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

  //  @Override
  public Void visitSList(List<Sexp> l) {
    List<Sexp> expanded = new ArrayList<Sexp>();
    // rule for :
    for (int i = 0; i < l.size(); i++) { // check for grouping rule.
      if (!isSList(l.get(i)) && l.get(i).toString().contains(":")) {
        String[] arr ;
        if(l.get(i).toString().contains("!")) {
          String[] split = separate(l.get(i).toString());
          arr =  split[1].split(":");
        }else {
          arr = l.get(i).toString().split(":");
        }

        String col1 = arr[0].replaceAll("\\d", "");
        String col2 = arr[1].replaceAll("\\d", "");

        String firstS = arr[0].replaceAll("\\D", "");
        String secondS = arr[1].replaceAll("\\D", "");

        if (firstS.isEmpty() && secondS.isEmpty() && !col1.isEmpty()
            && !col2.isEmpty()) {// column group
          // check column self reference.
          for (Sexp ss : rootList) {
            String rootC = ss.toString().replaceAll("\\d", ""); // column name
            if (col1.equals(rootC) || col2.equals(rootC)) {
              throw new IllegalArgumentException("Column self references error.");
            }
          }
        } else { // row group
          int second = Integer.parseInt(secondS);
          int first = Integer.parseInt(firstS);

          if (col1.equals(col2)) { // A == A Same Column
            if (first >= second) {
              throw new IllegalArgumentException(
                  "Wrong Direction Row first should be less than second: " + l.get(1).toString() + ", first: " + first + " second: " + second);
            }
          } else {  // B1:A1 2:1 diff column
            if (Coord.colNameToIndex(col1) >= Coord.colNameToIndex(col2)) {
              throw new IllegalArgumentException("Wrong Direction: " + l.get(1).toString()+ ", ColumnOne:"  + col1  + " ColumnTow: " + col2);
            }
          }

        }
      }
    }

    // expand group.
    for (int i = 0; i < l.size(); i++) {
      if (!isSList(l.get(i)) && l.get(i).toString().contains(":")) {
        expanded.addAll(expandGroup(l.get(i)));
      } else {
        // this item no need for expand
        expanded.add(l.get(i));
      }
    }

//    System.out.println("root: "+rootList);
//    System.out.println("exp: "+ expanded);
    // in case for formula
    for (int j = 1; j < expanded.size(); j++) {
      expanded.get(j).accept(new SexVisitorCheck(model, workbook, rootList));
    }

    String opt = expanded.get(0).toString();

    // sqrt
    if (opt.equalsIgnoreCase("SQRT")) {
      if (expanded.size() < 2) {
        throw new IllegalArgumentException("Missing argument need one: (SQRT a b)");
      }
    }

    if (opt.equalsIgnoreCase("SUB")) {
      if (containGroup(expanded)) {
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

    if (opt.equalsIgnoreCase("<")) {
      if (expanded.size() > 3) {
        throw new IllegalArgumentException("< Can't take more 2 arg.");
      }
      if (expanded.size() < 3) {
        throw new IllegalArgumentException("< Can't take less than 2 arg.");
      }
      if (containGroup(l)) {
        throw new IllegalArgumentException("< Can't take group.");
      }
      if (lessTHan(expanded.subList(1, expanded.size()))) {
        throw new IllegalArgumentException("< Expected Numbers.");
      }
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
        Sexp se;
        if (l.get(i).toString().contains("!")) {
          se = workbook.get(l.get(i).toString());
        } else {
          se = model.getCell(Coord.NameToCoord(l.get(i).toString()));
        }
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
    // direct self reference => A1 = A1
    for (int i = 0; i < rootList.size(); i++) {
      if (new SSymbol(s).equals(rootList.get(i))) { // self reference
        throw new IllegalArgumentException("Self Reference: " + s);
      }
    }

    String v;

    if (!s.contains("!")) {
      Coord co = Coord.NameToCoord(s);
       v = model.getRawValue(co);
    } else {
       v = workbook.getRawValue(s);
    }

    // no need for go deep if null.
    if (v == null) {
      return null;
    }

    // recursively go through the formula
    List<Sexp> newPath = new ArrayList<>(rootList);
    newPath.add(new SSymbol(s));
    Parser.parse(v).accept(new SexVisitorCheck(model, workbook, newPath));

    // double self reference => A1= B1 & B1= A1
    for (int i = 0; i < rootList.size(); i++) {
      if (new SSymbol(v).equals(rootList.get(i))) { // self reference
        throw new IllegalArgumentException("Self Reference: " + s);
      }
    }

    return null;
  }


  /**
   * return the sheetName + cellName.
   *
   * @param in
   * @return Array[0] = sheetName , Array[1] = cellName
   * @throws IllegalArgumentException if there's any missing value.
   */
  private String[] separate(String in) {
    String[] list = in.split("!");
    if (list.length < 2) {
      throw new IllegalArgumentException("Name error");
    }
    if (list[0].isEmpty()) {
      throw new IllegalArgumentException("Sheet name is empty");
    }
    if (list[1].isEmpty()) {
      throw new IllegalArgumentException("Cell name is empty");
    }
    return list;
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
    return UtilVisitor.expandGroup(sexp, model, workbook);
  }

  /**
   * @param sexp
   * @return true if sexp is SList
   */
  private boolean isSList(Sexp sexp) {
    return sexp instanceof SList;
  }

  /**
   * check if the list has ":" that's group
   *
   * @param l
   * @return true if it has ":"
   */
  public boolean containGroup(List<Sexp> l) {
    boolean res = false;
    for (int i = 0; i < l.size(); i++) {
      Sexp sexp = l.get(i);
      if (sexp instanceof SList) {
        res = containGroup(expandGroup(sexp));
      } else {
        if (sexp.toString().contains(":")) {
          res = true;
        }
      }
    }
    return res;
  }


  /**
   *
   * @param l
   * @return true if any Sexp not evaluate to numbers otherwise false.
   */
  private boolean lessTHan(List<Sexp> l) {
    Sexp st1 = null;
    Sexp st2 = null;
    if (l.get(0).toString().contains("!")) {
      st1 = workbook.get(l.get(0).toString());
    } else {
      if(UtilVisitor.isNumber(l.get(0))) {
        st1 = l.get(0);
      }else {
        if(UtilVisitor.isSList(l.get(0))) {
          l.get(0).accept(new SexVisitorCheck(model, workbook, rootList));
        }else {
          st1 = model.getCell(Coord.NameToCoord(l.get(0).toString()));
        }
      }
    }
    if (l.get(1).toString().contains("!")) {
      st2 = workbook.get(l.get(1).toString());
    } else {
      if(UtilVisitor.isNumber(l.get(1))) {
        st2 = l.get(1);
      }else {
        if(UtilVisitor.isSList(l.get(1))) {
          l.get(0).accept(new SexVisitorCheck(model, workbook, rootList));
        }else {
          st2 = model.getCell(Coord.NameToCoord(l.get(1).toString()));
        }
      }
    }
    return !(UtilVisitor.isNumber(st1) && UtilVisitor.isNumber(st2));
  }

}
