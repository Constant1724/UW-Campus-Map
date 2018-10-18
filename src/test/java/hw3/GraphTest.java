package hw3;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public class GraphTest {


    private static final Node A = NodeTest.create("A");
    private static final Node B = NodeTest.create("B");
    private static final Node C = NodeTest.create("C");
    private static final Node D = NodeTest.create("D");

    private static final int ZERO_COST = 0;

    private static final Edge A_TO_B = EdgeTest.create(A, B, ZERO_COST);

    private static final Edge A_TO_A = EdgeTest.create(A, A, ZERO_COST);

    private List<Node> Nodes;
    private List<Edge> Edges;
    
    private void addNodesToGraph(Graph g) {
        for(Node node: this.Nodes) {
            g.addNode(node);
        }
    }    
    
    private void addEdgesToGraph(Graph g) {
        for(Edge edge: this.Edges) {
            g.addEdge(edge);
        }
    }

    @Before
    private void initialize() {

        this.Nodes = new ArrayList<Node>();
        this.Nodes.add(A);
        this.Nodes.add(B);
        this.Nodes.add(C);
        this.Nodes.add(D);

        for (Integer i = -4; i < 5; i++) {
            this.Nodes.add(NodeTest.create(i.toString()));
        }

        this.Edges = new ArrayList<Edge>();
        for (Node start : this.Nodes) {
            for (Node end : this.Nodes) {
                this.Edges.add(EdgeTest.create(start, end, ZERO_COST));
            }
        }
    }

    /**
     * Test to check if addNode is implemented correctly.
     */
    @Test
    public void testAddNode() {
        Graph graph = new Graph();
        for (Node node : this.Nodes) {
            // It should not contain the Node before add.
            assertFalse(graph.containNode(node));

            // Successful add operation should return True.
            assertTrue(graph.addNode(node));

            // It should contain the Node after add.
            assertTrue(graph.containNode(node));

            // Adding duplicate should not succeed.
            assertFalse(graph.addNode(node));
        }

    }

    /**
     * Test to check if containNode is implemented correctly.
     */
    @Test
    public void testContainNode() {
        Graph graph = new Graph();

        for(Node node : this.Nodes) {
            // Empty graph shall not contain any node
            assertFalse(graph.containNode(node));
        }
        for(Node node: this.Nodes) {
            graph.addNode(node);
            // After a node has been added, the graph should contain the node
            assertTrue(graph.containNode(node));
        }
    }

    /**
     * Test to check if removeNode is implemented correctly.
     */
    @Test
    public void testRemoveNode() {
        Graph graph = new Graph();

        addNodesToGraph(graph);

        // Check if removeNode actually removes the node.
        for(Node node : this.Nodes) {
            // Before remove, the graph should contain the node
            assertTrue(graph.containNode(node));
            // Successful remove operation should return True
            assertTrue(graph.removeNode(node));
            // After remove, the graph should not contain the node.
            assertFalse(graph.containNode(node));
            // Node should not be able to remove twice.
            assertFalse(graph.removeNode(node));
        }

        // check if removeNode also remove any Edges contain that node
        graph = new Graph();
        addNodesToGraph(graph);
        addEdgesToGraph(graph);

        for(Node node : this.Nodes) {
            graph.removeNode(node);
            for(Edge edge : this.Edges) {
                // TODO add code to check if edge.start.equals(node) or edge.end.equals(node)
                // TODO assertFalse(graph.containEdge(edge));
            }
        }
    }

    /**
     * Test to check if getNodes is implemented correctly.
     */
    @Test
    public void testGetNodes() {
        Graph graph = new Graph();

        addNodesToGraph(graph);

        // the set object should not be null.
        assertNotNull(graph.getNodes());
        // size should be equal
        assertSame(graph.getNodes().size(), this.Nodes.size());

        for(Node node : this.Nodes) {
            // each node should be available in the graph.
            assertTrue(graph.getNodes().contains(node));
        }
    }

    /**
     * Test to check if addEdge is implemented correctly.
     */
    @Test
    public void testAddEdge() {
        Graph graph = new Graph();

        // make sure any edge with both nodes in graph, could be added to the graph,
        // and duplicate should not be added to the graph.
        addNodesToGraph(graph);
        for (Edge edge : this.Edges) {
            // It should not contain the Edge before add.
            assertFalse(graph.containEdge(edge));

            // Successful add operation should return True.
            assertTrue(graph.addEdge(edge));

            // It should contain the Edge after add.
            assertTrue(graph.containEdge(edge));

            // Adding duplicate should not succeed.
            assertFalse(graph.addEdge(edge));
        }

        graph = new Graph();

        // make sure that any edge with neither nodes in graph, could not be added to the graph.
        for (Edge edge : this.Edges) {
            assertFalse(graph.addEdge(edge));
        }

        // make sure that any edge with only one node in graph, could not be added to the graph.
        for (Node node : this.Nodes) {
            for (Edge edge : this.Edges) {
                // TODO add code to check if either edge.start is in the graph or edge.end is in the graph
                // TODO (exclusive or), then assertFalse(graph.addEdge(edge))

            }

        }

    }

    /**
     * Test to check if containEdge is implemented correctly.
     */
    @Test
    public void testContainEdge() {
        Graph graph = new Graph();
        addNodesToGraph(graph);
        
        for(Edge edge : this.Edges) {
            // Empty graph shall not contain any edge
            assertFalse(graph.containEdge(edge));
        }
        for(Edge edge: this.Edges) {
            graph.addEdge(edge);
            // After a edge has been added, the graph should contain the edge
            assertTrue(graph.containEdge(edge));
        }
    }

    /**
     * Test to check if removeEdge is implemented correctly.
     */
    @Test
    public void testRemoveEdge() {
        Graph graph = new Graph();

        addNodesToGraph(graph);
        addEdgesToGraph(graph);

        // Check if removeEdge actually removes the edge.
        for(Edge edge : this.Edges) {
            // Before remove, the graph should contain the edge
            assertTrue(graph.containEdge(edge));
            // Successful remove operation should return True
            assertTrue(graph.removeEdge(edge));
            // After remove, the graph should not contain the edge.
            assertFalse(graph.containEdge(edge));
            // Edge should not be able to remove twice.
            assertFalse(graph.removeEdge(edge));
        }

        // Remove edge should not remove any Node.
        for(Node node : this.Nodes) {
            assertTrue(graph.containNode(node));
        }

    }

    /**
     * Test to check if getEdges is implemented correctly.
     */
    @Test
    public void testGetEdges() {
        Graph graph = new Graph();

        addNodesToGraph(graph);
        addEdgesToGraph(graph);

        // the set object should not be null.
        assertNotNull(graph.getEdges());

        // size should be equal
        assertSame(graph.getEdges().size(), this.Edges.size());

        for(Edge edge : this.Edges) {
            // Each edge should be available in the graph.
            assertTrue(graph.getEdges().contains(edge));
        }
    }

    /**
     * Test to check if findPath actually find a path.
     */
    @Test
    public void testFindPath() {
        Graph graph = new Graph();

        // empty graph should not have path.
        assertSame(graph.findPath(A, B).size() , 0);

        // If either start or end is not in the graph, the path should be empty
        graph.addNode(A);
        assertSame(graph.findPath(A, B).size() , 0);

        // If both node exist in graph, but there is not an edge between them, the path should be empty.\
        graph.addNode(B);
        assertSame(graph.findPath(A, B).size() , 0);

        // If edge and both nodes, exist, the method should return the edge.
        graph.addEdge(A_TO_A);
        graph.addEdge(A_TO_B);
        assertEquals(graph.findPath(A, B).get(0), A_TO_B);
    }

    /**
     * Test to check if findPath returns the shortest path.
     */
    @Test
    public void testFindShortestPath() {
        // create a single graph with four Nodes and find the shortest path.
        Graph graph = new Graph();
        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        Edge aToB = EdgeTest.create(A, B, 2);
        Edge bToD = EdgeTest.create(B, D, 1);
        Edge aToC = EdgeTest.create(A, C, 3);
        Edge cToD = EdgeTest.create(C, D, 4);
        graph.addEdge(aToB);
        graph.addEdge(bToD);
        graph.addEdge(aToC);
        graph.addEdge(cToD);
        List<Edge> list = graph.findPath(A, D);
        assertEquals(list.get(0), aToB);
        assertEquals(list.get(0), bToD);
    }


}
