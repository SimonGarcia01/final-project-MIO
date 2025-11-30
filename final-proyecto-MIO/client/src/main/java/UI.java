public class UI {
    // Called by the client
    public void updateMap(double[][] newGraph) {
        // For now, just print it
        System.out.println("Received updated graph from server:");
        System.out.println(newGraph);
        System.out.println("-------------------------------");
    }
}
