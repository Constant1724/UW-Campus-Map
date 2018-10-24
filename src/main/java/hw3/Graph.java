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
    //TODO maintains another set of edges.
    /** map represents the graph */
  private Map<Node, List<Edge>> map;

    /** edges represents all edges in the graph */
  private Set<Edge> edges;

    /** Test flag, whether to enable expensive checks. */
  private static boolean TEST_FLAG = true;
  // Abstraction Function:
  //  this.map represents an adjacency list for the graph:
  //    in which map.keys() are list of nodes and
  //             map.getKey(Node) returns all children of this Node (out-edges from this Node).
  //  map.keys()   represents all Nodes in this graph and
  //  map.values() represents all edges in this graph.

  // Representation Invariant:
  //  this.map != null
  //  forall node in map.keys()          -> node!= null
  //  forall list<edges> in map.values() -> list<edges> != null
  //  forall edge in allEdges     -> edge != null
  //
  //  allEdges.size()     == set(allEdges).size()
  //
  //  forall edge in allEdges -> map.contains(edge.start) && map.contains(edge.end)
  //
  //  forall all node-list<edge> mapping -> for all edge in node-list<edge> -> edge.start.equals(node)
  //
  //  In other words:
  //    all keys(nodes) and values(list<edges>, mapping of start to its out-edges) and all edges should not be null.
  //    duplicate edges is not allowed. (No 2 identical edges in this graph)
  //        (Note that duplicate of nodes is impossible since map does not allow duplicate keys)
  //    Both nodes of any edge should exist in this graph.
  //    For any node-list<edge> mapping, the start of any edge in list<edge> must equals node.

  /**
   * creates a new empty graph (with no Nodes or Edges).
   *
   * @spec.effects creates a new empty graph
   */
  public Graph() {
    this.map = new HashMap<Node, List<Edge>>();
    this.edges = new
    checkRep();
  }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep() {
      assert this.map != null;
      if (!TEST_FLAG) {
          return;
      }


      for (Node node : this.map.keySet()) {
          assert node != null; // No null nodes
      }

        List<Edge> checkDuplicateEdge = new LinkedList<Edge>();
      for (List<Edge> list : this.map.values()) {
          assert list != null; // no null mapping
          checkDuplicateEdge.addAll(list);
          for (Edge edge : list) {
              assert edge != null; // no null edges
          }
      }

      for(Node node : this.map.keySet()) {
          for (Edge edge : this.map.get(node)) {
              assert edge.getStart().equals(node);
              assert this.map.containsKey(edge.getStart()) && this.map.containsKey(edge.getEnd());
          }
      }
        // check duplicate edges do not exist.
      assert checkDuplicateEdge.size() == new HashSet<Edge>(checkDuplicateEdge).size();

    }
  /**
   * Return an unmodifiable view of all nodes in the graph. Note that if the graph does not contain any Nodes, it
   * will an empty set
   *
   * @return a set of all nodes in the graph.
   */
  public Set<Node> getNodes() {
      return Collections.unmodifiableSet(this.map.keySet());
  }

  /**
   * Return an unmodifiable view of all edges in the graph. Note that if the graph does not contain any Edges, it
   * will an empty set
   *
   * @return a set of all edges in the graph.
   */
  public Set<Edge> getEdges() {
      Set<Edge> set = new HashSet<Edge>();

      for (List<Edge> list : this.map.values()) {
          set.addAll(list);
      }
      return set;
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
    throw new RuntimeException("Graph->addNode() is not yet implemented");
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
    throw new RuntimeException("Graph->addEdge() is not yet implemented");
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
    throw new RuntimeException("Graph->removeEdge() is not yet implemented");
  }

  /**
   * Find a path consists of edges connecting start and end, with the lowest cost. If multiple
   * shortest paths exist, this method may return any one.
   *
   * <p>If there does not exist a path from start to end, it will return an empty list. start and
   * end should not be the same node.
   *
   * @spec.requires start != Null and end != Null and !start.equals(end)
   * @param start the start of the path
   * @param end the end of the path
   * @return a list of edges in order such that start and end is connected by going through the
   *     first, second, third, ..., last edge in the list.
   */
  public List<Edge> findPath(Node start, Node end) {
    throw new RuntimeException("Graph->findPath() is not yet implemented");
  }
}
