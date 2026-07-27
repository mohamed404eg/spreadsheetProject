package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.Sexp;

public class WorksheetBuilderImpl implements WorksheetBuilder<Worksheet<Sexp>> {
  Worksheet<Sexp> model;
 public  WorksheetBuilderImpl(){
   model = new WorksheetModel();
 }

  @Override
  public WorksheetBuilder<Worksheet<Sexp>> createCell(int col, int row, String contents) {
   if(String.valueOf(contents.charAt(0)).equals( "=")){
     model.set(new Coord(col, row), contents.substring(1, contents.length()));
   }else {
     model.set(new Coord(col, row), contents);
   }
    return this;
  }

  @Override
  public Worksheet<Sexp> createWorksheet() {
    return model;
  }
}
