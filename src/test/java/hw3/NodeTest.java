package hw3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class NodeTest {
    public static final Node emptyNode = create("");
    public static final Node nonEmptyNode = create("Content");
    public static final Node spaceNode = create(" ");

    @Before
    public void initialize() {

    }
    public static Node create(String content) {
        return new Node();
    }

    @Test
    public void testConstructor() {
        create("");
        create("Content");
        create(" ");
    }

    /**
     * Test Nodes with same content should have the same hashCode.
     */
    @Test
    public void testHashCode() {
        assertSame(emptyNode.hashCode(), create("").hashCode());
        assertSame(nonEmptyNode.hashCode(), create("Content").hashCode());
        assertSame(spaceNode.hashCode(), create(" ").hashCode());

        assertNotSame(emptyNode, spaceNode);
        assertNotSame(emptyNode, nonEmptyNode);
        assertNotSame(nonEmptyNode, spaceNode);
    }
    /**
     * Test Nodes with same content should be equal.
     */
    @Test
    public void testEquals() {
        assertEquals(emptyNode.hashCode(), create("").hashCode());
        assertEquals(nonEmptyNode.hashCode(), create("Content").hashCode());
        assertEquals(spaceNode.hashCode(), create(" ").hashCode());

        assertNotEquals(emptyNode, spaceNode);
        assertNotEquals(emptyNode, nonEmptyNode);
        assertNotEquals(nonEmptyNode, spaceNode);
    }

}
