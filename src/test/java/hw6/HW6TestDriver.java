package hw6;

import hw3.Graph;
import hw3.HW3TestDriver;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements a testing driver which reads test scripts from files for testing Graph, the
 * Marvel parser, and your BFS algorithm.
 */
public class HW6TestDriver extends HW3TestDriver {
    private static String path = "src/main/java/hw6/data/";

  public static void main(String args[]) {}

  public HW6TestDriver(Reader r, Writer w) {super(r,w);}

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
        } else if (command.equals("FindPath ")) {
            findPath(arguments);
        } else {
            output.println("Unrecognized command: " + command);
        }
    } catch (Exception e) {
        output.println("Exception: " + e.toString());
    }

  }

  private void loadGraph(List<String> arguments) {
    if (arguments.size() != 2) {
        throw new CommandException("Baz arguments to CreateGraph: " + arguments);
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
          throw new CommandException("Baz arguments to CreateGraph: " + arguments);
      }
      String graphName = arguments.get(0);
      String startNode = arguments.get(1).replace('_', ' ');
      String endNode = arguments.get(2).replace('_', ' ');
      findPath(graphName, startNode, endNode);
  }
  private void findPath(String graphName, String startNode, String endNode) {
    Graph graph = graphs.get(graphName);
    Graph.Node start = graph.makeNode(startNode);
    Graph.Node end = graph.makeNode(endNode);
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
        List<Graph.Edge> path = MarvelPaths.findPath(graph, graph.makeNode(startNode), graph.makeNode(endNode));
        if (path == null) {
            output.println("no path found");
        } else {
            for (Graph.Edge edge : path) {
                output.println(String.format("%s to %s via %s", edge.getStart().getContent(),
                        edge.getEnd().getContent(), edge.getLabel()));
            }
        }
    }


  }
  //  public void runTests() {}
}
