package hw8;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public  class Coordinate {
    private final double x, y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Coordinate(DataParser.CoordinatesForCsv parserCoordinate) {
        this.x = parserCoordinate.getX();
        this.y = parserCoordinate.getY();
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Coordinate)) {
            return false;
        }
        Coordinate other = (Coordinate) obj;
        return this.x == other.getX() && this.y == other.getY();
    }
}