package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.WorkBookReader.WorkbookBuilder;
import edu.cs3500.spreadsheets.sexp.Sexp;

public class WorkbookBuilderImpl implements WorkbookBuilder<Sexp> {
    WorkBook<Sexp> workbook;

  public WorkbookBuilderImpl(){
    workbook = new WorkBookImpl();
  }


  @Override
  public WorkbookBuilder<Sexp> createCell(String sheetName, int col, int row,
      String contents) {
    workbook.set(sheetName + "!" + new Coord(col, row), contents);
    return this;
  }

  @Override
  public WorkBook<Sexp> createWorkbook() {
    return workbook;
  }

  @Override
  public void createSheetName(String sheetName) {
    workbook.add(new WorksheetModel(), sheetName);
  }
}
