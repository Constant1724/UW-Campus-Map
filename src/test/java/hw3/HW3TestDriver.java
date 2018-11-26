package hw3;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads testPathAllBuildings.test scripts from files for testing Graph.
 */
public class HW3TestDriver {

  public static void main(String args[]) {
    try {
      if (args.length > 1) {
        printUsage();
        return;
      }

      HW3TestDriver td;

      if (args.length == 0) {
        td =
            new HW3TestDriver(new InputStreamReader(System.in), new OutputStreamWriter(System.out));
      } else {

        String fileName = args[0];
        File tests = new File(fileName);

        if (tests.exists() || tests.canRead()) {
          td = new HW3TestDriver(new FileReader(tests), new OutputStreamWriter(System.out));
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
      System.err.println("to read from a file: java hw3.testPathAllBuildings.test.HW3TestDriver <name of input script>");
      System.err.println("to read from standard in: java hw3.testPathAllBuildings.test.HW3TestDriver");
  }

  /** String -> Graph: maps the names of graphs to the actual graph * */
  protected final Map<String, Graph<String, String>> graphs = new HashMap<>();

  protected final PrintWriter output;

  protected final BufferedReader input;

  /**
   * @spec.requires r != null && w != null
   * @spec.effects Creates a new HW3TestDriver which reads command from <tt>r</tt> and writes
   *     results to <tt>w</tt>.
   */
  public HW3TestDriver(Reader r, Writer w) {
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
    graphs.put(graphName, new Graph<String, String>());
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
    Graph<String, String> graph = graphs.get(graphName);
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

    addEdge(graphName, parentName, childName, edgeLabel);
  }

  private void addEdge(String graphName, String parentName, String childName, String edgeLabel) {
    // Insert your code here.
    Graph<String, String> graph = graphs.get(graphName);
    graph.addEdge(graph.makeEdge(graph.makeNode(parentName), graph.makeNode(childName), edgeLabel));
    String out =
        String.format(
            "added edge %s from %s to %s in %s", edgeLabel, parentName, childName, graphName);
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
    Graph<String, String> graph = graphs.get(graphName);
    List<String> list = new ArrayList<String>();
    for (Graph<String, String>.Node node : graph.getNodes()) {
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
    Graph<String, String> graph = graphs.get(graphName);
    String out = String.format("the children of %s in %s are:", parentName, graphName);
    List<Graph<String, String>.Edge> list =
        new ArrayList<Graph<String, String>.Edge>(graph.getEdges(graph.makeNode(parentName)));

    list.sort(
        (e1, e2) -> {
          if (e1.getEnd().getContent().compareTo(e2.getEnd().getContent()) == 0) {
            return e1.getLabel().compareTo(e2.getLabel());
          } else {
            return e1.getEnd().getContent().compareTo(e2.getEnd().getContent());
          }
        });
    for (Graph<String, String>.Edge edge : list) {
      out += String.format(" %s(%s)", edge.getEnd().getContent(), edge.getLabel());
    }
    output.println(out);
  }

  /** This exception results when the input file cannot be parsed properly. */
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
