package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import java.util.Objects;

public class MouseCoord implements IMouseCoord {

  private int x;
  private int y;

  public MouseCoord(int x, int y){
    this.x = x;
    this.y = y;
  }
  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IMouseCoord ms = (IMouseCoord) o;
    return ms.getX() == this.getX() && this.getY() == ms.getY();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }

}
