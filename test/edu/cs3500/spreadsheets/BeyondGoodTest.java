package edu.cs3500.spreadsheets;

import static org.junit.Assert.*;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import org.junit.Test;

public class BeyondGoodTest {

  @Test
  public void noArgs(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{});
    } finally {
     System.setOut(originalOut);
    }
    assertEquals("wrong args" + System.lineSeparator(), output.toString());
  }

  @Test
  public void wrongArgs(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"hi"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("nothing match." + System.lineSeparator(), output.toString());
  }

  @Test
  public void in(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("no args after -in!." + System.lineSeparator() , output.toString());
  }

  @Test
    public void WrongFileName(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "hi"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("No Operations: -eval,-save, -gui, -edit, -multEdit" + System.lineSeparator() , output.toString());
  }

  @Test
  public void InvalidFile(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "invalidformula.gOOD"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals( "No Operations: -eval,-save, -gui, -edit, -multEdit" + System.lineSeparator(), output.toString());
  }
  @Test
  public void validFileWOopt(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "posn-distances.gOOD"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("No Operations: -eval,-save, -gui, -edit, -multEdit" + System.lineSeparator(), output.toString());
  }

  @Test
  public void testEvalWoArgNoCoord(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "posn-distances.gOOD", "-eval"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("need cell name" + System.lineSeparator(), output.toString());
  }
  @Test
  public void testEvalWoArgOutCoord(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "posn-distances.gOOD", "-eval", "A99"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("", output.toString());
  }
  @Test
  public void testEvalNumber(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "posn-distances.gOOD", "-eval","A4" });
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("10.000000" + System.lineSeparator(), output.toString());
  }

  @Test
  public void testEvalBoolean(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "posn-distances.gOOD", "-eval","B3" });
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("false" + System.lineSeparator(), output.toString());
  }
  @Test
  public void testEvalString(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "double-quotes.gOOD", "-eval","A1" });
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("Jack says \"hi\". Path: C:\\Users\\" , output.toString());
  }

  @Test
  public void testSaveNotFileName(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "posn-distances.gOOD", "-save"});
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("need the save file name" + System.lineSeparator(), output.toString());
  }

  @Test
  public void testSave(){
    // copy the file
    BeyondGood.main(new String[]{"-in", "posn-distances.gOOD", "-save","posn-distances-copy.gOOD" });

    // compare both file
    File file = new File("posn-distances.gOOD");
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
      return;
    }
    // build the model
    Worksheet<Sexp> model1 = bd.createWorksheet();

    // second file
     file = new File("posn-distances-copy.gOOD");
    try {
      readable = new FileReader(file);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
     bd = new WorksheetBuilderImpl();
    try {
      WorksheetReader.read(bd, readable);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    // build the model
    Worksheet<Sexp> model2 = bd.createWorksheet();

   // start the compersion
    for (int i = 1; i <= model1.getMaxColumns() ; i++) {
      for (int r = 1; r <= model1.getColumnMaxSize(i); r++) {
        assertEquals(model1.getCell(new Coord(i, r)), model2.getCell(new Coord(i, r)));

      }
    }

  }

  @Test
  public void testEvalNumberWorkBook(){
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(output));

    try {
      BeyondGood.main(new String[]{"-in", "wb.gOOD", "-eval-wb","A!A1" });
    } finally {
      System.setOut(originalOut);
    }
    assertEquals("3.0" + System.lineSeparator(), output.toString());
  }

}