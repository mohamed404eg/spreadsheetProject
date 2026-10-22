package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;

public class AdaptorSpreadSheet implements IModelAdaptorSpreadSheet {
 private final Worksheet<Sexp> model;

  public AdaptorSpreadSheet(Worksheet<Sexp> model){
    this.model = model;
  }

  @Override
  public Sexp getCell(Coord coord) {
    return  this.model.getCell(coord);
  }

  @Override
  public void set(Coord coord, String content) {
    this.model.set(coord, content);
  }

  @Override
  public int getMaxRows() {
    return this.model.getMaxRows();
  }

  @Override
  public int getMaxColumns() {
    return this.model.getMaxColumns();
  }

  @Override
  public int getColumnMaxSize(int column) {
    return this.model.getColumnMaxSize(column);
  }

  @Override
  public String getRawValue(Coord coord) {
    return this.model.getRawValue(coord);
  }

  @Override
  public void remove(Coord coord) {
    this.model.remove(coord);
  }

}
