package hw6;

import hw3.Graph;
import hw3.HW3TestDriver;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a testing driver which reads testPathAllBuildings.test scripts from files for testing Graph, the
 * Marvel parser, and your BFS algorithm.
 */
public class HW6TestDriver extends HW3TestDriver {
  private static String path = "src/main/java/hw6/data/";

  public static void main(String args[]) {
    try {
      if (args.length > 1) {
        printUsage();
        return;
      }

      HW3TestDriver td;

      if (args.length == 0) {
        td =
            new HW6TestDriver(new InputStreamReader(System.in), new OutputStreamWriter(System.out));
      } else {

        String fileName = args[0];
        File tests = new File(fileName);

        if (tests.exists() || tests.canRead()) {
          td = new HW6TestDriver(new FileReader(tests), new OutputStreamWriter(System.out));
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

  public HW6TestDriver(Reader r, Writer w) {
    super(r, w);
  }

  @Override
  protected void executeCommand(String command, List<String> arguments) {
    List<String> superCommands = new ArrayList<>();
    superCommands.add("CreateGraph");
    superCommands.add("AddNode");
    superCommands.add("AddEdge");
    superCommands.add("ListNodes");
    superCommands.add("ListNodes");
    superCommands.add("ListChildren");
    try {
      if (superCommands.contains(command)) {
        super.executeCommand(command, arguments);
      } else if (command.equals("LoadGraph")) {
        loadGraph(arguments);
      } else if (command.equals("FindPath")) {
        findPath(arguments);
      } else {
        output.println("Unrecognized command: " + command);
      }
    } catch (Exception e) {
      output.println("Exception: " + e.toString());
      e.printStackTrace();
    }
  }

  private void loadGraph(List<String> arguments) {
    if (arguments.size() != 2) {
      throw new CommandException("Baz arguments to LoadGraph: " + arguments);
    }
    String graphName = arguments.get(0);
    String fileName = arguments.get(1);
    loadGraph(graphName, path + fileName);
  }

  private void loadGraph(String graphName, String filePath) {
    graphs.put(graphName, MarvelPaths.loadData(filePath));
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

  private void findPath(String graphName, String startNode, String endNode) {
    Graph<String, String> graph = graphs.get(graphName);
    Graph<String, String>.Node start = graph.makeNode(startNode);
    Graph<String, String>.Node end = graph.makeNode(endNode);
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
      List<Graph<String, String>.Edge> path =
          MarvelPaths.findPath(graph, graph.makeNode(startNode), graph.makeNode(endNode));
      if (path == null) {
        output.println("no path found");
      } else {
        for (Graph<String, String>.Edge edge : path) {
          output.println(
              String.format(
                  "%s to %s via %s",
                  edge.getStart().getContent(), edge.getEnd().getContent(), edge.getLabel()));
        }
      }
    }
  }
}
