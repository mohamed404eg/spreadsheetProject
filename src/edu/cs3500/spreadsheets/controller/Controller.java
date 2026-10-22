package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.EditorView;
import edu.cs3500.spreadsheets.view.IView;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class Controller implements IController {
  private  IView editorView;
  private  Worksheet<Sexp> model;
  private Coord selectedCell;
  int cellHeight = 30;
  int cellWidth = 60;
  int headerW = 40;
  int headerH = 30;
  int rowGrow = 20;
  int colGrow = 20;

  /**
   this rows will be used for View not guaranty the rows in model.
   the same for the Columns its may be the same but not use as reference to model date.
   use double then when need cast one time to avoid cut the number too much.
   */
  int rows;
  int columns;

  public Controller(){
  }

  // for Worksheet<Sexp>
  public Controller(Worksheet<Sexp> model) {
    if (model == null) {
      throw new IllegalArgumentException("null args");
    }
    this.model = model;
  }

  /**
   * run the Controller.
   * @param model the model
   * @param editorView the view
   * @throws IllegalArgumentException if model or editorView null
   */
  public void run(Worksheet<Sexp> model, IView editorView){
    if(model == null || editorView == null){
      throw new IllegalArgumentException("null args");
    }
    this.model = model;
    this.editorView = editorView;

    runHelp();
  }

  /**
   * you can call w/o IView, will create one {@code EditorView}
   * @param model
   */
  public void run(Worksheet<Sexp> model){
    if(model == null){
      throw new IllegalArgumentException("null args");
    }
    this.model = model;
    this.editorView = new EditorView(this);

    runHelp();
  }

  void runHelp(){
    configureButtonListener();
    configureMouseListener();
    editorView.makeVisible();

    rows = 5;
    rows = Math.max(rows,  model.getMaxRows());

    columns = 10;
    columns = Math.max(columns, model.getMaxColumns());

    configureScrollListener();

  }

  private void configureButtonListener() {
    Map<String, Runnable> buttonClickedMap = new HashMap<String, Runnable>();
    ButtonListener buttonListener = new ButtonListener();

    buttonClickedMap.put("Conform", new ConformButtonAction());

    buttonListener.setButtonClickedActionMap(buttonClickedMap);
    this.editorView.addActionListener(buttonListener);
  }

  private void configureMouseListener() {
    this.editorView.addMouseListener(new MouseConfig(this));
  }
  private void configureScrollListener() {
    this.editorView.addScrollListener(new ScrollChangeListener(this));
  }


  @Override
  public void selectCell(Coord coord) {
    this.selectedCell = coord;
    // update the view box.
    this.editorView.setSelected(coord);
  }



  @Override
  public void scrollChanged() {
    Rectangle visible = editorView.getViewRect();

    int right = visible.x + visible.width; // the most right point where the user can see.
    int bottom = visible.y + visible.height;  // the most bottom point where the user can see.

    int contentWidth = editorView.getContentWidth();
    int contentHeight = editorView.getContentHeight();

    if (contentWidth - right <= contentWidth * 0.20) {
      // extend columns
      columns += colGrow;
      editorView.changedRowsColumns( rows, columns);
    }


    if (contentHeight - bottom <= contentHeight * 0.20) {
      // extend rows
      rows += rowGrow;
      editorView.changedRowsColumns(rows,  columns);
    }

  }

  @Override
  public void selectCell(Coord coord, String sheetName) {
    this.selectCell(coord);
  }


  class ConformButtonAction implements Runnable {
    public void run() {
      String content = editorView.getInputValue();
      if (selectedCell != null) {
        if(content.isEmpty()){
          model.remove(selectedCell);
        }else {
          try {
            model.set(selectedCell, content);
          }catch (Exception e){
            editorView.ShowErrorMessage(e.getMessage());
          }
       }
        editorView.refresh();
      }
    }
  }


  /**
   * convert the x and y of the mouse on the sheet view to {@code Coord}
   * @param x the current x mouse positions
   * @param y the current y mouse positions
   * @return {@code  Coord}
   * @throws IllegalArgumentException if the mouse coordinate out of the cells.
   */
  public Coord mouseCoordToCellCoord(int x, int y){
   return this.editorView.mouseCoordToCellCoord(x,y);
  }

  @Override
  public Coord mouseCoordToCellCoord(int x, int y, String sheetName) {
    return this.mouseCoordToCellCoord(x,y);
  }


  @Override
  public Sexp getCell(Coord coord) {
    return  this.model.getCell(coord);
  }

  @Override
  public void set(Coord coord, String content) {
    this.model.set(coord, content);
  }

  @Override
  public int getMaxRows() {
    return this.model.getMaxRows();
  }

  @Override
  public int getMaxColumns() {
    return this.model.getMaxColumns();
  }

  @Override
  public int getColumnMaxSize(int column) {
    return this.model.getColumnMaxSize(column);
  }

  @Override
  public String getRawValue(Coord coord) {
    return this.model.getRawValue(coord);
  }

  @Override
  public void remove(Coord coord) {
    this.model.remove(coord);
  }
}




class RUN{
  public static void main(String[] args){
    Worksheet<Sexp> model = new WorksheetModel();
    model.set(new Coord(1, 1), "\"hi\"");
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(1, 3), "1");
    model.set(new Coord(1, 4), "(SUM A2 A3)");
    model.set(new Coord(1, 5), "(SUM A2 A4)");
    IController controller = new Controller();
    controller.run(model);
  }
}
