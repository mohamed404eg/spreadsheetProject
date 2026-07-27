package edu.cs3500.spreadsheets;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
  public static void main(String[] args)   {
    /*
      TODO: For now, look in the args array to obtain a filename and a cell name,
      - read the file and build a model from it, 
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */

    // -in some-filename -eval some-cellname

    File file = new File(args[1]);
    Readable readable;
    try {
       readable = new FileReader(file);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    WorksheetBuilder<Worksheet<Sexp>> bd = new WorksheetBuilderImpl();
    try {
      WorksheetReader.read(bd , readable);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    Worksheet<Sexp> ws = bd.createWorksheet();
    Sexp result = ws.getCell(Coord.NameToCoord(args[3]));

    if (result instanceof SNumber) {
      System.out.printf("%f%n", Double.valueOf(((SNumber) result).toString()));
    } else if (result instanceof SBoolean) {
      System.out.println(((SBoolean) result));
    } else if (result instanceof SString) {
      String value = ((SString) result).toString();
      System.out.printf(value );
    }



  }
}
