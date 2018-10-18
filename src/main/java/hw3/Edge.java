package hw3;

/**
 * Edge represents a immutable, directed, weighted edge in a graph
 *
 * Specification fields:
 *  @spec.specified start   : Node   // The start node of this edge.
 *  @spec.specified end     : Node   // The end node of this edge.
 *  @spec.specified cost    : Number // The cost of this edge.
 *
 * Abstract Invariant:
 *  All edges should have a start and an end, with a cost.
 *  Equality means two Edges are equal iff they have the same start, end and cost.
 */
public class Edge {

    /**
     * It should take parameter to take the given start and end Node, together with the cost
     *  to create a new Edge.
     * @spec.requires Neither of the parameter should be null, and cost should be non-negative.
     * @spec.effecs creates a new edge from start to end with cost.
     */
    public Edge() {
        throw new RuntimeException("Edge->constructor() is not yet implemented");
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
