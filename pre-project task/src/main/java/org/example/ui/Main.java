package org.example.ui;

import org.example.exceptions.GraphException;
import org.example.modelStructures.MatrixGraph;
import org.example.modelStructures.Vertex;

public class Main {

    public static void main(String[] args) {
        try {
            MatrixGraph graph = new MatrixGraph(10);

            System.out.println("=== ADDING VERTICES ===");
            graph.add(new Vertex("A", "1", 12, 13));
            graph.add(new Vertex("B", "2", 9, 10));
            graph.add(new Vertex("C", "3", 1, 7));
            graph.add(new Vertex("D", "4", 2, 6));
            graph.printMatrix();
            System.out.println();

            System.out.println("=== ADDING EDGES ===");
            graph.addEdge("1", "2", 5.0);
            graph.addEdge("1", "3", 2.0);
            graph.addEdge("2", "4", 1.5);
            graph.addEdge("3", "4", 3.2);
            graph.printMatrix();
            System.out.println();

            System.out.println("=== TRY PARALLEL EDGE (SHOULD FAIL) ===");
            try {
                graph.addEdge("1", "2", 7.0);
            } catch (GraphException ex) {
                System.out.println("Caught expected error: " + ex.getMessage());
            }
            System.out.println();

            System.out.println("=== REMOVE EDGE A->C ===");
            graph.removeEdge("1", "3");
            graph.printMatrix();
            System.out.println();

            System.out.println("=== REMOVE VERTEX B (COMPACTION WILL HAPPEN) ===");
            graph.removeVertex("2");
            graph.printMatrix();
            System.out.println();

            System.out.println("=== ADD MORE EDGES AFTER COMPACTION ===");
            graph.addEdge("1", "4", 9.9);
            graph.printMatrix();
            System.out.println();

            System.out.println("=== REMOVE VERTEX A (HEAD OF MATRIX) ===");
            graph.removeVertex("1");
            graph.printMatrix();
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}