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
    public void createVertex(IGraph<Stop> graph, String name, String id, double x, double y) {

        Stop s = new Stop(name, id, x, y);
        graph.add(s);

    }

    // We connect edges between vertexes
    public String connectEdges() {

        return "Edges created successfully thanks God!";
    }

}
