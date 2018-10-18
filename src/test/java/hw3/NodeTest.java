package hw3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class NodeTest {

    @Before
    public void initialize() {

    }

    /**
     * Place holder for constructor.
     *
     * When the fields are decided at implementation time, and constructor parameter is set,
     * modify the following properly.
     *
     * @param content content of a Node.
     * @return a new Node with content as its description.
     */
    public static Node create(String content) {
        return new Node();
    }

    /**
     * try to create Node with empty, space and character content.
     */
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
        assertSame(create("").hashCode(), create("").hashCode());
        assertSame(create("Content").hashCode(), create("Content").hashCode());
        assertSame(create(" ").hashCode(), create(" ").hashCode());

        assertNotSame(create("").hashCode(), create(" ").hashCode());
        assertNotSame(create("").hashCode(), create("Content").hashCode());
        assertNotSame(create("Content").hashCode(), create(" ").hashCode());
    }
    
    /**
     * Test Nodes with same content should be equal.
     */
    @Test
    public void testEquals() {
        assertEquals(create(""), create(""));
        assertEquals(create("Content"), create("Content"));
        assertEquals(create(" ").hashCode(), create(" ").hashCode());

        assertNotEquals(create(""), create(" ").hashCode());
        assertNotEquals(create(""), create("Content"));
        assertNotEquals(create("Content"), create(" ").hashCode());
    }

}
