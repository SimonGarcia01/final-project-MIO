package org.example.control;

import org.example.exceptions.GraphException;
import org.example.modelStructures.IGraph;
import org.example.modelStructures.GraphImpl;
import org.example.modelStructures.Vertex;

public class GraphController {

    // Constructor
    public GraphController() {

    }

    // Create graph
    public IGraph<Vertex> createGraph(int maxSize) {
        IGraph<Vertex> graph = new GraphImpl(maxSize);
        return graph;
    }

    // We read stops.csv file
    public void createAndAddVertex(IGraph<Vertex> graph, String name, String id, double x, double y, String lineId, int orientation, int variant, int stopSequence) {

        Vertex s = new Vertex(name, id, x, y, lineId, orientation, variant, stopSequence);
        graph.add(s);

    }

    public void createAndAddVertex(IGraph<Vertex> graph, String name, String id, double x, double y) {

        Vertex s = new Vertex(name, id, x, y);
        graph.add(s);

    }

    public Vertex createVertex(String name) {

        Vertex s = new Vertex(name);
        return s;

    }

    // We connect edges between vertexes
    public void connectEdge(IGraph<Vertex> graph, String lineId, String stop1Id, String stop2Id, String orientation, String variant, String stopSequence, double weight) throws GraphException {
        graph.addEdge(lineId, stop1Id, stop2Id, orientation, variant, stopSequence, weight);
    }

}
