package edu.cs3500.spreadsheets.view;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * save the model to the way that methods provide,
 * take Appendable then save to which methods chosen
 *
 */
public interface TextualSave {
  /**
   * Write the output into the give file name
   * @param FileName the file name output
   */
 void saveToDisc(String FileName) throws FileNotFoundException, UnsupportedEncodingException;
}
