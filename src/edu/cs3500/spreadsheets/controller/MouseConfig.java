package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * class for handle mouse click
 */
public class MouseConfig extends MouseAdapter implements MouseListener {
  IMouseController controller;
  String name = null;
  MouseConfig(IMouseController controller) {
  this.controller = controller;
  }
  MouseConfig(IMouseController controller, String name) {
    this.controller = controller;
    this.name = name;
  }

  /**
   * if the mouse outside the cells will send null as coord.
   * @param e the event to be processed
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

  //  System.out.println("x: " + x + " y: " + y);

    Coord coord = null;
    try {
      // mouse to coord
      coord = controller.mouseCoordToCellCoord(x, y, name);
      //System.out.println(coord);

    } catch (IllegalArgumentException ex) {
      coord = null;
    }

    // send to the controller
    controller.selectCell(coord, name);
  }

}
