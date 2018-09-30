package hw1;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is a container can be used to contain Balls. A given Ball may only appear in a BallContainer
 * once.
 */
public class BallContainer implements Iterable<Ball> {

  // Contents of the BallContainer.
    /**
     * @param contents the data structure that holds all balls.
     * @param sum_volume the sum of volumes of all balls currently in the BallContainer.
     * @param num_of_balls the number of balls currently in the BallContainer
     */
    private Set<Ball> contents;
    private double sum_volume;
    private int num_of_balls;
  /** Constructor that creates a new ballcontainer. */
  public BallContainer() {
    contents = new LinkedHashSet<Ball>();
    sum_volume = 0;
    num_of_balls = 0;
  }

  /**
   * Implements the Iterable interface for this container.
   *
   * @return an Iterator over the Ball objects contained in this container.
   */
  @Override
  public Iterator<Ball> iterator() {
    // If we just returned the iterator of "contents", a client could
    // call the remove() method on the iterator and modify it behind
    // our backs.  Instead, we wrap contents in an "unmodifiable set";
    // calling remove() on this iterator throws an exception.  This is
    // an example of avoiding "representation exposure."  You will
    // learn more about this concept later in the course.
    return Collections.unmodifiableSet(contents).iterator();
  }

  /**
   * Adds a ball to the container. This method returns <tt>true</tt> if ball was successfully added
   * to the container, i.e. ball is not already in the container. Of course, you are allowed to put
   * a Ball into a container only once. Hence, this method returns <tt>false</tt>, if ball is
   * already in the container.
   *
   * @param b Ball to be added
   * @spec.requires b != null
   * @return true if ball was successfully added to the container, i.e. ball was not already in the
   *     container. Returns false if ball was already in the container.
   */
  public boolean add(Ball b) {
    // Your code goes here.  Remove the exception after you're done.
      boolean flag = contents.add(b);
      if (flag) {
          sum_volume += b.getVolume();
          num_of_balls ++;
      }
    return flag;
  }

  /**
   * Removes a ball from the container. This method returns <tt>true</tt> if ball was successfully
   * removed from the container, i.e. ball was actually in the container. You cannot remove a Ball
   * if it is not already in the container, and so this method returns <tt>false</tt>, otherwise.
   *
   * @param b Ball to be removed
   * @spec.requires b != null
   * @return true if ball was successfully removed from the container, i.e. ball was in the
   *     container. Returns false if ball was not in the container.
   */
  public boolean remove(Ball b) {
    // Your code goes here.  Remove the exception after you're done.
      boolean flag = contents.remove(b);
      if (flag) {
          sum_volume -= b.getVolume();
          num_of_balls --;
      }
      return flag;
  }

  /**
   * Each Ball has a volume. This method returns the total volume of all the Balls in the container.
   *
   * @return the volume of the contents of the container
   */
  public double getVolume() {
    // Your code goes here.  Remove the exception after you're done.
      return sum_volume;
  }

  /**
   * Returns the number of Balls in this container.
   *
   * @return the number of Balls in this container
   */
  public int size() {
    // Your code goes here.  Remove the exception after you're done.
      return num_of_balls;
  }

  /** Empties the container, i.e. removes all its contents. */
  public void clear() {
    // Your code goes here.  Remove the exception after you're done.
      contents.clear();
      num_of_balls = 0;
      sum_volume = 0;
  }

  /**
   * This method returns true if this container contains the specified Ball. It returns false
   * otherwise.
   *
   * @param b Ball to be checked for membership in this container
   * @spec.requires b != null
   * @return true if this container contains the specified Ball. Returns false otherwise.
   */
  public boolean contains(Ball b) {
    // Your code goes here.  Remove the exception after you're done.
      return contents.contains(b);
  }
}
