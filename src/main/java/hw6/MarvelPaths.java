package hw6;

import hw3.Graph;
import java.util.*;
import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class loads a file, which is defined in the format for marvel.tsv, and create a graph with
 * data from that file.
 *
 * <p>It also provides functionality to find a path between two given nodes.
 */
public class MarvelPaths {
  // This is NOT an ADT!!!
  // This is NOT an ADT!!!

  /** default path for the data */
  public static final String MARVEL = "src/main/java/hw6/data/marvel.tsv";

  /**
   * Standard main method. Read in data and construct a graph and allow user to type in two nodes
   * and print out a path between these two nodes.
   *
   * <p>Note that both input and output will be directed to System.in and System.out
   *
   * <p>It provide usage messages.
   *
   * @param args list of command line arguments
   */
  public static void main(String[] args) {
    System.out.println("Loading data...");
    long start = System.currentTimeMillis();
    Graph<String, String> graph = loadData(MARVEL);
    long times = System.currentTimeMillis() - start;
    if (graph == null) {
      System.out.println("Malformed Data Detected");
      System.exit(1);
    }
    System.out.println("Loading complete in " + times + " ms");

    Scanner reader = new Scanner(System.in, "UTF-8"); // Reading from System.in
    System.out.println("Type exit at any time to quit");
    while (true) {
      System.out.println();
      System.out.println("Please input two character name:");

      Graph<String, String>.Node startNode = graph.makeNode(readInput(reader, "character1: "));
      Graph<String, String>.Node endNode = graph.makeNode(readInput(reader, "character2: "));
      if (!graph.containNode(startNode)) {
        System.out.println("Character " + startNode.getContent() + " NOT FOUND!");
      } else if (!graph.containNode(endNode)) {
        System.out.println("Character " + startNode.getContent() + " NOT FOUND!");
      } else {
        List<Graph<String, String>.Edge> path = MarvelPaths.findPath(graph, startNode, endNode);
        if (path == null) {
          System.out.println("no path found");
        } else {
          for (Graph.Edge edge : path) {
            System.out.println(
                String.format(
                    "%s to %s via %s",
                    edge.getStart().getContent(), edge.getEnd().getContent(), edge.getLabel()));
          }
        }
      }
    }
  }

  /**
   * Helper method to help print out prompts and parse user input and return it.
   *
   * @param reader a reader that reads in user input
   * @param prompt the prompt message, will be printed out before user type in anything.
   * @return a correctly formatted user input. specifically, all quotation marks will be excluded.
   */
  private static String readInput(Scanner reader, String prompt) {
    System.out.print(prompt);
    String character = reader.nextLine().replaceAll("\"", "");
    if (character.equals("exit")) {
      System.exit(0);
    }
    return character;
  }

  /**
   * Fill the graph with the data in a file, whose path is filename. Note that if any data is
   * malformed it will return null
   *
   * @spec.requires filename != null
   * @param filename the name of the datafile
   * @return a graph holding all data in filename, otherwise null.
   */
  public static @Nullable Graph<String, String> loadData(String filename) {
    Graph<String, String> graph = new Graph<>();
    Set<String> characters = new HashSet<>();
    Map<String, List<String>> books = new HashMap<>();

    try {
      MarvelParser.parseData(filename, characters, books);
    } catch (MarvelParser.MalformedDataException e) {
      System.err.println("MalFormed data in file: " + filename);
      return null;
    }

    for (String character : characters) {
      graph.addNode(graph.makeNode(character));
    }

    assert characters.size() == graph.getNodes().size(); // quick sanity check.

    for (Map.Entry<String, List<String>> entry : books.entrySet()) {
      for (String character1 : entry.getValue()) {
        for (String character2 : entry.getValue()) {
          if (character1.equals(character2)) { // we do not necessarily need to create self-edge.
            continue;
          }
          graph.addEdge(
              graph.makeEdge(
                  graph.makeNode(character1), graph.makeNode(character2), entry.getKey()));
        }
      }
    }
    return graph;
  }

  /**
   * find the path in the graph from start to end. Note that this guarantees to find the path with
   * fewest edges. If multiple path exist, this will return the lexicographically (alphabetically)
   * least path, in terms of the node's content, which is string in this case.
   *
   * <p>The path will be returned in the form of a list, where the first element is the start, the
   * second element is the next step, the third element is the next next step.. all the way to the
   * end.
   *
   * <p>If such a path does not exist, it will return null.
   *
   * @spec.requires graph != null and start != null and end != null
   * @param graph the graph to be searched in
   * @param start the start of the path to be searched.
   * @param end the end of the path to be searched
   * @return a list holding the path from start to end if there exists one, or null otherwise.
   */
  // @SuppressWarnings({"nullness", "initialization"})
  public static @Nullable List<Graph<String, String>.Edge> findPath(Graph<String, String> graph, Graph<String, String>.Node start, Graph<String, String>.Node end) {

    Map<Graph<String, String>.Node, List<Graph<String, String>.Edge>> mapping = new HashMap<>();
    mapping.put(start, new ArrayList<>());
    Queue<@KeyFor({"mapping"}) Graph<String, String>.Node> queue = new ArrayDeque<>();
    queue.add(start);

    while (!queue.isEmpty()) {

      @KeyFor({"mapping"}) Graph<String, String>.Node node = queue.poll();

      // The queue does not allow null elements, and the while loop condition guarantees that queue
      // is not empty
      // Therefore, there is no way for node to be null
      assert node != null
          : "@AssumeAssertion(nullness): queue does not allow null elements, and queue is not empty";
      if (node.equals(end)) {
        return mapping.get(node);
      }

      // make a sorted view of currentEdges, so that alphabetically cost least path is guaranteed.
      Queue<Graph<String, String>.Edge> currentEdgesSorted =
          new PriorityQueue<>(
              (o1, o2) -> {
                if (o1.getEnd().equals(o2.getEnd())) {
                  return o1.getLabel().compareTo(o2.getLabel());
                } else {
                  return o1.getEnd().getContent().compareTo(o2.getEnd().getContent());
                }
              });

      // Note that the node must be in the graph ADT. Since we are only adding endNode of edges that
      // are in graph,
      // to the queue. (Start node is an exception, but start node is required to be in the graph)
      // As a result, when we poll out an item in the queue, it must be in the graph, as long as
      // no other mutator are called.

      // Note that we cannot write @Keyfor for node, since graph.map is private and we cannot access
      // it from outer
      // class

      // Note that it makes sense to not write @Keyfor for the Node class. According to my design,
      // You need to first create a Node and then add the node to the graph. As a result, after you
      // create the
      // Node but before you add it to the graph, the node is not a key for the graph.
      @SuppressWarnings("incompatible")
      Set<Graph<String, String>.Edge> currentEdgesUnsorted = graph.getEdges(node);

      currentEdgesSorted.addAll(currentEdgesUnsorted);

      for (Graph<String, String>.Edge edge : currentEdgesSorted) {
        if (!mapping.containsKey(edge.getEnd())) {
          // Any node in queue must be a node in the mapping. Whenever we are adding a Node to the
          // queue,
          // we first add it to the mapping. Moreover, node has already been annotated as
          // @Keyfor("mapping")
          @NonNull @SuppressWarnings("incompatible")
          List<Graph<String, String>.Edge> previousList = mapping.get(node);
          List<Graph<String, String>.Edge> previousListCopy = new ArrayList<>(previousList);
          previousListCopy.add(edge);
          mapping.put(edge.getEnd(), previousListCopy);
          queue.add(edge.getEnd());
        }
      }
    }
    // return null if no path found.
    return null;
  }
}
