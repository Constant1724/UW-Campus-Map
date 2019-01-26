package UI;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Coordinate is an immutable representation of a point in Campus.
 *
 * <p>Specification fields:
 *
 * @spec.specfield x : double // x coordinate of a point in campus
 * @spec.specfield y : double // y coordinate of a point in campus
 */
public class Coordinate {
  // Abstraction Function:
  //      this.x represents the x coordinate of a point in campus
  //      this.y represents the y coordinate of a point in campus

  // Representation Invariant:
  //      No, this does not have an representation invariant.
  /** x and y coordinate of a point in campus. */
  private final double x, y;

  /**
   * creates an instance of Coordinate with x and y.
   *
   * @param x x coordinate of the point
   * @param y y coordinate of the point
   * @spec.effects creates an instance of Coordinate with x and y.
   */
  public Coordinate(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * get the x coordinate of the point
   *
   * @return the x coordinate of the point
   */
  public double getX() {
    return x;
  }

  /**
   * get the y coordinate of the point
   *
   * @return the y coordinate of the point
   */
  public double getY() {
    return y;
  }

  /**
   * Standard hashCode function.
   *
   * @return an int that all objects equal to this will also return.
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   * Standard equality operation.
   *
   * @param obj The object to be compared for equality.
   * @return true if and only if 'this' and 'obj' represent the same Edge.
   */
  @Override
  public boolean equals(@Nullable Object obj) {
    if (!(obj instanceof Coordinate)) {
      return false;
    }
    Coordinate other = (Coordinate) obj;
    return this.x == other.getX() && this.y == other.getY();
  }
}
