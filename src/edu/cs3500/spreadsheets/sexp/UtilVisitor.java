package edu.cs3500.spreadsheets.sexp;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkBook;
import edu.cs3500.spreadsheets.model.Worksheet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class UtilVisitor {

  private UtilVisitor() {
    // Utility class; do not instantiate.
  }

  /**
   * @param sexp
   * @return true if sexp is SList
   */
  public static boolean isSList(Sexp sexp){
    return sexp instanceof SList;
  }

  /**
   * cast Sexp to number otherwise throw exception
   *
   * @param value the {@code Sexp}
   * @return {@code SNumber}
   * @throws IllegalArgumentException if not {@code Snumber}
   */
  static public SNumber requireNumber(Sexp value) {
    if (!(value instanceof SNumber)) {
      throw new IllegalArgumentException("Expected a number");
    }
    return (SNumber) value;
  }

  /**
   * check if Sexp is Snumber
   *
   * @param value
   * @return true if its Snumber otherise false
   */
  static public Boolean isNumber(Sexp value) {
    return value instanceof SNumber;
  }

  /**
   * expand the give sexp list if its has group, no evaluated just add the symbol
   *
   * @param sexp
   * @return the expanded list
   */
  public static List<Sexp> expandGroup(Sexp sexp, Worksheet model, WorkBook workbook) {
    List<Sexp> sexpList = new ArrayList<>();
    if(sexp.toString().contains("!")){
      String[] separate = sexp.toString().split("!");

      String[] split = separate[1].split(":");

      String col1 = split[0].replaceAll("\\d", "");
      String col2 = split[1].replaceAll("\\d", "");
      String firstS = split[0].replaceAll("\\D", "");
      String secondS = split[1].replaceAll("\\D", "");

      if (firstS.isEmpty() && secondS.isEmpty() && !col1.isEmpty()
          && !col2.isEmpty()) {

        HashMap<Coord, Sexp> map = new HashMap<Coord, Sexp>();
        HashMap<Coord, Sexp> col1Map =  workbook.getCellsInColumn(Coord.colNameToIndex(col1), separate[0]);
        HashMap<Coord, Sexp> col2Map =  workbook.getCellsInColumn(Coord.colNameToIndex(col2), separate[0]);
        if(!col1Map.isEmpty()){
          map.putAll(col1Map);
        }
        if(!col2Map.isEmpty()){
          map.putAll(col2Map);
        }

        for (Coord coord : map.keySet()) {
          sexpList.add(new SSymbol(separate[0] + "!" + coord.toString()));
        }


      }else {
        int colStart = Coord.colNameToIndex(String.valueOf(split[0].replaceAll("\\d", "")));
        int colEnd = Coord.colNameToIndex(String.valueOf(split[1].replaceAll("\\d", "")));

        int rowStart = Integer.parseInt(String.valueOf(split[0].replaceAll("\\D", "")));
        int rowEnd = Integer.parseInt(String.valueOf(split[1].replaceAll("\\D", "")));

        for (int i = colStart; i <= colEnd; i++) {
          for (int j = rowStart; j <= rowEnd; j++) {
            sexpList.add(new SSymbol(separate[0] + "!" +Coord.colIndexToName(i) + j));
          }
        }
      }

    }else {

      String[] split = sexp.toString().split(":");

      String col1 = split[1].replaceAll("\\d", "");
      String col2 = split[0].replaceAll("\\d", "");

      String secondS = split[0].replaceAll("\\D", "");
      String firstS = split[1].replaceAll("\\D", "");

      if (firstS.isEmpty() && secondS.isEmpty() && !col1.isEmpty()
          && !col2.isEmpty()) {// column group
        HashMap<Coord, Sexp> map = new HashMap<>();
        map.putAll(model.getCellsInColumn(Coord.colNameToIndex(col1)));
        map.putAll(model.getCellsInColumn(Coord.colNameToIndex(col2)));

        for (Coord coord : map.keySet()) {
          sexpList.add(new SSymbol(coord.toString()));
        }

      } else { // row group
        int colStart = Coord.colNameToIndex(String.valueOf(split[0].replaceAll("\\d", "")));
        int colEnd = Coord.colNameToIndex(String.valueOf(split[1].replaceAll("\\d", "")));

        int rowStart = Integer.parseInt(String.valueOf(split[0].replaceAll("\\D", "")));
        int rowEnd = Integer.parseInt(String.valueOf(split[1].replaceAll("\\D", "")));

        for (int i = colStart; i <= colEnd; i++) {
          for (int j = rowStart; j <= rowEnd; j++) {
            sexpList.add(new SSymbol(Coord.colIndexToName(i) + j));
          }
        }
      }
    }

    return sexpList;
  }

}
