package hw5;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

/** A LinkedListNode is a labelled node that can link to other LinkedListNodes. */
public class LinkedListNode {

  // Abstraction Function:
  // AF(this) = A node with a label defined as this.label and the
  //            option to chain multiple nodes together. If there is a
  //            node that occurs after this in a chain, it will be stored 
  //            in this.next.
  //
  // Representation Invariant:
  //   this.label != null

  /** the label of this node */
  private String label;
  /** the next node in the list */
  private @Nullable LinkedListNode next;

  /**
   * @param label the label of this node
   *
   * @spec.requires label != null
   * @spec.modifies this
   * @spec.effects constructs the node with the given label
   */
  public LinkedListNode(String label) {
    this.label = label;
    this.next = null;
    checkRep();
  }

  /**
   * @param label the label of this node
   * @param node the node following this
   * @spec.requires label != null &amp;&amp; node != null
   * @spec.modifies this
   * @spec.effects constructs the node with the given label and given
   *               next node
   */
  public LinkedListNode(String label, LinkedListNode node) {
    this.label = label;
    this.next = node;
    checkRep();
  }

  /**
   * @param node the node that will be the new node after this
   * @spec.modifies this
   * @spec.effects sets the node following this to the given node
   */
  public void setNext(@Nullable LinkedListNode node) {
    checkRep();

    this.next = node;
    checkRep();
  }

  /** @return the node following this */
  public @Nullable LinkedListNode getNext() {
    checkRep();
    return this.next;
  }

  /** @return the label of this */
  public String getLabel() {
    checkRep();
    return this.label;
  }

  /**
   * @param label the new label of this node
   * @spec.requires label != null
   * @spec.modifies this
   * @spec.effects sets the label of this node
   * @throws IllegalArgumentException iff label == null
   */
  public void setLabel(String label) {
    checkRep();
    if (label == null) {
      throw new IllegalArgumentException();
    }

    this.label = label;
    checkRep();
  }

  /** @return true iff there is a node following this */
  public boolean hasNext() {
    checkRep();
    return this.next != null;
  }

  /** Throws an exception if the representation invariant is violated. */
  private void checkRep(@UnknownInitialization(LinkedListNode.class) LinkedListNode this) {
    assert this.label != null : "This node has a null label";
  }
}
