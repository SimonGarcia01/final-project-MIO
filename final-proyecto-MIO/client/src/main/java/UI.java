import utils.GraphImpl;

public class UI {

    private final GraphImpl graph;

    public UI() {
        this.graph = utils.GraphCreation.getGraph();
        System.out.println("Graph created successfully.");
    }

    // Called by the client when server sends updated matrix
    // synchronized so callers can't see a half-updated state
    public synchronized void updateMap(double[][] newMatrix) {
        graph.setMatrix(newMatrix);
        // only print when a new matrix arrives (no spam)
        System.out.println("\n[SERVER] Updated adjacency matrix received.");
        System.out.println("-------------------------------------------");
    }

    // Needed so the input thread can access the graph
    public GraphImpl getGraph() {
        return graph;
    }
}