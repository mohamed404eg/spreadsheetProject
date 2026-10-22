package edu.cs3500.spreadsheets.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.cs3500.spreadsheets.model.AdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IModelAdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.sexp.SNumber;
import org.junit.Before;
import org.junit.Test;


public class IModelAdaptorSpreadSheetTest {

  IModelAdaptorSpreadSheet controller;

  @Before
  public void init() {
    WorksheetBuilderImpl bd = new WorksheetBuilderImpl();
    controller =  new AdaptorSpreadSheet(bd.createWorksheet());
  }

@Test
  public void getAndSetCell(){
    controller.set(new Coord(1, 1), "1");
    assertEquals(new SNumber(1), controller.getCell(new Coord(1, 1)));
  }


  @Test
  public void getRows(){
    assertEquals(0, controller.getMaxRows());
  }
  @Test
  public void getColumns(){
    assertEquals(0, controller.getMaxColumns());
  }

  @Test
  public void getColumnSize(){
    assertEquals(0, controller.getColumnMaxSize(1));
  }


  @Test
  public void getRawValue(){
    controller.set(new Coord(1,1), "(SUM 1 2)");
    assertEquals("(SUM 1.0 2.0)", controller.getRawValue(new Coord(1, 1)));
  }


  @Test
  public void remove(){
    controller.set(new Coord(1,1), "(SUM 1 2)");
    assertEquals("(SUM 1.0 2.0)", controller.getRawValue(new Coord(1, 1)));
    controller.remove(new Coord(1, 1));
    assertNull(controller.getCell(new Coord(1, 1)));
  }



}
