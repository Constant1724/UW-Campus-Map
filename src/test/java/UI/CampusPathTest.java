package UI;

import static org.junit.Assert.*;

import org.junit.Test;

public class CampusPathTest {

  /**
   * create an instance of CampusPath with some hard-coded value.
   *
   * @return an instance of CampusPath with some hard-coded value.
   */
  protected static CampusPath makeCampusPath() {
    return new CampusPath(
        CoordinateTest.makeCoordinate(), CoordinateTest.makeAnotherCoordinate(), 1.5);
  }

  /**
   * create an instance of CampusPath with some hard-coded value.
   *
   * @return an instance of CampusPath with some hard-coded value.
   */
  protected static CampusPath makeAnotherCampusPath() {
    return new CampusPath(
        CoordinateTest.makeAnotherCoordinate(), CoordinateTest.makeCoordinate(), 1.8);
  }

  /** Test hashcode method is implemented properly. */
  @Test
  public void testHashCode() {
    assertEquals(makeCampusPath().hashCode(), makeCampusPath().hashCode());
    assertNotEquals(makeCampusPath().hashCode(), makeAnotherCampusPath().hashCode());
  }

  /** Test equals method is implemented properly. */
  @Test
  public void testEquals() {
    // Two same buildings should be equal
    assertEquals(makeCampusPath(), makeCampusPath());

    // Two different buildings should not be equal
    assertNotEquals(makeCampusPath(), makeAnotherCampusPath());

    // null should not be equal to any instance.
    assertFalse(makeCampusPath().equals(null));
    assertFalse(makeAnotherCampusPath().equals(null));
  }

  /** Test getOrigin method is implemented properly. */
  @Test
  public void testGetOrigin() {
    // It should have the same start Coordinate
    assertEquals(makeCampusPath().getOrigin(), CoordinateTest.makeCoordinate());

    assertEquals(makeAnotherCampusPath().getOrigin(), CoordinateTest.makeAnotherCoordinate());

    // It should have different start Coordinate
    assertNotEquals(makeCampusPath().getOrigin(), CoordinateTest.makeAnotherCoordinate());

    assertNotEquals(makeAnotherCampusPath(), CoordinateTest.makeCoordinate());
  }

  /** Test getDestination method is implemented properly. */
  @Test
  public void testGetDestination() {
    // It should have the same destination Coordinate
    assertEquals(makeCampusPath().getDestination(), CoordinateTest.makeAnotherCoordinate());
    assertEquals(makeAnotherCampusPath().getDestination(), CoordinateTest.makeCoordinate());

    // It should have different destination Coordinate
    assertNotEquals(makeCampusPath().getDestination(), CoordinateTest.makeCoordinate());
    assertNotEquals(
        makeAnotherCampusPath().getDestination(), CoordinateTest.makeAnotherCoordinate());
  }

  /** Test getCost method is implemented properly. */
  @Test
  public void testGetCost() {
    // It should have cost as expected.
    assertEquals(null, makeCampusPath().getCost(), 1.5, CoordinateTest.epsilon);
    assertEquals(null, makeAnotherCampusPath().getCost(), 1.8, CoordinateTest.epsilon);

    assertNotEquals(null, makeCampusPath().getCost(), 1.6, CoordinateTest.epsilon);
    assertNotEquals(null, makeAnotherCampusPath().getCost(), 1.79, CoordinateTest.epsilon);
  }
}
