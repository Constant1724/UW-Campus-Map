package hw8;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;

/**
 * CampusPath is an immutable representation of a path in Campus.
 *
 * Specification fields:
 * @spec.specfield origin      : Coordinate  // start of a path in campus
 * @spec.specfield destination : Coordinate  // end of a path in campus
 */
public class CampusPath {
    // Abstraction Function:
    //      this.origin represents the start of the path.
    //      this.destination represents the end of the path.
    //      this.cost represents the cost of the path.

    // Representation Invariant:
    //      this.origin != null and thi.destination != null

    /**
     * start of a path in campus
     */
    private Coordinate origin;

    /**
     * end of a path in campus
     */
    private Coordinate destination;

    /**
     * cost of a path in campus.
     */
    private double cost;

    /**
     * creates an instance of CampusPath with origin, destination and cost.
     *
     * @param origin start of the path
     * @param destination end of the path
     * @param cost cost of the path
     *
     * @spec.effects creates an instance of CampusPath with origin, destination and cost.
     */
  public CampusPath(Coordinate origin, Coordinate destination, double cost) {
    this.origin = origin;
    this.destination = destination;
    this.cost = cost;
    checkRep();
  }

    /**
     * get the start of the path
     * @return the start of the path
     */
    public Coordinate getOrigin() {
        return origin;
    }

    /**
     * get the end of the path
     * @return the end of the path
     */
    public Coordinate getDestination() {
        return destination;
    }

    /**
     * get the cost of the path
     * @return the cost of the path
     */
    public double getCost() {
        return cost;
    }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep(@UnknownInitialization(CampusPath.class) CampusPath this) {
        assert this.origin!= null && this.destination != null;
    }
}




