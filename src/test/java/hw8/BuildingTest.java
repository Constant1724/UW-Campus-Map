package hw8;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTest {

    private Building makeBuilding() {
        return new Building(new Coordinate(0.0, 0.0), "ah", "AHH");
    }

    private Building makeAnotherBuilding() {
        return new Building(new Coordinate(0.0, 0.0), "eihei", "EIHEIHEI");
    }

    /**
     * Test hashcode method is implemented properly.
     */
    @Test
    public void testHashCode() {
        assertEquals(makeBuilding().hashCode(), makeBuilding().hashCode());
        assertNotEquals(makeBuilding().hashCode(), makeAnotherBuilding().hashCode());
    }

    /**
     * Test equals method is implemented properly.
     */
    @Test
    public void testEquals() {
        // Two same buildings should be equal
        assertEquals(makeBuilding(), makeBuilding());

        // Two different buildings should not be equal
        assertNotEquals(makeBuilding(), makeAnotherBuilding());

        // null should not be equal to any instance.
        assertFalse(makeBuilding().equals(null));
        assertFalse(makeAnotherBuilding().equals(null));
    }

    /**
     * Test getLocation method is implemented properly.
     */
    @Test
    public void testGetLocation() {
        // They should have same coordinates
        assertEquals(makeBuilding().getLocation(), new Coordinate(0.0, 0.0));
        assertEquals(makeAnotherBuilding().getLocation(), new Coordinate(0.0, 0.0));

        // They should have different coordinates
        assertNotEquals(makeBuilding().getLocation(), new Coordinate(0.1, 0.0));
        assertNotEquals(makeAnotherBuilding().getLocation(), new Coordinate(0.0, 1.0));

    }

    /**
     * Test getShortName method is implemented properly.
     */
    @Test
    public void testGetShortName() {
        assertEquals(makeBuilding().getShortName(), "ah");
        assertEquals(makeAnotherBuilding().getShortName(), "eihei");
    }

    /**
     * Test getLongName method is implemented properly.
     */
    @Test
    public void testGetLongName() {
        assertEquals(makeBuilding().getLongName(), "AHH");
        assertEquals(makeAnotherBuilding().getLongName(), "EIHEIHEI");
    }

}
