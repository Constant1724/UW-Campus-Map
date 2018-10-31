package hw2;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * <b>RatTerm</b> is an immutable representation of a term in a single-variable polynomial
 * expression. The term has the form C*x^E where C is a rational number and E is an integer.
 *
 * <p>A RatTerm, t, can be notated by the pair (C . E), where C is the coefficient of t, and E is
 * the exponent of t.
 *
 * <p>The zero RatTerm, (0 . 0), is the only RatTerm that may have a zero coefficient. For example,
 * (0 . 7) is an invalid RatTerm and an attempt to construct such a RatTerm (through the constructor
 * or arithmetic operations on existing RatTerms) will return the semantically equivalent RatTerm (0
 * . 0). For example, (1 . 7) + (-1 . 7) = (0 . 0).
 *
 * <p>(0 . 0), (1 . 0), (1 . 1), (1 . 3), (3/4 . 17), (7/2 . -1), and (NaN . 74) are all valid
 * RatTerms, corresponding to the polynomial terms "0", "1", "x", "x^3", "3/4*x^17", "7/2*x^-1" and
 * "NaN*x^74", respectively.
 */
// See RatNum's documentation for a definition of "immutable".
public final class RatTerm {

  /** Coefficient of this term. */
  private final RatNum coeff;

  /** Exponent of this term. */
  private final int expt;

  // Abstraction Function:
  // For a given RatTerm t, "coefficient of t" is synonymous with
  // t.coeff, and, likewise, "exponent of t" is synonymous with t.expt.
  // All RatTerms with a zero coefficient are represented by the
  // zero RatTerm, z, which has zero for its coefficient AND exponent.
  //
  // Representation Invariant:
  // coeff != null
  // coeff.equals(RatNum.ZERO) ==> expt == 0

  /** A constant holding a Not-a-Number (NaN) value of type RatTerm */
  public static final RatTerm NaN = new RatTerm(RatNum.NaN, 0);

  /** A constant holding a zero value of type RatTerm */
  public static final RatTerm ZERO = new RatTerm(RatNum.ZERO, 0);

  /** A constant holding a one value of type RatNum */
  private static final RatNum ONE = new RatNum(1);

  /**
   * @param c the coefficient of the RatTerm to be constructed.
   * @param e the exponent of the RatTerm to be constructed.
   * @spec.requires c != null
   * @spec.effects Constructs a new RatTerm t, with t.coeff = c, and if c.equals(RatNum.ZERO), then
   *     t.expt = 0, otherwise t.expt = e
   */
  public RatTerm(RatNum c, int e) {
    if (c.equals(RatNum.ZERO)) {
      // If coefficient is zero, must set exponent to zero.
      coeff = RatNum.ZERO;
      expt = 0;
    } else {
      coeff = c;
      expt = e;
    }
    checkRep();
  }

  /**
   * Gets the coefficient of this RatTerm.
   *
   * @return the coefficient of this RatTerm.
   */
  @Pure
  public RatNum getCoeff() {
    return this.coeff;
  }

  /**
   * Gets the exponent of this RatTerm.
   *
   * @return the exponent of this RatTerm.
   */
  @Pure
  public int getExpt() {
    return this.expt;
  }

  /**
   * Returns true if this RatTerm is not-a-number.
   *
   * @return true if and only if this has NaN as a coefficient.
   */
  @Pure
  public boolean isNaN() {
    return this.coeff.isNaN();
  }

  /**
   * Returns true if this RatTerm is equal to 0.
   *
   * @return true if and only if this has zero as a coefficient.
   */
  @Pure
  public boolean isZero() {
    return this.coeff.equals(RatNum.ZERO);
  }

  /**
   * Returns the value of this RatTerm, evaluated at d.
   *
   * @param d The value at which to evaluate this term.
   * @return the value of this polynomial when evaluated at 'd'. For example, "3*x^2" evaluated at 2
   *     is 12. if (this.isNaN() == true), return Double.NaN
   */
  @Pure
  public double eval(double d) {
    if (this.isNaN()) {
      return Double.NaN;
    }
    return this.coeff.doubleValue() * Math.pow(d, this.expt);
    // Hint: You may find java.lang.Math's pow() method useful.
  }

  /**
   * Negation operation.
   *
   * @return a RatTerm equals to (-this). If this is NaN, then returns NaN.
   */
  @SideEffectFree
  public RatTerm negate() {
    if (this.isNaN()) {
      return RatTerm.NaN;
    }
    return new RatTerm(this.coeff.negate(), this.expt);
  }

  /**
   * Addition operation.
   *
   * @param arg The other value to be added.
   * @spec.requires arg != null
   * @return a RatTerm equals to (this + arg). If either argument is NaN, then returns NaN.
   * @throws IllegalArgumentException if (this.expt != arg.expt) and neither argument is zero or
   *     NaN.
   */
  @SideEffectFree
  public RatTerm add(RatTerm arg) {
    if (arg.isNaN() || this.isNaN()) {
      return RatTerm.NaN;
    }
    if (this.isZero()) {
      return arg;
    }
    if (arg.isZero()) {
      return this;
    }
    if (this.expt != arg.expt) {
      throw new IllegalArgumentException();
    }
    return new RatTerm(this.coeff.add(arg.coeff), this.expt);
  }

  /**
   * Subtraction operation.
   *
   * @param arg The value to be subtracted.
   * @spec.requires arg != null
   * @return a RatTerm equals to (this - arg). If either argument is NaN, then returns NaN.
   * @throws IllegalArgumentException if (this.expt != arg.expt) and neither argument is zero or
   *     NaN.
   */
  @SideEffectFree
  public RatTerm sub(RatTerm arg) {
    return this.add(arg.negate());
  }

  /**
   * Multiplication operation.
   *
   * @param arg The other value to be multiplied.
   * @spec.requires arg != null
   * @return a RatTerm equals to (this * arg). If either argument is NaN, then returns NaN.
   */
  @SideEffectFree
  public RatTerm mul(RatTerm arg) {
    if (this.isNaN() || arg.isNaN()) {
      return RatTerm.NaN;
    }
    return new RatTerm(this.coeff.mul(arg.coeff), this.expt + arg.expt);
  }

  /**
   * Division operation.
   *
   * @param arg The divisor.
   * @spec.requires arg != null
   * @return a RatTerm equals to (this / arg). If arg is zero, or if either argument is NaN, then
   *     returns NaN.
   */
  @SideEffectFree
  public RatTerm div(RatTerm arg) {
    if (arg.isZero() || this.isNaN() || arg.isNaN()) {
      return RatTerm.NaN;
    }
    return new RatTerm(this.coeff.div(arg.coeff), this.expt - arg.expt);
  }

  /**
   * Return the derivative of this RatTerm.
   *
   * @return a RatTerm that, q, such that q = dy/dx, where this == y. In other words, q is the
   *     derivative of this. If this.isNaN(), then return some q such that q.isNaN()
   *     <p>Given a term, a*x^b, the derivative of the term is: (a*b)*x^(b-1) for {@code b > 0} and
   *     0 for b == 0. (Do not worry about the case when {@code b < 0}. The caller of this function,
   *     RatPoly, contains a rep. invariant stating that b is never less than 0.)
   */
  @SideEffectFree
  public RatTerm differentiate() {
    if (this.isNaN()) {
      return RatTerm.NaN;
    }
    return new RatTerm(this.coeff.mul(new RatNum(this.expt)), this.expt - 1);
  }

  /**
   * Returns the antiderivative of this RatTerm.
   *
   * @return a RatTerm, q, such that dq/dx = this where the constant of intergration is assumed to
   *     be 0. In other words, q is the antiderivative of this. If this.isNaN(), then return some q
   *     such that q.isNaN()
   *     <p>Given a term, a*x^b, (where {@code b >= 0}) the antiderivative of the term is:
   *     a/(b+1)*x^(b+1) (Do not worry about the case when {@code b < 0}. The caller of this
   *     function, RatPoly, contains a rep. invariant stating that b is never less than 0.)
   */
  @SideEffectFree
  public RatTerm antiDifferentiate() {
    if (this.isNaN()) {
      return RatTerm.NaN;
    }
    return new RatTerm(this.coeff.div(new RatNum(this.expt + 1)), this.expt + 1);
  }

  /**
   * Returns a string representation of this RatTerm.
   *
   * @return A String representation of the expression represented by this.
   *     <p>There is no whitespace in the returned string.
   *     <p>If the term is itself zero, the returned string will just be "0".
   *     <p>If this.isNaN(), then the returned string will be just "NaN"
   *     <p>The string for a non-zero, non-NaN RatTerm is in the form "C*x^E" where C is a valid
   *     string representation of a RatNum (see {@link hw2.RatNum}'s toString method) and E is an
   *     integer. UNLESS: (1) the exponent E is zero, in which case T takes the form "C" (2) the
   *     exponent E is one, in which case T takes the form "C*x" (3) the coefficient C is one, in
   *     which case T takes the form "x^E" or "x" (if E is one) or "1" (if E is zero).
   *     <p>Valid example outputs include "3/2*x^2", "-1/2", "0", and "NaN".
   */
  @Override
  @Pure
  public String toString() {
    if (this.isNaN()) {
      return "NaN";
    }
    StringBuilder output = new StringBuilder();
    RatNum c = coeff;
    int e = expt;
    if (c.isNegative()) {
      output.append("-");
      c = c.negate();
    }
    if (c.equals(ONE) && (e == 1)) {
      output.append("x");
    } else if (e == 0) {
      output.append(c.toString());
    } else if (c.equals(ONE)) {
      output.append("x^" + e);
    } else if (e == 1) {
      output.append(c.toString() + "*x");
    } else {
      output.append(c.toString() + "*x^" + e);
    }
    return output.toString();
  }

  /**
   * Builds a new RatTerm, given a descriptive String.
   *
   * @param termStr A string of the format described in the @spec.requires clause.
   * @spec.requires 'termStr' is an instance of a string with no spaces that expresses a RatTerm in
   *     the form defined in the toString() method.
   *     <p>Valid inputs include "0", "x", and "-5/3*x^3", and "NaN".
   * @return a RatTerm t such that t.toString() = termStr
   */
  @SideEffectFree
  public static RatTerm valueOf(String termStr) {

    if (termStr.equals("NaN")) {
      return NaN;
    }

    // Term is: "R" or "R*x" or "R*x^N" or "x^N" or "x",
    // where R is a rational num and N is an integer.

    // First we parse the coefficient
    int multIndex = termStr.indexOf("*");
    RatNum coeff = null;
    if (multIndex == -1) {
      // "R" or "x^N" or "x"
      int xIndex = termStr.indexOf("x");
      if (xIndex == -1) {
        // "R"
        coeff = RatNum.valueOf(termStr);
      } else {
        int negIndex = termStr.indexOf("-");
        // "x^N" or "x" ==> coeff = 1
        if (negIndex == -1) {
          coeff = new RatNum(1);
        }
        // "-x^N" or "-x" ==> coeff = -1
        else if (negIndex == 0) {
          coeff = new RatNum(-1);
        } else {
          throw new RuntimeException(
              "Minus sign, '-', not allowed in the middle of input string: " + termStr);
        }
      }
    } else {
      // "R*x" or "R*x^N"
      coeff = RatNum.valueOf(termStr.substring(0, multIndex));
    }

    // Second we parse the exponent
    int powIndex = termStr.indexOf("^");
    int expt;
    if (powIndex == -1) {
      // "R" or "R*x" or "x"
      int xIndex = termStr.indexOf("x");
      if (xIndex == -1) {
        // "R"
        expt = 0;
      } else {
        // "R*x" or "x"
        expt = 1;
      }
    } else {
      // "R*x^N" or "x^N"
      expt = Integer.parseInt(termStr.substring(powIndex + 1));
    }
    return new RatTerm(coeff, expt);
  }

  /**
   * Standard hashCode function.
   *
   * @return an int that all objects equal to this will also.
   */
  @Override
  @Pure
  public int hashCode() {
    if (this.isNaN()) {
      return 0;
    }
    return (coeff.hashCode() * 7) + (expt * 43);
  }

  /**
   * Standard equality operation.
   *
   * @param obj The object to be compared for equality.
   * @return true iff 'obj' is an instance of a RatTerm and 'this' and 'obj' represent the same
   *     RatTerm. Note that all NaN RatTerms are equal.
   */
  @Override
  @Pure
  public boolean equals(@Nullable Object obj) {
    if (obj instanceof RatTerm) {
      RatTerm rt = (RatTerm) obj;
      if (this.isNaN() && rt.isNaN()) {
        return true;
      } else {
        return (this.expt == rt.expt) && this.coeff.equals(rt.coeff);
      }
    } else {
      return false;
    }
  }

  /** Checks that the representation invariant holds (if any). */
  private void checkRep(@UnknownInitialization(RatTerm.class) RatTerm this) {
    assert (coeff != null) : "coeff == null";
    assert (!coeff.equals(RatNum.ZERO) || expt == 0) : "coeff is zero while expt == " + expt;
  }
}
