package hw8;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class Coordinates {

    private double x, y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Coordinates other = (Coordinates) obj;
        return this.x == other.x && this.y == other.y;
    }
}
