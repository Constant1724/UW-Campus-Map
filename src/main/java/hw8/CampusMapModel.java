package hw8;

import hw3.Graph;
import hw6.MarvelParser;
import hw7.MarvelPaths2;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CampusMapModel represents a map of the campus, by loading data from data set.
 *
 * <p>Specifically, it loads information about all paths and buildings. For any path, it loads the
 * origin, destination and cost of that path. For any building, it loads both full name and
 * abbreviated short name and a location representing its entrance. If a building has multiple
 * entrance, then its shortName and longName must be unique for each entrance. For example: CHL (NE)
 * and CHL (SE) represents different entrances of the same building.
 *
 * <p>Client can list all buildings in the campus and find a route from one building to another.
 *
 * <p>Specification field:
 *
 * @spec.specfield Coordinate : represents the location of a point in the campus. // It could be
 *     origin, destination of an edge, or location of a building.
 * @spec.specfield Map : a map represents set of all edges in campus // A path should have an
 *     origin, destination and cost.
 * @spec.specfield Building : a set of all buildings in campus. // Each building should have its
 *     full name abbreviated short name and location representing its entrance.
 *     <p>Abstract Invariant: Locations of all Buildings Must be in Map, regardless there are paths
 *     to/from them.
 */
@Service
public class CampusMapModel {

  // Abstraction Function:
  //
  //      this.graph represents the campus map.
  //          Edges in graph are actual paths in the campus map.
  //          Nodes in graph are start and end points of all paths.
  //              Some Nodes also represent entrances of buildings, if there is a path to/from that
  // entrance of building.
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

  /** a graph represents all paths in the campus */
  private final Graph<Coordinate, Double> graph;

  /** a set of all Buildings in the Map. */
  private final Set<Building> buildings;

  /** Test flag, whether to enable expensive checks. */
  private static boolean TEST_FLAG = true;

  /** a parser that reads and parse data from disk.*/
  @Autowired
  private DataParser parser;

  /**
   * Creates an empty CampusMapModel.
   *
   * @spec.effects creates an empty CampusMapModel.
   */
  public CampusMapModel() {
    graph = new Graph<>();
    buildings = new HashSet<>();
    this.run();
    checkRep();
  }

//  /**
//   * Factory method: Creates an instance of CampusMapModel with campus path data and campus building
//   * data.
//   *
//   * @spec.requires pathFileName and buildingFileName are valid file paths.
//   * @param pathFileName the name of the file will be read that containing all information about
//   *     campus paths.
//   * @param buildingFileName the name of the file will be read that containing all information about
//   *     campus Buildings.
//   * @return an instance of CampusMapModel with campus path data and campus building data.
//   * @throws RuntimeException If there is an IOException while reading the file OR data file is
//   *     malformed.
//   */
//  public static CampusMapModel makeInstance(String pathFileName, String buildingFileName)
//      throws RuntimeException {
//    CampusMapModel model = new CampusMapModel();
//
//    DataParser parser;
//    model.addPath(parser.getPaths());
//    model.addBuilding(parser.getBuildings());
//    return model;
//  }

  /**
   * load and initialize a bunch of stuff.
   */
  private void run() {
    this.addPath(parser.getPaths());
    this.addBuilding(parser.getBuildings());
  }

  /**
   * add Campus paths in given parameter to the model.
   *
   * @spec.modifies this.graph
   * @spec.effects fill graph with edges in the given paths, each edge is bidirectional.
   * @param paths
   */
  private void addPath(Set<CampusPath> paths) {
    for (CampusPath path : paths) {
      Graph<Coordinate, Double>.Node start = this.graph.makeNode(path.getOrigin());
      Graph<Coordinate, Double>.Node end = this.graph.makeNode(path.getDestination());
      // Add the parsed data to the character and book collections.
      boolean result = true;

      if (!this.graph.containNode(start)) {
        result = this.graph.addNode(start) && result;
      }
      if (!this.graph.containNode(end)) {
        result = this.graph.addNode(end) && result;
      }

      // Path is bidirectional.
      this.graph.addEdge(this.graph.makeEdge(start, end, path.getCost()));

      this.graph.addEdge(this.graph.makeEdge(end, start, path.getCost()));

      // Quick Sanity check, if all operations are good
      assert result;
    }
  }

  /**
   * add Campus buildings in given parameter to the model.
   *
   * @spec.effects this.buildings, this.graph
   * @spec.modifies fill this.buildings with shortName, longName and location for each Building in
   *     the Campus Building dataset.
   * @spec.modifies add location of all buildings the this.graph if any location of a building is
   *     not in the graph.
   * @param buildings
   */
  private void addBuilding(Set<Building> buildings) {
    // In case there is a building not in graph, add it to graph.
    for (Building building : buildings) {
      if (graph.containNode(graph.makeNode(building.getLocation()))) {
        graph.addNode(graph.makeNode(building.getLocation()));
      }

      this.buildings.add(building);
    }
  }

  /**
   * Find the path in the graph from start-coordinate to end-coordinate. Note that this guarantees
   * to find the shortest path.
   *
   * <p>A path consists of one or more CampusPath instances.
   *
   * <p>If multiple least weight path exists, it will return any of them.
   *
   * <p>The path will be returned in the form of a list, where the first element is start-Node1,
   * second element is Node1-Node2, ... the last element is Node(N-1) - end. (N is the length of the
   * list)
   *
   * <p>If such a path does not exist, it will return null.
   *
   * <p>For self-edge, it will return an empty list.
   *
   * @spec.requires start != null and end != null and Map.contains(start) and Map.contains(end)
   * @param start the start of the path
   * @param end the end of the path
   * @return a list holding the path from start to end if there exists one, or null otherwise.
   */
  public @Nullable List<CampusPath> findPath(Coordinate start, Coordinate end) {
    checkRep();
    List<Graph<Coordinate, Double>.Edge> result =
        MarvelPaths2.findPath(this.graph, graph.makeNode(start), graph.makeNode(end));
    if (result == null) {
      return null;
    }
    // Change to List<CampusPath> in order to decrease coupling between model part and view part.
    List<CampusPath> paths = new ArrayList<>();
    for (Graph<Coordinate, Double>.Edge edge : result) {
      paths.add(
          new CampusPath(
              edge.getStart().getContent(), edge.getEnd().getContent(), edge.getLabel()));
    }
    checkRep();
    return paths;
  }

  /**
   * List all Buildings in the campus.
   *
   * <p>It returns an unmodifiable view of a mapping from abbreviated name to Building for all
   * Buildings.
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

      for (Building building : this.buildings) {
        assert graph.containNode(graph.makeNode(building.getLocation()));
      }
    }
  }
}
