package edu.cs3500.spreadsheets.view;

import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public abstract class TextualTest {

  Worksheet model;

  public abstract Worksheet wsm();

  Textual tx;
  Appendable out;

  @Before
  public void init() {
    model = wsm();
    out = new StringBuilder();
    tx = new Textual(model, out);
  }


  void setupModel() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2"); // number
    model.set(new Coord(1, 3),
        "(PRODUCT (SUM A1 A2) (SUB A2 A1))"); // variable
    model.set(new Coord(1, 4), "\"Jack says \\\"hi\\\". Path: C:\\\\Users\\\\\""); // string
    model.set(new Coord(2, 1), "(SUM A1:A4)");
    model.set(new Coord(2, 2), "(< A3 B1)"); // boolean
    model.set(new Coord(2, 4), "true"); // boolean
    model.set(new Coord(2, 6), "\"true\""); // string

  }

  /**
   * render the model then save it , then render to model back then compare both of them.
   *
   * @throws FileNotFoundException
   */
  @Test
  public void render() throws IOException {
    setupModel();
    tx.render();
    tx.saveToDisc("text.gOOD");

    // render again back
    File file = new File("text.gOOD");
    Readable readable = new FileReader(file);
    WorksheetBuilder<Worksheet<Sexp>> bd = new WorksheetBuilderImpl();
    WorksheetReader.read(bd, readable);
    // create the model
    Worksheet<Sexp> modelFile = bd.createWorksheet();

    // compare the tow model
    for (int c = 1; c <= model.getMaxColumns(); c++) {
      for (int r = 1; r <= model.getColumnMaxSize(c); r++) {
        Coord cord = new Coord(c, r);
        assertEquals(model.getCell(cord), modelFile.getCell(cord));
      }
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void ModelNull() {
    Textual tx = new Textual(null, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void ModelOut() {
    Textual tx = new Textual(model, null);
  }

  public static final class WorksheetModel extends TextualTest {
    @Override
    public Worksheet wsm() {
      return new edu.cs3500.spreadsheets.model.WorksheetModel();
    }
  }


}