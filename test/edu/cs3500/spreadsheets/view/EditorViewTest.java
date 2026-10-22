package edu.cs3500.spreadsheets.view;

import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.controller.IController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.sexp.Sexp;
import org.junit.Before;
import org.junit.Test;

public class EditorViewTest {

  Worksheet<Sexp> model;
  IController controller;
  IView view ;

    @Before
    public void init(){
      model = new WorksheetModel();
      model.set(new Coord(1, 1), "\"hi\"");
      model.set(new Coord(1, 2), "1");
      model.set(new Coord(1, 3), "1");
      model.set(new Coord(1, 4), "(SUM A2 A3)");
      model.set(new Coord(1, 5), "(SUM A2 A4)");
      controller = new Controller();
      view = new EditorView(controller);
      controller.run(model, view);
    }
  @Test(expected = IllegalArgumentException.class)
  public void testmouseCoordToCellCoordAtTheHeader(){
    view.mouseCoordToCellCoord(20, 20);
  }

  @Test
  public void testmouseCoordToCellCoordFirstCell(){
  assertEquals(new Coord(1, 1),   view.mouseCoordToCellCoord(40 + 1, 30 + 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testmouseCoordToCellCoordY(){
    assertEquals(new Coord(1, 1),   view.mouseCoordToCellCoord(40 + 1, 30 ));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testmouseCoordToCellCoordX(){
    assertEquals(new Coord(1, 1),   view.mouseCoordToCellCoord(40 , 30 + 1));
  }


  @Test
  public void coordToMouseCoordFirstCellTest(){
      assertEquals(new MouseCoord(40 + 60, 30 + 30), view.coordToMouseCoord(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void coordToMouseCoordColZero(){
    assertEquals(new MouseCoord(40 + 60 * 0, 30 + 30  * 0), view.coordToMouseCoord(new Coord(0, 1)));
  }
  @Test(expected = IllegalArgumentException.class)
  public void coordToMouseCoordSHeaderRowZero(){
    assertEquals(new MouseCoord(40 + 60 * 0, 30 + 30  * 0), view.coordToMouseCoord(new Coord(1, 0)));
  }


  // getContentHeight
  @Test
  public void testGetContentHeight(){
      assertEquals(30 + 30 * 5, view.getContentHeight());
  }

  // getContentWidth
  @Test
  public void testGetContentWidth(){
    assertEquals(40 + 60 * 10, view.getContentWidth());
  }

}