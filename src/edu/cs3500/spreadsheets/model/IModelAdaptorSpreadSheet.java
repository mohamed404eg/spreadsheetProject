package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;

public interface IModelAdaptorSpreadSheet {
  Sexp getCell(Coord coord);
  void set(Coord coord, String content);
  int getMaxRows();
  int getMaxColumns();
  int getColumnMaxSize(int column);
  String getRawValue(Coord coord);
  void remove(Coord coord);
}
