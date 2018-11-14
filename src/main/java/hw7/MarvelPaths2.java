package hw7;

import hw3.Graph;
import hw6.MarvelParser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

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

            Graph<String, Double>.Node startNode = graph.makeNode(readInput(reader, "character1: "));
            Graph<String, Double>.Node endNode = graph.makeNode(readInput(reader, "character2: "));
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
     * Helper method to help print out prompts and parse user input and return it.
     *
     * @param reader a reader that reads in user input
     * @param prompt the prompt message, will be printed out before user type in anything.
     * @return a correctly formatted user input. specifically, all quotation marks will be excluded.
     */
    private static String readInput(Scanner reader, String prompt) {
        System.out.print(prompt);
        String character = reader.nextLine().replaceAll("\"", "");
        if (character.equals("exit")) {
            System.exit(0);
        }
        return character;
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

        Map<List<String>, Integer> counts = new HashMap<>();

        for (List<String> values: books.values()) {
            for (int i = 0; i < values.size(); i++) {
                for (int j = i + 1; j < values.size(); j++) {
                    List<String> tuple = new ArrayList<>();
                    tuple.add(values.get(i));
                    tuple.add(values.get(j));
                    Collections.sort(tuple);
                    counts.put(tuple, counts.keySet().contains(tuple) ? counts.get(tuple) + 1 : 0);
                }
            }
        }

        for(Map.Entry<List<String>, Integer> entry : counts.entrySet()) {
            String character1 = entry.getKey().get(0);
            String character2 = entry.getKey().get(1);

            graph.addEdge(graph.makeEdge(graph.makeNode(character1), graph.makeNode(character2), 1.0 / entry.getValue()));
            graph.addEdge(graph.makeEdge(graph.makeNode(character2), graph.makeNode(character1), 1.0 / entry.getValue()));
        }


        return graph;
    }

    /**
     * Sum all the edges in the given lst and return the answer.
     *
     * @spec.requires lst != null
     * @param lst the list to be summed
     * @param <N> the generic type for Graph.//TODO check
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
     * @return a list holding the path from start to end if there exists one, or null otherwise.
     */
    public static <N extends @NonNull Object> @Nullable List<Graph<N, Double>.Edge> findPath(Graph<N, Double> graph, Graph<N, Double>.Node start, Graph<N, Double>.Node end) {

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

            // The queue does not allow null elements, and the while loop condition guarantees that queue
            // is not empty
            // Therefore, there is no way for minPath to be null
            assert minPath != null
                    : "@AssumeAssertion(nullness): queue does not allow null elements, and queue is not empty";

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
                    return minPath.subList(1, minPath.size() - 1);
                }
            }

            // Oops! We are exploring a node whose minPath has been found.
            if (finished.contains(minNode)) {
                continue;
            }

            // TODO: add suppressing.
            for (Graph<N, Double>.Edge edge : graph.getEdges(minNode)) {
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
