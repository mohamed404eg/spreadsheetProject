package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.UtilVisitor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Textual implements TextualSave, TextualRender{
 private Worksheet<?> model;
 private Appendable out;

  /**
   *
   * @param model
   * @param out
   * @throws IllegalArgumentException model null or out is .
   */
  public Textual(Worksheet<?> model, Appendable out){
   if(model == null || out == null){
     throw new IllegalArgumentException("Null Model or out given");
   }
   this.model = model;
   this.out = out;
  }

  @Override
  public void render() throws IOException {
    for (int i = 1; i <= model.getMaxColumns(); i++) {
      for (int r = 1; r <= model.getColumnMaxSize(i); r++) {
        Coord coord = new Coord(i, r);
        String v = model.getRawValue(coord);
        if(v != null){
          Sexp se = Parser.parse(v);
          if(UtilVisitor.isSList(se)){
            out.append(coord.toString()).append(" =").append(v).append("\n");
          }else {
            out.append(coord.toString()).append(" ").append(v).append("\n");
          }
        }
      }
    }
  }

  @Override
  public void saveToDisc(String FileName) throws FileNotFoundException, UnsupportedEncodingException {
    File file = new File(FileName);
    PrintWriter pw = new PrintWriter(file);
    pw.print(out.toString());
    pw.close();
  }
}
