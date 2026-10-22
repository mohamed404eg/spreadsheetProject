package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import org.junit.Before;
import org.junit.Test;

public abstract class WorksheetBuilderImplTest {

  WorksheetBuilder<Worksheet<Sexp>> bd;

 abstract WorksheetBuilder<Worksheet<Sexp>> sbd();

 @Before
 public void  init(){
   bd = sbd();
 }


  @Test
  public void createCell() {
   bd.createCell(1, 1, "1");
   bd.createCell(1, 2, "=(SUM A1 1)");
   bd.createCell(1, 3, "true");
   bd.createCell(1, 4, "false");
   bd.createCell(1, 5, "A1");
    Worksheet<Sexp> model = bd.createWorksheet();

    assertEquals(new SNumber(2),model.getCell(new Coord(1, 2)));
    assertEquals(new SBoolean(true),model.getCell(new Coord(1, 3)));
    assertEquals(new SBoolean(false),model.getCell(new Coord(1, 4)));
    assertEquals(new SNumber(1),model.getCell(new Coord(1, 5)));
  }

  @Test
  public void createCell2() {
    bd.createCell(1, 1, "\"Jack says \\\"hi\\\". Path: C:\\\\Users\\\\\"");
    Worksheet<Sexp> model = bd.createWorksheet();
   assertEquals(new SString("Jack says \"hi\". Path: C:\\Users\\") ,model.getCell(new Coord(1, 1)));
  }



  public final static class WorksheetBuilderImpl extends WorksheetBuilderImplTest{
    @Override
    WorksheetBuilder<Worksheet<Sexp>> sbd() {
      return new edu.cs3500.spreadsheets.model.WorksheetBuilderImpl();
    }
  }

}