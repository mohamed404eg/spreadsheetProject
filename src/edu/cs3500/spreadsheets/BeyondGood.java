package edu.cs3500.spreadsheets;

import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.controller.IMultController;
import edu.cs3500.spreadsheets.controller.MultController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkBook;
import edu.cs3500.spreadsheets.model.WorkBookImpl;
import edu.cs3500.spreadsheets.model.WorkBookReader;
import edu.cs3500.spreadsheets.model.WorkBookReader.WorkbookBuilder;
import edu.cs3500.spreadsheets.model.WorkbookBuilderImpl;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.EditorView;
import edu.cs3500.spreadsheets.view.IView;
import edu.cs3500.spreadsheets.view.MultView;
import edu.cs3500.spreadsheets.view.Textual;
import edu.cs3500.spreadsheets.view.View;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * The main class for our program.
 */
public class BeyondGood {

  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {

    if (args.length < 1) {
      System.out.println("wrong args");
      return;
    }

    if(args.length < 2 && args[0].equals("-in")){
      System.out.println("no args after -in!.");
      return;
    }
    if (args[0].equals("-in")) {
      if (args.length < 3) {
        System.out.println("No Operations: -eval,-save, -gui, -edit, -multEdit");
        return;
      }
      Worksheet<Sexp> ws;
      switch (args[2]) {
        case "-eval":
          if (args.length < 4) {
            System.out.println("need cell name");
            return;
          }
          // build the model
         ws = FileToModel(args[1]);
          eval(ws, args[3]);
          break;
        case "-eval-wb":
          if (args.length < 4) {
            System.out.println("need cell name");
            return;
          }
          // build the model
          try {
            WorkBook<Sexp> wb = WorkBookReader.read(new WorkbookBuilderImpl(), new FileReader(new File(args[1])));
            eval_wb(wb, args[3]);
          } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
          }
          break;
        case "-save":
          if (args.length < 4) {
            System.out.println("need the save file name");
            return;
          }
          // build the model
          ws = FileToModel(args[1]);
          save(ws, args[3]);
          break;
        case "-gui":
          // build the model
          ws = FileToModel(args[1]);
          gui(ws);
          break;
        case "-edit":
          // build the model
          ws = FileToModel(args[1]);
          editView(ws);
          break;
        case "-multEdit":
          try {
            WorkBook<Sexp> wb = WorkBookReader.read(new WorkbookBuilderImpl(), new FileReader(new File(args[1])));
            multEdit(wb);
          } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
          }
          break;
        default:
          System.out.println("operations doesn't match.");
      }


    } else if (args[0].equals("-gui")) {
      // empty view
      gui_empty();
    }else if(args[0].equals("-edit")){
      editView();
    }
    else if(args[0].equals("-multEdit")){
      multEdit();
    }
    else {
      System.out.println("nothing match.");
    }


  }

  /**
   * evaluate one cell in the workbook
   * @param wb
   * @param arg
   */
  private static void eval_wb(WorkBook<Sexp> wb, String arg) {
    Sexp sexp = wb.get(arg);
    if(sexp == null){
      System.out.println("");
    }else {
      System.out.println(sexp.toString());
    }
  }

  private static void multEdit() {
    WorkBook wb = new WorkBookImpl();
    wb.add(new WorksheetModel(), "1");
    wb.add(new WorksheetModel(), "2");
    wb.add(new WorksheetModel(), "3");
    IMultController controller = new MultController(wb);
    MultView multView = new MultView(controller);
    controller.run(multView);
  }

  private static void multEdit(WorkBook<Sexp> wb) {
    IMultController controller = new MultController(wb);
    MultView multView = new MultView(controller);
    controller.run(multView);
  }


  /**
   * show gui used the data in the give file name.
   *
   * @param model the model that view will use
   */
  private static void gui(Worksheet<Sexp> model) {
    IView view = new View(model);
  }

  /**
   * save this model into file w/ given name
   *
   * @param model
   * @param fileName
   */
  private static void save(Worksheet<Sexp> model, String fileName) {
    Appendable out = new StringBuilder();
    Textual tx = new Textual(model, out);
    try {
      tx.render();
      tx.saveToDisc(fileName);
    } catch (IOException e) {
      System.out.println("error at the save operation : " + e.getMessage());
    }
  }

  static void eval(Worksheet<Sexp> model, String cellName) {
    Sexp result = model.getCell(Coord.NameToCoord(cellName));

    if (result instanceof SNumber) {
      System.out.printf("%f%n", Double.valueOf(((SNumber) result).toString()));
    } else if (result instanceof SBoolean) {
      System.out.println(((SBoolean) result));
    } else if (result instanceof SString) {
      String value = ((SString) result).getValue();
      System.out.printf(value);
    }
  }

  /**
   * open empty gui
   */
  static void gui_empty() {
    Worksheet<Sexp> ws = new WorksheetModel();
    IView view = new View(ws);
  }


  static Worksheet<Sexp> FileToModel(String fileName) {
    // file
    File file = new File(fileName);
    Readable readable;
    try {
      readable = new FileReader(file);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
    WorksheetBuilder<Worksheet<Sexp>> bd = new WorksheetBuilderImpl();
    try {
      WorksheetReader.read(bd, readable);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    // build the model
    Worksheet<Sexp> ws = bd.createWorksheet();

    return ws;
  }

  /**
   * Open empty new {@code EditorView}
   */
  private static void  editView(){
    WorksheetBuilderImpl bd = new WorksheetBuilderImpl();
    Controller c = new  Controller();;
    c.run(bd.createWorksheet());
  }
  /**
   * show gui used the data in the give file name.
   *
   * @param model the model that view will use
   */
  private static void editView(Worksheet<Sexp> model) {
    Controller c = new  Controller();;
    c.run(model);
  }


}
