package edu.cs3500.spreadsheets.controller;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * class for will call {@code controller.scrollChanged()} when scroll bar changed.
 * this class if it has been linked to the view will occur.
 */
public class ScrollChangeListener implements ChangeListener {

  private final IMouseController controller;

  public ScrollChangeListener(IMouseController controller) {
    this.controller = controller;
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    controller.scrollChanged();
  }
}