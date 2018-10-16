package hw3;

/**
 * Edge represents a immutable edge in a graph that consists of a start Node and an end Node.
 * Note that the edge will also have a cost, which is the cost from start Node to end Node.
 */
public class Edge {
    public final Node start;
    public final Node end;
    public final int cost;

    /**
     * @spec.requires start != Null && end != Null && cost >= 0
     * @spec.effecs creates a new edge from start to end with cost.
     *
     * @param start start of the edge
     * @param end end of the edge
     * @param cost cost of the edge
     */
    public Edge(Node start, Node end, int cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

}
