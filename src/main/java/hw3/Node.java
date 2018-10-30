package hw3;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * Node represent a immutable vertex in a graph.
 *
 * <p>It holds the description about this Node.
 *
 * <p>Specification fields:
 *
 * @spec.specfield content : String // The description about this Node.
 *     <p>Abstract Invariant: Any Node should have a unique, non Null description. Equality means
 *     two Nodes are equal iff they have the same content.
 */
public class Node {
  /** the description(content) of this node */
  private String content;

  // Abstraction Function:
  // content is the description of this Node. It is also a unique identifier of the Node.

  // Representation Invariant:
  //      content != Null (Node should always have valid content)

  /**
   * @param content the content of the Node.
   * @spec.requires content != Null
   * @spec.effects creates a new Node with the given content as description.
   */
  public Node(String content) {
    this.content = content;
    checkRep();
  }

  /** Checks that the representation invariant holds (if any). */
  private void checkRep(@UnknownInitialization(Node.class) Node this) {
    assert (content != null);
  }

  /**
   * return the content of the Node
   *
   * @return content of the Node
   */
  @SideEffectFree
  public String getContent() {
    return this.content;
  }

  /**
   * Standard hashCode function.
   *
   * @return an int that all objects equal to this will also return.
   */
  @Override
  @SideEffectFree
  public int hashCode() {
    return this.content.hashCode();
  }

  /**
   * Standard equality operation.
   *
   * @param obj The object to be compared for equality.
   * @return true if and only if 'this' and 'obj' represent the same Node.
   */
  @Override
  @SideEffectFree
  public boolean equals(Object obj) {
    if (!(obj instanceof Node)) {
      return false;
    }

    return this.content.equals(((Node) obj).content);
  }
}
