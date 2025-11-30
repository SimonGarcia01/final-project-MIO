import utils.GraphCreation;
import utils.GraphImpl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Database {
    // Para registrar por qué paradas ha ido pasando cada bus
    private static final Map<Integer, Deque<Integer>> stopsByBus = new HashMap<>();

    // Grafo inmutable
    private final GraphImpl graph;

    public Database() {
        graph = GraphCreation.getGraph();
        System.out.println("Database created successfully.");
        System.out.println(graph.getEdges());
    }

    public void addStop(int busId, int stopId) {
        stopsByBus
                .computeIfAbsent(busId, id -> new ArrayDeque<>())
                .addLast(stopId);  // O addLast(stopId)
    }

    public int getLastStop(int busId) {
        Deque<Integer> q = stopsByBus.get(busId);
        return (q == null || q.isEmpty()) ? -1 : q.getLast();
    }

    public double[][] returnGraph(){
        return graph.getMatrix();
    }
}
