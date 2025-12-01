import utils.GraphImpl;
import java.util.*;

public class LineInspector implements Runnable {

    private final GraphImpl graph;

    public LineInspector(GraphImpl graph) {
        this.graph = graph;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("You can now type a lineId at any time:");

        while (true) {
            System.out.print("> ");
            String lineId = scanner.nextLine().trim();

            if (lineId.equalsIgnoreCase("exit")) {
                System.out.println("Input thread stopping.");
                break;
            }

            printLine(lineId);
        }
    }

    private void printLine(String lineId) {
        SortedMap<String, List<String>> edges = graph.getSortedMap();
        double[][] matrix = graph.getMatrix();

        if (!edges.containsKey(lineId)) {
            System.out.println("LineId '" + lineId + "' not found.");
            return;
        }

        List<String> edgeList = edges.get(lineId);

        System.out.println("\n--- INFO FOR LINE " + lineId + " ---");
        for (String info : edgeList) {
            System.out.println(info);
        }

        System.out.println("\n--- AVERAGE SPEEDS FOR LINE " + lineId + " ---");

        // according to your rules: adjacency matches the order in edgeList
        for (int i = 0; i < edgeList.size() - 1; i++) {
            double speed = matrix[i][i + 1];
            System.out.println("Segment " + i + " → " + (i + 1) + ": " + speed + " km/h");
        }

        System.out.println("--------------------------------------\n");
    }
}