package hw3;

import java.util.List;
import java.util.Set;

/**
 * Graph represents a mutable, directed and cyclic graph. Duplicate of nodes and edges in graph is
 * not allowed.
 *
 * Client can add, remove or view any nodes or edges.
 * Note that if a node is removed, any edges contain that node will be removed.
 *
 * Client may also find the path from one node to another.
 *
 * Specification field:
 * @spec.specified Nodes : a set of Nodes // Represent all Nodes in this Graph.
 * @spec.specified Edges : a set of Edges // Represent all Edges in this Graph.
 *
 * Abstract Invariant:
 *  Equality means two Edges are equal iff they have the same set of Nodes and Edges.
 *  The two nodes of any Edge must be in Graph.Nodes.
 */
public class Graph {

    /**
     * creates a new empty graph (with no Nodes or Edges).
     *
     * @spec.effects creates a new empty graph
     */
    public Graph() {
        throw new RuntimeException("Graph->constructor() is not yet implemented");
    }

    /**
     * Return a view of all nodes in the graph.
     * Note that if the graph does not contain any Nodes, it will an empty set
     *
     * @return a set of all nodes in the graph.
     */
    public Set<Node> getNodes() {
        throw new RuntimeException("Graph->getNodes() is not yet implemented");
    }

    /**
     * Return a view of all edges in the graph.
     * Note that if the graph does not contain any Edges, it will an empty set
     *
     * @return a set of all edges in the graph.
     */
    public Set<Edge> getEdges() {
        throw new RuntimeException("Graph->getEdges() is not yet implemented");
    }

    /**
     * return True if the given node is already in the Graph.
     *
     * @spec.requires node != Null
     * @param node to be checked if in the graph
     * @return True iff node is in graph
     */
    public boolean containNode(Node node) {
        throw new RuntimeException("Graph->containNode() is not yet implemented");
    }

    /**
     * Add the node to the graph, if the node is not in the graph.
     * If the node is already in the graph, modify nothing.
     *
     * @spec.requires node != Null
     * @spec.modifies this.Nodes if the graph does not contain the node.
     * @spec.effect if this.Nodes = S, this_post.Nodes = S ∪ node, iff node is not in S.
     *
     * @param node to be added to the graph
     * @return True iff node is not in graph, False otherwise.
     */
    public boolean addNode(Node node) {
        throw new RuntimeException("Graph->addNode() is not yet implemented");
    }

    /**
     * Remove the node from the graph, if the node is in the graph.
     * Note that this will also remove any edge that contain the node.
     *
     * If the node is not in the graph, modify nothing.
     *
     *
     * @spec.requires node != Null
     * @spec.modifies this.Nodes if the node is in the graph.
     *                this.Edges if any edge contain the node.
     * @spec.effect if this.Nodes = S, this_post.Nodes = S - node, iff node is in S.
     *              if this.Edges = E, this_post.Edges = E - any edges that contain the node,
     *                  iff any edge contain the node
     *
     * @param node to be removed from the graph
     * @return True iff node is in graph, False otherwise.
     */
    public boolean removeNode(Node node) {
        throw new RuntimeException("Graph->removeNode() is not yet implemented");
    }

    /**
     * return True if the given edge is already in the Graph.
     *
     * @spec.requires edge != Null
     * @param edge to be checked if in the graph
     * @return True iff edge is in graph
     */
    public boolean containEdge(Edge edge) {
        throw new RuntimeException("Graph->containEdge() is not yet implemented");
    }

    /**
     * Add the edge to the graph.
     *
     * If the edge is already in the graph, or edge.start/edge.end is not in the graph, modify nothing.
     *
     *
     * @spec.requires edge != Null
     * @spec.modifies this.Edges iff edge is not in the graph
     *  && graph.containNode(edge.start) && graph.containNode(edge.end)
     * @spec.effect if this.Edges = E, this_post.Edges = E ∪ edge, iff edge is not in E.
     *
     * @param edge to be added to the graph
     * @return True iff edge is not in graph, False otherwise.
     */
    public boolean addEdge(Edge edge) {
        throw new RuntimeException("Graph->addEdge() is not yet implemented");
    }
    
    /**
     * Remove the edge in the graph, if the edge is in the graph.
     * If the edge is not in the graph, modify nothing
     *
     * @spec.requires edge != Null
     * @spec.modifies this.Edges if the edge is in the graph
     * @spec.effect if this.Edges = E, this_post.Edges = E - edge, iff edge is in E.
     *
     * @param edge to be removed from the graph
     * @return True iff edge is in graph, False otherwise.
     */
    public boolean removeEdge(Edge edge) {
        throw new RuntimeException("Graph->removeEdge() is not yet implemented");
    }

    /**
     * Find a path consists of edges connecting start and end, with the lowest cost.
     * If multiple shortest paths exist, this method may return any one.
     *
     * If there does not exist a path from start to end, it will return an empty list.
     * start and end should not be the same node.
     *
     * @spec.requires start != Null && end != Null && !start.equals(end)
     *
     * @param start the start of the path
     * @param end the end of the path
     * @return a list of edges in order such that start and end is connected by going through the first, second,
     * third, ..., last edge in the list.
     */
    public List<Edge> findPath(Node start, Node end) {
        throw new RuntimeException("Graph->findPath() is not yet implemented");
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        throw new RuntimeException("Edge->hashCode() is not yet implemented");
    }

    /**
     * Standard equality operation.
     *
     * @param obj The object to be compared for equality.
     * @return true if and only if 'this' and 'obj' represent the same Graph.
     */
    @Override
    public boolean equals(Object obj) {
        throw new RuntimeException("Edge->equals() is not yet implemented");
    }


}
