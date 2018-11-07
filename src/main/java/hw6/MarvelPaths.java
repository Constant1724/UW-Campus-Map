package hw6;

import hw3.Graph;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;


public class MarvelPaths {

    /**
     * Fill the graph with the data in a file, whose path is filename.
     *      Note that if any data is malformed it will return null
     *
     * @spec.requires filename != null
     * @param filename the name of the datafile
     * @return a graph holding all data in filename, otherwise null.
     */
    public static @Nullable Graph loadData(String filename) {
        Graph graph = new Graph();
        Set<String> characters = new HashSet<>();
        Map<String, List<String>> books = new HashMap<>();


        try {
        MarvelParser.parseData(filename, characters, books);
        } catch (MarvelParser.MalformedDataException e) {
            System.err.println("MalFormed data in file: " + filename);
            return null;
        }

        for (String character : characters) {
            graph.addNode(graph.makeNode(character));
        }


        assert characters.size() == graph.getNodes().size(); // quick sanity check.

        for (Map.Entry<String, List<String>> entry : books.entrySet())  {
            for (String character1 : entry.getValue()) {
                for(String character2 : entry.getValue()) {

                    if (character1.equals(character2)) { // prevent self edge, since the hw spec does not say.
                        continue;
                    }
                    graph.addEdge(graph.makeEdge(graph.makeNode(character1),
                                                 graph.makeNode(character2),
                                                 entry.getKey()));

                }
            }
        }
        return graph;
    }

    /**
     * find the path in the graph from start to end. Note that this guarantees to find the path with fewest edges.
     *      If multiple path exist, this will return the lexicographically (alphabetically) least path, in terms of the
     *      node's content, which is string in this case.
     *
     *      The path will be returned in the form of a list, where the first element is the start,
     *      the second element is the next step, the third element is the next next step..
     *      all the way to the end.
     *
     *      If such a path does not exist, it will return null.
     *
     * @spec.requires graph != null and start != null and end != null
     *
     * @param graph the graph to be searched in
     * @param start the start of the path to be searched.
     * @param end   the end of the path to be searched
     * @return a list holding the path from start to end if there exists one, or null otherwise.
     */
    public static @Nullable List<Graph.Edge> findPath(Graph graph, Graph.Node start, Graph.Node end) {
        Queue<Graph.Node> queue = new LinkedList<>();
        queue.add(start);
        Map<Graph.Node, List<Graph.Edge>> mapping = new HashMap<>();
        mapping.put(start, new LinkedList<>());

        while (!queue.isEmpty()) {
            Graph.Node node = queue.poll();
            if (node.equals(end)) {
                return mapping.get(node);
            }

            // make a sorted view of currentEdges, so that alphabetically cost least path is guaranteed.
            Queue<Graph.Edge> currentEdges = new PriorityQueue<>(graph.getEdges(node).size(), (o1, o2) -> {
                if (o1.getEnd().equals(o2.getEnd())) {
                    return o1.getLabel().compareTo(o2.getLabel());
                } else {
                    return o1.getEnd().getContent().compareTo(o2.getEnd().getContent());
                }
            });
            currentEdges.addAll(graph.getEdges(node));

            for (Graph.Edge edge : currentEdges) {
                if (!mapping.containsKey(edge.getEnd())) {
                    queue.add(edge.getEnd());
                    mapping.put(edge.getEnd(), mapping.get(node));
                    mapping.get(edge.getEnd()).add(edge);
                }
            }

        }
        // return null if no path found.
        return null;
    }
}

