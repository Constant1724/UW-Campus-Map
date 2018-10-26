package hw3;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class EdgeTest {
  @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

  private static final Node A = NodeTest.create("start");
  private static final Node B = NodeTest.create("end");
  private static final String LABEL = "0";
  private static final String LABEL1 = "";

  /**
   * Place holder for constructor.
   *
   * <p>When the fields are decided at implementation time, and constructor parameter is set, modify
   * the following properly.
   *
   * @param start start of the Edge.
   * @param end end of the Edge.
   * @param label cost of the Edge.
   * @return a new Node with content as its description.
   */
  public static Edge create(Node start, Node end, String label) {

    return new Edge(start, end, label);
  }

  @Test
  public void testConstructor() {
    create(A, B, LABEL);
    create(A, B, LABEL1);
    create(A, A, LABEL);
    create(A, A, LABEL1);
  }

  /** Test Edges with Equals content should have the Equals hashCode. */
  @Test
  public void testHashCode() {
    Edge aToB = create(A, B, LABEL);
    Edge bToA = create(B, A, LABEL);
    Edge aToA = create(A, A, LABEL);

    assertEquals(aToB.hashCode(), create(A, B, LABEL).hashCode());
    assertEquals(bToA.hashCode(), create(B, A, LABEL).hashCode());
    assertEquals(aToA.hashCode(), create(A, A, LABEL).hashCode());

    assertNotEquals(aToB.hashCode(), bToA.hashCode());
    assertNotEquals(aToA.hashCode(), aToB.hashCode());
  }

  /** Test Edges with Equals content should be equal. */
  @Test
  public void testEquals() {
    Edge aToB = create(A, B, LABEL);
    Edge bToA = create(B, A, LABEL);
    Edge aToA = create(A, A, LABEL);

    assertEquals(aToB, create(A, B, LABEL));
    assertEquals(bToA, create(B, A, LABEL));
    assertEquals(aToA, create(A, A, LABEL));

    assertNotEquals(aToB, bToA.hashCode());
    assertNotEquals(aToA, aToB.hashCode());
  }

  /** Test getStart is implemented properly */
  @Test
  public void testGetStart() {
    assertEquals(create(A, B, LABEL).getStart(), A);
    assertEquals(create(A, A, LABEL).getStart(), A);
    assertEquals(create(B, A, LABEL).getStart(), B);
  }

  /** Test getStart is implemented properly */
  @Test
  public void testGetEnd() {
    assertEquals(create(A, B, LABEL).getEnd(), B);
    assertEquals(create(A, A, LABEL).getEnd(), A);
    assertEquals(create(B, A, LABEL).getEnd(), A);
  }

  /** Test getStart is implemented properly */
  @Test
  public void testGetLabel() {
    assertEquals(create(A, B, LABEL).getLabel(), LABEL);
    assertEquals(create(A, A, LABEL1).getLabel(), LABEL1);
    assertEquals(create(B, A, LABEL).getLabel(), LABEL);
  }
}
