package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * the most of the methods will reference to specific sheet to the workbook.
 */
public class WorkBookAdaptor implements IModelAdaptorSpreadSheet{
  WorkBook<Sexp> wb;
  String sheetName;
 public WorkBookAdaptor(WorkBook workBook, String SheetName) {
    this.wb = workBook;
    this.sheetName = SheetName;
  }
  @Override
  public Sexp getCell(Coord coord) {
    return this.wb.get(sheetName+"!"+coord);
  }

  @Override
  public void set(Coord coord, String content) {
   this.wb.set(sheetName+"!"+coord, content);
  }

  @Override
  public int getMaxRows() {
   throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getMaxColumns() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getColumnMaxSize(int column) {
    return this.wb.getCellsInColumn(column, sheetName).size();
  }

  @Override
  public String getRawValue(Coord coord) {
    return this.wb.getRawValue(sheetName+"!"+coord);
  }

  @Override
  public void remove(Coord coord) {
   wb.removeCell(sheetName+"!"+coord);
  }

}
