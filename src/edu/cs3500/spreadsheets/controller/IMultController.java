package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.IModelAdaptorSpreadSheet;
import edu.cs3500.spreadsheets.model.WorkBook;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.IMultView;
import java.util.List;

public interface IMultController extends IMouseController {

  /**
   * For Run the program.
   */

  void run(IMultView multView);

  /**
   * get the adaptor for the view can deal w/ every sheet alone.
   * @param SheetName
   * @return IModelAdaptorSpreadSheet for specific sheet.
   */
  IModelAdaptorSpreadSheet getWorkBookAdaptor(String SheetName);

  /**
   * get all sheet name.
   * @return the sheets name.
   */
  List<String> getSheetNames();
}
