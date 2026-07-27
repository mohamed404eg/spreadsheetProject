package edu.cs3500.spreadsheets.view;

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
 void saveToDisc(String FileName);
}
