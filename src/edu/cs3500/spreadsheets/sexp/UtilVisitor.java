package edu.cs3500.spreadsheets.sexp;

public final class UtilVisitor {

  private UtilVisitor() {
    // Utility class; do not instantiate.
  }

  /**
   * @param sexp
   * @return true if sexp is SList
   */
  public static boolean isSList(Sexp sexp){
    return sexp instanceof SList;
  }

  /**
   * cast Sexp to number otherwise throw exception
   *
   * @param value the {@code Sexp}
   * @return {@code SNumber}
   * @throws IllegalArgumentException if not {@code Snumber}
   */
  static public SNumber requireNumber(Sexp value) {
    if (!(value instanceof SNumber)) {
      throw new IllegalArgumentException("Expected a number");
    }
    return (SNumber) value;
  }

  /**
   * check if Sexp is Snumber
   *
   * @param value
   * @return true if its Snumber otherise false
   */
  static public Boolean isNumber(Sexp value) {
    return value instanceof SNumber;
  }
}
