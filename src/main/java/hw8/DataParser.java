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
    /** file name for campus path data set. */
    public static String campusPathFileName = "src/main/java/hw8/data/campus_paths.tsv";

    /** file name for campus building data set. */
    public static String campusBuildingFileName = "src/main/java/hw8/data/campus_buildings.tsv";

    private Set<CampusPath> paths;
    private Set<Building> buildings;

    public DataParser() {
        paths = new HashSet<>();
        buildings = new HashSet<>();
        this.run();
    }

    /**
     *
     * @return
     * @throws RuntimeException If there is an IOException while reading the file OR data file is
     *     malformed.
     */
    private void run() throws RuntimeException {
//        DataParser parser = new DataParser();

        try {
            this.loadPathData(campusPathFileName);
            this.loadBuildingData(campusBuildingFileName);
        } catch (MarvelParser.MalformedDataException e) {
            throw new RuntimeException("Malformed data", e);
        }

//        return parser;
    }

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
     * @spec.modifies this.paths
     * @spec.effects fill this.paths with start, end and cost of each CampusPath in the Campus path data set.
     * @throws MarvelParser.MalformedDataException if the file is not well-formed, see correct format
     *     above.
     * @throws RuntimeException If there is an IOException while reading the file.
     */
    private void loadPathData(String filename)
            throws MarvelParser.MalformedDataException, RuntimeException {
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(filename), Charset.defaultCharset())) {

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

                boolean result = this.paths.add(new CampusPath(originCoordinate, destinationCoordinate, distance));
                // Quick Sanity check to make sure there is no duplicate.
                assert result;

            }
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
     * @spec.effects this.buildings
     * @spec.modifies fill this.buildings with shortName, longName and location for each Building in
     *     the Campus Building dataset.
     * @throws MarvelParser.MalformedDataException if the file is not well-formed, see correct format
     *     above.
     * @throws RuntimeException If there is an IOException while reading the file.
     */
    private void loadBuildingData(String filename)
            throws MarvelParser.MalformedDataException, RuntimeException {
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(filename), Charset.defaultCharset())) {
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

                this.buildings.add(new Building(location, shortName, longName));

            }

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            throw new RuntimeException("Exception while trying to read: " + filename, e);
        }
    }


    public Set<CampusPath> getPaths() {
        return paths;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }
}
