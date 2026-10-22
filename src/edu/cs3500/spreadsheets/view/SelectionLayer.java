package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * draw box around the specific cell give to {@code setSelected(Coord selected) }
 * <p>give the same args value as the DrawSpreadSheet will make work appropriate</p>
 */
public class SelectionLayer extends JPanel {
  private Coord selected;
  private final int headerH;
  private final int headerW;
  private final int cellHeight ;
  private final int cellWidth;

  public SelectionLayer( int headerH, int headerW, int cellHeight, int cellWidth) {
    this.headerH = headerH;
    this.headerW = headerW;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;

    // no background, so underneath comp will appear
   this.setOpaque(false);
  }

  /**
   *
   * set the Coord for fn to set a box around it.
   * it will repaint after.
   * @param selected the coord of cell
   */
  public void setSelected(Coord selected) {
    this.selected = selected;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    //  drawSpreadSheet.repaint();
    if (selected != null) {
      drawSelection(g2d, selected);
    }
  }

  /**
   * will make the draw box around the selected cell.
   * @param g
   * @param selected the coordinate cell
   */
  private void drawSelection(Graphics2D g, Coord selected) {

    g.setColor(Color.BLUE);
    int ys = (cellHeight * (selected.row - 1)) + headerH ;
    int xs = (cellWidth * (selected.col - 1))  +  headerW;
    int yEnd = (cellHeight * selected.row) + headerH ;
    int xEnd = (cellWidth * selected.col)  +  headerW;

    // tow horznatle line
    g.drawLine(xs, ys, xEnd, ys); // top line
    g.drawLine(xs, yEnd, xEnd, yEnd) ;// bottom line

    // tow verticel line
    g.drawLine(xs, ys, xs, yEnd); // left line
    g.drawLine(xEnd, ys, xEnd, yEnd) ;// right line

  }


}