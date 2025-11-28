public class UI {
    private String graph = "";

    public String getGraph() {
        return graph;
    }

    // Called by the ICE observer
    public void updateMapNotif(String newGraph) {
        this.graph = newGraph;

        // For now, just print it
        System.out.println("Received updated graph from server:");
        System.out.println(graph);
        System.out.println("-------------------------------");
    }
}
