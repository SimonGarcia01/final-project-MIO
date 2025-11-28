package org.example.modelStructures;

import org.example.exceptions.GraphException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class MatrixGraph implements IGraph<Vertex> {

    private static final double NO_EDGE = Double.POSITIVE_INFINITY;
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    //Attributes
    private List<Vertex> vertices;
    private List<String> edges;
    private double[][] matrix;
    private int maxSize;
    
    //Constructor
    public MatrixGraph(int maxSize) {
        this.maxSize = maxSize;
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.matrix = new double[maxSize][maxSize];

        for (int i = 0; i < maxSize; i++)
            Arrays.fill(matrix[i], 0.0);
    }

    //Add a vertex
    @Override
    public void add(Vertex value) {
        if (vertices.size() >= maxSize)
            throw new IllegalStateException("Max graph size reached.");

        vertices.add(value);
    }

    @Override
    public void setVertexes(List<Vertex> vertexes) {
        this.vertices = vertexes;
    }

    @Override
    public List<String> getEdges() {
        return edges;
    }


    // Auxiliar method, returns true if are distinct and false if they aren't
    public boolean distinctValidation(String line1, String line2) {

        if(!line1.equals(line2)) {
            return true;
        }
        else {
            return false;
        }

    }

    //Add an edge between two vertices
    @Override
    public void addEdge(String lineId, String stop1Id, String stop2Id, double weight) throws GraphException {
        int i = findStopIndexById(stop1Id);
        //System.out.println(i);
        int j = findStopIndexById(stop2Id);

        /*
        if (i == j)
            throw new GraphException("Self-loops are not allowed.");
        */
        /*
        if (matrix[i][j] != 0.0)
            throw new GraphException("Parallel edges are not allowed.");
         */

        // ESTO NO LO ESTA HACIENDO BIEN. OJO
        matrix[i][j] = weight;

        String infoEdge = "\n Id de ruta: " + lineId;
        infoEdge += ", Origen: " + stop1Id;
        infoEdge += ", Destino: " + stop2Id;
        infoEdge += ", Peso: " + weight + ";\n";
        edges.add(infoEdge);
    }

    //Remove an edge between two vertices
    @Override
    public void removeEdge(String Stop1Id, String Stop2Id) throws GraphException {
        int i = findStopIndexById(Stop1Id);
        int j = findStopIndexById(Stop2Id);

        if (matrix[i][j] == 0.0)
            throw new GraphException("Edge does not exist.");

        matrix[i][j] = 0.0;
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
            matrix[n][i] = 0.0;
            matrix[i][n] = 0.0;
        }
    }

    @Override
    public String printMatrix() {
        StringBuilder text = new StringBuilder();
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            text.append(Arrays.toString(Arrays.copyOf(matrix[i], n)));
            text.append("\n");
        }
        return text.toString();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public int findStopIndexById(String id) throws GraphException {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getStopId().equals(id)) {
                return i;
            }
        }
        throw new GraphException("Stop with ID " + id + " not found.");
    }

}