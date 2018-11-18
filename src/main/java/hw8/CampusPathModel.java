package hw8;

import hw3.Graph;

import java.util.List;

public class CampusPathModel {

    private static Graph<Coordinates, Double> loadData(String pathFileName, String buildingFileName) {
        Graph<Coordinates, Double> graph = new Graph<>();

        List<CampusPath> edges = DataParser.parsePathData(pathFileName);

        // Since we just want to add all edges to the graph, it implies that both nodes of all edges must be in
        // the graph. As a result, if either start or end of an edge is not in the graph, we simply
        // add it to the graph.
        for (CampusPath edge : edges) {

            Graph<Coordinates, Double>.Node start = graph.makeNode(edge.getOrigin());
            Graph<Coordinates, Double>.Node end = graph.makeNode(edge.getDestination());
            Double cost = edge.getDistance();

            boolean result = true;

            if(!graph.containNode(start)) {
                result = graph.addNode(start) && result;
            }
            if(!graph.containNode(end)) {
                result = graph.addNode(end) && result;
            }
            result = graph.addEdge(graph.makeEdge(start, end, cost)) && result;

            // Quick Sanity check
            assert result;
        }

        // TODO add building
        return graph;
    }
}
