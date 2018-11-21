package hw8;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class Building {
    private final Coordinate location;

    private final String shortName;

    private final String longName;

    public Building(Coordinate location, String shortName, String longName) {
        this.location = location;
        this.shortName = shortName;
        this.longName = longName;
    }

    public Coordinate getLocation() {
        return location;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, shortName, longName);
    }
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
}
