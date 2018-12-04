package hw8;

import hw6.MarvelParser;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Repository
public class DataParser {
    /**
     * Reads Campus Path data set from disk. Each line of the input file represents a path and is defined by:
     * x1,y1 x2,y2 distance
     *
     * <p>x1,y1 is the start and x2,y2 is the end. distance is the distance of from x1,y1 to x2,y2.
     * All separated by tab.
     *
     * <p>There should not be duplicate paths in data set.
     *
     * @spec.requires filename is a valid file path
     * @param filename the name of the file that will be read
     * @return a set of all paths in campus, each with a start, an end and a distance
     * @throws MarvelParser.MalformedDataException if the file is not well-formed, see correct format
     *     above.
     * @throws RuntimeException If there is an IOException while reading the file.
     */
    public static Set<CampusPath> loadPathData(String filename)
            throws MarvelParser.MalformedDataException, RuntimeException {
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(filename), Charset.defaultCharset())) {
            Set<CampusPath> paths = new HashSet<>();

            String inputLine;

            // Skip the header.
            reader.readLine();

            while ((inputLine = reader.readLine()) != null) {

                // Parse the data, and throw an exception for malformed lines.
                String[] tokens = inputLine.split("\t", -1);
                if (tokens.length != 3) {
                    throw new MarvelParser.MalformedDataException(
                            "Line should contain exactly 2 tab: " + inputLine);
                }

                // Split x,y into [x, y] for both origin and destination.
                String[] origin = tokens[0].split(",", -1);
                String[] destination = tokens[1].split(",", -1);
                Double distance = Double.parseDouble(tokens[2]);

                Coordinate originCoordinate =
                        new Coordinate(Double.parseDouble(origin[0]), Double.parseDouble(origin[1]));
                Coordinate destinationCoordinate =
                        new Coordinate(Double.parseDouble(destination[0]), Double.parseDouble(destination[1]));

                boolean result = paths.add(new CampusPath(originCoordinate, destinationCoordinate, distance));
                // Quick Sanity check to make sure there is no duplicate.
                assert result;

            }

            return paths;
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            throw new RuntimeException("Exception while trying to read: " + filename, e);
        }
    }

    /**
     * Reads Campus Building data set from disk. Each line of the input file represents a building and is
     * defined by: shortName longName x y
     *
     * <p>shorName and longName are abbreviated and full name for a building. x and y represents the
     * location of that building.
     *
     * <p>If a building has multiple entrance, then its shortName and longName must be unique for each
     * entrance. For example: CHL (NE) and CHL (SE) represents different entrances of the same
     * building.
     *
     * <p>If there is an IOException while reading the file, a RuntimeException will be throwed If any
     * line of data is malformed
     *
     * @spec.requires filename is a valid file path
     * @param filename the name of the file that will be read
     * @return a set of buildings in campus, each with a full name, abbreviated name and a cost.
     * @throws MarvelParser.MalformedDataException if the file is not well-formed, see correct format
     *     above.
     * @throws RuntimeException If there is an IOException while reading the file.
     */
    public static Set<Building> loadBuildingData(String filename)
            throws MarvelParser.MalformedDataException, RuntimeException {
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(filename), Charset.defaultCharset())) {

            Set<Building> buildings = new HashSet<>();
            String inputLine;

            // Skip the header.
            reader.readLine();

            while ((inputLine = reader.readLine()) != null) {

                // Parse the data, and throw an exception for malformed lines.
                String[] tokens = inputLine.split("\t", -1);
                if (tokens.length != 4) {
                    throw new MarvelParser.MalformedDataException(
                            "Line should contain exactly 3 tab: " + inputLine);
                }

                String shortName = tokens[0];
                String longName = tokens[1];
                Double x = Double.parseDouble(tokens[2]);
                Double y = Double.parseDouble(tokens[3]);
                Coordinate location = new Coordinate(x, y);

                buildings.add(new Building(location, shortName, longName));
            }

            return buildings;

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            throw new RuntimeException("Exception while trying to read: " + filename, e);
        }
    }

}
