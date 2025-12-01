import utils.GraphCreation;
import utils.GraphImpl;


public class UI {

    private GraphImpl graph;

    public UI() {
        graph = GraphCreation.getGraph();
        System.out.println("Graph created successfully.");
    }

    // Called by the client when server sends updated matrix
    public synchronized void updateMap(double[][] newMatrix) {
        graph.setMatrix(newMatrix);
        System.out.println("Received updated graph from server.");
        System.out.println("-------------------------------");
    }

    // Needed so the input thread can access the graph
    public GraphImpl getGraph() {
        return graph;
    }
}

