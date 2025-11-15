package structures;

import exceptions.GraphException;

public interface IGraph<V> {
    void add(V value);
    void addEdge(V startValue, V endValue, double weight) throws GraphException;
    void removeVertex(V value) throws GraphException;
    void removeEdge(V startValue, V endValue) throws GraphException;
}
