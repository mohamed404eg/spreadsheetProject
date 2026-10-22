package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.IModelAdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.IView;

public interface IController extends  IModelAdaptorSpreadSheet, IMouseController {
  /**
   * run the Controller.
   * @param model      the model
   * @param editorView the view
   * @throws IllegalArgumentException if model or editorView null
   */
  void run(Worksheet<Sexp> model, IView editorView);

  /**
   * you can call w/o IView, will create one {@code EditorView}
   *
   * @param model the model
   */
  void run(Worksheet<Sexp> model);


}
