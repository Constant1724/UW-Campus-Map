package hw7;

import hw3.Graph;
import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts from files for your graph ADT and
 * improved MarvelPaths application using Dijkstra's algorithm.
 */
public class HW7TestDriver {
  public static void main(String args[]) {
    try {
      if (args.length > 1) {
        printUsage();
        return;
      }

      HW7TestDriver td;

      if (args.length == 0) {
        td =
            new HW7TestDriver(new InputStreamReader(System.in), new OutputStreamWriter(System.out));
      } else {

        String fileName = args[0];
        File tests = new File(fileName);

        if (tests.exists() || tests.canRead()) {
          td = new HW7TestDriver(new FileReader(tests), new OutputStreamWriter(System.out));
        } else {
          System.err.println("Cannot read from " + tests.toString());
          printUsage();
          return;
        }
      }

      td.runTests();

    } catch (IOException e) {
      System.err.println(e.toString());
      e.printStackTrace(System.err);
    }
  }

  protected static void printUsage() {
    System.err.println("Usage:");
    System.err.println("to read from a file: java hw7.test.HW7TestDriver <name of input script>");
    System.err.println("to read from standard in: java hw7.test.HW7TestDriver");
  }

  /** String -> Graph: maps the names of graphs to the actual graph * */
  protected final Map<String, Graph<String, Double>> graphs = new HashMap<>();

  protected final PrintWriter output;

  protected final BufferedReader input;

  private static String path = "src/main/java/hw7/data/";

  /**
   * @spec.requires r != null && w != null
   * @spec.effects Creates a new HW7TestDriver which reads command from <tt>r</tt> and writes
   *     results to <tt>w</tt>.
   */
  public HW7TestDriver(Reader r, Writer w) {
    input = new BufferedReader(r);
    output = new PrintWriter(w);
  }

  /**
   * @spec.effects Executes the commands read from the input and writes results to the output
   * @throws IOException if the input or output sources encounter an IOException
   */
  public void runTests() throws IOException {
    String inputLine;
    while ((inputLine = input.readLine()) != null) {
      if ((inputLine.trim().length() == 0) || (inputLine.charAt(0) == '#')) {
        // echo blank and comment lines
        output.println(inputLine);
      } else {
        // separate the input line on white space
        StringTokenizer st = new StringTokenizer(inputLine);
        if (st.hasMoreTokens()) {
          String command = st.nextToken();

          List<String> arguments = new ArrayList<>();
          while (st.hasMoreTokens()) {
            arguments.add(st.nextToken());
          }

          executeCommand(command, arguments);
        }
      }
      output.flush();
    }
  }

  protected void executeCommand(String command, List<String> arguments) {
    try {
      if (command.equals("CreateGraph")) {
        createGraph(arguments);
      } else if (command.equals("AddNode")) {
        addNode(arguments);
      } else if (command.equals("AddEdge")) {
        addEdge(arguments);
      } else if (command.equals("ListNodes")) {
        listNodes(arguments);
      } else if (command.equals("ListChildren")) {
        listChildren(arguments);
      } else if (command.equals("LoadGraph")) {
        loadGraph(arguments);
      } else if (command.equals("FindPath")) {
        findPath(arguments);
      } else {
        output.println("Unrecognized command: " + command);
      }
    } catch (Exception e) {
      output.println("Exception: " + e.toString());
    }
  }

  private void createGraph(List<String> arguments) {
    if (arguments.size() != 1) {
      throw new CommandException("Bad arguments to CreateGraph: " + arguments);
    }

    String graphName = arguments.get(0);
    createGraph(graphName);
  }

  private void createGraph(String graphName) {
    // Insert your code here.
    graphs.put(graphName, new Graph<String, Double>());
    output.println("created graph " + graphName);
  }

  private void addNode(List<String> arguments) {
    if (arguments.size() != 2) {
      throw new CommandException("Bad arguments to addNode: " + arguments);
    }

    String graphName = arguments.get(0);
    String nodeName = arguments.get(1);

    addNode(graphName, nodeName);
  }

  private void addNode(String graphName, String nodeName) {
    // Insert your code here.
    Graph<String, Double> graph = graphs.get(graphName);
    graph.addNode(graph.makeNode(nodeName));
    output.println(String.format("added node %s to %s", nodeName, graphName));
  }

  private void addEdge(List<String> arguments) {
    if (arguments.size() != 4) {
      throw new CommandException("Bad arguments to addEdge: " + arguments);
    }

    String graphName = arguments.get(0);
    String parentName = arguments.get(1);
    String childName = arguments.get(2);
    String edgeLabel = arguments.get(3);

    addEdge(graphName, parentName, childName, Double.parseDouble(edgeLabel));
  }

  private void addEdge(String graphName, String parentName, String childName, Double edgeLabel) {
    // Insert your code here.
    Graph<String, Double> graph = graphs.get(graphName);
    graph.addEdge(graph.makeEdge(graph.makeNode(parentName), graph.makeNode(childName), edgeLabel));
    String out =
        String.format(
            "added edge %.3f from %s to %s in %s", edgeLabel, parentName, childName, graphName);
    output.println(out);
  }

  private void listNodes(List<String> arguments) {
    if (arguments.size() != 1) {
      throw new CommandException("Bad arguments to listNodes: " + arguments);
    }

    String graphName = arguments.get(0);
    listNodes(graphName);
  }

  private void listNodes(String graphName) {
    // Insert your code here.
    Graph<String, Double> graph = graphs.get(graphName);
    List<String> list = new ArrayList<String>();
    for (Graph<String, Double>.Node node : graph.getNodes()) {
      list.add(node.getContent());
    }
    Collections.sort(list);
    String out = graphName + " contains:";
    for (String node : list) {
      out += " " + node;
    }
    output.println(out);
  }

  private void listChildren(List<String> arguments) {
    if (arguments.size() != 2) {
      throw new CommandException("Bad arguments to listChildren: " + arguments);
    }

    String graphName = arguments.get(0);
    String parentName = arguments.get(1);
    listChildren(graphName, parentName);
  }

  private void listChildren(String graphName, String parentName) {
    // Insert your code here.
    Graph<String, Double> graph = graphs.get(graphName);
    String out = String.format("the children of %s in %s are:", parentName, graphName);
    List<Graph<String, Double>.Edge> list =
        new ArrayList<>(graph.getEdges(graph.makeNode(parentName)));

    list.sort(
        (e1, e2) -> {
          if (e1.getEnd().getContent().compareTo(e2.getEnd().getContent()) == 0) {
            return e1.getLabel().compareTo(e2.getLabel());
          } else {
            return e1.getEnd().getContent().compareTo(e2.getEnd().getContent());
          }
        });

    for (Graph<String, Double>.Edge edge : list) {
      out += String.format(" %s(%.3f)", edge.getEnd().getContent(), edge.getLabel());
    }
    output.println(out);
  }

  /** This exception results when the input file cannot be parsed properly. */
  private void loadGraph(List<String> arguments) {
    if (arguments.size() != 2) {
      throw new CommandException("Baz arguments to LoadGraph: " + arguments);
    }
    String graphName = arguments.get(0);
    String fileName = arguments.get(1);
    loadGraph(graphName, path + fileName);
  }

  private void loadGraph(String graphName, String filePath) {
    graphs.put(graphName, MarvelPaths2.loadData(filePath));
    output.println("loaded graph " + graphName);
  }

  private void findPath(List<String> arguments) {
    if (arguments.size() != 3) {
      throw new CommandException("Baz arguments to FindPath: " + arguments);
    }
    String graphName = arguments.get(0);
    String startNode = arguments.get(1).replace('_', ' ');
    String endNode = arguments.get(2).replace('_', ' ');
    findPath(graphName, startNode, endNode);
  }

  protected void findPath(String graphName, String startNode, String endNode) {
    Graph<String, Double> graph = graphs.get(graphName);
    Graph<String, Double>.Node start = graph.makeNode(startNode);
    Graph<String, Double>.Node end = graph.makeNode(endNode);
    boolean ifBothNodesInGraph = true;
    if (!graph.containNode(start)) {
      ifBothNodesInGraph = false;
      output.println("unknown character " + startNode);
    }
    if (!graph.containNode(end)) {
      ifBothNodesInGraph = false;
      output.println("unknown character " + endNode);
    }
    if (ifBothNodesInGraph) {
      output.println(String.format("path from %s to %s:", startNode, endNode));
      List<Graph<String, Double>.Edge> path =
          MarvelPaths2.findPath(graph, graph.makeNode(startNode), graph.makeNode(endNode));
      if (path == null) {
        output.println("no path found");
      } else {
        for (Graph<String, Double>.Edge edge : path) {
          output.println(
              String.format(
                  "%s to %s with weight %.3f",
                  edge.getStart().getContent(), edge.getEnd().getContent(), edge.getLabel()));
        }
        output.println(String.format("total cost: %.3f", MarvelPaths2.sumCost(path)));
      }
    }
  }

  public static class CommandException extends RuntimeException {

    public CommandException() {
      super();
    }

    public CommandException(String s) {
      super(s);
    }

    public static final long serialVersionUID = 3495;
  }
}
