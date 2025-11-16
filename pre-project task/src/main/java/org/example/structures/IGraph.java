package org.example.structures;

import org.example.exceptions.GraphException;

import java.util.List;

public interface IGraph<Stop> {
    void add(Stop value);
    void addEdge(String Stop1Id, String Stop2Id, double weight) throws GraphException;
    void removeVertex(String StopId) throws GraphException;
    void removeEdge(String Stop1Id, String Stop2Id) throws GraphException;
    String printMatrix();
    public List<Stop> getVertices();
}
