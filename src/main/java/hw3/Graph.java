package hw3;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

import java.util.*;

/**
 * Graph represents a mutable, directed and cyclic graph. Duplicate of nodes or edges in graph is
 * not allowed.
 *
 * <p>Client can add, remove or view any nodes or edges. Note that if a node is removed, any edges
 * contain that node will be removed.
 *
 * <p>Client may also find the path from one node to another.
 *
 * <p>Node and Edge are helper class and represent a node and a edge in the graph, respectively.
 *
 * <p>Specification field:
 *
 * @spec.specfield Nodes : a set of Nodes // Represent all Nodes in this Graph.
 * @spec.specfield Edges : a set of Edges // Represent all Edges in this Graph.
 *     <p>Abstract Invariant: The two nodes of any Edge in Graph.Edges must be in Graph.Nodes.
 *
 *
 * @param <N> represents the type of data to be stored in single Node in the graph. N should be immutable
 * @param <E> represents the type of data ot be stored in single Edge in the graph. E should be immutable
 */
public class Graph<N extends @NonNull Object, E extends @NonNull Object> {
  /** map represents the graph */
  private final Map<Node, Set<Edge>> map;

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
    this.map = new HashMap<>();
    checkRep();
  }

  /** Checks that the representation invariant holds (if any). */
  @SideEffectFree
  private void checkRep(@UnknownInitialization(Graph.class) Graph<N, E> this) {
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
  @Pure
  public Set<@KeyFor("map") Node> getNodes() {
    checkRep();
    Set<@KeyFor("map") Node> result = Collections.unmodifiableSet(this.map.keySet());
    checkRep();
    return result;
  }

  /**
   * Return an unmodifiable view of all out-edges in the graph for a given node. Note that if the
   * node does not have any Edges, it will return an empty set
   *
   * @spec.requires node != null and map.containsKey(node)
   * @param node the node to be searched to return all out-edges.
   * @return a set of all out-edges in the graph for the node.
   */
  @Pure
  public Set<Edge> getEdges(@KeyFor("map") Node node) {
    checkRep();
    Set<Edge> result = Collections.unmodifiableSet(map.get(node));
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
  @Pure
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
      for (Iterator<Edge> it = set.iterator(); it.hasNext(); ) {
        Edge edge = it.next();
        if (edge.getEnd().equals(node)) {
          it.remove();
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
  @Pure
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
    if (!this.map.containsKey(edge.getStart()) || !this.map.containsKey(edge.getEnd())) {
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
    if (this.map.containsKey(edge.getStart())) {
      result = this.map.get(edge.getStart()).remove(edge);
    }
    checkRep();
    return result;
  }
  /**
   * create a new Node with content.
   *
   * @param content the content of the Node.
   * @spec.requires content != Null
   * @return a new instance of Node
   */
  public Node makeNode(N content) {
    return new Node(content);
  }
  /**
   * create a new Edge with start, end and label
   *
   * @param start the start of this Edge.
   * @param end the end of this Edge.
   * @param label the label of this Edge.
   * @spec.requires start != null and end != null and label != null
   * @return a new instance of Edge
   */
  public Edge makeEdge(Node start, Node end, E label) {
    return new Edge(start, end, label);
  }

  /**
   * Node represent a immutable vertex in a graph.
   *
   * <p>It holds the description about this Node.
   *
   * <p>Specification fields:
   *
   * @spec.specfield content : String // The description about this Node.
   *     <p>Abstract Invariant: Any Node should have a unique, non Null description. Equality means
   *     two Nodes are equal iff they have the same content.
   */
  public class Node {
    /** the description(content) of this node */
    private N content;

    // Abstraction Function:
    // content is the description of this Node. It is also a unique identifier of the Node.

    // Representation Invariant:
    //      content != Null (Node should always have valid content)

    /**
     * @param content the content of the Node.
     * @spec.requires content != Null
     * @spec.effects creates a new Node with the given content as description.
     */
    private Node(N content) {
      this.content = content;
      checkRep();
    }

    /** Checks that the representation invariant holds (if any). */
    @SideEffectFree
    private void checkRep(@UnknownInitialization(Graph.Node.class) Node this) {
      assert (content != null);
    }

    /**
     * return the content of the Node
     *
     * @return content of the Node
     */
    @Pure
    public N getContent() {
      return this.content;
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    @Pure
    public int hashCode() {
      return this.content.hashCode();
    }

    /**
     * Standard equality operation.
     *
     * @param obj The object to be compared for equality.
     * @return true if and only if 'this' and 'obj' represent the same Node.
     */
    @Override
    @Pure
    public boolean equals(@Nullable Object obj) {
      if (!(obj instanceof Graph<?,?>.Node)) {
        return false;
      }

      return this.content.equals(((Graph<?,?>.Node) obj).content);
    }
  }

  /**
   * Edge represents an immutable, directed, weighted edge in a graph
   *
   * <p>Specification fields:
   *
   * @spec.specfield start : Node // The start node of this edge.
   * @spec.specfield end : Node // The end node of this edge.
   * @spec.specfield label : String // The label of this edge.
   *     <p>Abstract Invariant: All edges should have a start and an end, with a label. Equality
   *     means two Edges are equal iff they have the same start, end and cost.
   */
  public class Edge {
    /** the start of this edge */
    private Node start;
    /** the end of this edge */
    private Node end;
    /** the label of this edge */
    private E label;

    // Abstraction Function:
    //  An edge is defined with this.start  as its starting Node
    //                      and this.end    as its ending Node
    //                      and this.label  as its label.

    // Representation Invariant:
    //      start != null && end != null && label != null (All fields should be valid)

    /**
     * @param start the start of this Edge.
     * @param end the end of this Edge.
     * @param label the label of this Edge.
     * @spec.requires start != null and end != null and label != null
     * @spec.effects creates a new edge from start to end with label.
     */
    private Edge(Node start, Node end, E label) {
      this.start = start;
      this.end = end;
      this.label = label;
      checkRep();

    }

    /** Checks that the representation invariant holds (if any). */
    @SideEffectFree
    private void checkRep(@UnknownInitialization(Graph.Edge.class) Edge this) {
      assert (this.start != null && this.end != null && this.label != null);
    }

    /**
     * get the start Node of this Edge
     *
     * @return the start Node of this Edge
     */
    @Pure
    public Node getStart() {
      return this.start;
    }

    /**
     * get the end Node of this Edge
     *
     * @return the end Node of this Edge
     */
    @Pure
    public Node getEnd() {
      return this.end;
    }

    /**
     * get the label of this Edge
     *
     * @return the label of this Edge
     */
    @Pure
    public E getLabel() {
      return this.label;
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    @Pure
    public int hashCode() {
      return Objects.hash(this.start, this.end, this.label);
    }

    /**
     * Standard equality operation.
     *
     * @param obj The object to be compared for equality.
     * @return true if and only if 'this' and 'obj' represent the same Edge.
     */
    @Override
    @Pure
    public boolean equals(@Nullable Object obj) {
      if (!(obj instanceof Graph<?,?>.Edge)) {
        return false;
      }
      Graph<?,?>.Edge other = (Graph<?,?>.Edge) obj;

      return this.start.equals(other.getStart())
          && this.end.equals(other.getEnd())
          && this.label.equals(other.label);
    }
  }
}
