package org.example;

import org.example.model.GraphController;
import org.example.model.Stop;
import org.example.structures.IGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Task {

    // Attributes, global variables
    private static IGraph<Stop> graph;
    private static GraphController control = new GraphController();
    private static HashMap<String, String> stopsWithLines = new HashMap<>();
    private static HashMap<String, String> stopsWithOrientations = new HashMap<>();
    private static HashMap<String, String> stopsWithVariants = new HashMap<>();
    private static HashMap<String, String> stopsWithStopSequences = new HashMap<>();

    public static void main(String[] args) {

        Task task = new Task();

        // 2111 is the size of the stops that are in linestops. There are 8 stops that are not in linestops.
        graph = control.createGraph(2111);
        System.out.println(task.readStopsAndCreateVertexes());
        //System.out.println(graph.printMatrix());

        // MUY IMPORTANTE, NO BORRAR. Aunque no se vea porque no tiene retorno,
        // esta creando un mapa con las paradas y sus respectivas rutas asociadas. OJO
        task.createFiltersAndOrdering();
        System.out.println(stopsWithVariants);
        //System.out.println(stopsWithLines);

        // Connect edges
        System.out.println(task.readLinesStopsAndCreateEdges());

        
    }

    public void createFiltersAndOrdering() {

        String path = "src/main/java/org/example/data/linestops-241.csv";
        File csv = new File(path);

        int imported = 0;
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

                // Stops by line
                String stopId = parts[4].trim();
                String lineId = parts[3].trim();

                stopsWithLines.put(stopId, lineId);

                // Add orientation filter. Lines by orientation
                // So, it means that is stops by line and orientation. :)
                String orientation = parts[2].trim();
                stopsWithOrientations.put(lineId, orientation);

                // Stops by line, orientation and variant
                // Add another filter and order
                String variant = parts[6].trim();
                stopsWithVariants.put(orientation, variant);
                
                // Stops by line, orientation, variant and stopsequence
                // Add another filter and order
                String stopsequence = parts[1].trim();
                stopsWithVariants.put(variant, stopsequence);

            }

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();

        }

    }


    public String readStopsAndCreateVertexes() {

        createFiltersAndOrdering();

        String[] nolinestops = {"4", "5", "6", "9", "10", "21", "22", "41"};

        String path = "src/main/java/org/example/data/stops-241.csv";
        File csv = new File(path);

        int imported = 0;
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

                String id = parts[0].trim();

                if(Arrays.asList(nolinestops).contains(id)) {
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
                String lineId = stopsWithLines.get(id);
                String orientation = stopsWithOrientations.get(lineId);
                String variant = stopsWithVariants.get(orientation);
                String stopsequence = stopsWithStopSequences.get(variant);
                control.createAndAddVertex(graph, name, id, x, y, lineId, orientation, variant, stopsequence);
                imported++;

            }

            //System.out.println(stopsWithLines);
            //System.out.println("Imported stops: " + imported + " from " + csv.getPath());
            //System.out.println(graph.getVertices().toString());
            return "Vertexes created successfully.";

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();
            return "Vertexes not created successfully.";

        }

    }

    // This method is for connect edges
    public String readLinesStopsAndCreateEdges() {

        String path = "src/main/java/org/example/data/linestops-241.csv";
        File csv = new File(path);

        int imported = 0;
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
                    if (low.contains("stop_sequence") || low.contains("stopsequence") || low.contains("stop_id") || low.contains("stopid"))
                        continue;
                }

                // Simple CSV split (assumes stable CSV without quoted commas)
                String[] parts = line.split(",", -1);
                if (parts.length < 1) {
                    // Not enough columns — skip line
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                int sequence = Integer.parseInt(parts[1].trim());
                

                //control.connectEdge(stop1Id, "stop2Id", 1);
                imported++;

            }

            System.out.println("Imported stops: " + imported + " from " + csv.getPath());
            // Hago groupBy por ruta
            //System.out.println("Paradas agrupadas por ruta: ");
            graph.getVertices().stream().collect(Collectors.groupingBy(Stop::getLineId));
            System.out.println("P: " + graph.getVertices());

            // Falta o hacer mas agrupaciones
            // O encontrar la manera de conectarlos por secuencia.
            // ----- TRABAJO EN PROGRESO ----
            // AVANCES SIGNIFICATIVOS, ¡GRACIAS A DIOS!
            return "Vertexes created successfully.";

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();
            return "Vertexes not created successfully.";


        }

    }

}
