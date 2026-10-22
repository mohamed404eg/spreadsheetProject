package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.SexVisitorCheck;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import edu.cs3500.spreadsheets.sexp.evaluator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class WorkBookImpl implements WorkBook<Sexp> {
  private HashMap<String, Worksheet<Sexp>> sheets;

  public  WorkBookImpl(){
    sheets = new HashMap<>();
  }
  @Override
  public Sexp get(String name){
    String[] l = separate(name);
    cheekSheet(l[0]);

  return sheets.get(l[0]).getCell(Coord.NameToCoord(l[1]));
  }

  @Override
  public void set(String name, String content) {
    String[] l = separate(name);
    cheekSheet(l[0]);
    ArrayList<Sexp> root = new ArrayList<>();

    root.add(new SSymbol(name));

    SexpVisitor<Void> cv = new SexVisitorCheck(sheets.get(l[0]),this, root);

    sheets.get(l[0]).set(
        Coord.NameToCoord(l[1])
        , content, cv);
  }

  @Override
  public void removeCell(String name) {
    String[] l = separate(name);
    cheekSheet(l[0]);
    sheets.get(l[0]).remove(Coord.NameToCoord(l[1]));
  }

  @Override
  public HashMap<Coord, Sexp> getCellsInRow(int row, String sheetName) {
    if(row <= 0){
      throw new IllegalArgumentException("Row out of bounds <= 0");
    }
    cheekSheet(sheetName);
    return sheets.get(sheetName).getCellsInRow(row);
  }

  @Override
  public HashMap<Coord, Sexp> getCellsInColumn(int column, String sheetName) {
    if(column <= 0){
      throw new IllegalArgumentException("column out of bounds <= 0");
    }
    cheekSheet(sheetName);
    return sheets.get(sheetName).getCellsInColumn(column);
  }

  @Override
  public String getRawValue(String name) {
    String[] l = separate(name);

    return sheets.get(l[0]).getRawValue(Coord.NameToCoord(l[1]));
  }

  @Override
  public void add(Worksheet<Sexp> sheet, String sheetName) {
    if(sheets.containsKey(sheetName)){
      throw new IllegalArgumentException("Sheet with same name already exists");
    }

    sheet.setVisitor(new evaluator(sheet, this));

    sheets.put(sheetName, sheet);
  }

  @Override
  public void remove(String sheetName) {
    for( Entry<String, Worksheet<Sexp>> sheet  : sheets.entrySet()){
      if(!sheet.getKey().equals(sheetName) && sheet.getValue().containsRefSheet(sheetName)){
        throw new IllegalArgumentException("other sheet has reference to this sheet. sheetName: " + sheet.getValue());
      }
    }
    sheets.remove(sheetName);
  }

  @Override
  public List<String> getSheetNames() {
    return new ArrayList<>(sheets.keySet());
  }


  /**
   * return the sheetName + cellName.
   * @param in
   * @return Array[0] = sheetName , Array[1] = cellName
   * @throws IllegalArgumentException if there's any missing value.
   */
  private String[] separate(String in) {
    String[] list =  in.split("!");
    if(list.length < 2 ){
      throw new IllegalArgumentException("Name error");
    }
    if(list[0].isEmpty()){
      throw new IllegalArgumentException("Sheet name is empty");
    }
    if(list[1].isEmpty()){
      throw new IllegalArgumentException("Cell name is empty");
    }
    return list;
  }

  /**
   * check the rules for the sheetName variable.
   * @param SheetName
   * @throws IllegalArgumentException if any rules violated.
   */
  void cheekSheet(String SheetName){
    if(!sheets.containsKey(SheetName)){
      throw new IllegalArgumentException("Sheet name " + SheetName + " not exists");
    }
  }
}
