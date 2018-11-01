package hw3;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.*;
import org.checkerframework.dataflow.qual.SideEffectFree;

import java.util.Objects;

/**
 * Edge represents an immutable, directed, weighted edge in a graph
 *
 * <p>Specification fields:
 *
 * @spec.specfield start : Node // The start node of this edge.
 * @spec.specfield end : Node // The end node of this edge.
 * @spec.specfield label : String // The label of this edge.
 *     <p>Abstract Invariant: All edges should have a start and an end, with a label. Equality means
 *     two Edges are equal iff they have the same start, end and cost.
 */
public class Edge {
  /** the start of this edge */
  private Node start;
  /** the end of this edge */
  private Node end;
  /** the label of this edge */
  private String label;

  // Abstraction Function:
  //  An edge is defined with this.start  as its starting Node
  //                      and this.end    as its ending Node
  //                      and this.label  as its label.

  // Representation Invariant:
  //      start != null && end != null && label != null (All fields should be valid)

  /**
   * @param start the start of this Edge.
   * @param end the end of this Edge.
   * @param label the label of this Edge.
   * @spec.requires start != null and end != null and label != null
   * @spec.effects creates a new edge from start to end with label.
   */
  public Edge(Node start, Node end, String label) {
    this.start = start;
    this.end = end;
    this.label = label;
    checkRep();
  }

  /** Checks that the representation invariant holds (if any). */
  @SideEffectFree
  private void checkRep(@UnknownInitialization(Edge.class) Edge this) {
    assert (this.start != null && this.end != null && this.label != null);
  }

  /**
   * get the start Node of this Edge
   *
   * @return the start Node of this Edge
   */
  @SideEffectFree
  public @NonNull Node getStart() {
    return this.start;
  }

  /**
   * get the end Node of this Edge
   *
   * @return the end Node of this Edge
   */
  @SideEffectFree
  public Node getEnd() {
    return this.end;
  }

  /**
   * get the label of this Edge
   *
   * @return the label of this Edge
   */
  @SideEffectFree
  public String getLabel() {
    return this.label;
  }

  /**
   * Standard hashCode function.
   *
   * @return an int that all objects equal to this will also return.
   */
  @Override
  @SideEffectFree
  public int hashCode() {
    return Objects.hash(this.start, this.end, this.label);
  }

  /**
   * Standard equality operation.
   *
   * @param obj The object to be compared for equality.
   * @return true if and only if 'this' and 'obj' represent the same Edge.
   */
  @Override
  @SideEffectFree
  public boolean equals(@Nullable Object obj) {
    if (!(obj instanceof Edge)) {
      return false;
    }
    Edge other = (Edge) obj;

    return this.start.equals(other.getStart())
        && this.end.equals(other.getEnd())
        && this.label.equals(other.label);
  }
}
