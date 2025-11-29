package org.example.ui;

import org.example.control.GraphController;
import org.example.modelStructures.IGraph;
import org.example.modelStructures.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Task {

    // Attributes, global variables
    private static IGraph<Vertex> graph;
    private static GraphController control = new GraphController();
    private List<String[]> input = new ArrayList<>();

    // Scanner
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Task task = new Task();

        // 2111 is the size of the stops that are in linestops. There are 8 stops that are not in linestops.
        graph = control.createGraph(2111);

        task.readStopsAndCreateVertexes();
        // Create lines
        task.readAndCreateLines();
        // Create and add edges
        task.createAndConnectEdges();


        int option;

        do {
            System.out.println("\n*** BIENVENIDO A NUESTRO GRAFO ***\n");
            System.out.println("-- Menu --\n");
            System.out.println("1. Mostrar lista de paradas ordenadas por ruta, orientación, variante y stopsequence con subgrafo.");
            System.out.println("2. Mostrar lista de vértices.");
            System.out.println("3. Mostrar matriz de pesos del grafo (por ahora, donde hay una relación se pone 1 y donde no 0). Matriz muy grande, utilizar buscador de consola para encontrar 1.0's");
            System.out.println("4. Salir.");

            System.out.print("Digite su opcion: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println(graph.getEdges());
                    break;
                case 2:
                    System.out.println(graph.getVerticesSortedMap());
                    break;
                case 3:
                    System.out.println(graph.printMatrix());
                    break;
                case 4:
                    System.out.println("\n¡Chao profe! ¡Esperamos que le haya gustado nuestro avance!");
                    break;
                default:
                    System.out.println("\nOpción no valida. Vuelva a intentarlo.");
                    break;
            }

        } while (option != 4);

    }

    public void createMaps() {

        String path = "src/main/resources/linestops-241.csv";
        File csv = new File(path);


        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                // If first line looks like a header, skip it
                if (firstLine) {
                    firstLine = false;
                    String low = line.toLowerCase();
                    if (low.contains("stop_id") || low.contains("stopid") || low.contains("id"))
                        continue;
                }


                // Simple CSV split (assumes stable CSV without quoted commas)
                String[] parts = line.split(",", -1);
                if (parts.length < 1) {
                    // Not enough columns — skip line
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                // We add each line to the arraylist
                input.add(parts);

            }

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();

        }

    }

    // This method read stops files and create vertexes
    public String readStopsAndCreateVertexes() {

        createMaps();

        String[] nolinestops = {"4", "5", "6", "9", "10", "21", "22", "41"};

        String path = "src/main/resources/stops-241.csv";
        File csv = new File(path);

        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                // If first line looks like a header, skip it
                if (firstLine) {
                    firstLine = false;
                    String low = line.toLowerCase();
                    if (low.contains("stop_id") || low.contains("stopid") || low.contains("id"))
                        continue;
                }

                // Simple CSV split (assumes stable CSV without quoted commas)
                String[] parts = line.split(",", -1);
                if (parts.length < 1) {
                    // Not enough columns — skip line
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }


                String stopId = parts[0].trim();

                if (Arrays.asList(nolinestops).contains(stopId)) {
                    continue;
                }

                String name = parts[3].trim();
                double x;
                double y;

                try {
                    x = Double.parseDouble(parts[4].trim()) / 1e7;
                    y = Double.parseDouble(parts[5].trim()) / 1e7;
                } catch (NumberFormatException nfe) {
                    System.err.println("Skipping line with invalid coordinates: " + line);
                    continue;
                }

                // We create the stops. Each stop is a vertex.
                control.createAndAddVertex(graph, name, stopId, x, y);
                //control.createAdjancenyMatrix(graph, stopId);

            }

            return "Vertexes created successfully.";

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();
            return "Vertexes not created successfully.";

        }

    }

    // This method create and add edges to the graph
    public String createAndConnectEdges() {
        // We create the edge between two vertexes.
        try {

            // No recorro vertices sino linestops que son las conexiones
            String lineId;
            String stop1Id;
            String stop2Id;
            String orientation;
            String variant;
            String stopSequence;


            for (int i = 0; i < input.size(); i++) {

                lineId = input.get(i)[3];
                stop1Id = input.get(i)[4];
                orientation = input.get(i)[2];
                variant = input.get(i)[6];

                stopSequence = input.get(i)[1];

                control.connectEdge(graph, lineId, stop1Id, orientation, variant, stopSequence);

            }

            for(int i = 0; i < input.size()-1; i++) {
                stop1Id = input.get(i)[4];
                stop2Id = input.get(i + 1)[4];
                control.createAdjacencyMatrix(graph, stop1Id, stop2Id);
            }

            control.group(graph);

            return "Edges created and added to the graph successfully";

        } catch (Exception e) {

            System.err.println("Error reading file :" + e.getMessage());
            e.printStackTrace();
            return "Edges created and added to the graph unsuccessfully.";

        }

    }

    public String readAndCreateLines() {

        String path = "src/main/resources/lines-241.csv";
        File csv = new File(path);

        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                // If first line looks like a header, skip it
                if (firstLine) {
                    firstLine = false;
                    String low = line.toLowerCase();
                    if (low.contains("line_id") || low.contains("lineid") || low.contains("id"))
                        continue;
                }

                // Simple CSV split (assumes stable CSV without quoted commas)
                String[] parts = line.split(",", -1);
                if (parts.length < 1) {
                    // Not enough columns — skip line
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }


                String lineId = parts[0].trim();
                String lineName = parts[2].trim();

                control.createLine(graph, lineId, lineName);

            }

            return "Lines created successfully.";

        } catch (Exception e) {

            System.err.println("Error reading file :" + e.getMessage());
            e.printStackTrace();
            return "Lines created unsuccessfully.";
        }


    }

}