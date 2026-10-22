package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.event.ChangeListener;

public interface IMultView {
  /**
   * for set up the ActionListener w/ the given ActionListener.
   * @param listener
   */
  void addActionListener(ActionListener listener);

  void addMouseListener(MouseListener listener,  String name);
  /**
   * ADD the given {@code ChangeListener} as ChangeListener to the scroll bar in the view.
   * @param listener
   * @param name the specific sheet to add the listener.
   */
  void addScrollListener(ChangeListener listener, String name);

  /**
   * get the visible area that use see also {@code Rectangle} incloud  visible.x so that the scroll bar positions.
   * @return Rectangle of the component that has scroll bar.
   */
  Rectangle getViewRect(String sheetName);

  /**
   * Update the view max rows and columns that should appear.
   *<p> "it may not be related to the model date." </p>
   * @param rows
   * @param columns
   */
  void changedRowsColumns(int rows, int columns, String sheetName);

  /**
   * get the height of the content all cell and the header.
   * @return the content height.
   */
  int getContentHeight(String sheetName);
  /**
   * get the Width of the content all cell and the header.
   * @return the content Width.
   */
  int getContentWidth(String sheetName);

  int getRows(String sheetName);
  int getColumns(String sheetName);
  /**
   * send the {@code Coord} when the user clicked.
   * @param coord
   * @param sheetName
   */
  void selectCell(Coord coord, String sheetName);

  /**
   * turn the mouse concordant to cell coord.
   * @param x
   * @param y
   * @param sheetName
   * @return the cell coord of the given mouse.
   */
  Coord mouseCoordToCellCoord(int x, int y, String sheetName);

  /**
   * the value of the text field.
   * @return String
   */
  String getInputValue();


  /**
   * show to the user the given message
   * @param message the error message
   */
  void ShowErrorMessage(String message);
  /**
   * make the view render the view again
   */
  void refresh();

  /**
   * make the view visible.
   */
  void showView();
}
