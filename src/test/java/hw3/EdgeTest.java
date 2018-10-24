package hw3;

import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import org.junit.Test;


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

  /** Test Edges with same content should have the same hashCode. */
  @Test
  public void testHashCode() {
    Edge aToB = create(A, B, LABEL);
    Edge bToA = create(B, A, LABEL);
    Edge aToA = create(A, A, LABEL);

    assertSame(aToB.hashCode(), create(A, B, LABEL).hashCode());
    assertSame(bToA.hashCode(), create(B, A, LABEL).hashCode());
    assertSame(aToA.hashCode(), create(A, A, LABEL).hashCode());

    assertNotSame(aToB.hashCode(), bToA.hashCode());
    assertNotSame(aToA.hashCode(), aToB.hashCode());
  }

  /** Test Edges with same content should be equal. */
  @Test
  public void testEquals() {
    Edge aToB = create(A, B, LABEL);
    Edge bToA = create(B, A, LABEL);
    Edge aToA = create(A, A, LABEL);

    assertEquals(aToB, create(A, B, LABEL).hashCode());
    assertEquals(bToA, create(B, A, LABEL).hashCode());
    assertEquals(aToA, create(A, A, LABEL).hashCode());

    assertNotEquals(aToB, bToA.hashCode());
    assertNotEquals(aToA, aToB.hashCode());
  }

  /** Test getStart is implemented properly */
  @Test
  public void testGetStart() {
    assertEquals(create(A, B, LABEL).getStart(), A);
    assertEquals(create(A, A, LABEL).getStart(), A);
    assertEquals(create(B, A, LABEL).getStart(), LABEL);
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
