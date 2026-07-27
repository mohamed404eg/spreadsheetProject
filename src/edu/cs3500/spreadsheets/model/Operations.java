package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

public interface Operations {

  /**
   * sum operations take list of expressions and evaluate
   *
   * @param l the list of Sexp
   * @return the result Sexp, the default value is zero.
   */
  Sexp sum(List<Sexp> l);

  /**
   * subtraction the second from the first
   *
   * @param sb1
   * @param sb2
   * @return the result of the subtractions
   */
  Sexp sub(Sexp sb1, Sexp sb2);

  /**
   * product all the given Sexp
   *
   * @param l
   * @return the result,the default value is zero.
   */
  Sexp product(List<Sexp> l);


  /**
   * determine whether the first less than the second the symbol "<"
   *
   * @param v1 the first
   * @param v2 the second
   * @return Sexp(true) if the first less than the second otherwise Sexp(False)
   */
  Sexp lessThan(Sexp v1, Sexp v2);

  /**
   * the square root for the given Sexp
   *
   * @param v
   * @return the square root
   */
  Sexp sqrt(Sexp v);

  /**
   * take the average, if there non-numeric its count at the denm but not add to num
   * <p> the avg equation = {@code denm / num} </p>
   *
   * @param l
   * @return the average
   * @throws ArithmeticException if there is not any numeric
   */
  Sexp avg(List<Sexp> l);


}
