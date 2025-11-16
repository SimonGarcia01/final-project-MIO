package org.example.ui;

import org.example.control.GraphController;
import org.example.modelStructures.IGraph;
import org.example.modelStructures.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Task {

    // Attributes, global variables
    private static IGraph<Vertex> graph;
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

        // MUY IMPORTANTE, NO BORRAR. Aunque no se vea porque no tiene retorno,
        // esta creando un mapa con todos los agrupamientos creando asi un orden compuesto. OJO
        task.createFiltersAndOrdering();

        // Group by lines
        System.out.println(task.readLinesStopsAndGroup());
        //System.out.println(graph.getVertices());

        // Create and add edges
        System.out.println(task.createAndConnectEdges());

        //System.out.println(graph.printMatrix());
        

        
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
                stopsWithOrientations.put(stopId, orientation);

                // Stops by line, orientation and variant
                // Add another filter and order
                String variant = parts[6].trim();
                stopsWithVariants.put(stopId, variant);
                
                // Stops by line, orientation, variant and stopsequence
                // Add another filter and order
                String stopsequence = parts[1].trim();
                stopsWithStopSequences.put(stopId, stopsequence);
                //System.out.println(stopsWithStopSequences);

            }

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();

        }

    }

    // This method read stops files and create vertexes
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

                String stopId = parts[0].trim();

                if(Arrays.asList(nolinestops).contains(stopId)) {
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
                String lineId = stopsWithLines.get(stopId);
                String orientation = stopsWithOrientations.get(stopId);
                String variant = stopsWithVariants.get(stopId);
                String stopsequence = stopsWithStopSequences.get(stopId);
                control.createAndAddVertex(graph, name, stopId, x, y, lineId, orientation, variant, stopsequence);
                imported++;

            }

            // ** TEST **
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

    // This method read linestops file and group by line
    public String readLinesStopsAndGroup() {

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

                imported++;

            }

            System.out.println("Imported stops: " + imported + " from " + csv.getPath());
            // Hago groupBy por ruta
            //System.out.println("Paradas agrupadas por ruta: ");
            graph.getVertices().stream().collect(Collectors.groupingBy(Vertex::getLineId));
            

            return "Group by line ready.";

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();
            return "Group by line unsuccessfully.";

        }

    }

    // This method create and add edges to the graph
    public String createAndConnectEdges(){
        // We create the edge between two vertexes.
        try {

            createFiltersAndOrdering();

            // No lo puedo igualar a graph.getVertices() porque sino me borra los elementos de la lista original
            // Vertexes 1
            var vertexes1 = new ArrayList<>(graph.getVertices());
            vertexes1.remove(graph.getVertices().size()-1);

            // Vertexes 2
            var vertexes2 = new ArrayList<>(graph.getVertices());
            vertexes2.remove(0);

            // Iterator
            Iterator<Vertex> itOne=vertexes1.iterator();
            Iterator<Vertex> itTwo=vertexes2.iterator();

            while(itOne.hasNext() && itTwo.hasNext()) {

                Vertex vertex1 = itOne.next();
                Vertex vertex2 = itTwo.next();

                control.connectEdge(graph, vertex1.getStopId(), vertex2.getStopId(), 1);

            }

            //System.out.println(graph);

            return "Edges created and added to the graph successfully";

        } catch (Exception e) {

            System.err.println("Error reading file :" + e.getMessage());
            e.printStackTrace();
            return "Edges created and added to the graph unsuccessfully.";

        }
        
    }

}
