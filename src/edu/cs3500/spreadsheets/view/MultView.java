package edu.cs3500.spreadsheets.view;

import com.sun.tools.javac.Main;
import edu.cs3500.spreadsheets.controller.IMultController;
import edu.cs3500.spreadsheets.controller.MultController;
import edu.cs3500.spreadsheets.model.AdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkBook;
import edu.cs3500.spreadsheets.model.WorkBookImpl;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.naming.OperationNotSupportedException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

public class MultView extends JFrame implements IMultView {
  private JButton JBConform;
  private JButton JBReject;
  private JTextField JBInput;
  private JPanel barTool;
  private JPanel buttons;
  //
  int cellHeight = 30;
  int cellWidth = 60;
  int headerW = 40;
  int headerH = 30;
  int Rows;
  int Columns;
  //
  HashMap<String, JScrollPane> scrollPaneHashMap;
  IMultController controller;
  //
  Coord coord;
  String sheetName;

  public MultView(IMultController controller) {
    super();
    this.setPreferredSize(new Dimension(900, 600));
    this.setLayout(new BorderLayout());

    this.controller = controller;

    // barTool
    barTool = new JPanel(new BorderLayout());
    barTool.setPreferredSize(new Dimension(
        this.getPreferredSize().width, 40));
    // top toolbar
    this.add(barTool, BorderLayout.NORTH);
    // buttons
    buttons = new JPanel(new FlowLayout());
    barTool.add(buttons, BorderLayout.WEST);
   // Conform
    JBConform = new JButton("✔️");
    JBConform.setActionCommand("Conform");
    buttons.add(JBConform);
    // Reject
    JBReject = new JButton("✖️");
    buttons.add(JBReject);
    // JBInput
    JBInput = new JTextField();
    barTool.add(JBInput, BorderLayout.CENTER);


    JTabbedPane tabbedPane = new JTabbedPane();
    //tabbedPane.setBackground( Color.BLACK);
    tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));
    this.add(tabbedPane);

    Rows = 5;
    Columns = 10;

    scrollPaneHashMap = new HashMap<>();

    for (String name: controller.getSheetNames()) {
      JScrollPane scrollPane1 = new JScrollPane(
          new CustomOverlayPanel(controller.getWorkBookAdaptor(name), headerH, headerW,
              cellHeight, cellWidth, Rows, Columns));
      tabbedPane.add(name, scrollPane1);
      scrollPaneHashMap.put(name, scrollPane1);
    }


    // JBReject
    JBReject.addActionListener((ActionEvent e) -> {
      setJBReject();
    });

    this.pack();
  }



  @Override
  public void addActionListener(ActionListener listener) {
    // for the buttons.
    this.JBConform.addActionListener(listener);
  }

  @Override
  public void addMouseListener(MouseListener listener, String name) {
    // try to get CustomOverlayPanel from scrollerPen.
    CustomOverlayPanel panel = castScrollPaneHashMapToPanel(name);
    panel.addMouseListener(listener);
  }

  @Override
  public void addScrollListener(ChangeListener listener, String name) {
    scrollPaneHashMap.get(name).getViewport().addChangeListener(listener);
  }

  /**
   * get the viewPort for the scrollPane.
   * @param sheetName
   * @return
   */
  @Override
  public Rectangle getViewRect(String sheetName) {
    return scrollPaneHashMap.get(sheetName).getViewport().getViewRect();
  }

  @Override
  public void changedRowsColumns(int rows, int columns, String sheetName) {
    CustomOverlayPanel panel =  castScrollPaneHashMapToPanel(sheetName);
    panel.changedRowsColumns(rows, columns);
  }

  @Override
  public int getContentHeight(String sheetName) {
    CustomOverlayPanel panel = castScrollPaneHashMapToPanel(sheetName);
    return panel.getContentHeight();
  }

  @Override
  public int getContentWidth(String sheetName) {
    CustomOverlayPanel panel = castScrollPaneHashMapToPanel(sheetName);
    return panel.getContentWidth();
  }

  @Override
  public int getRows(String sheetName) {
    CustomOverlayPanel panel = castScrollPaneHashMapToPanel(sheetName);
    return panel.getRows();
  }

  @Override
  public int getColumns(String sheetName) {
    CustomOverlayPanel panel = castScrollPaneHashMapToPanel(sheetName);
    return panel.getColumns();
  }

  @Override
  public void selectCell(Coord coord, String sheetName) {
    CustomOverlayPanel panel = castScrollPaneHashMapToPanel(sheetName);
    panel.setSelected(coord);
    panel.repaint();

    this.coord = coord;
    this.sheetName = sheetName;
    refreshInputValue();
  }

  void refreshInputValue(){
    if(coord != null) {
      setInputValue(getRawValue(coord, sheetName));
      //setInputValue(getCellRawValue(coord));
    }else {
      setInputValue("");
    }
  }


  /**
   * set the value of the {@code JBInput}
   * @param text the value will be set
   */
  void setInputValue(String text){
    if(text != null) {
      JBInput.setText(text);
    }else {
      JBInput.setText("");
    }
  }

  public Coord mouseCoordToCellCoord(int x, int y, String sheetName) {
    CustomOverlayPanel panel = castScrollPaneHashMapToPanel(sheetName);
    return panel.mouseCoordToCellCoord(x, y);
  }

  private String getRawValue(Coord coord, String sheetName) {
    return controller.getWorkBookAdaptor(sheetName).getRawValue(coord);
  }


  /**
   * the action fn for the reject button
   */
  void setJBReject() {
    if (coord != null) {
      try {
        // determine which cell was clicked
        setInputValue(getRawValue(coord, sheetName));
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

  @Override
  public void ShowErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void refresh() {
    this.repaint();
  }
  @Override
  public void showView() {
    this.setVisible(true);
  }


  /**
   * cast hash map scroll to CustomOverlayPanel
   * @param name
   * @return CustomOverlayPanel from scrollPen
   */
 private CustomOverlayPanel castScrollPaneHashMapToPanel( String name) {
    return (CustomOverlayPanel) scrollPaneHashMap
        .get(name)
        .getViewport()
        .getView();
  }
}

 class Run{
   public  static void main(String[] args){
     WorkBook wb = new WorkBookImpl();
     wb.add(new WorksheetModel(), "1");
     wb.add(new WorksheetModel(), "2");
     wb.add(new WorksheetModel(), "3");
     wb.add(new WorksheetModel(), "4");

     IMultController controller = new MultController(wb);
     MultView multView = new MultView(controller);
     controller.run(multView);
   }
}
