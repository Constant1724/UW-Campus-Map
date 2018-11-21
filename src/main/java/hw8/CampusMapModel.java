package hw8;

import hw3.Graph;
import hw7.MarvelPaths2;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
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
 * @spec.specfield Building : a set of all buildings in campus. // each building should have its full name abbreviated short name and location representing its entrance.
 *
 * Abstract Invariant:
 *      Locations of all Buildings Must be in Map, regardless there are paths to/from them.
 */
public class CampusMapModel {

    // Abstraction Function:
    //
    //      this.graph represents the campus map.
    //          Edges in graph are actual paths in the campus map.
    //          Nodes in graph are start and end points of all paths.
    //              Some Nodes also represent entrances of buildings, if there is a path to/from that entrance of building.
    //
    //      this.buildings represents the set of all Buildings in the campus map.
    //
    //
    // Representation Variation:
    //      graph != null and buildings != null
    //      forall building in buildings -> building.location must be in graph.
    //
    // In other words:
    //      graph, buildings should not be null.
    //      Every building should have its location as a node in this.graph.

    /**
     * a graph represents all paths in the campus
     */
    private final Graph<Coordinate, Double> graph;

    /**
     * a set of all Buildings in the Map.
     */
    private final Set<Building> buildings;

    /** Test flag, whether to enable expensive checks. */
    private static boolean TEST_FLAG = true;

    /**
     * Creates an empty CampusMapModel.
     *
     * @spec.effects creates an empty CampusMapModel.
     */
    private CampusMapModel() {
        graph = new Graph<>();
        buildings = new HashSet<>();
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

        List<DataParser.CampusPathForCsv> edges = new ArrayList<>();
        DataParser.parsePathData(pathFileName, edges);

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
            this.graph.addEdge(this.graph.makeEdge(start, end, cost));

            this.graph.addEdge(this.graph.makeEdge(end, start, cost));

            // Quick Sanity check, if all operations are good
            assert result;
        }

        Map<String, String> tempNames = new HashMap<>();

        Map<@KeyFor("tempNames") String, DataParser.CoordinatesForCsv> tempLocations = new HashMap<>();
        DataParser.parseBuildingData(buildingFileName, tempNames, tempLocations);

        // Add all locations of buildings to the map, in case there is not an edge to/from some building.
        // And fill this.locations with data from tempLocations. Specifically, use our Immutable Coordinate class.
        for(Map.Entry<String, DataParser.CoordinatesForCsv> entry : tempLocations.entrySet()) {
            Coordinate location = new Coordinate(entry.getValue());

            // I add the KeyFor property such that any key in tempLocations must be key for tempNames.
            // As a result, since entry.getKey is a key in tempLocations, it must also be a key for tempNames.
            // In addition, tempNames does not allow null values,
            //  therefore, there is no way for tempNames.get(entry.getKey()) to be null
            @SuppressWarnings("incompatible")
            @NonNull String shortName = tempNames.get(entry.getKey());
            String longName = entry.getKey();
            Graph<Coordinate, Double>.Node node = graph.makeNode(location);
            if (!graph.containNode(node)) {
                graph.addNode(node);
            }
            buildings.add(new Building(location, shortName, longName));

        }
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
     * List all Buildings in the campus.
     *
     * It returns an unmodifiable view of a mapping from abbreviated name to Building for all Buildings.
     *
     * @return an unmodifiable view of a mapping from abbreviated name to Building for all Buildings.
     */
    public Set<Building> listBuildings() {
        checkRep();
        Set<Building> result = Collections.unmodifiableSet(this.buildings);
        checkRep();
        return result;
    }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep(@UnknownInitialization(CampusMapModel.class) CampusMapModel this) {
        assert this.graph != null && this.buildings != null;
        if (TEST_FLAG) {

            for(Building building : this.buildings) {
                assert graph.containNode(graph.makeNode(building.getLocation()));
            }
        }

    }

 }
