import utils.GraphImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;


public class LineInspector implements Runnable {

    private final GraphImpl graph;

    public LineInspector(GraphImpl graph) {
        this.graph = graph;
    }

    long startTime;
    long endTime;
    long latency;
    int count = 0;

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        startTime = System.currentTimeMillis();

        while (true) {
            System.out.print("\nEnter line ID to inspect (or 'q' to quit): ");
            String lineId = scanner.nextLine().trim();

            if (lineId.equalsIgnoreCase("q")) {
                System.out.println("Exiting LineInspector.");
                break;
            }

            if (lineId.isEmpty()) {
                continue;
            }

            printLineTable(lineId);
        }

        scanner.close();
    }

    private void printLineTable(String lineId) {
        SortedMap<String, List<String>> sorted = graph.getSortedMap();
        List<String> infoList = sorted.get(lineId);

        if (infoList == null || infoList.isEmpty()) {
            System.out.println("Line not found or no stops for line: " + lineId);
            return;
        }

        // Extract stop IDs from your infoEdge entries.
        // You appended the stop1Id as the last comma-separated token in makeInfoEdge(...)
        List<String> stopIds = new ArrayList<>();
        for (String info : infoList) {
            // normalize: remove leading newline/spaces
            String trimmed = info.trim();
            String[] parts = trimmed.split(",\\s*");
            // last part should be the stopId you appended (no label), defensively check length
            if (parts.length > 0) {
                String last = parts[parts.length - 1].trim();
                stopIds.add(last);
            }
        }

        if (stopIds.size() < 2) {
            System.out.println("Not enough stops for line " + lineId + " to show pairs.");
            return;
        }

        // Lock on graph while reading matrix/vertices to avoid race with updateMap
        double[][] matrix;
        // snapshot vertices or use findNameByStopId directly
        synchronized (graph) {
            matrix = graph.getMatrix();
        }

        System.out.println("\n----- LINE " + lineId + " -----");
        System.out.printf("| %-30s | %-30s | %-13s |%n", "Stop A", "Stop B", "AverageSpeed");
        System.out.println("-------------------------------------------------------------------------------");

        for (int k = 0; k < stopIds.size() - 1; k++) {

            String stopAId = stopIds.get(k);
            String stopBId = stopIds.get(k + 1);

            int i = graph.findStopIndexById(stopAId);
            int j = graph.findStopIndexById(stopBId);

            if (i < 0 || j < 0) {
                // Could not find indices; print with placeholders
                String stopAName = graph.findNameByStopId(stopAId);
                String stopBName = graph.findNameByStopId(stopBId);
                System.out.printf("| %-30s | %-30s | %-13s |%n", stopAName, stopBName, "N/A");
                continue;
            }

            double speed;
            synchronized (graph) {
                // guard access in case matrix reference/memory switched by updateMap
                speed = matrix[i][j];
            }

            if(!stopAId.equals(stopBId)) {
                String stopAName = graph.findNameByStopId(stopAId);
                String stopBName = graph.findNameByStopId(stopBId);
                count++;
                System.out.printf("| %-30s | %-30s | %13.2f |%n", stopAName, stopBName, speed);

            }

        }
        endTime = System.currentTimeMillis();
        latency = (endTime-startTime) ;
        // Considering the network, advice of the teacher
        latency = (endTime-startTime) ;
        latency = (endTime-startTime) ;
        System.out.println();
        System.out.println("Latency: " + latency + "ms");
        System.out.println("Throughput: " + ((double) count / (double)latency) + " request per ms");
    }
}