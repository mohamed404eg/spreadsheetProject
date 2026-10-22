package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IModelAdaptorSpreadSheet;
import javax.swing.*;
import java.awt.*;

public class CustomOverlayPanel extends JLayeredPane {
  private DrawSpreadSheet backgroundComp;
  private SelectionLayer foregroundComp;
  IModelAdaptorSpreadSheet sheet;
  int headerH;
  int headerW;
  int cellHeight;
  int cellWidth;
  int rows;
  int columns;

  public CustomOverlayPanel(IModelAdaptorSpreadSheet sheet,
      int headerH,
      int headerW,
      int cellHeight,
      int cellWidth, int rows, int columns) {
    this.sheet = sheet;
    this.headerH = headerH;
    this.headerW = headerW;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;
    this.rows = rows;
    this.columns = columns;


    // Use no layout
    setLayout(null);


    backgroundComp = new DrawSpreadSheet(sheet, headerH, headerW, cellHeight,
        cellWidth, rows, columns);

    // Force the background to take up the full panel size
    add(backgroundComp, JLayeredPane.DEFAULT_LAYER);

    foregroundComp =  new SelectionLayer(headerH, headerW, cellHeight, cellWidth);

    // Place it on a higher layer so it floats on top
    add(foregroundComp, JLayeredPane.PALETTE_LAYER);

    // Pin the button to the top-left or center manually if needed

    resize();

  }

  /**
   *
   * set the Coord for fn to set a box around it.
   * it will repaint after.
   * @param selected the coord of cell
   */
  public void setSelected(Coord selected) {
    this.foregroundComp.setSelected(selected);
  }


  public void changedRowsColumns(int rows, int columns) {
 //   System.out.println("rows updated");

    this.rows = rows;
    this.columns = columns;
    backgroundComp.changedRowsColumns(rows, columns);


    resize();
  }

  /**
   * update size and position.
   */
  void resize(){
    int width = headerW + cellWidth * columns;
    int height = headerH + cellHeight * rows;

    setPreferredSize(new Dimension(width, height));

    backgroundComp.setBounds(0, 0, width, height);
    foregroundComp.setBounds(0, 0, width, height);

  //  repaint();
  }


  public int getRows(){
    return rows;
  };
  public int getColumns(){
    return columns;
  };


  public int getContentHeight() {
    return headerH + (rows * cellHeight);
  }


  public int getContentWidth() {
    return headerW + (columns * cellWidth);
  }

  /**
   * convert the x and y of the mouse on the sheet view to {@code Coord}
   * @param x the current x mouse positions
   * @param y the current y mouse positions
   * @return {@code  Coord}
   * @throws IllegalArgumentException if the mouse coordinate out of the cells.
   */
  public Coord mouseCoordToCellCoord(int x, int y){
    if(x- headerW <= 0 || y - headerH <= 0){
      throw new IllegalArgumentException();
    }
    int row = (int) Math.ceil( ((double)  (y - headerH) / cellHeight) );
    int column =(int)  Math.ceil((double) (x - headerW) / cellWidth);

    return new Coord((int) column, (int) row);
  }


}