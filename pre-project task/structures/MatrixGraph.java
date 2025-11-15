package structures;

import java.util.*;

import exceptions.GraphException;


public class MatrixGraph<V> implements IGraph<V> {

    private static final double NO_EDGE = Double.POSITIVE_INFINITY;

    //Attributes
    private final List<Vertex<V>> vertices;
    private double[][] matrix;
    private final int maxSize;
    
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
    public void add(V value) {
        if (vertices.size() >= maxSize)
            throw new IllegalStateException("Max graph size reached.");

        vertices.add(new Vertex<>(value));
    }

    //Add an edge between two vertices
    @Override
    public void addEdge(V startValue, V endValue, double weight) throws GraphException {
        int i = indexOf(startValue);
        int j = indexOf(endValue);

        if (i == j)
            throw new GraphException("Self-loops are not allowed.");

        if (matrix[i][j] != NO_EDGE)
            throw new GraphException("Parallel edges are not allowed.");

        matrix[i][j] = weight;
    }

    //Remove an edge between two vertices
    @Override
    public void removeEdge(V startValue, V endValue) throws GraphException {
        int i = indexOf(startValue);
        int j = indexOf(endValue);

        if (matrix[i][j] == NO_EDGE)
            throw new GraphException("Edge does not exist.");

        matrix[i][j] = NO_EDGE;
    }

    //Remove a vertex
    @Override
    public void removeVertex(V value) throws GraphException {
        int idx = indexOf(value);

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
    public void printMatrix() {
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            System.out.println(Arrays.toString(Arrays.copyOf(matrix[i], n)));
        }
    }

    //This returns the index of the vertex with the given value
    private int indexOf(V value) throws GraphException {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).value.equals(value))
                return i;
        }
        throw new GraphException("Vertex not found: " + value);
    }

    public List<Vertex<V>> getVertices() {
        return vertices;
    }

    
}