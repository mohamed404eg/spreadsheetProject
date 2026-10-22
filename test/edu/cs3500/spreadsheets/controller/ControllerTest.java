package edu.cs3500.spreadsheets.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.MouseCoord;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

  Worksheet<Sexp> model;
  IController controller;
  FakeIView fakeIView;

  @Before
  public void init() {
    WorksheetBuilderImpl bd = new WorksheetBuilderImpl();

    model = bd.createWorksheet();
    controller = new Controller();
    fakeIView = new FakeIView(controller);
    controller.run(model, fakeIView);
  }

  /**
   * test the conform button and the value get from the view, no every thing been tested.
   */
  @Test
  public void testControllerConformButton() {

    Coord coord = new Coord(1, 1);
    // set the cell
    controller.selectCell(coord);
    // set the value
    fakeIView.setInputValue("1");
    // check the value
    assertNull(controller.getCell(coord));
    // call action event
    fakeIView.callActionListener("Conform");
    // check the value back
    assertEquals(new SNumber(1), controller.getCell(coord));

    /// update again
     coord = new Coord(1, 1);
    // set the cell
    controller.selectCell(coord);
    // set the value
    fakeIView.setInputValue("(< 1 2)");
    // call action event
    fakeIView.callActionListener("Conform");
    // check the value back
    assertEquals(new SBoolean(true), controller.getCell(coord));


  }

  @Test
  public void testGetValueBack() {

    /// test get the value direct from the view
    assertNull(fakeIView.getInputValue());
    // set the value
    fakeIView.setInputValue("(< 2 1)");
    // check the value
    assertEquals("(< 2 1)", fakeIView.getInputValue());

  }


  @Test
  public void testTheCoordUpdate() {
    WorksheetBuilderImpl bd = new WorksheetBuilderImpl();

    Worksheet<Sexp> model = bd.createWorksheet();
    Controller controller = new Controller();
    FakeIView fakeIView = new FakeIView(controller);
    controller.run(model, fakeIView);
    /// test the Coord Update
    fakeIView.setSelectedCoord(new Coord(1, 2));
    // now test from the controller
    // set the value
    fakeIView.setInputValue("3");
    // call action event
    fakeIView.callActionListener("Conform");
    // check the value back
    assertEquals(new SNumber(3), controller.getCell(new Coord(1, 2)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void NullModel() {
    controller.run(null, fakeIView);
  }

  @Test(expected = IllegalArgumentException.class)
  public void NullView() {
    controller.run(model, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void NullModelWithoutView() {
    controller.run(null);
  }



    // test for the mouse listener

  /**
   * this test for test that the controller call the right method back to the view
   * after the mouse been clicked.
   * view mouse clicked -> controller mouse clicked sned back -> view I got the coord for the cell.
   */
  @Test
  public void testMouseListener(){
    // get the selected coord
    assertNull(fakeIView.getSelected());
    // then trigger the mouse event.
    fakeIView.CallMouseListener(fakeIView.headerW+( fakeIView.cellWidth * 1) , fakeIView.cellHeight + (fakeIView.headerH) * 1); // A1
    // now check if the controller call the right method.
    assertEquals(new Coord(1, 1), fakeIView.getSelected());

    /// back to back test to ensure
    // call the trigger the mouse event.
    fakeIView.CallMouseListener(1, 1); // if the x and y outside cell will return null
    // now check if the controller call the right method back.
    assertNull(fakeIView.getSelected());

    /// back to back test to ensure
    int column = 10; // if you go more then that will break `because we still didn't find way to support indefinably` !!!
    int row = 5;
    // then trigger the mouse event.
    MouseCoord mc = fakeIView.coordToMouseCoord(new Coord(column, row));
    fakeIView.CallMouseListener(mc.getX(), mc.getY());
    // now check if the controller call the right method.
    assertEquals(new Coord(column, row), fakeIView.getSelected());
  }
  Random rand = new Random();

  @Test
  public void mouseCoordToCellCoordTestRnadomCell(){

    // get the selected coord
    assertNull(fakeIView.getSelected());

    /// back to back test to ensure
    int column = rand.nextInt(1000000) + 1; // if you go more then that will break `because we still didn't find way to support indefinably` !!!
    int row = rand.nextInt(1000000) + 1;
    // then trigger the mouse event.
    MouseCoord mc = fakeIView.coordToMouseCoord(new Coord(column, row));
    fakeIView.CallMouseListener(mc.getX(), mc.getY());

    // now check if the controller call the right method.
    assertEquals(new Coord(column, row), fakeIView.getSelected());
  }



  // test scroll event.


  @Test
  public void testScrollEvent(){

    assertEquals(5, fakeIView.getRows());
    assertEquals(10, fakeIView.getColumns());

    // call scroll event
    fakeIView.callScrollListener(); // this point depend  on getVisibleArea in fakeView

    // now the controller will call back and change the rows and columns by 0.2 in the view.

    assertEquals((int) (5 + 20), fakeIView.getRows());
    assertEquals((int) (10+ 20), fakeIView.getColumns());


    fakeIView.callScrollListener();

    assertEquals((int) (5 + 20 + 20), fakeIView.getRows());
    assertEquals((int) (10 + 20 ), fakeIView.getColumns());

    fakeIView.callScrollListener();

    assertEquals((int) (5 + 20 + 20), fakeIView.getRows());
    assertEquals((int) (10 + 20), fakeIView.getColumns() );



    fakeIView.callScrollListener();

    assertEquals((int) (5 + 20 + 20), fakeIView.getRows());
    assertEquals((int) (10 + 20), fakeIView.getColumns() );


  }





}
