package hw3;

import org.junit.Test;

import static org.junit.Assert.*;

public class EdgeTest {
    private static final Node A = NodeTest.create("start");
    private static final Node B = NodeTest.create("end");
    private static final int ZERO_COST = 0;
    private static final int NON_ZERO_COST = 1;

    /**
     * Place holder for constructor.
     *
     * When the fields are decided at implementation time, and constructor parameter is set,
     * modify the following properly.
     *
     * @param start start of the Edge.
     * @param end   end of the Edge.
     * @param cost  cost of the Edge.
     * @return a new Node with content as its description.
     */
    public static Edge create(Node start, Node end, int cost) {

        return new Edge();
    }

    @Test
    public void testConstructor() {
        create(A, B, ZERO_COST);
        create(A, B, NON_ZERO_COST);
        create(A, A, ZERO_COST);
        create(A, A, NON_ZERO_COST);
    }
    /**
     * Test Edges with same content should have the same hashCode.
     */
    @Test
    public void testHashCode() {
        Edge aToB = create(A, B, ZERO_COST);
        Edge bToA = create(B, A, ZERO_COST);
        Edge aToA = create(A, A, ZERO_COST);
        
        assertSame(aToB.hashCode(), create(A, B, ZERO_COST).hashCode());
        assertSame(bToA.hashCode(), create(B, A, ZERO_COST).hashCode());
        assertSame(aToA.hashCode(), create(A, A, ZERO_COST).hashCode());
        
        assertNotSame(aToB.hashCode(), bToA.hashCode());
        assertNotSame(aToA.hashCode(), aToB.hashCode());
    }
    /**
     * Test Edges with same content should be equal.
     */
    @Test
    public void testEquals() {
        Edge aToB = create(A, B, ZERO_COST);
        Edge bToA = create(B, A, ZERO_COST);
        Edge aToA = create(A, A, ZERO_COST);

        assertEquals(aToB, create(A, B, ZERO_COST).hashCode());
        assertEquals(bToA, create(B, A, ZERO_COST).hashCode());
        assertEquals(aToA, create(A, A, ZERO_COST).hashCode());

        assertNotEquals(aToB, bToA.hashCode());
        assertNotEquals(aToA, aToB.hashCode());

    }
}
