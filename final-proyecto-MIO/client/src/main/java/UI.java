    import utils.GraphCreation;
import utils.GraphImpl;

    import java.time.LocalDate;

    public class UI {

    private GraphImpl graph;

    public UI (){
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
    }
}

