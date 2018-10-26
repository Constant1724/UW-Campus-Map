package hw3;

import java.util.*;

/**
 * Graph represents a mutable, directed and cyclic graph. Duplicate of nodes and edges in graph is
 * not allowed.
 *
 * <p>Client can add, remove or view any nodes or edges. Note that if a node is removed, any edges
 * contain that node will be removed.
 *
 * <p>Client may also find the path from one node to another.
 *
 * <p>Specification field:
 *
 * @spec.specfield Nodes : a set of Nodes // Represent all Nodes in this Graph.
 * @spec.specfield Edges : a set of Edges // Represent all Edges in this Graph.
 *     <p>Abstract Invariant: The two nodes of any Edge in Graph.Edges must be in Graph.Nodes.
 */
public class Graph {
  /** map represents the graph */
  private Map<Node, Set<Edge>> map;

  /** Test flag, whether to enable expensive checks. */
  private static boolean TEST_FLAG = false;
  // Abstraction Function:
  //
  //  this.map represents an adjacency list for the graph:
  //    in which map.keys() are list of nodes and
  //             map.getKey(Node) returns all children of the Node (out-edges from the Node).
  //
  //  map.keys()   represents all Nodes in this graphs.
  //  map.get(key) represents a set of Edges with key as start node.
  //

  // Representation Invariant:
  //  this.map != null
  //  forall node in map.keys()                   -> node!= null
  //  forall list<edges> in map.values()          -> list<edges> != null
  //  forall edge in map.get(key) for all key     -> edge != null
  //
  //  forall edge in map.get(key) for all key ->  map.contains(edge.end) && edge.start.equals(key)
  //
  //
  //  In other words:
  //    all keys(nodes) and values(list<edges>, mapping of start to its out-edges) and all edges
  // should not be null.
  //        (Note that duplicate of nodes is impossible since map does not allow duplicate keys)
  //        (Note that duplicate of edges is impossible since any node maps to a set, which does not
  // allow duplicates.)
  //
  //    Foreach node-Set<edge> mapping relationship in this.map, the start of any edge in Set<Edge>
  // must equals node,
  //          and the end of any edge in Set<Edge> must be in the graph.

  /**
   * creates a new empty graph (with no Nodes or Edges).
   *
   * @spec.effects creates a new empty graph
   */
  public Graph() {
    this.map = new HashMap<Node, Set<Edge>>();
    checkRep();
  }

  /** Checks that the representation invariant holds (if any). */
  private void checkRep() {
    assert this.map != null;
    if (!TEST_FLAG) {
      return;
    }
    for (Node node : this.map.keySet()) {
      assert node != null; // No null keys in map
      assert this.map.get(node) != null; // No null values in map
      for (Edge edge : this.map.get(node)) {
        assert edge != null; // No null edges in graph
        assert edge.getStart().equals(node); // All edges in one set must have the same start node.
        assert this.map.containsKey(
            edge.getEnd()); // The end node of all edges must be in the graph.
      }
    }
  }
  /**
   * Return an unmodifiable view of all nodes in the graph. Note that if the graph does not contain
   * any Nodes, it will an empty set
   *
   * @return a set of all nodes in the graph.
   */
  public Set<Node> getNodes() {
    checkRep();
    Set<Node> result = Collections.unmodifiableSet(this.map.keySet());
    checkRep();
    return result;
  }

  /**
   * Return an unmodifiable view of all edges in the graph. Note that if the graph does not contain
   * any Edges, it will an empty set
   *
   * @return a set of all edges in the graph.
   */
  public Set<Edge> getEdges() {
    checkRep();

    Set<Edge> result = new HashSet<Edge>();
    for (Set<Edge> set : this.map.values()) {
      result.addAll(set);
    }
    result = Collections.unmodifiableSet(result);
    checkRep();
    return result;
  }

  /**
   * return True if the given node is already in the Graph.
   *
   * @spec.requires node != Null
   * @param node to be checked if in the graph
   * @return True iff node is in graph
   */
  public boolean containNode(Node node) {
    checkRep();
    boolean result = this.map.containsKey(node);
    checkRep();
    return result;
  }

  /**
   * Add the node to the graph, if the node is not in the graph. If the node is already in the
   * graph, modify nothing.
   *
   * @spec.requires node != Null
   * @spec.modifies this.Nodes if the graph does not contain the node.
   * @spec.effects if this.Nodes = S, this_post.Nodes = S ∪ node, iff node is not in S.
   * @param node to be added to the graph
   * @return True iff node is not in graph, False otherwise.
   */
  public boolean addNode(Node node) {
    checkRep();
    if (this.map.containsKey(node)) {
      checkRep();
      return false;
    }
    this.map.put(node, new HashSet<Edge>());
    checkRep();
    return true;
  }

  /**
   * Remove the node from the graph, if the node is in the graph. Note that this will also remove
   * any edge that contain the node.
   *
   * <p>If the node is not in the graph, modify nothing.
   *
   * @spec.requires node != Null
   * @spec.modifies this.Nodes if the node is in the graph. this.Edges if any edge contain the node.
   * @spec.effects if this.Nodes = S, this_post.Nodes = S - node, iff node is in S. if this.Edges =
   *     E, this_post.Edges = E - any edges that contain the node, iff any edge contain the node
   * @param node to be removed from the graph
   * @return True iff node is in graph, False otherwise.
   */
  public boolean removeNode(Node node) {
    checkRep();
    if (!this.map.containsKey(node)) {
      checkRep();
      return false;
    }
    this.map.remove(node);

    for (Set<Edge> set : this.map.values()) {
      Iterator<Edge> iterator = set.iterator();
      while (iterator.hasNext()) {
        Edge edge = iterator.next();
        if (edge.getEnd().equals(node)) {
          iterator.remove();
        }
      }
    }
    checkRep();
    return true;
  }

  /**
   * return True if the given edge is already in the Graph.
   *
   * @spec.requires edge != Null
   * @param edge to be checked if in the graph
   * @return True iff edge is in graph
   */
  public boolean containEdge(Edge edge) {
    checkRep();
    boolean result = false;
    if (this.map.containsKey(edge.getStart())) {
      result = this.map.get(edge.getStart()).contains(edge);
    }
    checkRep();
    return result;
  }

  /**
   * Add the edge to the graph.
   *
   * <p>If the edge is already in the graph, or edge.start/edge.end is not in the graph, modify
   * nothing.
   *
   * @spec.requires edge != Null
   * @spec.modifies this.Edges iff edge is not in the graph and graph.containNode(edge.start) and
   *     graph.containNode(edge.end)
   * @spec.effects if this.Edges = E, this_post.Edges = E ∪ edge, iff edge is not in E.
   * @param edge to be added to the graph
   * @return True iff edge is not in graph, False otherwise.
   */
  public boolean addEdge(Edge edge) {
    checkRep();
    Node start = edge.getStart();
    Node end = edge.getEnd();
    if (!this.containNode(start) || !this.containNode(end) || this.containEdge(edge)) {
      checkRep();
      return false;
    }
    boolean result = this.map.get(edge.getStart()).add(edge);
    checkRep();
    return result;
  }

  /**
   * Remove the edge in the graph, if the edge is in the graph. If the edge is not in the graph,
   * modify nothing
   *
   * @spec.requires edge != Null
   * @spec.modifies this.Edges if the edge is in the graph
   * @spec.effects if this.Edges = E, this_post.Edges = E - edge, iff edge is in E.
   * @param edge to be removed from the graph
   * @return True iff edge is in graph, False otherwise.
   */
  public boolean removeEdge(Edge edge) {
    checkRep();
    boolean result = false;
    if (this.containEdge(edge)) {
      result = this.map.get(edge.getStart()).remove(edge);
    }
    checkRep();
    return result;
  }
}
