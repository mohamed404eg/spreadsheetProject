package edu.cs3500.spreadsheets.controller;

import static org.junit.Assert.*;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkBook;
import edu.cs3500.spreadsheets.model.WorkBookImpl;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.view.IMultView;
import edu.cs3500.spreadsheets.view.MultView;
import java.util.ArrayList;
import java.util.List;
import javax.naming.OperationNotSupportedException;
import org.junit.Before;
import org.junit.Test;

/**
 * the goal of these tests is to test the controller call the right methods.
 */
public class MultControllerTest {

  WorkBook workBook;
  MultController controller;
  FkMultView view;
  @Before
  public void setUp() throws Exception {
    workBook = new WorkBookImpl();
    workBook.add(new WorksheetModel(), "1");

    controller = new MultController(workBook);

    view = new FkMultView(controller);
    controller.run(view);
  }

  @Test
  public void getWorkBookAdaptor() {
    // has been tested in IModelAdaptorSpreadSheetTest
  }

  @Test
  public void getSheetNames() {
    List<String> sheetNames = new ArrayList<>();
    sheetNames.add("1");

    assertEquals(sheetNames, controller.getSheetNames());
  }

  @Test
  public void mouseCoordToCellCoord() {
    assertEquals(new Coord(1,2),
        controller.mouseCoordToCellCoord(50, 70, "1"));
  }
  @Test(expected = UnsupportedOperationException.class)
  public void mouseCoordToCellCoordWOSheetName() {
    controller.mouseCoordToCellCoord(40, 70);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMouseCoordToCellCoord() {
    controller.mouseCoordToCellCoord(10, 30, "1");
  }


  @Test(expected = UnsupportedOperationException.class)
  public void selectCellWithOutSheetName() {
    controller.selectCell(new Coord(1,1));
  }

  @Test
  public void selectCell() {
    view.CallMouseCliked(50,50);
   assertEquals(new Coord(1,1), view.getSelectedCell());
   assertEquals("1", view.getSheetName());
  }

  @Test
  public void scrollChanged() {
    assertEquals(10, view.getRows(""));
    assertEquals(5, view.getColumns(""));

    int x1 = 100;
    int x2 = 200;
    int y1 = 100;
    int y2 = 100;
    view.setRectangle(x1, y1, x2, y2);

    // column, 340 - 300 <= 66 add 10c
    // rows 330 - 200 <= 68 no add

   // call scroll check
    view.CallScrollListener();
    assertEquals(10, view.getRows("")); // rows => y
    assertEquals(5 + 10, view.getColumns("")); // columns => x

     x1 = 100;
     x2 = 100;
     y1 = 200;
     y2 = 100;
    view.setRectangle(x1, y1, x2, y2);
    // column, 1240 - 300 <= 66 no
    // rows 330 - 300 <= 68 add 10r
    view.CallScrollListener();

    assertEquals(10 + 10, view.getRows("")); // rows => y
    assertEquals(15, view.getColumns("")); // columns => x
  }

  @Test
  public void testConformButton() {
    view.CallMouseCliked(50,50);
    assertEquals(new Coord(1,1), view.getSelectedCell());
    assertEquals("1", view.getSheetName());

    view.setInputValue("2");
    view.ConformButton();

    assertEquals(new SNumber(2), controller.getWorkBookAdaptor("1").getCell(new Coord(1,1)));
  }

}