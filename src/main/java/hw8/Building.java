package hw8;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Building is an immutable representation of a building in Campus.
 *
 * Specification fields:
 * @spec.specfield location  : Coordinate // location of a building.
 * @spec.specfield shortName : String   // Abbreviated name of a building
 * @spec.specfield longName  : String   // Full name of a building
 */
public class Building {
    // Abstraction Function:
    //      this.location represents the coordinate of this Building as a point in campus map.
    //      this.shortName represents the abbreviated name of this Building.
    //      this.longName represents the full name of this Building.

    // Representation Invariant:
    //      this.location != null and this.shortName != null and this.longName != null

    /**
     * Location of a building.
     */
    private final Coordinate location;

    /**
     * Abbreviated name of a building.
     */
    private final String shortName;

    /**
     * Full name of a building.
     */
    private final String longName;

    /**
     * creates an instance of building with location, abbreviated name and full name.
     *
     * @spec.requires location != null and shorName != null and longName != null
     * @param location location of a building.
     * @param shortName abbreviated name of a building
     * @param longName  full name of a building.
     *
     * @spec.effects creates an instance of building with location, abbreviated name and full name.
     */
    public Building(Coordinate location, String shortName, String longName) {
        this.location = location;
        this.shortName = shortName;
        this.longName = longName;
        checkRep();
    }

    /**
     * get the location of a building
     * @return the location of a building
     */
    public Coordinate getLocation() {
        return location;
    }

    /**
     * get the abbreviated name of a building
     * @return the abbreviated name of a building
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * get the full name of a building
     * @return the full name of a building
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        return Objects.hash(location, shortName, longName);
    }

    /**
     * Standard equality operation.
     *
     * @param obj The object to be compared for equality.
     * @return true if and only if 'this' and 'obj' represent the same Edge.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Building)) {
            return  false;
        }
        Building other = (Building) obj;
        return this.location.equals(other.location)
                && this.longName.equals(other.longName)
                && this.shortName.equals(other.shortName);
    }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep(@UnknownInitialization(Building.class) Building this) {
        assert this.location != null && this.longName != null && this.shortName != null;
    }
}
