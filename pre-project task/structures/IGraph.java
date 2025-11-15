package structures;

import java.util.List;

import exceptions.GraphException;
import model.Stop;

public interface IGraph<Stop> {
    void add(Stop value);
    void addEdge(String Stop1Id, String Stop2Id, double weight) throws GraphException;
    void removeVertex(String StopId) throws GraphException;
    void removeEdge(String Stop1Id, String Stop2Id) throws GraphException;
    void printMatrix();
    public List<Stop> getVertices();
}
