package hw3;

/**
 * Node represent a immutable vertex in a graph.
 *
 * <p>It holds the description about this Node.
 *
 * <p>Specification fields:
 *
 * @spec.specfield content : words // The description about this Node.
 *     <p>Abstract Invariant: Any Node should have a unique, non Null description. Equality means
 *     two Nodes are equal iff they have the same content.
 */
public class Node {

  /**
   * It should have parameters to take the given description as its content.
   *
   * @spec.requires input description should not be null.
   * @spec.effects creates a new Node with the given description.
   */
  public Node() {
    throw new RuntimeException("Node->constructor() is not yet implemented");
  }

  /**
   * return the content of the Node
   *
   * @return content of the Node
   */
  public String getContent() {
    throw new RuntimeException("Node->getContent() is not yet implemented");
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
   * @return true if and only if 'this' and 'obj' represent the same Node.
   */
  @Override
  public boolean equals(Object obj) {
    throw new RuntimeException("Edge->equals() is not yet implemented");
  }
}
