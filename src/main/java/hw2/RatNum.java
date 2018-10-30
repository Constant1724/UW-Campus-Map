package hw2;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * <b>RatNum</b> represents an <b>immutable</b> rational number. It includes all of the elements in
 * the set of rationals, as well as the special "NaN" (not-a-number) element that results from
 * division by zero.
 *
 * <p>The "NaN" element is special in many ways. Any arithmetic operation (such as addition)
 * involving "NaN" will return "NaN". With respect to comparison operations, such as less-than,
 * "NaN" is considered equal to itself, and larger than all other rationals.
 *
 * <p>Examples of RatNums include "-1/13", "53/7", "4", "NaN", and "0".
 */

// ("immutable" is a common term for which "Effective Java" (p. 63)
// provides the following definition: "An immutatable class is simply
// a class whose instances cannot be modified.  All of the information
// contained in each instance is provided when it is created and is
// fixed for the lifetime of the object.")
public final class RatNum extends Number implements Comparable<RatNum> {

  /** Numerator of the rational number */
  private final int numer;

  /** Denominator of the rational number */
  private final int denom;

  // Abstraction Function:
  //   A RatNum r is NaN if r.denom = 0, (r.numer / r.denom) otherwise.
  // (An abstraction function explains what the state of the fields in a
  // RatNum represents.  In this case, a rational number can be
  // understood as the result of dividing two integers, or not-a-number
  // if we would be dividing by zero.)

  // Representation invariant for every RatNum r:
  //   (r.denom >= 0) &&
  //   (r.denom > 0 ==> there does not exist integer i > 1 such that
  //                    r.numer mod i = 0 and r.denom mod i = 0;)
  //   In other words,
  //     * r.denom is always non-negative.
  //     * r.numer/r.denom is in reduced form (assuming r.denom is not zero).
  // (A representation invariant tells us something that is true for all
  // instances of a RatNum)

  /** A constant holding a Not-a-Number (NaN) value of type RatNum */
  public static final RatNum NaN = new RatNum(1, 0);

  /** A constant holding a zero value of type RatNum */
  public static final RatNum ZERO = new RatNum(0);

  /**
   * @param n The value of the new RatNum.
   * @spec.effects Constructs a new RatNum = n.
   */
  public RatNum(int n) {
    numer = n;
    denom = 1;
    checkRep();
  }

  /**
   * @param n The numerator of the new RatNum.
   * @param d The denominator of the new RatNum.
   * @spec.effects If d = 0, constructs a new RatNum = NaN. Else constructs a new RatNum = (n / d).
   */
  public RatNum(int n, int d) {
    // special case for zero denominator; gcd(n,d) requires d != 0
    if (d == 0) {
      numer = n;
      denom = 0;

    } else {

      // reduce ratio to lowest terms
      int g = gcd(n, d);
      n = n / g;
      d = d / g;

      if (d < 0) {
        numer = -n;
        denom = -d;
      } else {
        numer = n;
        denom = d;
      }
    }
    checkRep();
  }

  /** Checks that the representation invariant holds (if any). */
  private void checkRep(@UnknownInitialization(RatNum.class) RatNum this) {
    assert (denom >= 0) : "Denominator of a RatNum cannot be less than zero";
    if (denom > 0) {
      int thisGcd = gcd(numer, denom);
      assert (thisGcd == 1 || thisGcd == -1) : "RatNum not in lowest form";
    }
  }

  /**
   * Returns true if this is NaN
   *
   * @return true iff this is NaN (not-a-number)
   */
  @Pure
  public boolean isNaN() {
    return (denom == 0);
  }

  /**
   * Returns true if this is negative.
   *
   * @return true iff {@code this < 0}
   */
  @Pure
  public boolean isNegative() {
    return (compareTo(ZERO) < 0);
  }

  /**
   * Returns true if this is positive.
   *
   * @return true iff {@code this > 0}
   */
  @Pure
  public boolean isPositive() {
    return (compareTo(ZERO) > 0);
  }

  /**
   * Compares two RatNums.
   *
   * @param rn the RatNum to be compared
   * @spec.requires rn != null
   * @return a negative number if {@code this < rn}, 0 if this = rn, a positive number if {@code
   *     this > rn}
   */
  @Override
  @Pure
  public int compareTo(RatNum rn) {
    if (this.isNaN() && rn.isNaN()) {
      return 0;
    } else if (this.isNaN()) {
      return 1;
    } else if (rn.isNaN()) {
      return -1;
    } else {
      RatNum diff = this.sub(rn);
      return diff.numer;
    }
  }

  /**
   * Approximates the value of this rational.
   *
   * @return a double approximation for this. Note that "NaN" is mapped to {@link Double#NaN}, and
   *     the {@link Double#NaN} value is treated in a special manner by several arithmetic
   *     operations, such as the comparison and equality operators. See the <a
   *     href="https://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.2.3">Java Language
   *     Specification, section 4.2.3</a>, for more details.
   */
  @Override
  @Pure
  public double doubleValue() {
    if (isNaN()) {
      return Double.NaN;
    } else {
      // convert int values to doubles before dividing.
      return ((double) numer) / ((double) denom);
    }
  }

  /**
   * Returns an integer approximation for this. The rational number is rounded to the nearest
   * integer.
   */
  @Override
  @Pure
  public int intValue() {
    // round to nearest by adding +/- .5 before truncating division.
    // we expect the implementation to use "round half away from zero".
    // for more info, see http://en.wikipedia.org/wiki/Rounding#Round_half_away_from_zero

    // note that even though the result is guaranteed to fit in an
    // int, we need to use longs for the computation.
    if (numer >= 0) {
      return (int) (((long) numer + (denom / 2)) / denom);
    } else {
      return (int) (((long) numer - (denom / 2)) / denom);
    }
  }

  /** Returns a float approximation for this. This method is specified by our superclass, Number. */
  @Override
  @Pure
  public float floatValue() {
    return (float) doubleValue();
  }

  /**
   * Returns a long approximation for this. This method is specified by our superclass, Number. The
   * value returned is rounded to the nearest long.
   */
  @Override
  @Pure

  public long longValue() {
    return intValue();
  }

  // in the implementation comments for the following methods, <this>
  // is notated as "a/b" and <arg> likewise as "x/y"

  /**
   * Returns the additive inverse of this RatNum.
   *
   * @return a Rational equal to (0 - this).
   */
  @SideEffectFree
  public RatNum negate() {
    return new RatNum(-this.numer, this.denom);
  }

  /**
   * Addition operation.
   *
   * @param arg The other value to be added.
   * @spec.requires arg != null
   * @return a RatNum equal to (this + arg). If either argument is NaN, then returns NaN.
   */
  @SideEffectFree
  public RatNum add(RatNum arg) {
    // a/b + x/y = ay/by + bx/by = (ay + bx)/by
    return new RatNum((this.numer * arg.denom) + (arg.numer * this.denom), this.denom * arg.denom);
  }

  /**
   * Subtraction operation.
   *
   * @param arg The value to be subtracted.
   * @spec.requires arg != null
   * @return a RatNum equal to (this - arg). If either argument is NaN, then returns NaN.
   */
  @SideEffectFree
  public RatNum sub(RatNum arg) {
    // a/b - x/y = a/b + -x/y
    return this.add(arg.negate());
  }

  /**
   * Multiplication operation.
   *
   * @param arg The other value to be multiplied.
   * @spec.requires arg != null
   * @return a RatNum equal to (this * arg). If either argument is NaN, then returns NaN.
   */
  @SideEffectFree
  public RatNum mul(RatNum arg) {
    // (a/b) * (x/y) = ax/by
    return new RatNum(this.numer * arg.numer, this.denom * arg.denom);
  }

  /**
   * Division operation.
   *
   * @param arg The divisor.
   * @spec.requires arg != null
   * @return a RatNum equal to (this / arg). If arg is zero, or if either argument is NaN, then
   *     returns NaN.
   */
  @SideEffectFree
  public RatNum div(RatNum arg) {
    // (a/b) / (x/y) = ay/bx
    if (arg.isNaN()) {
      return arg;
    } else {
      return new RatNum(this.numer * arg.denom, this.denom * arg.numer);
    }
  }

  /**
   * Returns the greatest common divisor of 'a' and 'b'.
   *
   * @param a, b The numbers for which to find the GCD.
   * @spec.requires b != 0
   * @return d such that a % d = 0 and b % d = 0
   */
  @Pure
  private static int gcd(int a, int b) {
    // Euclid's method
    if (b == 0) {
      return 0;
    }
    while (b != 0) {
      int tmp = b;
      b = a % b;
      a = tmp;
    }
    return a;
  }

  /**
   * Standard hashCode function.
   *
   * @return an int that all objects equal to this will also return.
   */
  @Override
  @Pure
  public int hashCode() {
    // all instances that are NaN must return the same hashcode;
    if (this.isNaN()) {
      return 0;
    }
    return (this.numer * 2) + (this.denom * 3);
  }

  /**
   * Standard equality operation.
   *
   * @param obj The object to be compared for equality.
   * @return true if and only if 'obj' is an instance of a RatNum and 'this' and 'obj' represent the
   *     same rational number. Note that NaN = NaN for RatNums.
   */
  @Override
  @Pure
  public boolean equals(@Nullable Object obj) {
    if (obj instanceof RatNum) {
      RatNum rn = (RatNum) obj;

      // special case: check if both are NaN
      if (this.isNaN() && rn.isNaN()) {
        return true;
      } else {
        return (this.numer == rn.numer) && (this.denom == rn.denom);
      }
    } else {
      return false;
    }
  }

  /**
   * @return a String representing this, in reduced terms. The returned string will either be "NaN",
   *     or it will take on either of the forms "N" or "N/M", where N and M are both integers in
   *     decimal notation and M != 0.
   */
  @Override
  @Pure
  public String toString() {
    // using '+' as String concatenation operator in this method
    if (isNaN()) {
      return "NaN";
    } else if (denom != 1) {
      return numer + "/" + denom;
    } else {
      return Integer.toString(numer);
    }
  }

  /**
   * Makes a RatNum from a string describing it.
   *
   * @param ratStr A string of the format described in the @spec.requires clause.
   * @spec.requires 'ratStr' is an instance of a string, with no spaces, of the form:
   *     <UL>
   *       <LI>"NaN"
   *       <LI>"N/M", where N and M are both integers in decimal notation, and M != 0, or
   *       <LI>"N", where N is an integer in decimal notation.
   *     </UL>
   *
   * @return NaN if ratStr = "NaN". Else returns a RatNum r = ( N / M ), letting M be 1 in the case
   *     where only "N" is passed in.
   */
  @SideEffectFree
  public static RatNum valueOf(String ratStr) {
    int slashLoc = ratStr.indexOf('/');
    if (ratStr.equals("NaN")) {
      return new RatNum(1, 0);
    } else if (slashLoc == -1) {
      // not NaN, and no slash, must be an Integer
      return new RatNum(Integer.parseInt(ratStr));
    } else {
      // slash, need to parse the two parts separately
      int n = Integer.parseInt(ratStr.substring(0, slashLoc));
      int d = Integer.parseInt(ratStr.substring(slashLoc + 1, ratStr.length()));
      return new RatNum(n, d);
    }
  }

  /**
   * Declare a serialization version number. This field is necessary because our parent class
   * (Number) implements Serializable; see the api docs for java.lang.Serializable for more details.
   */
  private static final long serialVersionUID = -8593953691277016262L;
}
