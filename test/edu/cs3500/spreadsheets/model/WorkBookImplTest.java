package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.*;

import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public abstract class WorkBookImplTest {

  WorkBook wb  = nswb();

  public  abstract WorkBook nswb();

  @Before
  public void setUp() throws Exception {
     wb = nswb();
     Worksheet<Sexp> ws1 = new WorksheetModel();
     Worksheet<Sexp> ws2 = new WorksheetModel();
     wb.add(ws1, "A");
     wb.add(ws2, "B");
  }
  @Test
  public void firstTest(){
    wb.set("A!A1", "1");
    wb.set("B!A1", "1");
    wb.set("A!A2", "B!A1");
    wb.set("B!A2", "A!A1");

    assertEquals(wb.get("A!A1"), wb.get("B!A1"));
    assertEquals(wb.get("A!A2"), wb.get("B!A2"));
    assertEquals( new SNumber(1), wb.get("A!A1"));
    assertEquals( new SNumber(1), wb.get("B!A1"));
  }
  // get
  @Test
  public void getNoneExistCell(){
    assertNull(wb.get("A!A1"));
  }

  // removeCell

  @Test
  public void removeCellCellExistCell(){
    assertNull(wb.get("A!A1"));

    wb.removeCell("A!A1");
    assertNull(wb.get("A!A1"));

    wb.set("A!A1", "1");
    assertEquals(new SNumber(1), wb.get("A!A1"));

    wb.removeCell("A!A1");
    assertNull(wb.get("A!A1"));
  }

  @Test(expected=IllegalArgumentException.class)
  public void removeCellSheetNotExist(){
    wb.removeCell("C!A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  removeCell1(){
    wb.removeCell("!A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  removeCell2(){
    wb.removeCell("A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  removeCell3(){
    wb.removeCell("AA1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void removeCell4(){
    wb.removeCell("!");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  removeCell5(){
    wb.removeCell("");
  }

  /// formula test.
  // SUM
  @Test
  public void testFormulaNoOtherRef(){
    wb.set("A!A1", "(SUM A!A2 A!A3 A!A4)");
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    assertEquals(new SNumber(3), wb.get("A!A1"));
  }

  @Test
  public void testFormulaGroupRows(){
    wb.set("A!A1", "(SUM A!A2:A4)");
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    assertEquals(new SNumber(3), wb.get("A!A1"));
  }
  @Test
  public void testFormulaGroupColumns(){
    wb.set("A!A1", "(SUM A!B:C A!C:D)");
    wb.set("A!B2", "1");
    wb.set("A!C3", "1");
    wb.set("A!D4", "1");

    assertEquals(new SNumber(4), wb.get("A!A1"));
  }

  // SUB
  @Test
  public void testFormulaSUBNoOtherRef(){
    wb.set("A!A1", "(SUB A!A2 A!A3)");
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    assertEquals(new SNumber(0), wb.get("A!A1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaSUBGroupRows(){ // row include all rows and columns in that rectangle.

    wb.set("A!A2", "1");
    wb.set("A!A3", "1");

    wb.set("A!A1", "(SUB A!A2:A4)");


    assertEquals(new SNumber(0), wb.get("A!A1"));
  }
  @Test
  public void testFormulaSUBGroupColumns(){
    wb.set("A!B2", "1");
    wb.set("A!C3", "1");
    wb.set("A!D4", "1");

    wb.set("A!A1", "(SUB A!B:C)");

    assertEquals(new SNumber(0), wb.get("A!A1"));
  }

  // PRODUCT
  @Test
  public void testFormulaPRODUCTNoOtherRef(){
    wb.set("A!A1", "(PRODUCT A!A2 A!A3 A!A4)");
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    assertEquals(new SNumber(1), wb.get("A!A1"));
  }

  @Test
  public void testFormulaPRODUCTGroupRows(){
    wb.set("A!A2", "1");
    wb.set("A!A3", "2");
    wb.set("A!A4", "3");

    wb.set("A!A1", "(PRODUCT A!A2:A4)");


    assertEquals(new SNumber(6), wb.get("A!A1"));
  }
  @Test
  public void testFormulaPRODUCTGroupColumns(){
    wb.set("A!A1", "(PRODUCT A!B:C A!C:D)");
    wb.set("A!B2", "2");
    wb.set("A!C3", "2");
    wb.set("A!D4", "2");

    assertEquals(new SNumber(16), wb.get("A!A1"));
  }

  // < less
  @Test
  public void testFormulaLessNoOtherRef(){
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    wb.set("A!A1", "(< A!A2 A!A3)");

    assertEquals(new SBoolean(false), wb.get("A!A1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaLessGroupRows(){
    wb.set("A!A1", "(< A!A2:A3)");
    wb.set("A!A2", "1");
    wb.set("A!A3", "2");

  //  assertEquals(new SBoolean(true), wb.get("A!A1"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testFormulaLessGroupColumns(){
    wb.set("A!A1", "(< A!B:C A!C:D)");
    wb.set("A!B2", "2");
    wb.set("A!D4", "1");

    assertEquals(new SBoolean(false), wb.get("A!A1"));
  }
  // AVG

  @Test
  public void testFormulaAVGNoOtherRef(){
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    wb.set("A!A1", "(AVG A!A2 A!A3 A!A4)");

    assertEquals(new SNumber(1), wb.get("A!A1"));
  }
  @Test(expected = ArithmeticException.class)
  public void testFormulaAVGNoOtherRefDivideByZero(){
    wb.set("A!A1", "(AVG A!A2 A!A3 A!A4)");
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    assertEquals(new SNumber(1), wb.get("A!A1"));
  }

  @Test
  public void testFormulaAVGGroupRows(){
    wb.set("A!A2", "1");
    wb.set("A!A3", "1");
    wb.set("A!A4", "1");

    wb.set("A!A1", "(AVG A!A2:A4)");


    assertEquals(new SNumber(1), wb.get("A!A1"));
  }
  @Test
  public void testFormulaAVGGroupColumns(){
    wb.set("A!A1", "(SUM A!B:C A!C:D)");
    wb.set("A!B2", "1");
    wb.set("A!C3", "1");
    wb.set("A!D4", "1");

    assertEquals(new SNumber(4), wb.get("A!A1"));
  }


  /// other sheet use the formula reference to other sheet
  // SUM
  @Test
  public void testFormulaNoOtherRefAtoB(){
    wb.set("A!A1", "(SUM B!A2 B!A3 B!A4)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    assertEquals(new SNumber(3), wb.get("A!A1"));
  }

  @Test
  public void testFormulaGroupRowsAtoB(){
    wb.set("A!A1", "(SUM B!A2:B4)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    assertEquals(new SNumber(3), wb.get("A!A1"));
  }
  @Test
  public void testFormulaGroupColumnsAtoB(){
    wb.set("B!B2", "1");
    wb.set("B!C3", "1");
    wb.set("B!D4", "1");

    wb.set("A!A1", "(SUM B!B:C B!C:D)");


    assertEquals(new SNumber(4), wb.get("A!A1"));
  }

  // SUB
  @Test
  public void testFormulaSUBNoOtherRefAtoB(){
    wb.set("A!A1", "(SUB B!A2 B!A3)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    assertEquals(new SNumber(0), wb.get("A!A1"));
  }

  @Test
  public void testFormulaSUBGroupRowsAtoB(){
    wb.set("A!A1", "(SUB B!A2:A3)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");

    assertEquals(new SNumber(0), wb.get("A!A1"));
  }
  @Test
  public void testFormulaSUBGroupRowsAtoB2Args(){
    wb.set("A!A1", "(SUB B!A2:A3)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");

    assertEquals(new SNumber(0), wb.get("A!A1"));
  }
  @Test
  public void testFormulaSUBGroupColumnsAtoB(){
    wb.set("B!B2", "1");
    wb.set("B!C3", "1");
    wb.set("B!D4", "1");
    wb.set("A!A1", "(SUB B!B:C)");
    assertEquals(new SNumber(0), wb.get("A!A1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaSUBGroupColumnsAtoBSetAfter(){

    wb.set("A!A1", "(SUB B!B:C)");
    wb.set("B!B2", "1");
    wb.set("B!C3", "1");
    wb.set("B!D4", "1");
    assertEquals(new SNumber(0), wb.get("A!A1"));
  }

  // PRODUCT
  @Test
  public void testFormulaPRODUCTNoOtherRefAtoB(){
    wb.set("A!A1", "(PRODUCT B!A2 B!A3 B!A4)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    assertEquals(new SNumber(1), wb.get("A!A1"));
  }

  @Test
  public void testFormulaPRODUCTGroupRowsAtoB(){
    wb.set("A!A1", "(PRODUCT B!A2:A4)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "2");
    wb.set("B!A4", "3");

    assertEquals(new SNumber(6), wb.get("A!A1"));
  }
  @Test
  public void testFormulaPRODUCTGroupColumnsAtoB(){
    wb.set("A!A1", "(PRODUCT B!B:C B!C:D)");
    wb.set("B!B2", "2");
    wb.set("B!C3", "2");
    wb.set("B!D4", "2");

    assertEquals(new SNumber(16), wb.get("A!A1"));
  }

  // < less
  @Test
  public void testFormulaLessNoOtherRefAtoB(){
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    wb.set("A!A1", "(< B!A2 B!A3)");

    assertEquals(new SBoolean(false), wb.get("A!A1"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testFormulaLessNoOtherRefAtoBSetAfter(){
    wb.set("A!A1", "(< B!A2 B!A3)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    assertEquals(new SBoolean(false), wb.get("A!A1"));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFormulaLessGroupRowsAtoB(){
    wb.set("B!A2", "1");
    wb.set("B!A3", "2");
    wb.set("A!A1", "(< B!A2:A3)");

    assertEquals(new SBoolean(true), wb.get("A!A1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaLessGroupRowsAto3Args(){
    wb.set("A!A1", "(< B!A2:A4)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "2");

    assertEquals(new SBoolean(true), wb.get("A!A1"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testFormulaLessGroupRowsAtoBSetAfter(){
    wb.set("A!A1", "(< B!A2:A3)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "2");
    assertEquals(new SBoolean(true), wb.get("A!A1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaLessGroupColumnsAtoB(){
    wb.set("A!A1", "(< B!B:C B!C:D)");
    wb.set("B!B2", "2");
    wb.set("B!D4", "1");

    assertEquals(new SBoolean(false), wb.get("A!A1"));
  }
  // AVG

  @Test
  public void testFormulaAVGNoOtherRefAtoB(){
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    wb.set("A!A1", "(AVG B!A2 B!A3 B!A4)");

    assertEquals(new SNumber(1), wb.get("A!A1"));
  }
  @Test(expected = ArithmeticException.class)
  public void testFormulaAVGNoOtherRefAtoBSetAfter(){
    wb.set("A!A1", "(AVG B!A2 B!A3 B!A4)");
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");

    assertEquals(new SNumber(1), wb.get("A!A1"));
  }

  @Test
  public void testFormulaAVGGroupRowsAtoB(){
    wb.set("B!A2", "1");
    wb.set("B!A3", "1");
    wb.set("B!A4", "1");
    wb.set("A!A1", "(AVG B!A2:A4)");


    assertEquals(new SNumber(1), wb.get("A!A1"));
  }
  @Test
  public void testFormulaAVGGroupColumnsAtoB(){
    wb.set("B!B2", "1");
    wb.set("B!C3", "1");
    wb.set("B!D4", "1");
    wb.set("A!A1", "(SUM B!B:C B!C:D)");


    assertEquals(new SNumber(4), wb.get("A!A1"));
  }



  // self reference

  @Test(expected=IllegalArgumentException.class)
  public void testSelfReferenceSameSheet(){
    wb.set("A!A1", "A!A2");
    wb.set("A!A2", "A!A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void testSelfReferenceSameSheetSameCell(){
    wb.set("A!A1", "A!A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void testSelfReferenceSameSheetSameCellFormula(){
    wb.set("A!A1", "(SUM A!A1)");
  }
  @Test(expected=IllegalArgumentException.class)
  public void testSelfReferenceDifferentSheet(){
    wb.set("A!A1", "B!A1");
    wb.set("B!A1", "A!A1");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testSelfReferenceDifferentThreeSheet(){
    wb.add(new WorksheetModel(), "C");
    wb.set("A!A1", "B!A1");
    wb.set("B!A1", "C!A1");
    wb.set("C!A1", "A!A1");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testSelfReferenceFormulaDifferentSheet(){
    wb.set("A!A1", "B!A1");
    wb.set("B!A1", "(SUM A!A1)");
  }





  // getCellsInRow
  @Test
  public void tsetgetCellsInRow(){
    HashMap<Coord, Sexp> A = new HashMap<Coord, Sexp>();
    A.put(new Coord(1,1), new SNumber(2));
    A.put(new Coord(2,1), new SNumber(3));
    A.put(new Coord(3,1), new SList(new  Sexp[]{new SSymbol("SUM"), new SSymbol("A!A1")}));
    wb.set("A!A1", "2");
    wb.set("A!B1", "3");
    wb.set("A!C1", "(SUM A!A1)");
    assertEquals(A, wb.getCellsInRow(1, "A"));

    HashMap<Coord, Sexp> B = new HashMap<Coord, Sexp>();
    wb.set("B!A1", "1");
    wb.set("B!B1", "2");
    wb.set("B!C1", "(SUM B!A1)");
    B.put(new Coord(1,1), new SNumber(1));
    B.put(new Coord(2,1), new SNumber(2));
    B.put(new Coord(3,1), new SList(new  Sexp[]{new SSymbol("SUM"), new SSymbol("B!A1")}));

    assertEquals(B, wb.getCellsInRow(1, "B"));
  }
  @Test(expected=IllegalArgumentException.class)
  public void getCellsInRowNegativeRow(){
    wb.getCellsInRow(-1,"A");
  }
  @Test(expected=IllegalArgumentException.class)
  public void getCellsInZeroRow(){
    wb.getCellsInRow(0,"A");
  }
  @Test(expected=IllegalArgumentException.class)
  public void getCellsInRowNoSheet(){
    wb.getCellsInRow(1,"J");
  }


  // getCellsInColumn
  @Test
  public void tsetgetCellsInColumn(){
    HashMap<Coord, Sexp> A = new HashMap<Coord, Sexp>();
    A.put(new Coord(1,1), new SNumber(2));
    A.put(new Coord(1,2), new SNumber(3));
    A.put(new Coord(1,3), new SNumber(3));
    A.put(new Coord(1,4), new SNumber(3));
    wb.set("A!A1", "2");
    wb.set("A!A2", "3");
    wb.set("A!A3", "3");
    wb.set("A!A4", "3");

  assertEquals(A , wb.getCellsInColumn(1, "A"));


    HashMap<Coord, Sexp> B = new HashMap<Coord, Sexp>();
    B.put(new Coord(3,1), new SNumber(2));
    B.put(new Coord(3,2), new SNumber(3));
    B.put(new Coord(3,3), new SNumber(3));
    B.put(new Coord(3,4), new SNumber(3));
    B.put(new Coord(3,5),  new SList(new  Sexp[]{new SSymbol("SUM"), new SSymbol("B!C4")}));
    wb.set("B!C1", "2");
    wb.set("B!C2", "3");
    wb.set("B!C3", "3");
    wb.set("B!C4", "3");
    wb.set("B!C5", "(SUM B!C4)");
    assertEquals(B, wb.getCellsInColumn(3, "B"));

  }

  @Test(expected=IllegalArgumentException.class)
  public void getCellsInColumnNegativeRow(){
    wb.getCellsInColumn(-1,"A");
  }
  @Test(expected=IllegalArgumentException.class)
  public void getCellsInColumnZeroRow(){
    wb.getCellsInColumn(0,"A");
  }
  @Test(expected=IllegalArgumentException.class)
  public void getCellsInColumnNoSheet(){
    wb.getCellsInColumn(1,"J");
  }

  // getRawValue
 @Test
  public void  testgetRawValue(){
    wb.set("A!A1", "1");
    wb.set("A!A2", "(SUM A!A1)");

    assertEquals("(SUM A!A1)", wb.getRawValue("A!A2"));
    assertEquals("1.0", wb.getRawValue("A!A1"));
  }



  // add
  @Test(expected=IllegalArgumentException.class)
  public void addSheetWithSameName(){
    wb.add(new WorksheetModel(), "A");
  }

  // remove
  @Test(expected=IllegalArgumentException.class)
  public void removeSheetThenAccectIt(){
    wb.remove("A");
    wb.set("A!A1", "1");
  }

  @Test
  public void removeSheetNormal(){
    wb.remove("A");
    wb.remove("B");
    wb.remove("C");
  }


  @Test(expected=IllegalArgumentException.class)
  public void removeSheetHasBeenRef(){
    wb.set("B!A1", "A!A1");
    wb.remove("A");
  }

  @Test(expected=IllegalArgumentException.class)
  public void removeSheetHasBeenRef2(){
    wb.set("B!A1", "A!A1");
    wb.set("A!A2", "B!A1");

    wb.remove("B");
    wb.remove("A");
  }
  @Test(expected=IllegalArgumentException.class)
  public void removeSheetHasBeenRef4(){
    wb.set("B!A1", "A!A1");
    wb.set("A!A2", "B!A1");

    wb.remove("A");
    wb.remove("B");
  }


  // getSheetNames
  @Test
  public void testgetSheetNames(){
    List<String> sheetNames = new ArrayList<String>();
    sheetNames.add("A");
    sheetNames.add("B");
    assertEquals(sheetNames, wb.getSheetNames());

    wb.remove("A");
    sheetNames.remove("A");
    assertEquals(sheetNames, wb.getSheetNames());

    wb.add(new WorksheetModel(), "A");
    sheetNames.add("A");
    sheetNames.sort(Comparator.naturalOrder()); // use internal hashmap
    assertEquals(sheetNames, wb.getSheetNames());
  }


  /// weird input
  // get
  @Test(expected=IllegalArgumentException.class)
  public void  getcell(){
    wb.get("!A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  getcell2(){
    wb.get("A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  getcell3(){
    wb.get("AA1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  getcell4(){
    wb.get("!");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  getcell5(){
    wb.get("");
  }
  // set
  @Test(expected=IllegalArgumentException.class)
  public void  setcell(){
    wb.set("!A1", "");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  setcell2(){
    wb.set("A1", "1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  setcell3(){
    wb.set("AA1", "1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void setcell4(){
    wb.set("!", "1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  setcell5(){
    wb.set("", "1");
  }
  // getRawValue
  @Test(expected=IllegalArgumentException.class)
  public void  getRawValuecell(){
    wb.getRawValue("!A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  getRawValuecell2(){
    wb.getRawValue("A1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  getRawValuecell3(){
    wb.getRawValue("AA1");
  }
  @Test(expected=IllegalArgumentException.class)
  public void getRawValueCell4(){
    wb.getRawValue("!");
  }
  @Test(expected=IllegalArgumentException.class)
  public void  getRawValue5(){
    wb.getRawValue("");
  }

  @Test(expected=IllegalArgumentException.class)
  public void  referenceSheetNotExistFormula(){
    wb.set("!A1", "(SUM C!A1)");
  }

  @Test(expected=IllegalArgumentException.class)
  public void  referenceSheetNotExistDirect(){
    wb.set("!A1", "C!A1");
  }





  public static final class TestWorkBook extends WorkBookImplTest {
    @Override
    public WorkBook<Sexp> nswb(){
      return new WorkBookImpl();
    }
  }
}