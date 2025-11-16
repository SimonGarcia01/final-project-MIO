package org.example;

import exceptions.GraphException;
import structures.MatrixGraph;

public class Main {

    public static void main(String[] args) {
        try {
            MatrixGraph<String> graph = new MatrixGraph<>(10);

            System.out.println("=== ADDING VERTICES ===");
            graph.add("A");
            graph.add("B");
            graph.add("C");
            graph.add("D");
            graph.printMatrix();
            System.out.println();

            System.out.println("=== ADDING EDGES ===");
            graph.addEdge("A", "B", 5.0);
            graph.addEdge("A", "C", 2.0);
            graph.addEdge("B", "D", 1.5);
            graph.addEdge("C", "D", 3.2);
            graph.printMatrix();
            System.out.println();

            System.out.println("=== TRY PARALLEL EDGE (SHOULD FAIL) ===");
            try {
                graph.addEdge("A", "B", 7.0);
            } catch (GraphException ex) {
                System.out.println("Caught expected error: " + ex.getMessage());
            }
            System.out.println();

            System.out.println("=== REMOVE EDGE A->C ===");
            graph.removeEdge("A", "C");
            graph.printMatrix();
            System.out.println();

            System.out.println("=== REMOVE VERTEX B (COMPACTION WILL HAPPEN) ===");
            graph.removeVertex("B");
            graph.printMatrix();
            System.out.println();

            System.out.println("=== ADD MORE EDGES AFTER COMPACTION ===");
            graph.addEdge("A", "D", 9.9);
            graph.printMatrix();
            System.out.println();

            System.out.println("=== REMOVE VERTEX A (HEAD OF MATRIX) ===");
            graph.removeVertex("A");
            graph.printMatrix();
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}