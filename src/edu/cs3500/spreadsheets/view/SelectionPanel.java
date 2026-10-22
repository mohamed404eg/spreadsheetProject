package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IModelAdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * comp for overlay the SelectionLayer over the DrawSpreadSheet and DrawSpreadSheet still appear.
 * replace w/ CustomOverlayPanel.
 */
public class SelectionPanel extends JPanel {

  private SelectionLayer selectionLayer;
  DrawSpreadSheet drawSpreadSheet;
  IModelAdaptorSpreadSheet sheet;
  int headerH;
  int headerW;
  int cellHeight;
  int cellWidth;
  int rows;
  int columns;


  public SelectionPanel(IModelAdaptorSpreadSheet sheet,
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

  //  Dimension dimension = new Dimension(1000,33000); //new Dimension((cellWidth * columns) + headerW,  (cellHeight * rows) + headerH);


//    if (!(sheet instanceof Worksheet)) {
//      throw new IllegalArgumentException(
//          "SelectionPanel requires a worksheet-compatible view model");
//    }

     drawSpreadSheet = new DrawSpreadSheet(sheet, headerH, headerW, cellHeight,
        cellWidth, rows, columns);

    selectionLayer = new SelectionLayer(headerH, headerW, cellHeight, cellWidth);

    // make comp overlay each other
 //  this.setLayout(new OverlayLayout(this));

    setLayout(new OverlayLayout(this));
   add(selectionLayer);   // top
    add(drawSpreadSheet);  // bottom



    // set one dimension
  // this.setPreferredSize(dimension);
  //  drawSpreadSheet.setPreferredSize(dimension);
  //  selectionLayer.setPreferredSize(dimension);

    // test
   // drawSpreadSheet.setBackground(Color.yellow);

   // selectionLayer.setBackground(Color.GRAY);
    this.setBackground(Color.GREEN);
  }

  /**
   *
   * set the Coord for fn to set a box around it.
   * it will repaint after.
   * @param selected the coord of cell
   */
  public void setSelected(Coord selected) {
    this.selectionLayer.setSelected(selected);
  }


  public void changedRowsColumns(int rows, int columns) {
    System.out.println("rows updated");

    this.rows = rows;
    this.columns = columns;
    drawSpreadSheet.changedRowsColumns(rows, columns);
    resize();
  }

  void resize(){
   Dimension dimension = new Dimension((cellWidth * columns) + headerW,  (cellHeight * rows) + headerH);

    // set one dimension
  //  this.setPreferredSize(dimension);
    selectionLayer.setPreferredSize(dimension);
    drawSpreadSheet.setPreferredSize(dimension);

    //System.out.println(dimension);
  //  System.out.println("dimension: " + dimension);
  // this.repaint();

     System.out.println(selectionLayer.getAlignmentY());
    System.out.println(drawSpreadSheet.getAlignmentY());


  }
  @Override
  public Dimension getPreferredSize() {
    // Force the panel to be at least as big as your largest inner component
    return new Dimension((cellWidth * columns) + headerW,  (cellHeight * rows) + headerH);
// Replace with your target/dynamic size
  }



  }