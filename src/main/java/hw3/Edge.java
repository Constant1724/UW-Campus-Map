package hw3;

/**
 * Edge represents a immutable, directed, weighted edge in a graph
 *
 * <p>Specification fields:
 *
 * @spec.specfield start : Node // The start node of this edge.
 * @spec.specfield end : Node // The end node of this edge.
 * @spec.specfield label : String // The label of this edge.
 *     <p>Abstract Invariant: All edges should have a start and an end, with a cost. Equality means
 *     two Edges are equal iff they have the same start, end and cost.
 */
public class Edge {

  /**
   * It should take parameter to take the given start and end Node, together with the cost to create
   * a new Edge.
   *
   * @spec.requires Neither of the parameter should be null, and cost should be non-negative.
   * @spec.effects creates a new edge from start to end with cost.
   */
  public Edge() {
    throw new RuntimeException("Edge->constructor() is not yet implemented");
  }

  /**
   * get the start Node of this Edge
   *
   * @return the start Node of this Edge
   */
  public Node getStart() {
    throw new RuntimeException("Edge->getStart() is not yet implemented");
  }

  /**
   * get the end Node of this Edge
   *
   * @return the end Node of this Edge
   */
  public Node getEnd() {
    throw new RuntimeException("Edge->getEnd() is not yet implemented");
  }

  /**
   * get the label of this Edge
   *
   * @return the label of this Edge
   */
  public String getLabel() {
    throw new RuntimeException("Edge->getLabel() is not yet implemented");
  }

  /**
   * Standard hashCode function.
   *
   * @return an int that all objects equal to this will also return.
   */
  @Override
  public int hashCode() {
    throw new RuntimeException("Edge->hashCode() is not yet implemented");
  }

  /**
   * Standard equality operation.
   *
   * @param obj The object to be compared for equality.
   * @return true if and only if 'this' and 'obj' represent the same Edge.
   */
  @Override
  public boolean equals(Object obj) {
    throw new RuntimeException("Edge->equals() is not yet implemented");
  }
}
