package UI;

import DataStructure.Graph;
import hw6.MarvelParser;
import PathFinding.MarvelPaths2;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

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
   * Factory method: Creates an instance of CampusMapModel with campus path data and campus building
   * data.
   *
   * @spec.requires pathFileName and buildingFileName are valid file paths.
   * @param pathFileName the name of the file will be read that containing all information about
   *     campus paths.
   * @param buildingFileName the name of the file will be read that containing all information about
   *     campus Buildings.
   * @return an instance of CampusMapModel with campus path data and campus building data.
   * @throws RuntimeException If there is an IOException while reading the file OR data file is
   *     malformed.
   */
  public static CampusMapModel makeInstance(String pathFileName, String buildingFileName)
      throws RuntimeException {
    CampusMapModel model = new CampusMapModel();
    try {
      model.loadPathData(pathFileName);
      model.loadBuildingData(buildingFileName);
    } catch (MarvelParser.MalformedDataException e) {
      throw new RuntimeException("Malformed data", e);
    }
    return model;
  }

  /**
   * Reads Campus Path data set. Each line of the input file represents a path and is defined by:
   * x1,y1 x2,y2 distance
   *
   * <p>x1,y1 is the start and x2,y2 is the end. distance is the distance of from x1,y1 to x2,y2.
   * All separated by tab.
   *
   * <p>There should not be duplicate paths in data set.
   *
   * @spec.requires filename is a valid file path
   * @param filename the name of the file that will be read
   * @spec.modifies this.graph
   * @spec.effects fill graph with edges in the Campus Path data set, each edge is bidirectional.
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

        Graph<Coordinate, Double>.Node start = this.graph.makeNode(originCoordinate);
        Graph<Coordinate, Double>.Node end = this.graph.makeNode(destinationCoordinate);
        // Add the parsed data to the character and book collections.
        boolean result = true;

        if (!this.graph.containNode(start)) {
          result = this.graph.addNode(start) && result;
        }
        if (!this.graph.containNode(end)) {
          result = this.graph.addNode(end) && result;
        }

        // Path is bidirectional.
        this.graph.addEdge(this.graph.makeEdge(start, end, distance));

        this.graph.addEdge(this.graph.makeEdge(end, start, distance));

        // Quick Sanity check, if all operations are good
        assert result;
      }
    } catch (IOException e) {
      System.err.println(e.toString());
      e.printStackTrace(System.err);
      throw new RuntimeException("Exception while trying to read: " + filename, e);
    }
  }

  /**
   * Reads Campus Building data set. Each line of the input file represents a building and is
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
   * @spec.effects this.buildings, this.graph
   * @spec.modifies fill this.buildings with shortName, longName and location for each Building in
   *     the Campus Building dataset.
   * @spec.modifies add location of all buildings the this.graph if any location of a building is
   *     not in the graph.
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

        // In case there is a building not in graph, add it to graph.
        if (graph.containNode(graph.makeNode(location))) {
          graph.addNode(graph.makeNode(location));
        }

        this.buildings.add(new Building(location, shortName, longName));
      }

    } catch (IOException e) {
      System.err.println(e.toString());
      e.printStackTrace(System.err);
      throw new RuntimeException("Exception while trying to read: " + filename, e);
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
