package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IModelAdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.WorkBook;
import edu.cs3500.spreadsheets.model.WorkBookAdaptor;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.IMultView;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultController implements IMultController {
  WorkBook<Sexp> workBook;
  IMultView multView;

  // ratio grow
  int colGrow = 10;
  int rowGrow = 10;

  //
  Coord coord;
  String sheetName;

  public MultController(WorkBook workBook) {
    this.workBook = workBook;
  }

  /**
   * set up the controller.
   */
  private void setUp() {
    configureScrollListener();
    configureMouseListener();
    configureButtonListener();
  }
  @Override
  public void run(IMultView multView) {
    this.multView = multView;
    setUp();

    this.multView.showView();
  }

    @Override
  public IModelAdaptorSpreadSheet getWorkBookAdaptor(String sheetName) {
    return new WorkBookAdaptor(workBook, sheetName);
  }

  @Override
  public List<String> getSheetNames() {
    return workBook.getSheetNames();
  }

  private void configureScrollListener() {
    for (String name : workBook.getSheetNames()){
      this.multView.addScrollListener(new ScrollChangeListener(this), name);
    }
  }
  private void configureMouseListener() {
    for (String name : workBook.getSheetNames()){
      this.multView.addMouseListener(new MouseConfig(this, name), name);
    }
  }
  private void configureButtonListener() {
    Map<String, Runnable> buttonClickedMap = new HashMap<String, Runnable>();
    ButtonListener buttonListener = new ButtonListener();

    buttonClickedMap.put("Conform", new ConformButtonAction());

    buttonListener.setButtonClickedActionMap(buttonClickedMap);
    this.multView.addActionListener(buttonListener);
  }

  class ConformButtonAction implements Runnable {
    public void run() {
      String content = multView.getInputValue();
      if (coord != null) {
        if(content.isEmpty()){
          workBook.removeCell(sheetName+"!"+coord);
        }else {
          try {
           workBook.set(sheetName+"!"+coord, content);
          }catch (Exception e){
            multView.ShowErrorMessage(e.getMessage());
          }
        }
        multView.refresh();
      }
    }
  }


  @Override
  public Coord mouseCoordToCellCoord(int x, int y) {
    throw new  UnsupportedOperationException("Not supported.");
  }

  @Override
  public Coord mouseCoordToCellCoord(int x, int y, String sheetName) {
    return this.multView.mouseCoordToCellCoord(x, y, sheetName);
  }

  @Override
  public void selectCell(Coord coord) {
    throw new  UnsupportedOperationException("Not supported.");
  }

  @Override
  public void scrollChanged() {
    for (String name : workBook.getSheetNames()){
      ScrollCheck(multView.getViewRect(name), multView.getContentWidth(name), multView.getContentHeight(name), multView.getColumns(name), multView.getRows(name),name);
    }
  }

  private void ScrollCheck(Rectangle visible,  int contentWidth, int contentHeight, int columns, int rows, String sheetName) {
    int right = visible.x + visible.width; // the most right point where the user can see.
    int bottom = visible.y + visible.height;  // the most bottom point where the user can see.
    if (contentWidth - right <= contentWidth * 0.20) {
      // extend columns
      columns += colGrow;
      multView.changedRowsColumns( rows, columns, sheetName);
    }
    if (contentHeight - bottom <= contentHeight * 0.20) {
      // extend rows
      rows += rowGrow;
      multView.changedRowsColumns( rows, columns, sheetName);
    }
  }
  @Override
  public void selectCell(Coord coord, String sheetName) {
    this.coord = coord;
    this.sheetName = sheetName;
    this.multView.selectCell(coord, sheetName);
  }
}
