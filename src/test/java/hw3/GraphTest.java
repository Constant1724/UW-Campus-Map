package hw3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class GraphTest {
  @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

  private static final Graph<String, String>.Node A = NodeTest.create("A");
  private static final Graph<String, String>.Node B = NodeTest.create("B");
  private static final Graph<String, String>.Node C = NodeTest.create("C");
  private static final Graph<String, String>.Node D = NodeTest.create("D");
  private static final Graph<String, String>.Node EXCLUDE = NodeTest.create("EXCLUDE");

  private static final String LABEL = "AHHh";
  private static final String ANOTHER_LABEL = "AHHHHHHHHHHHHHH";

  private static final Graph<String, String>.Edge A_TO_B = EdgeTest.create(A, B, ANOTHER_LABEL);

  private static final Graph<String, String>.Edge A_TO_A = EdgeTest.create(A, A, ANOTHER_LABEL);

  private List<Graph<String, String>.Node> Nodes;
  private List<Graph<String, String>.Edge> Edges;

  private void addNodesToGraph(Graph<String, String> g) {
    for (Graph<String, String>.Node node : this.Nodes) {
      g.addNode(node);
    }
  }

  private void addEdgesToGraph(Graph<String, String> g) {
    for (Graph<String, String>.Edge edge : this.Edges) {
      g.addEdge(edge);
    }
  }

  @Before
  public void initialize() {

    this.Nodes = new ArrayList<Graph<String, String>.Node>();
    this.Nodes.add(A);
    this.Nodes.add(B);
    this.Nodes.add(C);
    this.Nodes.add(D);

    for (Integer i = -4; i < 5; i++) {
      this.Nodes.add(NodeTest.create(i.toString()));
    }

    this.Edges = new ArrayList<Graph<String, String>.Edge>();
    for (Graph<String, String>.Node start : this.Nodes) {
      for (Graph<String, String>.Node end : this.Nodes) {
        this.Edges.add(EdgeTest.create(start, end, LABEL));
      }
    }
  }

  /** Test to check if addNode is implemented correctly. */
  @Test
  public void testAddNode() {
    Graph<String, String> graph = new Graph<String, String>();
    for (Graph<String, String>.Node node : this.Nodes) {
      // It should not contain the Graph<String, String>.Node before add.
      assertFalse(graph.containNode(node));

      // Successful add operation should return True.
      assertTrue(graph.addNode(node));

      // It should contain the Graph<String, String>.Node after add.
      assertTrue(graph.containNode(node));

      // Adding duplicate should not succeed.
      assertFalse(graph.addNode(node));
    }
  }

  /** Test to check if containNode is implemented correctly. */
  @Test
  public void testContainNode() {
    Graph<String, String> graph = new Graph<String, String>();

    for (Graph<String, String>.Node node : this.Nodes) {
      // Empty graph shall not contain any node
      assertFalse(graph.containNode(node));
    }
    for (Graph<String, String>.Node node : this.Nodes) {
      graph.addNode(node);
      // After a node has been added, the graph should contain the node
      assertTrue(graph.containNode(node));
    }
  }

  /** Test to check if removeNode is implemented correctly. */
  @Test
  public void testRemoveNode() {
    Graph<String, String> graph = new Graph<String, String>();

    addNodesToGraph(graph);

    // Check if removeNode actually removes the node.
    for (Graph<String, String>.Node node : this.Nodes) {
      // Before remove, the graph should contain the node
      assertTrue(graph.containNode(node));
      // Successful remove operation should return True
      assertTrue(graph.removeNode(node));
      // After remove, the graph should not contain the node.
      assertFalse(graph.containNode(node));
      // Graph<String, String>.Node should not be able to be removed twice.
      assertFalse(graph.removeNode(node));
    }

    // check if removeNode also remove any Edges contain that node
    graph = new Graph<String, String>();
    addNodesToGraph(graph);
    addEdgesToGraph(graph);

    for (Graph<String, String>.Node node : this.Nodes) {
      graph.removeNode(node);
      // if any edge contain the node, check if the edge is removed
      for (Graph<String, String>.Edge edge : this.Edges) {
        if (edge.getStart().equals(node) || edge.getEnd().equals(node)) {
          assertFalse(graph.containEdge(edge));
        }
      }
    }
  }

  /** Test to check if getNodes is implemented correctly. */
  @Test
  public void testGetNodes() {
    Graph<String, String> graph = new Graph<String, String>();

    addNodesToGraph(graph);

    // the set object should not be null.
    assertNotNull(graph.getNodes());
    // size should be equal
    assertEquals(graph.getNodes().size(), this.Nodes.size());

    for (Graph<String, String>.Node node : this.Nodes) {
      // each node should be available in the graph.
      assertTrue(graph.getNodes().contains(node));
    }
  }

  /** Test to check if getNodes returns an unmodifiable view */
  @Test(expected = UnsupportedOperationException.class)
  public void testGetNodesUnmodifiableView() {
    Graph<String, String> graph = new Graph<String, String>();

    addNodesToGraph(graph);
    Set<Graph<String, String>.Node> view = graph.getNodes();

    for (Graph<String, String>.Node node : view) {
      // removing should not affect graph.
      view.remove(node);
      assertTrue(graph.containNode(node));
      // adding should not affect graph.
      view.add(EXCLUDE);
      assertFalse(graph.containNode(EXCLUDE));
    }
  }

  /** Test to check if addEdge is implemented correctly. */
  @Test
  public void testAddEdge() {
    Graph<String, String> graph = new Graph<String, String>();

    // make sure any edge with both nodes in graph, could be added to the graph,
    // and duplicate should not be added to the graph.
    addNodesToGraph(graph);
    for (Graph<String, String>.Edge edge : this.Edges) {
      // It should not contain the Graph<String, String>.Edge before add.
      assertFalse(graph.containEdge(edge));

      // Successful add operation should return True.
      assertTrue(graph.addEdge(edge));

      // It should contain the Graph<String, String>.Edge after add.
      assertTrue(graph.containEdge(edge));

      // Adding duplicate should not succeed.
      assertFalse(graph.addEdge(edge));
    }

    graph = new Graph<String, String>();

    // make sure that any edge with neither nodes in graph, could not be added to the graph.
    for (Graph<String, String>.Edge edge : this.Edges) {
      assertFalse(graph.addEdge(edge));
    }

    // make sure that any edge with only one node in graph, could not be added to the graph.
    for (Graph<String, String>.Node node : this.Nodes) {
      graph.addNode(node);
      for (Graph<String, String>.Edge edge : this.Edges) {
        if (graph.containNode(edge.getStart()) ^ graph.containNode(edge.getEnd())) {
          assertFalse(graph.addEdge(edge));
        }
      }
    }
  }

  /** Test to check if containEdge is implemented correctly. */
  @Test
  public void testContainEdge() {
    Graph<String, String> graph = new Graph<String, String>();
    addNodesToGraph(graph);

    for (Graph<String, String>.Edge edge : this.Edges) {
      // Empty graph shall not contain any edge
      assertFalse(graph.containEdge(edge));
    }
    for (Graph<String, String>.Edge edge : this.Edges) {
      graph.addEdge(edge);
      // After a edge has been added, the graph should contain the edge
      assertTrue(graph.containEdge(edge));
    }
  }

  /** Test to check if removeEdge is implemented correctly. */
  @Test
  public void testRemoveEdge() {
    Graph<String, String> graph = new Graph<String, String>();

    addNodesToGraph(graph);
    addEdgesToGraph(graph);

    // Check if removeEdge actually removes the edge.
    for (Graph<String, String>.Edge edge : this.Edges) {
      // Before remove, the graph should contain the edge
      assertTrue(graph.containEdge(edge));
      // Successful remove operation should return True
      assertTrue(graph.removeEdge(edge));
      // After remove, the graph should not contain the edge.
      assertFalse(graph.containEdge(edge));
      // Graph<String, String>.Edge should not be able to be removed twice.
      assertFalse(graph.removeEdge(edge));
    }

    // Remove edge should not remove any Graph<String, String>.Node.
    for (Graph<String, String>.Node node : this.Nodes) {
      assertTrue(graph.containNode(node));
    }
  }

  /** Test to check if getEdges is implemented correctly. */
  @Test
  public void testGetEdges() {
    Graph<String, String> graph = new Graph<String, String>();

    addNodesToGraph(graph);
    addEdgesToGraph(graph);

    // the set object should not be null.
    for (Graph<String, String>.Node node : Nodes) {
      assertNotNull(graph.getEdges(node));
    }

    for (Graph<String, String>.Node node : Nodes) {
      Set<Graph<String, String>.Edge> edges = graph.getEdges(node);

        // testPathAllBuildings.test if the return view only contains edges, whose start.equals(node)
      for (Graph<String, String>.Edge edge : edges) {
        assertEquals(edge.getStart(), node);
      }

      // size should be equal
      int count = 0;
      for (Graph<String, String>.Edge edge : Edges) {
        if (edge.getStart().equals(node)) {
          count++;
        }
      }
      assertEquals(edges.size(), count);
    }
  }

  /** Test to check if getEdges returns an unmodifiable view */
  @Test(expected = UnsupportedOperationException.class)
  public void testGetEdgesUnmodifiableView() {
    Graph<String, String> graph = new Graph<String, String>();

    addNodesToGraph(graph);
    addEdgesToGraph(graph);

    for (Graph<String, String>.Node node : Nodes) {
      Set<Graph<String, String>.Edge> view = graph.getEdges(node);
      for (Graph<String, String>.Edge edge : view) {
        // removing should not affect graph.
        view.remove(edge);
        assertTrue(graph.containEdge(edge));
        // adding should not affect graph.
        view.add(A_TO_B);
        assertFalse(graph.containEdge(A_TO_B));
      }
    }
  }
}
