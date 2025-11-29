package org.example.modelStructures;

import org.example.exceptions.GraphException;

import java.util.HashMap;
import java.util.List;

public interface IGraph<Vertex> {
    void add(Vertex value);
    void addEdge(String lineId, String stop1Id, String orientation, String variant, String stopSequence, double weight) throws GraphException;
    void removeVertex(String StopId) throws GraphException;
    void removeEdge(String Stop1Id, String Stop2Id) throws GraphException;
    String printMatrix();
    List<Vertex> getVertices();
    void setVertexes(List<Vertex> vertexes);
    String getEdges();
    public void createLine(String lineId, String lineName);
    public void group();
}
