package org.example.structures;

import org.example.exceptions.GraphException;
import org.example.model.Stop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MatrixGraph implements IGraph<Stop> {

    private static final double NO_EDGE = Double.POSITIVE_INFINITY;

    //Attributes
    private final List<Stop> vertices;
    private double[][] matrix;
    private int maxSize;
    
    //Constructor
    public MatrixGraph(int maxSize) {
        this.maxSize = maxSize;
        this.vertices = new ArrayList<>();
        this.matrix = new double[maxSize][maxSize];

        for (int i = 0; i < maxSize; i++)
            Arrays.fill(matrix[i], NO_EDGE);
    }

    //Add a vertex
    @Override
    public void add(Stop value) {
        if (vertices.size() >= maxSize)
            throw new IllegalStateException("Max graph size reached.");

        vertices.add(value);
    }

    //Add an edge between two vertices
    @Override
    public void addEdge(String stop1Id, String stop2Id, double weight) throws GraphException {
        int i = findStopIndexById(stop1Id);
        int j = findStopIndexById(stop2Id);

        if (i == j)
            throw new GraphException("Self-loops are not allowed.");

        if (matrix[i][j] != NO_EDGE)
            throw new GraphException("Parallel edges are not allowed.");

        matrix[i][j] = weight;
    }

    //Remove an edge between two vertices
    @Override
    public void removeEdge(String Stop1Id, String Stop2Id) throws GraphException {
        int i = findStopIndexById(Stop1Id);
        int j = findStopIndexById(Stop2Id);

        if (matrix[i][j] == NO_EDGE)
            throw new GraphException("Edge does not exist.");

        matrix[i][j] = NO_EDGE;
    }

    //Remove a vertex
    @Override
    public void removeVertex(String StopId) throws GraphException {
        int idx = findStopIndexById(StopId);

        vertices.remove(idx);

        // Shift rows upward
        for (int r = idx; r < vertices.size(); r++) {
            matrix[r] = matrix[r + 1];
        }

        // Shift columns left
        for (int r = 0; r < vertices.size() + 1; r++) {
            for (int c = idx; c < vertices.size(); c++) {
                matrix[r][c] = matrix[r][c + 1];
            }
        }

        // Cleanup last row and column
        int n = vertices.size();
        for (int i = 0; i < maxSize; i++) {
            matrix[n][i] = NO_EDGE;
            matrix[i][n] = NO_EDGE;
        }
    }

    @Override
    public String printMatrix() {
        StringBuilder text = new StringBuilder();
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            text.append(Arrays.toString(Arrays.copyOf(matrix[i], n)));
        }
        return text.toString();
    }

    //This returns the index of the vertex with the given value
    private int indexOf(Stop value) throws GraphException {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).equals(value))
                return i;
        }
        throw new GraphException("Vertex not found: " + value);
    }

    public List<Stop> getVertices() {
        return vertices;
    }

    public int findStopIndexById(String id) throws GraphException {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new GraphException("Stop with ID " + id + " not found.");
    }

    public Stop findById(String id) {
        return vertices.get(1);
    }
    
}