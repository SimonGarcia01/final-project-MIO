import utils.GraphCreation;
import utils.GraphImpl;


public class UI {

    private GraphImpl graph;
    long startTime;
    long endTime;

    public UI () {
        startTime = System.currentTimeMillis();
        graph = GraphCreation.getGraph();
        System.out.println("Graph created successfully.");
    }

    // Called by the client
    public void updateMap(double[][] newGraph) {
        // For now, just print it
        graph.setMatrix(newGraph);
        System.out.println("Received updated graph from server:");
        System.out.println(newGraph);
        System.out.println("-------------------------------");
        endTime = System.currentTimeMillis();
        System.out.println("Latency: "  + (endTime - startTime));
    }




    // Needed so the input thread can access the graph
    public GraphImpl getGraph() {
        return graph;
    }
}