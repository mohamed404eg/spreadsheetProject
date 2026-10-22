package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestView {


  public static void main(String[] args) throws FileNotFoundException {

//    Worksheet ws;
//    ws = new WorksheetModel();
//
//    ws.set(new Coord(1,1) , "\"hi\"");
//    ws.set(new Coord(1, 4), "\"Jack says \\\"hi\\\". Path: C:\\\\Users\\\\\"");
//    ws.set(new Coord(1,5) , "A1");


   File file = new File("text.gOOD");
   // File file = new File("double-quotes.gOOD");
//    File file = new File("empty.gOOD");
  //  File file = new File("invalidformula.gOOD");
    //File file = new File("posn-distances.gOOD");
    // File file = new File("selfReference.gOOD");
    Readable readable = new FileReader(file);
    WorksheetBuilder<Worksheet<Sexp>> bd = new WorksheetBuilderImpl();
    WorksheetReader.read(bd, readable);
    // create the model
    Worksheet<Sexp> ws = bd.createWorksheet();

    IView view = new View(ws);
    view.makeVisible();
  }
}
