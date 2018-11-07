package hw6;

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
  //TODO
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

    if (superCommands.contains(command)) {
      super.executeCommand(command, arguments);
    } else if (command.equals("LoadGraph")) {

    } else if (command.equals("FindPath ")) {

    } else {
      output.println("Unrecognized command: " + command);
    }

  }

  private void loadGraph(List<String> arguments) {

  }
  //  public void runTests() {}
}
