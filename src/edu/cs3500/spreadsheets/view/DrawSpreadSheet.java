package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IModelAdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class DrawSpreadSheet extends JPanel {

  private final IModelAdaptorSpreadSheet sheet;
  // configuration
  private final int headerH;
  private final int headerW;
  private final int cellHeight ;
  private final int cellWidth;
  private  int maxX;
  private  int maxY;
  int rows;
  int columns;

  public DrawSpreadSheet(IModelAdaptorSpreadSheet sheet, int headerH, int headerW, int cellHeight, int cellWidth, int rows, int columns ) {
    super();
    if (sheet == null){
      throw new IllegalArgumentException("Sheet null");
    }
    this.sheet = sheet;
    this.rows = rows;
    this.columns = columns;
    // prepare configuration
    maxX = (cellWidth * columns) + headerW;
    maxY = (cellHeight * rows) + headerH;
    //this.setPreferredSize(new Dimension(maxX, maxY));
    this.headerH = headerH;
    this.headerW = headerW;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;
  }
  public DrawSpreadSheet(IModelAdaptorSpreadSheet sheet, int headerH, int headerW, int cellHeight, int cellWidth ) {
    if (sheet == null){
      throw new IllegalArgumentException("Sheet null");
    }
    this.sheet = sheet;
    this.rows = sheet.getMaxRows();
    this.columns = sheet.getMaxRows();
    // prepare configuration
    maxX = (cellWidth * columns) + headerW;
    maxY = (cellHeight * rows) + headerH;
    //this.setPreferredSize(new Dimension(maxX, maxY));
    this.headerH = headerH;
    this.headerW = headerW;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;
  }


  @Override
  protected void paintComponent(Graphics g) {
  //  System.out.println("paintComponent");
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    maxX = (cellWidth * columns) + headerW;
    maxY = (cellHeight * rows) + headerH;

    // Draw horizontal lines
    // Draw vertical lines
    // Draw column headers
    // Draw row headers
    // Draw each visible cell's value

    setHeaderBackGround(g2d);
    drawLines(g2d);
    drawCells(g2d);
    drawHeaderValues(g2d);


  }

  /**
   * take the text and the maxWidth allowed and the Graphics2D has been used , clipping the text to
   * until it fit the width.
   *
   * @param text     the text you want cut
   * @param maxWidth the width you want the return text fit
   * @param g2d      the Graphics2D that will be use it to evaluate the text size pixel
   * @return text that fit width appropriate.
   */
  String ClipText(String text, int maxWidth, Graphics2D g2d) {
    FontMetrics metrics = g2d.getFontMetrics();
    int width = metrics.stringWidth(text);
    if (width <= maxWidth) {
      return text;
    } else {
      int maxS = text.length();
      // keep cutting until its fit
      while (metrics.stringWidth(text.substring(0, maxS)) >= maxWidth) {
        maxS--;
      }
      return text.substring(0, maxS);
    }
  }


  /**
   * set header backGround to lightGray, it better to call before draw line and anything you want to
   * appear in front
   *
   * @param g2d
   */
  void setHeaderBackGround(Graphics2D g2d) {
    Color OriginColor = g2d.getColor();

    g2d.setColor(Color.lightGray);
    g2d.fill3DRect(0, 0, maxX, headerH, true);
    // rows header backGround Color to gray
    g2d.setColor(Color.lightGray);
    g2d.fill3DRect(0, 0, headerW, maxY, true);

    // reset g2d back value
    g2d.setColor(OriginColor);
  }


  /**
   * draw cell and header lines
   *
   * @param g2d
   */
  void drawLines(Graphics2D g2d) {
    Color OriginColor = g2d.getColor();

    DrawHorizontalLines(g2d);
    DrawVerticalLines(g2d);

    // reset g2d back value
    g2d.setColor(OriginColor);
  }


  /**
   * draw Horizontal Lines for cell and header
   *
   * @param g2d
   */
  void DrawHorizontalLines(Graphics2D g2d) {
    // Draw header horizontal line
    g2d.setColor(Color.red);
    g2d.drawLine(0, headerH, maxX, headerH);
//    System.out.println("DrawHorizontalLines");
//    System.out.println(headerH + " " + headerW +  " " + maxY);

    // Draw horizontal lines
    g2d.setColor(Color.black);
    for (int r = 1; r <= rows; r++) {
      int y = (cellHeight * r) + headerH;
      g2d.drawLine(0, y, maxX, y);
    }

  }

  /**
   * draw vertical Lines for cell and header
   *
   * @param g2d
   */
  void DrawVerticalLines(Graphics2D g2d) {
    // header row vertical line
    g2d.setColor(Color.red);
    g2d.drawLine(headerW, 0, headerW, maxY);

    // Draw vertical lines
    g2d.setColor(Color.black);
    for (int c = 1; c <=  columns; c++) {
      int x = (cellWidth * c) + headerW;
      g2d.drawLine(x, 0, x, maxY);
     // System.out.println("Draw line x: " + x);
    }
  }

  /**
   * draw header value that up or at the side
   *
   * @param g2d
   */
  void drawHeaderValues(Graphics2D g2d) {
    // draw Column header value
    drawColumnHeaderValue(g2d);
    // draw row header value
    drawRowHeaderValue(g2d);
  }

  /**
   * draw column header value
   *
   * @param g2d
   */
  void drawColumnHeaderValue(Graphics2D g2d) {
    Color OriginColor = g2d.getColor();
    g2d.setColor(Color.black);
    g2d.setFont(new Font(null, Font.BOLD, getFont().getSize()));
    for (int c = 1; c <= columns; c++) {
      int y = headerH / 2;
      int x = (cellWidth * c);
      g2d.drawString(ClipText(Coord.colIndexToName(c), headerW - 5, g2d), x, y);
    }
    g2d.setColor(OriginColor);
  }

  /**
   * draw row header value
   *
   * @param g2d
   */
  void drawRowHeaderValue(Graphics2D g2d) {
    Font OriginFont = getFont();
    Color OriginColor = g2d.getColor();

    g2d.setColor(Color.black);
    g2d.setFont(new Font(null, Font.BOLD, getFont().getSize()));
    for (int r = 1; r <= rows; r++) {
      int y = (cellHeight * r) + headerH / 2;
      int x = 0;
      int PaddingX = 5;
      g2d.drawString(ClipText(String.valueOf(r), headerW - 5, g2d), x + PaddingX, y);

    }

    // reset g2d back value
    g2d.setColor(OriginColor);
    g2d.setFont(OriginFont);
  }

  /**
   * draw each cell value at their positions, clip the value if exceed the cell width
   *
   * @param g2d
   */
  void drawCells(Graphics2D g2d) {
    Color OriginColor = g2d.getColor();

    // draw the cell value
    g2d.setColor(Color.black);
    for (int c = 1; c <= columns; c++) {
      for (int r = 1; r <= rows; r++) {
        int y = (cellHeight * r) + cellHeight / 2;
        int x = headerW + (cellWidth * c) - cellWidth + 3;
        drawCell(g2d, c, r, x, y);
      }
    }

    // reset g2d back value
    g2d.setColor(OriginColor);
  }

  /**
   * draw value of a single cell
   *
   * @param g2d the Graphics2D used
   * @param c   column index of the cell
   * @param r   row index of the cell
   * @param x   the x coordinate of cell of gui
   * @param y   the y coordinate of cell of gui
   */
  void drawCell(Graphics2D g2d, int c, int r, int x, int y) {
    Sexp sexp = null;
    try {
      sexp = sheet.getCell(new Coord(c, r));
    } catch (Exception e) {
      g2d.drawString(ClipText(e.getMessage(), cellWidth, g2d), x, y);
      return;
    }
    if (sexp != null) {
      if (sexp instanceof SString) {
        String s = ((SString) sexp).getValue();
        g2d.drawString(ClipText(s, cellWidth, g2d), x, y);
      } else {
        g2d.drawString(ClipText(sexp.toString(), cellWidth, g2d), x, y);
      }
    }
  }


  public void changedRowsColumns(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;

    maxX = (cellWidth * columns) + headerW;
    maxY = (cellHeight * rows) + headerH;
    this.setPreferredSize(new Dimension(maxX, maxY));
  //  System.out.println(maxX + " : " + maxY);


  }

}
