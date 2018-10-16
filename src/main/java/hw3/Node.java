package hw3;

/**
 * Node represent a immutable vertex in a graph.
 *
 * It may hold information about the vertex.
 *
 * Note that Node.content refers to the info of this vertex.
 *      For HW3, it will only be a constant.
 */
public class Node {
    public final String content;

    /**
     * @spec.requires content != Null
     * @spec.effects creates a new Node with content as its information.
     *
     * @param content the information of this node
     */
    public Node(String content) {
        this.content = content;
    }
}
