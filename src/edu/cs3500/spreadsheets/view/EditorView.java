package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.IController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.UtilVisitor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ChangeListener;

/**
 * the editor view
 */
public class EditorView extends JFrame implements IView {
  private JScrollPane scrollPane;
  private JButton JBConform;
  private JButton JBReject;
  private JTextField JBInput;
  private JPanel barTool;
  private JPanel buttons;
  static IController controller;
  int cellHeight = 30;
  int cellWidth = 60;
  int headerW = 40;
  int headerH = 30;
  private  CustomOverlayPanel customOverlayPanel;
  Coord coord;
  /**
   this rows will be used for View not guaranty the rows in model.
   the same for the Columns its may be the same but not use as reference to model date.
   */
  int Rows;
  int Columns;

  public EditorView(IController controller){
    super();

    this.controller = controller;

    this.setTitle("SpreadSheet Editor");
    this.setPreferredSize(new Dimension(600, 300));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // default
    this.setLocation(800, 300);
    this.setLayout(new BorderLayout());

    // barTool
    barTool = new JPanel(new BorderLayout());
    barTool.setPreferredSize(new Dimension(
        this.getPreferredSize().width, 40));

    // top toolbar
    this.add(barTool, BorderLayout.NORTH);

    // buttons
    buttons = new JPanel(new FlowLayout());
    barTool.add(buttons, BorderLayout.WEST);
//    Conform
    JBConform = new JButton("✔️");
    JBConform.setActionCommand("Conform");
    buttons.add(JBConform);
    //barTool.setBackground(Color.GREEN);

    // Reject
    JBReject = new JButton("✖️");
    buttons.add(JBReject);

    // JBInput
    JBInput = new JTextField();
    barTool.add(JBInput, BorderLayout.CENTER);

    // JBReject
    JBReject.addActionListener((ActionEvent e) -> {
      setJBReject();
    });


    Rows = 5;
    Columns = 10;

 //   selectionPanel = new SelectionPanel(controller, headerH, headerW, cellHeight, cellWidth, Rows, Columns);
    customOverlayPanel = new CustomOverlayPanel(controller, headerH, headerW, cellHeight, cellWidth, Rows, Columns);
    scrollPane = new JScrollPane(customOverlayPanel);

    this.add(scrollPane, BorderLayout.CENTER);

 //   scrollPane.setBackground(Color.blue);
    //this.setBackground(Color.pink);

    // set the preferred stuff
    this.pack();
    // this.makeVisible();
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
    this.JBConform.addActionListener(listener);
  }


  @Override
  public void addMouseListener(MouseListener listener) {
    this.customOverlayPanel.addMouseListener(listener);
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
    double row =  Math.ceil((double)(y - headerH) / cellHeight);
    double column =  Math.ceil((double)(x - headerW) / cellWidth);

    return new Coord((int) column, (int) row);
  }

  @Override
  public Coord mouseCoordToCellCoord(int x, int y, String sheetName) {
    return this.mouseCoordToCellCoord(x, y);
  }

  public void setSelected(Coord selected) {
    this.customOverlayPanel.setSelected(selected);

    this.coord = selected;
    refreshInputValue();
  }

  @Override
  public MouseCoord coordToMouseCoord(Coord coord) {
   int column = this.headerW+( this.cellWidth * coord.col);
   int row = this.cellHeight + (this.headerH * coord.row);
    return new MouseCoord(column, row);
  }

  @Override
  public void addScrollListener(ChangeListener listener) {
    scrollPane.getViewport().addChangeListener(listener);
  }

  @Override
  public Rectangle getViewRect() {
    return  scrollPane.getViewport().getViewRect();
  }

  @Override
  public void changedRowsColumns(int rows, int columns) {
    this.Rows = rows;
    this.Columns = columns;
    customOverlayPanel.changedRowsColumns(rows, columns);
    //System.out.println("call");
  }

  @Override
  public int getContentHeight() {
    return headerH + (Rows * cellHeight);
  }

  @Override
  public int getContentWidth() {
    return headerW + (Columns * cellWidth);
  }

  void refreshInputValue(){
    if(coord != null) {
      setInputValue(getCellRawValue(coord));
    }else {
      setInputValue("");
    }
  }



  /**
   *
   *
   * get the raw value for the give cell
   * @param coord
   * @return the raw value as String
   */
  String getCellRawValue(Coord coord){
    String v =  controller.getRawValue(coord);
    if(v == null){
      return  "";
    }

    Sexp se = Parser.parse(v);
    if(UtilVisitor.isSList(se)){
      return  "=" + v;
    }else {
      return v;
    }
  }

  /**
   * set the value of the {@code JBInput}
   * @param text the value will be set
   */
  void setInputValue(String text){
    JBInput.setText(text);
  }


  /**
   * the action fn for the reject button
   */
  void setJBReject() {
    if (coord != null) {
      try {
        // determine which cell was clicked
        setInputValue(getCellRawValue(coord));
      } catch (IllegalArgumentException ex) {
        //throw new RuntimeException(ex);
        setInputValue("");
      }
    }
  }

  /**
   * the value of the input text
   * @return the value for the cell content update.
   */
  @Override
  public String getInputValue() {
    String content = JBInput.getText();
    if (!content.isEmpty() && content.charAt(0) == '=') {
      content = content.substring(1);
    }
    return content;
  }



}

