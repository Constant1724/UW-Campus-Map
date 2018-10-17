package hw3;

import org.junit.Before;
import org.junit.Test;
import hw3.NodeTest;

import static org.junit.Assert.*;
//import static org.junit.Assert.assertNotEquals;

public class EdgeTest {

    public static final Edge empty = create();
    public static final Edge nonEmpty = create("Content");
    public static final Edge space = create(" ");

    @Before
    public void initialize() {

    }
    private Edge create(Node start, Node end, int cost) {

        return new Edge();
    }

    @Test
    public void testConstructor() {
        create("");
        create("Content");
        create(" ");
    }

    @Test
    public void testHashCode() {
//        assertSame(empty.hashCode(), create("").hashCode());
//        assertSame(nonEmpty.hashCode(), create("Content").hashCode());
//        assertSame(space.hashCode(), create(" ").hashCode());
//
//        assertNotSame(empty, space);
//        assertNotSame(empty, nonEmpty);
//        assertNotSame(nonEmpty, space);
    }

    @Test
    public void testEquals() {
//        assertEquals(empty.hashCode(), create("").hashCode());
//        assertEquals(nonEmpty.hashCode(), create("Content").hashCode());
//        assertEquals(space.hashCode(), create(" ").hashCode());
//
//        assertNotEquals(empty, space);
//        assertNotEquals(empty, nonEmpty);
//        assertNotEquals(nonEmpty, space);
    }
}
