package hw3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class NodeTest {

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
        create("Content1");
        create(" ");
    }

    /**
     * Test Nodes with same content should have the same hashCode.
     */
    @Test
    public void testHashCode() {
        assertSame(create("").hashCode(), create("").hashCode());
        assertSame(create("Content1").hashCode(), create("Content1").hashCode());
        assertSame(create(" ").hashCode(), create(" ").hashCode());

        assertNotSame(create("").hashCode(), create(" ").hashCode());
        assertNotSame(create("").hashCode(), create("Content2").hashCode());
        assertNotSame(create("Content2").hashCode(), create(" ").hashCode());
    }
    
    /**
     * Test Nodes with same content should be equal.
     */
    @Test
    public void testEquals() {
        assertEquals(create(""), create(""));
        assertEquals(create("Content1"), create("Content1"));
        assertEquals(create(" ").hashCode(), create(" ").hashCode());

        assertNotEquals(create(""), create(" ").hashCode());
        assertNotEquals(create(""), create("Content2"));
        assertNotEquals(create("Content2"), create(" ").hashCode());
    }

    /**
     * Test get content method is implemented properly.
     */
    @Test
    public void testGetContent() {
        assertEquals(create("A").getContent(), "A");
        assertEquals(create(" ").getContent(), " ");
        assertEquals(create("1").getContent(), "1");
    }


}
