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
    public void createAndAddVertex(IGraph<Vertex> graph, String name, String stopId, double x, double y) {

        Vertex s = new Vertex(name, stopId, x, y);
        graph.add(s);

    }
    
    public Vertex createVertex(String name) {

        Vertex s = new Vertex(name);
        return s;

    }

    // We connect edges between vertexes
    public void connectEdge(IGraph<Vertex> graph, String lineId, String stop1Id, String orientation, String variant, String stopSequence, double weight) throws GraphException {
        graph.addEdge(lineId, stop1Id, orientation, variant, stopSequence, weight);
    }

    // We create lines for later assign the name associated to a lineId
    public void createLine(IGraph<Vertex> graph, String lineId, String lineName) {
        graph.createLine(lineId, lineName);
    }

    public void group(IGraph<Vertex> graph) {
        graph.group();
    }

    public void createAdjacencyMatrix(IGraph<Vertex> graph, String stop1Id, String stop2Id) throws GraphException {
        graph.createAdjacencyMatrix(stop1Id, stop2Id);
    }
}
