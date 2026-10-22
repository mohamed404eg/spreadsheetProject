package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.event.ChangeListener;

/**
 * Interface for view the spreadSheet
 */
public interface IView {

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
   * show the view to the user
   */
  void  makeVisible();

  /**
   * for set up the ActionListener w/ the given ActionListener.
   * @param listener
   */
  void addActionListener(ActionListener listener);

  void addMouseListener(MouseListener listener);

  /**
   * get the value of text Field.
   * @return
   */
  String getInputValue();

  /**
   * transform x and y mouse coordinate to Coord
   * @param x the x coordinate
   * @param y the y coordinate
   * @return {@code Coord} or {@code null} if coordinate outside the sheet
   */
  Coord mouseCoordToCellCoord(int x, int y);
  Coord mouseCoordToCellCoord(int x, int y, String sheetName);
  /**
   * send the selected Coord to the ui will make it update.
   * @param selected
   */
  void setSelected(Coord selected);

  /**
   * coord mouse to {@code MouseCoord}
   * @param coord
   * @return {@code MouseCoord}
   */
  MouseCoord coordToMouseCoord(Coord coord);

  /**
   * ADD the given {@code ChangeListener} as ChangeListener to the scroll bar in the view.
   * @param listener
   */
  void addScrollListener(ChangeListener listener);

  /**
   * get the visible area that use see also {@code Rectangle} incloud  visible.x so that the scroll bar positions.
   * @return Rectangle of the component that has scroll bar.
   */
  Rectangle getViewRect();

  /**
   * Update the view max rows and columns that should appear.
   *<p> "it may not be related to the model date." </p>
   * @param rows
   * @param columns
   */
  void changedRowsColumns(int rows, int columns);

  /**
   * get the height of the content all cell and the header.
   * @return the content height.
   */
  int getContentHeight();
  /**
   * get the Width of the content all cell and the header.
   * @return the content Width.
   */
  int getContentWidth();

}
