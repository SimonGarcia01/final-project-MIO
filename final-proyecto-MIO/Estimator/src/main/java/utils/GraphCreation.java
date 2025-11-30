package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GraphCreation {

    // Attributes, global variables
    private static GraphImpl graph;
    private static List<String[]> input = new ArrayList<>();

    public static GraphImpl getGraph() {
        makeGraph();
        return graph;
    }

    public static void makeGraph() {

        // 2111 is the size of the stops that are in linestops. There are 8 stops that are not in linestops.
        graph = new GraphImpl(2111);

        readStopsAndCreateVertexes();
        // Create lines
        readAndCreateLines();
        // Create and add edges
        createAndConnectEdges();

    }

    public static void createMaps() {

        InputStream inputStream = GraphCreation.class
                .getClassLoader()
                .getResourceAsStream("linestops-241.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
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

            System.err.println("Error reading file linestops-241.csv:" + e.getMessage());
            e.printStackTrace();

        }

    }

    // This method read stops files and create vertexes
    public static String readStopsAndCreateVertexes() {

        createMaps();

        String[] nolinestops = {"4", "5", "6", "9", "10", "21", "22", "41"};

        InputStream inputStream = GraphCreation.class
                .getClassLoader()
                .getResourceAsStream("stops-241.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
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
                    x = Double.parseDouble(parts[6].trim());
                    y = Double.parseDouble(parts[7].trim());
                } catch (NumberFormatException nfe) {
                    System.err.println("Skipping line with invalid coordinates: " + line);
                    continue;
                }

                // We create the stops. Each stop is a vertex.
                Vertex s = new Vertex(name, stopId, x, y);
                graph.add(s);

            }

            return "Vertexes created successfully.";

        } catch (IOException e) {

            System.err.println("Error reading file stops-241.csv: " + e.getMessage());
            e.printStackTrace();
            return "Vertexes not created successfully.";

        }

    }

    // This method create and add edges to the graph
    public static String createAndConnectEdges() {
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

                graph.addEdge(lineId, stop1Id, orientation, variant, stopSequence);

            }

            //The distance
            for(int i = 0; i < input.size()-1; i++) {
                stop1Id = input.get(i)[4];
                stop2Id = input.get(i + 1)[4];
                graph.createAdjacencyMatrix(stop1Id, stop2Id);
            }

            graph.group();

            return "Edges created and added to the graph successfully";

        } catch (Exception e) {

            System.err.println("Error reading file :" + e.getMessage());
            e.printStackTrace();
            return "Edges created and added to the graph unsuccessfully.";

        }

    }

    public static String readAndCreateLines() {

        InputStream inputStream = GraphCreation.class
                .getClassLoader()
                .getResourceAsStream("lines-241.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
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

                graph.createLine(lineId, lineName);

            }

            return "Lines created successfully.";

        } catch (Exception e) {

            System.err.println("Error reading file lines-241.csv:" + e.getMessage());
            e.printStackTrace();
            return "Lines created unsuccessfully.";
        }


    }
}
