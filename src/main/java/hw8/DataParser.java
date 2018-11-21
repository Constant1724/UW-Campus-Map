package hw8;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.checkerframework.checker.nullness.qual.*;
import org.checkerframework.dataflow.qual.Pure;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataParser {
    // This is NOT an ADT!!
    // This is NOT an ADT!!

    /**
     *  Reads Campus Path data set. Each line of the input file represents a path and is defined by:
     *      x1,y1   x2,y2   distance
     *
     *      x1,y1 is the start and x2,y2 is the end. distance is the distance of from x1,y1 to x2,y2.
     *      All separated by tab.
     *
     *      There should not be duplicate paths in data set.
     *
     * @spec.requires filename is a valid file path and result != null
     * @param filename the name of the file that will be read
     * @param result the list of CampusPath Objects, each one representing an edge defined by one line in the data set, respectively;
     *               typically empty when the routine is called.
     *
     * @spec.modifies result
     * @spec.effects fills result with a list of CampusPathForCsv Object.
     * @throws RuntimeException if there is an IOException while reading the filename.
     */
  public static void parsePathData(String filename, List<CampusPathForCsv> result) throws RuntimeException{

    try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
        CsvToBean<CampusPathForCsv>  csvToBean = new CsvToBeanBuilder<CampusPathForCsv>(reader)
              .withType(CampusPathForCsv.class)
              .withSeparator('\t')
              .withIgnoreLeadingWhiteSpace(true)
              .build();
      result.addAll(csvToBean.parse());
    } catch (IOException e) {
      System.err.println(e.toString());
      e.printStackTrace(System.err);
      throw new RuntimeException("Exception while trying to read: " + filename, e);
    }
  }

    /**
     *  Reads Campus BuildingForCsv data set. Each line of the input file represents a building and is defined by:
     *      shortName   longName    x   y
     *
     *      shorName and longName are abbreviated and full name for a building.
     *      x and y represents the location of that building.
     *
     *      If a building has multiple entrance, then its shortName and longName must be unique for each entrance.
     *      For example: CHL (NE) and CHL (SE) represents different entrances of the same building.
     *
     *  If there is an IOException while reading the file, none of the parameters will be changed.
     *
     * @spec.requires filename is a valid file path
     * @param filename the name of the file that will be read
     * @param longNameToShort map from longName to short Name for all buildings appear in the data set;
     *                        typically empty when the routine is called
     * @param longNameToLocation map from longName to coordinate representing that building
     *                           for all buildings appear in the data set; typically empty when the routine is called.
     *
     * @spec.modifies longNameToShort, longNameToLocation
     * @spec.effects fills longNameToShort with a map from each longName of a building to its corresponding shortName.
     * @spec.effects fills longNameToLocation with a map from each longName of a building to
     *                  an coordinate representing that building
     *
     */
  public static void parseBuildingData(
          String filename, Map<String, String> longNameToShort, Map<@KeyFor("#2") String, CoordinatesForCsv> longNameToLocation) {

      List<BuildingForCsv> result;

        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            CsvToBean<BuildingForCsv> csvToBean = new CsvToBeanBuilder<BuildingForCsv>(reader)
                    .withType(BuildingForCsv.class)
                    .withSeparator('\t')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            result = csvToBean.parse();
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            return;
        }

      for(BuildingForCsv building : result) {
            String short_name = building.getShort_name();
            String long_name = building.getLong_name();
            Double x = Double.parseDouble(building.getX());
            Double y = Double.parseDouble(building.getY());

            if (longNameToShort.containsKey(long_name)) {
                assert short_name.equals(longNameToShort.get(long_name));
            } else {
                longNameToShort.put(long_name, short_name);
            }

            if (!longNameToLocation.containsKey(long_name)) {
                longNameToLocation.put(long_name, new CoordinatesForCsv(x, y));
            }
        }
  }


    public static class BuildingForCsv {
        @CsvBindByName
        private String short_name;

        @CsvBindByName
        private String long_name;

        @CsvBindByName
        private String x;

        @CsvBindByName
        private String y;

        public BuildingForCsv() {
            this.short_name = "";
            this.long_name = "";
            this.x = "";
            this.y = "";
        }

        @Pure
        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        @Pure
        public String getLong_name() {
            return long_name;
        }
        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }

        @Pure
        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        @Pure
        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }


    public static class CoordinateConverterForCsv extends AbstractBeanField<String> {
        @Override
        protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
            try {
                // TODO check
                String[] parts = value.split(",", -1);
                return new CoordinatesForCsv(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
            } catch (RuntimeException e) {
                String message = "";
                if (e.getMessage() != null) {
                    message = e.getMessage();
                }
                throw new CsvDataTypeMismatchException(message);
            }
        }
    }

    public static class CoordinatesForCsv {

        private double x, y;

        public CoordinatesForCsv(double x, double y) {
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

    }

    public static class CampusPathForCsv {

        @CsvCustomBindByName(converter = CoordinateConverterForCsv.class)
        private CoordinatesForCsv origin;

        @CsvCustomBindByName(converter = CoordinateConverterForCsv.class)
        private CoordinatesForCsv destination;

        @CsvBindByName
        private double distance;

        public CampusPathForCsv() {
            this.origin = new CoordinatesForCsv(0, 0);
            this.destination = new CoordinatesForCsv(0, 0);
            this.distance = 0.0;
        }

        @Pure
        public CoordinatesForCsv getOrigin() {
            return origin;
        }

        public void setOrigin(CoordinatesForCsv origin) {
            this.origin = origin;
        }

        @Pure
        public CoordinatesForCsv getDestination() {
            return destination;
        }

        public void setDestination(CoordinatesForCsv destination) {
            this.destination = destination;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }

}
