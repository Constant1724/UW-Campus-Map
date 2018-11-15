package hw7;

import hw3.Graph;
import hw6.MarvelParser;
import hw6.MarvelPaths;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

/**
 * This class loads a file, which is defined in the format for marvel.tsv, and create a graph with
 * data from that file.
 *
 * <p>It also provides functionality to find a shortest path between two given nodes.
 */
public class MarvelPaths2 {
    // This is NOT an ADT!!!
    // This is NOT an ADT!!!

    /** default path for the data */
    public static final String MARVEL = "src/main/java/hw6/data/marvel.tsv";

    /**
     * Standard main method. Read in data and construct a graph and allow user to type in two nodes
     * and print out a path between these two nodes.
     *
     * <p>Note that both input and output will be directed to System.in and System.out
     *
     * <p>It provide usage messages.
     *
     * @param args list of command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Loading data...");
        long start = System.currentTimeMillis();
        Graph<String, Double> graph = loadData(MARVEL);
        long times = System.currentTimeMillis() - start;
        if (graph == null) {
            System.out.println("Malformed Data Detected");
            System.exit(1);
        }
        System.out.println("Loading complete in " + times + " ms");

        Scanner reader = new Scanner(System.in, "UTF-8"); // Reading from System.in
        System.out.println("Type exit at any time to quit");
        while (true) {
            System.out.println();
            System.out.println("Please input two character name:");

            Graph<String, Double>.Node startNode = graph.makeNode(MarvelPaths.readInput(reader, "character1: "));
            Graph<String, Double>.Node endNode = graph.makeNode(MarvelPaths.readInput(reader, "character2: "));
            if (!graph.containNode(startNode)) {
                System.out.println("Character " + startNode.getContent() + " NOT FOUND!");
            } else if (!graph.containNode(endNode)) {
                System.out.println("Character " + endNode.getContent() + " NOT FOUND!");
            } else {
                List<Graph<String, Double>.Edge> path = MarvelPaths2.findPath(graph, startNode, endNode);
                if (path == null) {
                    System.out.println("no path found");
                } else {
                    for (Graph<String, Double>.Edge edge : path) {
                        System.out.println(
                                String.format(
                                        "%s to %s with weight %.3f",
                                        edge.getStart().getContent(), edge.getEnd().getContent(), edge.getLabel()));
                    }
                    System.out.println(String.format("total cost: %.3f", sumCost(path)));
                }
            }
        }
    }

    /**
     * Fill the graph with the data in a file, whose path is filename. Note that if any data is
     * malformed it will return null
     *
     * @spec.requires filename != null
     * @param filename the name of the datafile
     * @return a graph holding all data in filename, otherwise null.
     */
    public static @Nullable Graph<String, Double> loadData(String filename) {
        Graph<String, Double> graph = new Graph<>();
        Set<String> characters = new HashSet<>();
        Map<String, List<String>> books = new HashMap<>();

        try {
            MarvelParser.parseData(filename, characters, books);
        } catch (MarvelParser.MalformedDataException e) {
            System.err.println("MalFormed data in file: " + filename);
            return null;
        }

        // Fill nodes in the graph.
        for (String character : characters) {
            graph.addNode(graph.makeNode(character));
        }

        // quick sanity check.
        assert characters.size() == graph.getNodes().size();

        // construct a mapping where key is sorted tuple of any two characters,
        // and value is the number of common books their have
        Map<List<String>, Integer> counts = new HashMap<>();

        for (List<String> values: books.values()) {
            for (int i = 0; i < values.size(); i++) {
                for (int j = i + 1; j < values.size(); j++) {
                    List<String> tuple = new ArrayList<>();
                    tuple.add(values.get(i));
                    tuple.add(values.get(j));
                    Collections.sort(tuple);
                    // Make it 1 if the tuple does not exist.
                    // count + 1 if the tuple exists.
                    counts.put(tuple, counts.containsKey(tuple) ? counts.get(tuple) + 1 : 1);
                }
            }
        }

        for(Map.Entry<List<String>, Integer> entry : counts.entrySet()) {
            // Avoid infinity cost edge.
            if (entry.getValue() != 0) {
                String character1 = entry.getKey().get(0);
                String character2 = entry.getKey().get(1);
                Graph<String, Double>.Node node1 = graph.makeNode(character1);
                Graph<String, Double>.Node node2 = graph.makeNode(character2);
                double cost = 1.0 / entry.getValue();
                // Connection is bidirectional so we add two edges.
                graph.addEdge(graph.makeEdge(node1, node2, cost));
                graph.addEdge(graph.makeEdge(node2, node1, cost));
            }
        }


        return graph;
    }

    /**
     * Sum all the edges in the given lst and return the answer.
     *
     * @spec.requires lst != null
     * @param lst the list to be summed
     * @param <N> the generic type for the data type in Graph&lt;N, E&gt;.Node.//TODO check
     * @return a Double that represents the sum of all edges in the list.
     */
    public static <N extends @NonNull Object> Double sumCost(List<Graph<N, Double>.Edge> lst) {
        double count = 0.0;
        for (Graph<N, Double>.Edge edge : lst) {
            count += edge.getLabel();
        }
        return count;
    }

    /**
     * find the path in the graph from start to end. Note that this guarantees to find the path with
     * least weight. If multiple least weight path exists, it will return any of them
     *
     * <p>The path will be returned in the form of a list, where the first element is the start, the
     * second element is the next step, the third element is the next next step.. all the way to the
     * end.
     *
     * <p>If such a path does not exist, it will return null.
     *
     * @spec.requires graph != null and start != null and end != null
     * @param graph the graph to be searched in
     * @param start the start of the path to be searched.
     * @param end the end of the path to be searched
     * @param <N> the generic type for the data type in Graph&lt;N, E&gt;.Node.//TODO check
     * @return a list holding the path from start to end if there exists one, or null otherwise.
     */
    public static <N extends @NonNull Object> @Nullable List<Graph<N, Double>.Edge> findPath
    (Graph<N, Double> graph, Graph<N, Double>.Node start, Graph<N, Double>.Node end) {

        // Create a PriorityQueue with a comparator.
        // Note that since the number of edges from start to some node is << than the total number of edges in graph,
        // We may just loop through the edges and sum them up.
        // It holds path from start to some node as lists of edges. Note that the path is not necessarily the minPath.
        Queue<List<Graph<N, Double>.Edge>> active = new PriorityQueue<>(Comparator.comparing(MarvelPaths2::sumCost));

        // This set holds all Nodes whose minPath from start to the Node has been found.
        Set<Graph<N, Double>.Node> finished = new HashSet<>();

        // Add a start -> start edge with 0 cost as start point.
        List<Graph<N, Double>.Edge> startToStart = new ArrayList<>();
        startToStart.add(graph.makeEdge(start, start, 0.0));
        active.add(startToStart);

        while(!active.isEmpty()) {
            // Poll out the path with minimum cost.
            List<Graph<N, Double>.Edge> minPath = active.poll();

            // active does not allow null elements, and the while loop condition guarantees that active
            // is not empty
            // Therefore, there is no way for minPath to be null
            assert minPath != null
                    : "@AssumeAssertion(nullness): active does not allow null elements, and active is not empty";

            // Get end node of the path.
            Graph<N, Double>.Node minNode = minPath.get(minPath.size() - 1).getEnd();

            // Great! We find a min
            if (minNode.equals(end)) {
                // Note here we would like to get rid of the startTostart edge added at the very beginning.
                // Since that edge is not informative.

                // if the minPath has size 1, i.e the only edge is a self edge, return an empty list.
                // Otherwise, return the list from 1 to end.
                if (minPath.size() == 1) {
                    return new ArrayList<>();
                } else {
                    return minPath.subList(1, minPath.size());
                }
            }

            // Oops! We are exploring a node whose minPath has been found.
            if (finished.contains(minNode)) {
                continue;
            }

            // Note that the node must be in the graph ADT.
            // Because minNode is obtained from some edge in a list in in active.
            // Note that the start of any list in active is a self-edge of start node. start node must be
            // in the graph.
            // After that whenever we are adding a new list to active, we only append an edge we just
            // get out of the graph to previous list.
            // As a result, we prove by induction that any edge in a list in active must be an edge in graph.
            // The Graph ADT requires that if an edge is in the graph, both start and end must be in the graph as well.
            // Therefore, minNode must be in the graph.
            // The only exception is the self-edge of the start node,
            // but start node is required to be in the graph as well.


            // Note that we cannot write @Keyfor for node, since graph.map is private and we cannot access
            // it from outer class

            // Note that it makes sense to not write @Keyfor for the Node class. According to my design,
            // You need to first create a Node and then add the node to the graph. As a result, after you
            // create the
            // Node but before you add it to the graph, the node is not a key for the graph.
            @SuppressWarnings("incompatible")
            Set<Graph<N, Double>.Edge> edges = graph.getEdges(minNode);
            for (Graph<N, Double>.Edge edge : edges) {
                // If the end node is not finished, i.e whose minPath has not been found.
                if (!finished.contains(edge.getEnd())) {
                    // Make a shallow Copy and append current edge.
                    List<Graph<N, Double>.Edge> newPath = new ArrayList<>(minPath);
                    newPath.add(edge);
                    active.add(newPath);
                }
            }

            // Mark minNode as whose minimum path is known.
            finished.add(minNode);
        }

        // We did not find a Path.
        return null;
    }
}
