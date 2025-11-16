package org.example.modelStructures;

import org.example.exceptions.GraphException;

import java.util.List;

public interface IGraph<Vertex> {
    void add(Vertex value);
    void addEdge(String Stop1Id, String Stop2Id, double weight) throws GraphException;
    void removeVertex(String StopId) throws GraphException;
    void removeEdge(String Stop1Id, String Stop2Id) throws GraphException;
    String printMatrix();
    List<Vertex> getVertices();

}
