import Demo.BusUpdate;
import utils.BusIdDate;
import utils.GraphCreation;
import utils.GraphImpl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Database {
    // Para registrar por qué paradas ha ido pasando cada bus
    private static final Map<Integer, Deque<BusIdDate>> stopsByBus = new HashMap<>();


    // Grafo inmutable
    private final GraphImpl graph;

    public Database() {
        graph = GraphCreation.getGraph();
        System.out.println("[Database] Database created successfully.");
    }

    public void addStop(int busId, int stopId, int lineId, String date) {
        stopsByBus
                .computeIfAbsent(busId, id -> new ArrayDeque<>())
                .addLast(new BusIdDate(stopId, date, lineId));
    }

    public BusIdDate getLastStop(int busId) {
        Deque<BusIdDate> q = stopsByBus.get(busId);
        return (q == null || q.isEmpty()) ? null : q.getLast();
    }

    public double[][] returnGraph(){
        return graph.getMatrix();
    }

    public void updateArc(int matrixStopId1, int matrixStopId2, double newAverageSpeed, BusUpdate busUpdate) {

        if(matrixStopId1 == -1 || matrixStopId2 == -1 || newAverageSpeed == -2.0) {

            addStop(busUpdate.busId, busUpdate.stopId, busUpdate.lineId, busUpdate.timestamp);
        }
        else {
            addStop(matrixStopId1,matrixStopId2, busUpdate.lineId, busUpdate.timestamp);
            int averageCounter = graph.getAverageCounter(matrixStopId1,matrixStopId2);
            double oldAvg = graph.getAverageSpeed(matrixStopId1,matrixStopId2);

            double updatedAvg = (oldAvg * averageCounter + newAverageSpeed) / (averageCounter + 1);

            graph.updateAverageSpeed(matrixStopId1,matrixStopId2,updatedAvg);
        }

    }

    public void restartLocations(int busId) {
        stopsByBus.remove(busId);
    }

}
