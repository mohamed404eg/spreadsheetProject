package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.view.CustomOverlayPanel;
import edu.cs3500.spreadsheets.view.IMultView;
import edu.cs3500.spreadsheets.view.MultView;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FkMultView implements IMultView {
  IMultController controller;
  CustomOverlayPanel overlayPanel;
  int cellHeight = 30;
  int cellWidth = 60;
  int headerW = 40;
  int headerH = 30;
  int Rows = 10;
  int Columns = 5;

  Coord coord;
  String sheetName;
  MouseListener listenerClicked;
  ActionListener buttonListener;
  String value;
  ChangeListener scrollListener;
  Rectangle rectangle;

  FkMultView(IMultController controller) {
    this.controller = controller;

    overlayPanel = new CustomOverlayPanel(controller.getWorkBookAdaptor(controller.getSheetNames().get(0)), headerH, headerW,
        cellHeight, cellWidth, Rows, Columns);
  }
  @Override
  public void addActionListener(ActionListener listener) {
    buttonListener = listener;
  }

  @Override // the value set for the test
  public void addMouseListener(MouseListener listener, String name) {
   listenerClicked = listener;
  }
  public void CallMouseCliked(int x, int y) {
    listenerClicked.mouseClicked(new MouseEvent(new JPanel(), 1, 50, 3, x, y, 1, false,1));
  }

  public void ConformButton(){
    buttonListener.actionPerformed(new ActionEvent(listenerClicked, 0, "Conform"));
  }
  @Override
  public void addScrollListener(ChangeListener listener, String name) {
  this.scrollListener = listener;
  }

  public void CallScrollListener() {
    scrollListener.stateChanged(new ChangeEvent(this));
  }

  @Override
  public Rectangle getViewRect(String sheetName) {
    return rectangle;
  }
  public void setRectangle(int x, int y, int width, int height){
    rectangle = new Rectangle(x, y, width, height);
  }
  @Override
  public void changedRowsColumns(int rows, int columns, String sheetName) {
    overlayPanel.changedRowsColumns(rows, columns);
  this.Rows = overlayPanel.getRows();
  this.Columns = overlayPanel.getColumns();
  }

  @Override
  public int getContentHeight(String sheetName) {
    return overlayPanel.getContentHeight();
  }

  @Override
  public int getContentWidth(String sheetName) {
    return overlayPanel.getContentWidth();
  }

  @Override
  public int getRows(String sheetName) {
    return overlayPanel.getRows();
  }

  @Override
  public int getColumns(String sheetName) {
    return overlayPanel.getColumns();
  }

  @Override
  public void selectCell(Coord coord, String sheetName) {
    this.coord = coord;
    this.sheetName = sheetName;
  }

  @Override
  public Coord mouseCoordToCellCoord(int x, int y, String sheetName) {
    return overlayPanel.mouseCoordToCellCoord(x, y);
  }

  @Override
  public String getInputValue() {
    return value;
  }

  @Override
  public void ShowErrorMessage(String message) {

  }

  @Override
  public void refresh() {

  }

  @Override
  public void showView() {
  }

  public Coord getSelectedCell(){
    return coord;
  }
  public String getSheetName() {
    return sheetName;
  }

  public void setInputValue(String inputValue) {
    this.value = inputValue;
  }
}
