package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.*;

import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;
import org.junit.Before;
import org.junit.Test;

public class WorkBookAdaptorTest {
  WorkBook<Sexp> wb;
  IModelAdaptorSpreadSheet adaptor;
  @Before
  public void setUp() throws Exception {
    wb = new WorkBookImpl();
    wb.add(new WorksheetModel(), "A");
    wb.add(new WorksheetModel(), "B");
    wb.add(new WorksheetModel(), "C");
    wb.set("A!A1", "1");
    wb.set("A!A2", "2");
    wb.set("A!C1", "3");
    wb.set("A!D1", "3");
    wb.set("A!A3", "(SUM A!A1 A!A2)");

    adaptor = new WorkBookAdaptor(wb, "A");
  }

  @Test
  public void getCell() {
    assertEquals(new SNumber(1),adaptor.getCell(new Coord(1,1)));
  }

  @Test
  public void set() {
    adaptor.set(new Coord(1, 1), "10");
    assertEquals(new SNumber(10),adaptor.getCell(new Coord(1,1)));

  }

  @Test(expected= UnsupportedOperationException.class)
  public void getMaxRows() {

    assertEquals(1,adaptor.getMaxRows());
  }

  @Test(expected= UnsupportedOperationException.class)
  public void getMaxColumns() {
    assertEquals(1,adaptor.getMaxColumns());
  }

  @Test
  public void getColumnMaxSize() {
    assertEquals(3, adaptor.getColumnMaxSize(1));
  }

  @Test
  public void getRawValue() {
    assertEquals("(SUM A!A1 A!A2)",adaptor.getRawValue(new Coord(1,3)));
    assertEquals("1.0",adaptor.getRawValue(new Coord(1,1)));
  }

  @Test
  public void removeCell() {
    assertEquals("(SUM A!A1 A!A2)",adaptor.getRawValue(new Coord(1,3)));
    adaptor.remove(new Coord(1,3));
    assertNull(adaptor.getCell(new Coord(1, 3)));
  }
}