package edu.cs3500.spreadsheets.view;


import edu.cs3500.spreadsheets.model.AdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;

public class View extends JFrame implements IView {
  private JScrollPane scrollPane;
  private DrawSpreadSheet SpreadSheetJPanel;
  int cellHeight = 30;
  int cellWidth = 60;
  int headerW = 40;
  int headerH = 30;

  public View(Worksheet worksheet) {
    super();
    this.setTitle("SpreadSheet");
    this.setPreferredSize(new Dimension(600, 300));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // default
    this.setLocation(800, 300);
    SpreadSheetJPanel = new DrawSpreadSheet(new AdaptorSpreadSheet(worksheet), headerH, headerW, cellHeight, cellWidth);
    scrollPane = new JScrollPane(SpreadSheetJPanel);
    this.add(scrollPane);
    // set the preferred stuff
    this.pack();
    this.makeVisible();
  }


  @Override
  public void ShowErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }



  @Override
  public void addActionListener(ActionListener listener) {

  }

  @Override
  public String getInputValue() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Coord mouseCoordToCellCoord(int x, int y) {
    return null;
  }

  @Override
  public Coord mouseCoordToCellCoord(int x, int y, String sheetName) {
    return null;
  }

  @Override
  public void setSelected(Coord selected) {

  }

  @Override
  public MouseCoord coordToMouseCoord(Coord coord) {
    return null;
  }

  @Override
  public void addScrollListener(ChangeListener listener) {

  }

  @Override
  public Rectangle getViewRect() {
    return null;
  }

  @Override
  public void changedRowsColumns(int rows, int columns) {

  }

  @Override
  public int getContentHeight() {
    return 0;
  }

  @Override
  public int getContentWidth() {
    return 0;
  }
}
