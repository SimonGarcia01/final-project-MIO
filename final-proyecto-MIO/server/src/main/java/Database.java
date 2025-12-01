import Demo.BusUpdate;
import utils.StopIdDate;
import utils.GraphCreation;
import utils.GraphImpl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Database {
    // Para registrar por qué paradas ha ido pasando cada bus
    private static final Map<Integer, Deque<StopIdDate>> stopsByBus = new HashMap<>();


    // Grafo inmutable
    private final GraphImpl graph;

    public Database() {
        graph = GraphCreation.getGraph();
        System.out.println("[Database] Database created successfully.");
    }

    public void addStop(int busId, int stopId, int lineId, String timestamp) {
        if (stopId < 0) {
            System.out.println("[DATABASE] Ignored invalid stopId=" + stopId + " for bus=" + busId);
            return;
        }

        stopsByBus
                .computeIfAbsent(busId, id -> new ArrayDeque<>())
                .addLast(new StopIdDate(stopId, timestamp, lineId));

        System.out.println("DATABASE ADD STOP " + busId + " " + stopId + " " + lineId + " " + timestamp);
    }

    public StopIdDate getLastStop(int busId) {
        Deque<StopIdDate> q = stopsByBus.get(busId);
        if (q == null || q.isEmpty()) return null;
        return q.getLast();
    }

    public double[][] returnGraph(){
        return graph.getMatrix();
    }

    public void updateArc(int matrixStopId1, int matrixStopId2, double newAverageSpeed, BusUpdate busUpdate) {

        if (busUpdate.stopId < 0) {
            System.out.println("[DATABASE] Ignoring update with stopId=-1");
            return;
        }


        StopIdDate last = getLastStop(busUpdate.busId);

        if (last == null || last.stopId != busUpdate.stopId) {
            addStop(
                    busUpdate.busId,
                    busUpdate.stopId,
                    busUpdate.lineId,
                    busUpdate.timestamp
            );
        }

        if (matrixStopId1 == -1 && matrixStopId2 == -1 && newAverageSpeed == -2.0) {
            System.out.println("[DATABASE] Position update only, no speed update.");
            return;
        }

        int count = graph.getAverageCounter(matrixStopId1, matrixStopId2);
        double oldAvg = graph.getAverageSpeed(matrixStopId1, matrixStopId2);

        double updatedAvg = (oldAvg * count + newAverageSpeed) / (count + 1);

        graph.updateAverageSpeed(matrixStopId1, matrixStopId2, updatedAvg);

        System.out.println("[DATABASE] Updated avg speed: " + updatedAvg);

    }

    public void restartLocations(int busId) {
        stopsByBus.remove(busId);
    }

}
