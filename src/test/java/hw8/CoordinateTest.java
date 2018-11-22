package hw8;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoordinateTest {

    /**
     * create an instance of Coordinate some with hard-coded value.
     * @return an instance of Coordinate some with hard-coded value.
     */
    protected static Coordinate makeCoordinate() {
        return new Coordinate(1.0, 1.0);
    }

    /**
     * create an instance of Coordinate some with hard-coded value.
     * @return an instance of Coordinate some with hard-coded value.
     */
    protected static Coordinate makeAnotherCoordinate() {
        return new Coordinate(1.67, 39.4);
    }

    public static double epsilon = 0.0001;

    /**
     * Test hashCode method is implemented properly.
     */
    @Test
    public void testHashCode() {
        // Same Coordinate should have same hashCode.
        assertEquals(makeCoordinate().hashCode(), makeCoordinate().hashCode());
        assertEquals(makeAnotherCoordinate().hashCode(), makeAnotherCoordinate().hashCode());

        // Different Coordinate should have different hashCode.
        assertNotEquals(makeCoordinate().hashCode(), makeAnotherCoordinate().hashCode());
    }
    /**
     * Test equals method is implemented properly.
     */
    @Test
    public void testEquals() {
        // Same Coordinate should be equal
        assertEquals(makeCoordinate(), makeCoordinate());
        assertEquals(makeAnotherCoordinate(), makeAnotherCoordinate());

        // Different Coordinate should not be equal.
        assertNotEquals(makeAnotherCoordinate(), makeCoordinate());
    }
    /**
     * Test getX method is implemented properly.
     */
    @Test
    public void testGetX() {
        assertEquals(null, makeCoordinate().getX(), 1.0, epsilon);
        assertEquals(null, makeAnotherCoordinate().getX(), 1.67, epsilon);

        assertNotEquals(null, makeCoordinate().getX(),1.1, epsilon);
        assertNotEquals(null, makeAnotherCoordinate().getX(), 1.68, epsilon);
    }

    /**
     * Test getY method is implemented properly.
     */
    @Test
    public void testGetY() {
        assertEquals(null, makeCoordinate().getY(), 1.0, epsilon);
        assertEquals(null, makeAnotherCoordinate().getY(), 39.4, epsilon);

        assertNotEquals(null, makeCoordinate().getY(),1.1, epsilon);
        assertNotEquals(null, makeAnotherCoordinate().getY(), 39.5, epsilon);
    }
}
