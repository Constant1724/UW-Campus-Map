package hw8;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

public class CampusPath {

    @CsvCustomBindByName(converter = CoordinateConverter.class)
    private Coordinates origin;

    @CsvCustomBindByName(converter = CoordinateConverter.class)
    private Coordinates destination;

    @CsvBindByName
    private double distance;

    public Coordinates getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinates origin) {
        this.origin = origin;
    }

    public Coordinates getDestination() {
        return destination;
    }

    public void setDestination(Coordinates destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
