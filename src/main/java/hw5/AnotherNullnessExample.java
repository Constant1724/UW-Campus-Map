package hw5;

/** A class that makes a linked list. */
public class AnotherNullnessExample {

  /** @param args the command line arguments of this program (ignored) */
  public static void main(String[] args) {
    // You can remove lines of code in this method if they violate
    //   @spec.requires
    NullFilledLinkedList list = new NullFilledLinkedList();

    LinkedListNode node1 = new LinkedListNode("Node 1");

    LinkedListNode node2 = new LinkedListNode("Node 2");
    node2.setLabel(null);

    LinkedListNode node3 = null;

    list.add(node1);
    list.add(node2);
    list.add(node3);
  }
}
