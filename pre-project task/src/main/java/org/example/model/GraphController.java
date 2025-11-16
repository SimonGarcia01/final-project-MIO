package org.example.model;

import org.example.structures.IGraph;
import org.example.structures.MatrixGraph;

public class GraphController {

    // Constructor
    public GraphController() {

    }

    // Create graph
    public IGraph<Stop> createGraph(int maxSize) {
        IGraph<Stop> graph = new MatrixGraph(maxSize);
        return graph;
    }

    // We read stops.csv file
    public void createAndAddVertex(IGraph<Stop> graph, String name, String lineId, String id, double x, double y) {

        Stop s = new Stop(name, lineId, id, x, y);
        graph.add(s);

    }

    public void createAndAddVertex(IGraph<Stop> graph, String name, String id, double x, double y) {

        Stop s = new Stop(name, id, x, y);
        graph.add(s);

    }

    public Stop createVertex(String name) {

        Stop s = new Stop(name);
        return s;

    }

    // We connect edges between vertexes
    public String connectEdge(String stop1Id, String stop2Id, double weight) {

        return "Edges created successfully thanks God!";
    }

}
