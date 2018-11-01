package hw1;


import org.checkerframework.dataflow.qual.SideEffectFree;

import java.util.*;

/**
 * This is a container can be used to contain Balls. The key difference between a BallContainer and
 * a Box is that a Box has a finite volume. Once a box is full, a client cannot put in more Balls.
 */
public class Box implements Iterable<Ball> {

  /** ballContainer is used to internally store balls for this Box. */
  private BallContainer ballContainer;

  /**
   * maxVolume is The maxVolume of the Box. The sum of volumes of all balls should be less than this
   * value
   */
  private double maxVolume;

  /**
   * Constructor that creates a new box.
   *
   * @param maxVolume total volume of balls that this box can contain
   */
  public Box(double maxVolume) {
    // Your code goes here.  Remove the exception after you're done.
    ballContainer = new BallContainer();
    this.maxVolume = maxVolume;
  }

  /**
   * Implements the Iterable interface for this box.
   *
   * @return an Iterator over the Ball objects contained in this box
   */
  @Override
  public Iterator<Ball> iterator() {
    return ballContainer.iterator();
  }

  /**
   * Adds a Ball to this box.
   *
   * @param b Ball to be added
   * @spec.requires b != null
   * @return true if ball was successfully added to the box, i.e. ball was not already in the box
   *     and the box was not already full. Returns false, if ball was already in the box or if the
   *     box was too full to contain the new ball.
   */
  public boolean add(Ball b) {
    // Your code goes here.  Remove the exception after you're done.
    if (ballContainer.getVolume() + b.getVolume() <= maxVolume) {
      return ballContainer.add(b);
    } else {
      return false;
    }
  }

  /**
   * This method returns an iterator that yields all the balls in this box in order of ascending
   * size, i.e., the smallest Ball first, followed by Balls of increasing size.
   *
   * @return an iterator that yields all the balls in this box in order of ascending size
   */
  @SideEffectFree
  public Iterator<Ball> getBallsFromSmallest() {
    // Your code goes here.  Remove the exception after you're done.
    List<Ball> list = new ArrayList<Ball>(ballContainer.size());
    for (Ball ball : ballContainer) {
      list.add(ball);
    }
    list.sort(
        new Comparator<Ball>() {
          @Override
          public int compare(Ball b1, Ball b2) {
            if (b1.getVolume() - b2.getVolume() > 0) {
              return 1;
            } else if (b1.getVolume() - b2.getVolume() < 0) {
              return -1;
            } else {
              return 0;
            }
          }
        });
    return list.iterator();
  }

  /**
   * Removes a ball from the box. This method returns <tt>true</tt> if ball was successfully removed
   * from the container, i.e. ball was actually in the box. This method returns <tt>false</tt>
   * otherwise.
   *
   * @param b Ball to be removed
   * @spec.requires b != null
   * @return true if the ball was successfully removed from the box, i.e. ball was actually in the
   *     box. Returns false if the ball was not in the box.
   */
  public boolean remove(Ball b) {
    return ballContainer.remove(b);
  }

  /**
   * Each Ball has a volume. This method returns the total volume of all the Balls in the box.
   *
   * @return the volume of the contents of the box
   */
  @SideEffectFree
  public double getVolume() {
    return ballContainer.getVolume();
  }

  /**
   * Returns the number of Balls in this box.
   *
   * @return the number of Balls in this box
   */
  @SideEffectFree
  public int size() {
    return ballContainer.size();
  }

  /** Empties the box, i.e. removes all its contents. */
  public void clear() {
    ballContainer.clear();
  }

  /**
   * This method returns <tt>true</tt> if this box contains the specified Ball. It returns
   * <tt>false</tt> otherwise.
   *
   * @param b Ball to be checked for membership in this box
   * @spec.requires b != null
   * @return true if this box contains the specified Ball. Returns false otherwise.
   */
  @SideEffectFree
  public boolean contains(Ball b) {
    return ballContainer.contains(b);
  }
}
