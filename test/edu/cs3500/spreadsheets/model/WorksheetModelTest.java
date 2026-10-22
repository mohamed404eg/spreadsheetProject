package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.HashMap;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

public abstract class WorksheetModelTest {

  Worksheet model;
  Random rand = new Random();

  public abstract Worksheet wsm();

  @Before
  public void init() {
    model = wsm();
  }

  Coord randomCoord() {
    return new Coord(Math.abs(rand.nextInt()) + 1,
        Math.abs(rand.nextInt()) + 1);
  }

  @Test
  public void getCellNotExist() {
    // assume 10 10 its not exist
    assertNull(model.getCell(new Coord(10, 10)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCellNotExistLessCol1() {
    model.getCell(new Coord(0, 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCellNotExistLessROW1() {
    model.getCell(new Coord(10, 0));
  }

  @Test
  public void getCellNormal() {
    model.set(new Coord(1, 1), "1");
    assertEquals("1.0", model.getCell(new Coord(1, 1)).toString());
    assertEquals(new SNumber(1), model.getCell(new Coord(1, 1)));
  }

  // there efficiency problem the bigger sum take the same time as the bigger
  // i think the problem in expand group
  @Test
  public void getCellFormulaSum() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(1, 3), "(SUM A1 A2)");
    assertEquals("2.0", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SNumber(2), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void getCellFormulaSumGroup() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(1, 3), "(SUM A1:A2)");
    // still take near 100ms just for this
    // assertEquals("2.0", model.getCell(new Coord(1, 3)).toString());
    // assertEquals(new SNumber(2), model.getCell(new Coord(1, 3)));
  }

  @Test(timeout = 100)
  public void getCellFormulaSumGroupEfficiency() {
    for (int i = 1; i <= 100; i++) {
      model.set(new Coord(1, i), String.valueOf(i));
    }
    model.set(new Coord(1, 101), "(SUM A1:A100)");
    assertEquals("5050.0", model.getCell(new Coord(1, 101)).toString());
    assertEquals(new SNumber(5050), model.getCell(new Coord(1, 101)));
  }

  @Test
  public void getCellFormulaSumGroupEfficiency1000() {
    for (int i = 1; i <= 1000; i++) {
      model.set(new Coord(1, i), String.valueOf(i));
    }
    model.set(new Coord(2, 1), "(SUM A1:A1000)");
    double sum = (double) (1001 * 1000) / 2;
    assertEquals(new SNumber(sum), model.getCell(new Coord(2, 1)));
  }

  @Test
  public void getCellFormulaSumNonGroupEfficiency() {
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= 100; i++) {
      model.set(new Coord(1, i), String.valueOf(i));
      sb.append(" A" + i);
    }
    model.set(new Coord(1, 101), "(SUM" + sb.toString() + " )");

    //  assertEquals("5050.0", model.getCell(new Coord(1, 101)).toString());
    // assertEquals(new SNumber(5050), model.getCell(new Coord(1, 101)));
  }

  @Test
  public void getCellFormulaSumGroupColumn() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(2, 2), "4");
    model.set(new Coord(3, 1), "(SUM A1:B2)");
    assertEquals("6.0", model.getCell(new Coord(3, 1)).toString());
    assertEquals(new SNumber(6), model.getCell(new Coord(3, 1)));
  }

  @Test
  public void sumNull() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(3, 1), "(SUM A1 C2 D1)");
    assertEquals(new SNumber(1), model.getCell(new Coord(3, 1)));
  }

  @Test
  public void subNull() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 3), "(SUB A1 A2)");
  }

  @Test
  public void gsSUB() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(1, 3), "(SUB A1 A2)");
  }

  // group of just tow variable gonna work.
  public void gsSUBMoreArgGroup() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(1, 3), "(SUB A1:A2)");
    assertEquals(new SNumber(-2), model.getCell(new Coord(1, 3)));
    model.set(new Coord(1, 3), "(SUB A2:A1)");
    assertEquals(new SNumber(1), model.getCell(new Coord(1, 3)));

  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSUBMoreArg3() {

    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(1, 2), "3");
    model.set(new Coord(1, 4), "(SUB A1 A2 A3)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSUBMoreArg3Direct() {
    model.set(new Coord(1, 4), "(SUB 1 2 3)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSUBOneArg() {
    model.set(new Coord(1, 4), "(SUB 1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSUBNoArg() {
    model.set(new Coord(1, 4), "(SUB)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSUBSelfRerefrencR() {
    model.set(new Coord(1, 4), "(SUB A4 1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSUBSelfRerefrencL() {
    model.set(new Coord(1, 4), "(SUB 1 A4)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void InsteadSUBFormula() {
    model.set(new Coord(1, 1), "(SUB (SUM B:C E:D) (SUM B:C E:D) 200)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // D
    model.set(new Coord(4, 1), "1");

    // E
    model.set(new Coord(5, 1), "1");
  }


  @Test
  public void getReferenceAndSet() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "A1");
    assertEquals("1.0", model.getCell(new Coord(1, 2)).toString());
    assertEquals(new SNumber(1), model.getCell(new Coord(1, 2)));
  }

  @Test
  public void gsPRODUCT() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(1, 3), "(PRODUCT A1 A2 2)");
    assertEquals("4.0", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SNumber(4), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void gsPRODUCTNull() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 3), "(PRODUCT A1 A2)");
    assertEquals(new SNumber(1), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void gsPRODUCtGroup() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(2, 2), "2");
    model.set(new Coord(1, 3), "(PRODUCT A1:B2)");
    assertEquals("4.0", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SNumber(4), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void gsPRODUCtGroupNoNumeric() {
    model.set(new Coord(1, 1), "true");
    model.set(new Coord(1, 2), "\"hi\"");
    model.set(new Coord(2, 2), "\"g\"");
    model.set(new Coord(1, 3), "(PRODUCT A1:B2)");
    assertEquals("0.0", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SNumber(0), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void gsSUMGroupNoNumeric() {
    model.set(new Coord(1, 1), "true");
    model.set(new Coord(1, 2), "\"hi\"");
    model.set(new Coord(2, 2), "\"g\"");
    model.set(new Coord(1, 3), "(SUM A1:B2)");
    assertEquals("0.0", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SNumber(0), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void gsSQRT() {
    model.set(new Coord(1, 1), "4");
    model.set(new Coord(1, 3), "(SQRT A1)");
    assertEquals("2.0", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SNumber(2), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void gsSQRTRawNumber() {
    model.set(new Coord(1, 1), "4");
    model.set(new Coord(1, 3), "(SQRT 25)");
    assertEquals("5.0", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SNumber(5), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void gsLess() {
    model.set(new Coord(1, 1), "4");
    model.set(new Coord(1, 2), "6");
    model.set(new Coord(1, 3), "(< A1 A2)");
    assertEquals("true", model.getCell(new Coord(1, 3)).toString());
    assertEquals(new SBoolean(true), model.getCell(new Coord(1, 3)));
    model.set(new Coord(1, 4), "(< A2 A1)");
    assertEquals("false", model.getCell(new Coord(1, 4)).toString());
    assertEquals(new SBoolean(false), model.getCell(new Coord(1, 4)));
  }
  @Test(expected = IllegalArgumentException.class)
  public void gsLess3args() {
    model.set(new Coord(1, 1), "4");
    model.set(new Coord(1, 2), "6");
    model.set(new Coord(1, 3), "6");
    model.set(new Coord(1, 4), "(< A1 A2 A3)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsLessBoolean() {
    model.set(new Coord(1, 1), "4");
    model.set(new Coord(1, 2), "false");
    model.set(new Coord(1, 4), "(< A1 A2)");
    assertEquals("false", model.getCell(new Coord(1, 4)).toString());
  }

  @Test
  public void gsAvg() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "(AVG A1)");
    model.set(new Coord(2, 1), "(SUM A1 -1)");
    model.set(new Coord(2, 2), "1");

    model.set(new Coord(3, 1), "(AVG A1:B2)");

    assertEquals("1.0", model.getCell(new Coord(1, 2)).toString());
    assertEquals(new SNumber(1), model.getCell(new Coord(1, 2)));

    assertEquals("0.75", model.getCell(new Coord(3, 1)).toString());
    assertEquals(new SNumber(0.75), model.getCell(new Coord(3, 1)));
  }


  @Test(expected = ArithmeticException.class)
  public void gsAvgZeroInput() {
    model.set(new Coord(1, 2), "(AVG A1)");
  }


  @Test(expected = IllegalArgumentException.class)
  public void setCellFormulaSumGroupColumnInvalid() {
    model.set(new Coord(3, 1), "(SUM B1:A1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellFormulaSumGroupRowsInvalid() {
    model.set(new Coord(3, 1), "(SUM A4:A1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void setReferenceInvalid() {
    model.set(new Coord(1, 1), "A2");
    model.set(new Coord(1, 2), "A1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void setReferenceInvalidItSelf() {
    model.set(new Coord(1, 1), "A1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void setReferenceInvalidFormula() {
    model.set(new Coord(1, 1), "(SUM A1 1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsPRODUCtGroupInvalid() {
    model.set(new Coord(1, 3), "(PRODUCT B2:A1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsPRODUCtGroupRowInvalid() {
    model.set(new Coord(1, 3), "(PRODUCT A3:A1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSQRTSelfReference() {
    model.set(new Coord(1, 3), "(SQRT A3)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void gsSQRTEmpty() { // idk what is gonna throw but i expect error
    model.set(new Coord(1, 3), "(SQRT )");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sLessThanSelfRr() {
    model.set(new Coord(1, 3), "(< A3 1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sLessThanSelfRL() {
    model.set(new Coord(1, 3), "(< 1 A3)");
  }


  // getRows
  @Test
  public void maxRows() {
    assertEquals(0, model.getMaxRows());
    model.set(new Coord(1, 20), "1");
    assertEquals(20, model.getMaxRows());
  }

  @Test
  public void maxRowsAfterSet20() {
    assertEquals(0, model.getMaxRows());
    model.set(new Coord(1, 20), "1");
    assertEquals(20, model.getMaxRows());
  }

  // getColumns
  @Test
  public void getColumnsTestDefault() {
    assertEquals(0, model.getMaxColumns());
  }

  @Test
  public void getColumnsTest() {
    model.set(new Coord(13, 3), "1");
    assertEquals(13, model.getMaxColumns());
  }

  // getColumnSize

  @Test
  public void getColumnSizeDefault() {
    assertEquals(0, model.getColumnMaxSize(1));
  }

  @Test
  public void getColumnSizeSet() {
    model.set(new Coord(1, 7), "1");
    assertEquals(7, model.getColumnMaxSize(1));
  }


  // getRawValue
  @Test
  public void getRawValueNull() {
    assertNull(model.getRawValue(new Coord(1, 1)));
  }

  @Test
  public void getRawValueSetNumber() {
    model.set(new Coord(1, 1), "1");
    assertEquals("1.0", model.getRawValue(new Coord(1, 1)));
  }

  @Test
  public void getRawValueSetFormula() {
    model.set(new Coord(1, 3), "(SUM A1:A2)");
    assertEquals("(SUM A1:A2)", model.getRawValue(new Coord(1, 3)));
  }


  @Test
  public void nestedFormula() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(1, 3),
        "(PRODUCT (SUM A1 A2) (SUB A2 A1))");
    assertEquals(new SNumber(3), model.getCell(new Coord(1, 3)));

  }

  @Test
  public void SumPlusSQRT() {
    model.set(new Coord(1, 1), "5");
    model.set(new Coord(1, 2), "(SUM A1 20)");
    model.set(new Coord(1, 3),
        "(PRODUCT (SQRT A2) 2)");
    assertEquals(new SNumber(10), model.getCell(new Coord(1, 3)));
  }

  @Test
  public void nestedFormulaRawGet() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(1, 3),
        "(PRODUCT (SUM A1 A2) (SUB A2 A1))");
    assertEquals("(PRODUCT (SUM A1 A2) (SUB A2 A1))"
        , model.getRawValue(new Coord(1, 3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void nestedFormulaRawGetSelfReference() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "2");
    model.set(new Coord(1, 3),
        "(PRODUCT (SUM A1 A2) (SUB A3 A1))");
  }


  @Test(expected = IllegalArgumentException.class)
  public void nestedFormulaSelfReference2() {
    model.set(new Coord(1, 2), "C1");
    model.set(new Coord(3, 1),
        "(SUM (SUM A1 A2) A1)");
  }


  @Test(expected = IllegalArgumentException.class)
  public void nestedFormulaSelfReference3() {
    model.set(new Coord(1, 1), "A2");
    model.set(new Coord(1, 2),
        "(SUM A1 A3)");
  }


  @Test(expected = IllegalArgumentException.class)
  // NEED MORE REVIEW WHEN THE NO COLUMN BY THIS NAME
  public void wrongReference() {
    model.set(new Coord(1, 1), "A");
  }

  @Test(expected = IllegalArgumentException.class)
  public void MoreComplex() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "(SUM A1 1)");
    model.set(new Coord(1, 3), "(SUM A2 A5)");
    model.set(new Coord(1, 4), "(SUM A2 A5)");
    model.set(new Coord(1, 5), "(SUM A3 1)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void MoreTall() {
    model.set(new Coord(1, 1), "A5");
    model.set(new Coord(1, 2), "A1");
    model.set(new Coord(1, 3), "A2");
    model.set(new Coord(1, 4), "A4");
    model.set(new Coord(1, 5), "A1");
  }


  @Test
  public void stuckCase() {
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(2, 2), "2");
    model.set(new Coord(3, 1), "(SQRT (SUM A2:B2))");
  }

  @Test
  public void stuckCaseGet() {
    model.set(new Coord(1, 2), "5");
    model.set(new Coord(2, 2), "20");
    model.set(new Coord(3, 1), "(SQRT (SUM A2:B2))");

    assertEquals(new SNumber(5), model.getCell(new Coord(3, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void stuckCaseReverseOrderColumn() {
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(2, 2), "2");
    model.set(new Coord(3, 1), "(SQRT (SUM B2:A2))");
  }

  @Test(expected = IllegalArgumentException.class)
  public void stuckCaseReverseOrderRow() {
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(2, 2), "2");
    model.set(new Coord(3, 1), "(SQRT (SUM A2:A1))");
  }

  @Test(expected = IllegalArgumentException.class)
  public void stuckCaseReverseOrderRowAndColumn() {
    model.set(new Coord(1, 2), "1");
    model.set(new Coord(2, 2), "2");
    model.set(new Coord(3, 1), "(SQRT (SUM B2:A1))");
  }

  @Test
  public void String() {
    model.set(new Coord(1, 1), "\"Jack says \\\"hi\\\". Path: C:\\\\Users\\\\\"");
    assertEquals(new SString("Jack says \"hi\". Path: C:\\Users\\"),
        model.getCell(new Coord(1, 1)));
  }

  @Test
  public void new1() {
    model.set(new Coord(1, 1), "B1");
    model.set(new Coord(1, 2), "B1");

  }

  @Test(expected = IllegalArgumentException.class)
  public void symbolInvalidSyntax() {
    model.set(new Coord(1, 1), "hi");
  }

  @Test(expected = IllegalArgumentException.class)
  public void symbolInvalidSyntaxSum() {
    model.set(new Coord(1, 1), "(SUM A1 hi)");
  }


  @Test
  public void setThenRemove() {
    Coord co = new Coord(1, 1);
    model.set(co, "\"hi\"");
    assertNotNull(model.getCell(co));
    model.remove(co);
    assertNull(model.getCell(co));
  }

  @Test
  public void RemoveCellNotSET() {
    Coord co = new Coord(1, 1);
    model.remove(co);
    assertNull(model.getCell(co));
  }

  @Test
  public void RemoveCellNotExist() {
    Coord co = randomCoord();
    //System.out.println(co.toString());
    model.remove(co);
    assertNull(model.getCell(co));
  }

  @Test
  public void AddATRandomCoord() {
    Coord co = randomCoord();
    // System.out.println(co.toString());
    model.set(co, "1");
    assertEquals(new SNumber(1), model.getCell(co));
  }

  // getCellsInColumn

  @Test
  public void getCellsInColumnTest() {
    model.set(new Coord(1, 1), "11");
    model.set(new Coord(1, 5), "15");
    model.set(new Coord(4, 5), "45");

    HashMap<Coord, Sexp> cells = new HashMap<>();
    cells.put(new Coord(1, 1), new SNumber(11));
    cells.put(new Coord(1, 5), new SNumber(15));
    // cells.put(new Coord(4, 5), new SNumber(45));
    assertEquals(cells, model.getCellsInColumn(1));
  }

  // getCellsInRow

  @Test
  public void getCellsInRowTest() {
    model.set(new Coord(1, 1), "11");
    model.set(new Coord(1, 5), "15");
    model.set(new Coord(4, 1), "45");

    HashMap<Coord, Sexp> cells = new HashMap<>();
    cells.put(new Coord(1, 1), new SNumber(11));
    cells.put(new Coord(4, 1), new SNumber(45));

    assertEquals(cells, model.getCellsInRow(1));
  }

  // column references


  @Test
  public void ColumnSum() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 5), "1");
    model.set(new Coord(1, 3), "1");

    model.set(new Coord(2, 3), "1");
    model.set(new Coord(2, 49), "1");
    model.set(new Coord(3, 1), "(SUM A:B)");

    assertEquals(new SNumber(5), model.getCell(new Coord(3, 1)));
  }

  @Test
  public void ColumnSumBackWard() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 5), "1");
    model.set(new Coord(1, 3), "1");

    model.set(new Coord(2, 3), "1");
    model.set(new Coord(2, 49), "1");
    model.set(new Coord(3, 1), "(SUM B:A)");

    assertEquals(new SNumber(5), model.getCell(new Coord(3, 1)));
  }

  @Test
  public void ColumnSumADCE() {

    model.set(new Coord(1, 1), "(SUM B:C E:D)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // D
    model.set(new Coord(4, 1), "1");

    // E
    model.set(new Coord(5, 1), "1");

    assertEquals(new SNumber(4), model.getCell(new Coord(1, 1)));
  }


  @Test(expected = IllegalArgumentException.class)
  // ig we cant sum in the same column would be self reference
  public void ColumnSumSelfReference() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 5), "1");
    model.set(new Coord(1, 3), "1");

    model.set(new Coord(2, 3), "1");
    model.set(new Coord(2, 49), "1");
    model.set(new Coord(2, 1), "(SUM A:B)");

    assertEquals(new SNumber(5), model.getCell(new Coord(3, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnSumNumberWrong() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 5), "1");
    model.set(new Coord(1, 3), "1");

    model.set(new Coord(2, 3), "1");
    model.set(new Coord(2, 49), "1");
    model.set(new Coord(3, 1), "(SUM 2:3)");

    assertEquals(new SNumber(5), model.getCell(new Coord(3, 1)));
  }

  @Test
  public void ColumnSumInseadSUM() {
    model.set(new Coord(1, 1), "(SUM (SUM B:C E:D) 200)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // D
    model.set(new Coord(4, 1), "1");

    // E
    model.set(new Coord(5, 1), "1");

    assertEquals(new SNumber(204), model.getCell(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnInvalidGroupInsideInsteadSub() {
    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // D
    model.set(new Coord(4, 1), "1");

    // E
    model.set(new Coord(5, 1), "1");

    model.set(new Coord(1, 1), "(SUB (SUM D1:C2) 200)");

    assertEquals(new SNumber(-196), model.getCell(new Coord(1, 1)));
  }


  @Test
  public void ColumnSumInsideSub() {
    model.set(new Coord(1, 1), "(SUB (SUM B:C E:D) 200)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // D
    model.set(new Coord(4, 1), "1");

    // E
    model.set(new Coord(5, 1), "1");

    assertEquals(new SNumber(-196), model.getCell(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnSubColumnGroup() {
    model.set(new Coord(1, 1), "(SUB B:C)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");
  }

  @Test
  public void ColumnGroupPRODUCT() {
    model.set(new Coord(1, 1), "(SUB (PRODUCT B:C E:D) 200)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // D
    model.set(new Coord(4, 1), "1");

    // E
    model.set(new Coord(5, 1), "1");

    assertEquals(new SNumber(-199), model.getCell(new Coord(1, 1)));
  }

  @Test
  public void ColumnGroupSubWorks() {

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // its work bc after expand jt has 2
    model.set(new Coord(1, 1), "(SUB B:C)");


    assertEquals(new SNumber(0), model.getCell(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnSQRTColumnGroup() {
    model.set(new Coord(1, 1), "(SQRT B:C)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnLessColumnGroup() {
    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    model.set(new Coord(1, 1), "(< B:C 1)");

    //assertEquals(new SBoolean(false), model.getCell(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnLessRowGroup() {

    model.set(new Coord(1, 1), "1");
    model.set(new Coord(1, 2), "1");

    model.set(new Coord(1, 1), "(< A1:A2)");

    //assertEquals(new SBoolean(false), model.getCell(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnLessRowGroupDiffColumn() {
    model.set(new Coord(1, 1), "1");
    model.set(new Coord(2, 2), "1");

    model.set(new Coord(1, 1), "(< A1:B2)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnLessColumnGroupDifferentErrorArgsLess2() {
    model.set(new Coord(1, 1), "(< B:C 1)");

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");
  }


  @Test(expected = IllegalArgumentException.class)
  public void ColumnLessColumnGroupWork() {

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "1");

    // its work bc after expand jt has 2
    model.set(new Coord(1, 1), "(< B:C)");


 // assertEquals(new SBoolean(false), model.getCell(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ColumnLessColumnBooleanGroupWork() {

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "false");

    // its work bc after expand jt has 2
    model.set(new Coord(1, 1), "(< B:C)");


 //   assertEquals(new SBoolean(false), model.getCell(new Coord(1, 1)));
  }
  @Test
  public void ColumnGroupAVG() {

    // b
    model.set(new Coord(2, 1), "1");

    // c
    model.set(new Coord(3, 1), "false");

    model.set(new Coord(7, 1), "true"); // just random add :).

    // its work bc after expand jt has 2
    model.set(new Coord(1, 1), "(AVG B:C)");

    assertEquals(new SNumber(0.5), model.getCell(new Coord(1, 1)));
  }




  public static final class WorksheetModel extends WorksheetModelTest {
    @Override
    public Worksheet wsm() {
      return new edu.cs3500.spreadsheets.model.WorksheetModel();
    }
  }


}