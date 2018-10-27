package hw5;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

/** A simple mutable LinkedList with potential for nullness errors. */
public class NullFilledLinkedList {

  // Abstraction Function:
  // AF(this) = An ordered list where this.head is the first node in the 
  // list, tail is the last node in the list, and the nth index of this is 
  // stored in the value of this.head.next.next.next... 
  // (using .next n times).
  // Iff this list is empty, this.head == null.
  //
  // Representation Invariant:
  // true

  /** the first element of this list*/
  private @Nullable LinkedListNode head;
  /** the last element of this list*/
  private @Nullable LinkedListNode tail;

  /**
   * Constructs an empty Linked List
   *
   * @spec.modifies this
   * @spec.effects this_post = []
   */
  public NullFilledLinkedList() {
    head = null;
    tail = null;
    checkRep();
  }

  /**
   * @param node the node to add to this
   * @spec.requires node != null
   * @spec.modifies this
   * @spec.effects this_post = this_pre + [node]
   */
  public void add(LinkedListNode node) {
    checkRep();

    if (head == null) {
      head = node;
    } 
    if (tail == null) {
      tail = node;
    } else {
      tail.setNext(node);
    }
    checkRep();
  }

  /** Throws an exception if the representation invariant is violated. */
  private void checkRep(@UnknownInitialization (NullFilledLinkedList.class) NullFilledLinkedList this) {
  }
}
