package hw8;

import hw3.Graph;
import hw7.MarvelPaths2;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

/**
 * CampusMapModel represents a map of the campus, by loading data from data set.
 *
 * Specifically, it loads information about all paths and buildings.
 *      For any path, it loads the origin, destination and cost of that path.
 *      For any building, it loads both full name and abbreviated short name and a location representing its entrance.
 *          If a building has multiple entrance, then its shortName and longName must be unique for each entrance.
 *          For example: CHL (NE) and CHL (SE) represents different entrances of the same building.
 *
 *
 * Client can list all buildings in the campus and find a route from one building to another.
 *
 * Specification field:
 *
 * @spec.specfield Coordinate : represents the location of a point in the campus. // It could be origin, destination of an edge, or location of a building.
 * @spec.specfield Map : a map represents set of all edges in campus // A path should have an origin, destination and cost.
 * @spec.specfield Buildings : a set of all buildings in campus. // each building should have its full name abbreviated short name and location representing its entrance.
 *
 */
public class CampusMapModel {

    // Abstraction Function:
    //
    //      this.graph represents the campus map.
    //          Edges in graph are actual paths in the campus map.
    //          Nodes in graph are start and end points of all paths.
    //              Some Nodes also represent entrances of buildings, if there is a path to/from that entrance of building.
    //
    //      this.names represents a mapping from long name of a building to its short name.
    //
    //      this.locations represents a mapping from long name of a building to its locations in the campus
    //
    // Representation Variation:
    //      graph != null and names != null and locations != null
    //      names.keySet().equals(locations.keySet())
    //      forall Coordinate in locations -> Coordinate must be in graph.
    //
    // In other words:
    //      graph, names and locations should not be null.
    //      names and locations should have the exactly same set of keys.
    //      Every building should have its location as a node in this.graph.

    /**
     * a graph represents all paths in the campus
     */
    private final Graph<Coordinate, Double> graph;

    /**
     * map from full name to abbreviated short name for all Buildings.
     */
    private final Map<String, String> names;

    /**
     * map from full name to location for all Buildings.
     */
    private final Map<String, Coordinate> locations;

    /** Test flag, whether to enable expensive checks. */
    private static boolean TEST_FLAG = true;

    /**
     * Creates an empty CampusMapModel.
     *
     * @spec.effects creates an empty CampusMapModel.
     */
    private CampusMapModel() {
        names = new HashMap<>();
        locations = new HashMap<>();
        graph = new Graph<>();
        checkRep();
    }

  /**
   * Factory method:
   * Creates an instance of CampusMapModel with campus path data and campus building data.
   *
   * @spec.requires pathFileName and buildingFileName are valid file paths.
   *
   * @param pathFileName the name of the file will be read that containing all information about campus paths.
   * @param buildingFileName the name of the file will be read that containing all information about campus Buildings.
   * @return an instance of CampusMapModel with campus path data and campus building data.
   */
  public static CampusMapModel makeInstance(String pathFileName, String buildingFileName) {
        CampusMapModel model = new CampusMapModel();
        model.loadData(pathFileName, buildingFileName);
        return model;
    }

  /**
   * Reads Campus Path and Building data set and load them to Map and Buildings, as defined in class annotations.
   *
   * Note that all Buildings are guaranteed to be in the Map, no matter there are edges to/from them or not.
   *
   * @spec.requires pathFileName and buildingFileName are valid file paths.
   *
   * @param pathFileName the name of the file will be read that containing all information about campus paths.
   * @param buildingFileName the name of the file will be read that containing all information about campus Buildings.
   */
  private void loadData(String pathFileName, String buildingFileName) {

        List<DataParser.CampusPathForCsv> edges = DataParser.parsePathData(pathFileName);

        // Since we just want to add all edges to the graph, it implies that both nodes of all edges must be in
        // the graph. As a result, if either start or end of an edge is not in the graph, we simply
        // add it to the graph.
        for (DataParser.CampusPathForCsv edge : edges) {

            Graph<Coordinate, Double>.Node start = this.graph.makeNode(new Coordinate(edge.getOrigin()));
            Graph<Coordinate, Double>.Node end = this.graph.makeNode(new Coordinate(edge.getDestination()));
            Double cost = edge.getDistance();

            boolean result = true;

            if(!this.graph.containNode(start)) {
                result = this.graph.addNode(start) && result;
            }
            if(!this.graph.containNode(end)) {
                result = this.graph.addNode(end) && result;
            }
            // Path is bidirectional.
            result = this.graph.addEdge(this.graph.makeEdge(start, end, cost)) && result;
            result = this.graph.addEdge(this.graph.makeEdge(end, start, cost)) && result;

            // Quick Sanity check, if all operations are good
            assert result;
        }

        Map<String, DataParser.CoordinatesForCsv> tempLocations = new HashMap<>();
        DataParser.parseBuildingData(buildingFileName, this.names, tempLocations);

        // Add all locations of buildings to the map, in case there is not an edge to/from some building.
        // And fill this.locations with data from tempLocations. Specifically, use our Immutable Coordinate class.
        for(Map.Entry<String, DataParser.CoordinatesForCsv> entry : tempLocations.entrySet()) {
            Graph<Coordinate, Double>.Node node = graph.makeNode(new Coordinate(entry.getValue()));
            if (!graph.containNode(node)) {
                graph.addNode(node);
            }
            this.locations.put(entry.getKey(), new Coordinate(entry.getValue()));
        }

        // Quick Sanity check, if loading building data is good.
        assert this.names.size() == this.locations.size();
    }

    /**
     * Find the path in the graph from start-coordinate to end-coordinate.
     * Note that this guarantees to find the shortest path.
     *
     * If multiple least weight path exists, it will return any of them.
     *
     * <p>The path will be returned in the form of a list,
     *      where the first element is start-Node1,
     *                second element is Node1-Node2,
     *                ...
     *                the last element is Node(N-1) - end. (N is the length of the list)
     *
     * <p>If such a path does not exist, it will return null.
     *
     * For self-edge, it will return an empty list.
     *
     * @spec.requires start != null and end != null and Map.contains(start) and Map.contains(end)
     *
     * @param start the start of the path
     * @param end   the end of the path
     * @return a list holding the path from start to end if there exists one, or null otherwise.
     */
    public @Nullable List<Graph<Coordinate, Double>.Edge> findPath (Coordinate start, Coordinate end) {
        checkRep();
        List<Graph<Coordinate, Double>.Edge> result =
                MarvelPaths2.findPath(this.graph, graph.makeNode(start), graph.makeNode(end));
        checkRep();
        return result;
    }

    /**
     * List the name of all Buildings in the campus.
     *
     * It returns an unmodifiable view of a mapping from longName to shortName for all Buildings.
     *
     * @return an unmodifiable view of mapping from longName to shortName for all Buildings.
     */
    public Map<String, String> listBuildings() {
        checkRep();
        Map<String, String> result = Collections.unmodifiableMap(this.names);
        checkRep();
        return result;
    }

    /**
     * List the Coordinate of all Buildings in the campus.
     *
     * It returns an unmodifiable view of a mapping from longName to coordinate for all Buildings.
     *
     * @return an unmodifiable view of mapping from longName to coordinate for all Buildings.
     */
    public Map<String, Coordinate> listLocations() {
        checkRep();
        Map<String, Coordinate> result = Collections.unmodifiableMap(this.locations);
        checkRep();
        return result;
    }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep() {
        assert this.graph != null && this.locations != null && this.names != null;
        assert this.names.size() == this.locations.size();

        if (TEST_FLAG) {
            assert this.names.keySet().equals(this.locations.keySet());
            for(Coordinate location : this.locations.values()) {
                assert graph.containNode(graph.makeNode(location));
            }
        }

    }


    public class Coordinate {
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
        public boolean equals(Object obj) {
            if (!(obj instanceof Coordinate)) {
                return false;
            }
            Coordinate other = (Coordinate) obj;
            return this.x == other.getX() && this.y == other.getY();
        }
    }



}
