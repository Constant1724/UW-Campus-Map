package hw8;

import hw3.Graph;
import hw7.MarvelPaths2;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

/**
 * CampusPathModel represents a map of the campus, by loading data from data set.
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
 * @spec.specfield Map : a map represents set of all edges in campus // A path should have an origin, destination and cost.
 * @spec.specfield Buildings : a set of all buildings in campus. // each building should have its full name abbreviated short name and location representing its entrance.
 *
 */
public class CampusPathModel {

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
    //
    // In other words:
    //      graph, names and locations should not be null.
    //      names and locations should have the exactly same set of keys.

    /**
     * a graph represents all paths in the campus
     */
    private final Graph<Coordinates, Double> graph;

    /**
     * map from full name to abbreviated short name for all buildings.
     */
    private final Map<String, String> names;

    /**
     * map from full name to location for all buildings.
     */
    private final Map<String, Coordinates> locations;

    /** Test flag, whether to enable expensive checks. */
    private static boolean TEST_FLAG = false;

    /**
     * Creates an empty CampusPathModel.
     *
     * @spec.effects creates an empty CampusPathModel.
     */
    private CampusPathModel() {
        names = new HashMap<>();
        locations = new HashMap<>();
        graph = new Graph<>();
        checkRep();
    }

  /**
   * Factory method:
   * Creates an instance of CampusPathModel with campus path data and campus building data.
   *
   * @spec.requires pathFileName and buildingFileName are valid file paths.
   *
   * @param pathFileName the name of the file will be read that containing all information about campus paths.
   * @param buildingFileName the name of the file will be read that containing all information about campus buildings.
   * @return an instance of CampusPathModel with campus path data and campus building data.
   */
  public static CampusPathModel makeInstance(String pathFileName, String buildingFileName) {
        CampusPathModel model = new CampusPathModel();
        model.loadData(pathFileName, buildingFileName);
        return model;
    }

  /**
   * Reads Campus Path and Building data set and load them to Map and Buildings, as defined in class annotations.
   *
   * @spec.requires pathFileName and buildingFileName are valid file paths.
   *
   * @param pathFileName the name of the file will be read that containing all information about campus paths.
   * @param buildingFileName the name of the file will be read that containing all information about campus buildings.
   */
  private void loadData(String pathFileName, String buildingFileName) {

        List<CampusPath> edges = DataParser.parsePathData(pathFileName);

        // Since we just want to add all edges to the graph, it implies that both nodes of all edges must be in
        // the graph. As a result, if either start or end of an edge is not in the graph, we simply
        // add it to the graph.
        for (CampusPath edge : edges) {

            Graph<Coordinates, Double>.Node start = this.graph.makeNode(edge.getOrigin());
            Graph<Coordinates, Double>.Node end = this.graph.makeNode(edge.getDestination());
            Double cost = edge.getDistance();

            boolean result = true;

            if(!this.graph.containNode(start)) {
                result = this.graph.addNode(start) && result;
            }
            if(!this.graph.containNode(end)) {
                result = this.graph.addNode(end) && result;
            }
            result = this.graph.addEdge(this.graph.makeEdge(start, end, cost)) && result;

            // Quick Sanity check, if all operations are good
            assert result;
        }


        DataParser.parseBuildingData(buildingFileName, this.names, this.locations);

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
    public @Nullable List<Graph<Coordinates, Double>.Edge> findPath (Coordinates start, Coordinates end) {
        checkRep();
        List<Graph<Coordinates, Double>.Edge> result =
                MarvelPaths2.findPath(this.graph, graph.makeNode(start), graph.makeNode(end));
        checkRep();
        return result;
    }

    /**
     * List the name of all buildings in the campus.
     *
     * It returns an unmodifiable view of a mapping from longName to shortName for all buildings.
     *
     * @return an unmodifiable view of mapping from longName to shortName for all buildings.
     */
    public Map<String, String> listBuildings() {
        checkRep();
        Map<String, String> result = Collections.unmodifiableMap(this.names);
        checkRep();
        return result;
    }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep() {
        assert this.graph != null && this.locations != null && this.names != null;
        assert this.names.size() == this.locations.size();

        if (TEST_FLAG) {
            assert this.names.keySet().equals(this.locations.keySet());
        }

    }



}
