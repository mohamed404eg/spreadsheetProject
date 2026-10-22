package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.*;

import edu.cs3500.spreadsheets.model.WorkBookReader.WorkbookBuilder;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Test;

public class WorkbookBuilderImplTest {



  @Test
  public void testCreateWorkbook() throws FileNotFoundException {
    WorkBook<Sexp> wb = new WorkBookImpl();
    wb.add(new WorksheetModel(), "A");
    wb.set("A!A1", "3");
    wb.set("A!A2", "(SUM A!A1 3)");

    WorkBook<Sexp> wb2 = WorkBookReader.read(new WorkbookBuilderImpl(), new FileReader(new File("wb.gOOD")));

    assertEquals(wb.get("A!A1"), wb2.get("A!A1"));
    assertEquals(new SNumber(3), wb.get("A!A1"));
    assertEquals(new SNumber(3), wb2.get("A!A1"));

    assertEquals(wb.get("A!A2"), wb2.get("A!A2"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void useSheetBeforeCreate() throws FileNotFoundException {
    WorkBook<Sexp> wb2 = WorkBookReader.read(new WorkbookBuilderImpl(), new FileReader(new File("wb-invalid.gOOD")));
  }
  @Test(expected = IllegalArgumentException.class)
  public void selfReferenceDiffSheet() throws FileNotFoundException {
    WorkBook<Sexp> wb2 = WorkBookReader.read(new WorkbookBuilderImpl(), new FileReader(new File("wb-slef-reference.gOOD")));
  }


}