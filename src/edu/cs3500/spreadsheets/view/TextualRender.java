package edu.cs3500.spreadsheets.view;

import java.io.IOException;

/**
 * render the model to Appendable
 */
public interface TextualRender {
  /**
   * call for start the render model to the given appendable
   */
  void render() throws IOException;
}
