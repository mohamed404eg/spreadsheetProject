package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.view.IView;
import edu.cs3500.spreadsheets.view.MouseCoord;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * for testing the controller
 */
public class FakeIView implements IView {
  IController controller;
  ActionListener listener;
  MouseListener mouseListener;
  String input;
  Coord selected;
  int cellHeight = 30;
  int cellWidth = 60;
  int headerW = 40;
  int headerH = 30;
  int rows;
  int columns;
  int xScroll = 400;
  int yScroll = 200;

  ChangeListener scrollChangeListener;

  public FakeIView(IController controller){
    this.controller = controller;
    selected = null;
    rows = 5;
    columns = 10;
  }
  @Override
  public void ShowErrorMessage(String message) {

  }

  public int getRows(){
    return rows;
  }
  public int getColumns(){
    return columns;
  }
  @Override
  public void refresh() {

  }

  @Override
  public void makeVisible() {

  }


  @Override
  public void addActionListener(ActionListener listener) {
  this.listener = listener;
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    this.mouseListener = listener;
  }



  /**
   * call the Actions Listener w/o the button been clicked.
   * @param command
   */
  public void callActionListener(String command){
    this.listener.actionPerformed(new ActionEvent(this,0,command));
  }

  @Override
  public String getInputValue() {
    return input;
  }


  @Override
  public void setSelected(Coord selected) {
    this.selected = selected;
  }

  public Coord getSelected(){
    return selected;
  }

  /**
   * set the button value .
   * @param v
   */
  public void setInputValue(String v) {
   this.input = v;
  }

  public void  setSelectedCoord(Coord coord){
    controller.selectCell(coord);
  }

  /**
   * call mouse event mouseClicked w/ x y coordinate.
   * note: the controller will call setSelected.
   * @param x
   * @param y
   */
  public void CallMouseListener(int x, int y){
    this.mouseListener.mouseClicked(new MouseEvent(new JPanel(),0,0, 0, x, y, 1, false, 0));
  }

  /**
   * convert the x and y of the mouse on the sheet view to {@code Coord}
   * @param x the current x mouse positions
   * @param y the current y mouse positions
   * @return {@code  Coord}
   * @throws IllegalArgumentException if the mouse coordinate out of the cells.
   */
  public Coord mouseCoordToCellCoord(int x, int y){
    if(x- headerH <= 1 || y - headerW <= 1){
      throw new IllegalArgumentException();
    }
    double row =  Math.ceil((double)(y - headerH) / cellHeight);
    double column =  Math.ceil((double)(x - headerW) / cellWidth);

    return new Coord((int) column, (int) row);
  }

  @Override
  public Coord mouseCoordToCellCoord(int x, int y, String sheetName) {
    return null;
  }


  @Override
  public MouseCoord coordToMouseCoord(Coord coord) {
    int column = this.headerW+( this.cellWidth * coord.col);
    int row = this.cellHeight + (this.headerH * coord.row);
    return new MouseCoord(column, row);
  }

  @Override
  public void addScrollListener(ChangeListener listener) {
     this.scrollChangeListener = listener;
  }


  /**
   * call for call the scroll event.
   */
  public void callScrollListener() {
    this.scrollChangeListener.stateChanged(new ChangeEvent(this));
  }

  @Override
  public Rectangle getViewRect() {
    // these value related to the controller test so don't changed
    return new Rectangle(xScroll+=100, yScroll+=100, 200, 400);
  }

  @Override
  public void changedRowsColumns(int rows, int columns) {

    this.rows = rows;
    this.columns = columns;

  }



  @Override
  public int getContentHeight() {
    return headerH + (rows * cellHeight);
  }

  @Override
  public int getContentWidth() {
    return headerW + (columns * cellWidth);
  }

}
