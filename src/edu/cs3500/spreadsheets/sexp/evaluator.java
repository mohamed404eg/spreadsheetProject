package edu.cs3500.spreadsheets.sexp;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Operations;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import java.util.ArrayList;
import java.util.List;

public class evaluator implements SexpVisitor<Sexp>, Operations {

  Worksheet<Sexp> model;

  public evaluator(Worksheet<Sexp> worksheetModel) {
    this.model = worksheetModel;
  }

  // for test
  public evaluator() {
  }

  @Override
  public Sexp visitBoolean(boolean b) {
    return new SBoolean(b);
  }

  @Override
  public Sexp visitNumber(double d) {
    return new SNumber(d);
  }

  @Override
  public Sexp visitSList(List<Sexp> l) {
    List<Sexp> expanded = new ArrayList<Sexp>(l);

    for (int i = 0; i < l.size(); i++) {
      if(!UtilVisitor.isSList(l.get(i))){
        if (l.get(i).toString().contains(":")) {
          expanded = new ArrayList<>(l.subList(0, i));
          expanded.addAll(expandGroup(l.get(i)));
          expanded.addAll(l.subList(i + 1, l.size()));
        }
      }
    }

    String opt = expanded.get(0).toString().toUpperCase();

    switch (opt) {
      case "SUM":
        return sum(expanded.subList(1, expanded.size()));
      case "SUB":
        return sub(expanded.get(1), expanded.get(2));
      case "PRODUCT":
        return product(expanded.subList(1, expanded.size()));
      case "SQRT":
        return sqrt(expanded.get(1));
      case "<":
        return lessThan(expanded.get(1), expanded.get(2));
      case "AVG":
        return avg(expanded.subList(1, expanded.size()));
      default:
        throw new IllegalArgumentException("The Operation not supported yet");
    }

  }

  private List<Sexp> expandGroup(Sexp sexp) {
    String[] split = sexp.toString().split(":");

    int colStart = Coord.colNameToIndex(split[0].replaceAll("\\d", ""));
    int colEnd = Coord.colNameToIndex(split[1].replaceAll("\\d", ""));

    int rowStart = Integer.parseInt(split[0].replaceAll("\\D", ""));
    int rowEnd = Integer.parseInt(split[1].replaceAll("\\D", ""));

    List<Sexp> sexpList = new ArrayList<>();
    for (int i = colStart; i <= colEnd; i++) {
      for (int j = rowStart; j <= rowEnd; j++) {
        sexpList.add(model.getCell(new Coord(i, j)));
      }
    }

    return sexpList;
  }


  @Override
  public Sexp visitSymbol(String s) {
    return model.getCell(Coord.NameToCoord(s)).accept(this);
  }

  @Override
  public Sexp visitString(String s) {
    return new SString(s);
  }

  @Override
  public Sexp sum(List<Sexp> arg) {
    SNumber sum = new SNumber(0);
    for (Sexp sexp : arg) {
      if (sexp != null) {
        Sexp v = sexp.accept(this);
        if (UtilVisitor.isNumber(v)) {
          sum = new SNumber(UtilVisitor.requireNumber(v).num + sum.num);
        }
      }
    }

    return sum;
  }

  // another implement for efficiency, didn't improve much , i think expand its not good efficiency
  public Sexp sum(List<Sexp> arg, double res) {
    Sexp ex = arg.remove(0);
    if (ex != null) {
      Sexp v = ex.accept(this);
      if (UtilVisitor.isNumber(v)) {
        res += UtilVisitor.requireNumber(v).num;
      }
    }
    if (arg.isEmpty()) {
      return new SNumber(res);
    } else {
      return sum(arg, res);
    }
  }

  @Override
  public Sexp sub(Sexp sb1, Sexp sb2) {
    return new SNumber(UtilVisitor.requireNumber(sb1.accept(this)).num - UtilVisitor.requireNumber(sb2.accept(this)).num);
  }

  @Override
  public Sexp product(List<Sexp> arg) {
    SNumber sum = new SNumber(1);
    boolean noNumber = true;
    for (Sexp sexp : arg) {
      if (sexp != null) {
        Sexp v = sexp.accept(this);
        if (UtilVisitor.isNumber(v)) {
          noNumber = false;
          sum = new SNumber(UtilVisitor.requireNumber(v).num * sum.num);
        }
      }
    }
    if (noNumber) {
      return new SNumber(0);
    }
    return sum;
  }

  @Override
  public Sexp lessThan(Sexp v1, Sexp v2) {
    SNumber num1 = UtilVisitor.requireNumber(v1.accept(this));
    SNumber num2 = UtilVisitor.requireNumber(v2.accept(this));
    return new SBoolean(num1.num < num2.num);
  }

  @Override
  public Sexp sqrt(Sexp v) {
    SNumber num = UtilVisitor.requireNumber(v.accept(this));
    return new SNumber(Math.sqrt(num.num));
  }

  @Override
  public Sexp avg(List<Sexp> l) {
    double count = l.size();
    SNumber sum = (SNumber) sum(l);
    return new SNumber(sum.num / count);
  }




  /**
   * check if the list has ":" that's group
   *
   * @param l
   * @return true if it has ":"
   */
  static public boolean containGroup(List<Sexp> l) {
    boolean res = false;
    for (int i = 0; i < l.size(); i++) {
      Sexp sexp = l.get(i);
      if (sexp instanceof SList) {
        res = containGroup((List<Sexp>) sexp);
      } else {
        if (sexp.toString().contains(":")) {
          res = true;
        }
      }
    }
    return res;
  }

}
