package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * the methods that {@code MouseConfig} need.
 */
public interface IMouseController {

  /**
   * delegate the method to the view through the controller.
   * @param x
   * @param y
   * @return
   */
  Coord mouseCoordToCellCoord(int x, int y);
  Coord mouseCoordToCellCoord(int x, int y, String sheetName);
  /**
   * send the {@code Coord} when the user clicked.
   * @param coord
   */
  void selectCell(Coord coord);

  /**
   * the call back if the scroll bar event happen.
   */
  void scrollChanged();
  /**
   * send the {@code Coord} when the user clicked.
   * @param coord
   * @param sheetName
   */
  void selectCell(Coord coord, String sheetName);
}