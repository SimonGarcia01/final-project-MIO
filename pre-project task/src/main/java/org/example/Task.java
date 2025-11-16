package org.example;

import org.example.model.GraphController;
import org.example.model.Stop;
import org.example.structures.IGraph;
import org.example.structures.MatrixGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Task {

    public static void main(String[] args) {

        GraphController control = new GraphController();

        // 2111 is the size of the stops that are in linestops. There are 8 stops that are not in linestops.
        IGraph<Stop> graph = control.createGraph(2111);

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
                control.createVertex(graph, name, id, x, y);
                imported++;

            }

            //System.out.println("Imported stops: " + imported + " from " + csv.getPath());

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();

        }

        System.out.println(graph.printMatrix());

        // Edges:
        System.out.println(control.connectEdges());

    }

}
