import Demo.ArcUpdate;
import Demo.BusUpdate;
import Demo.Data;
import utils.GraphImpl;
import utils.Vertex;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EstimatorConsumer {

    private final GraphImpl graph;

    public EstimatorConsumer(GraphImpl graph) {
        this.graph = graph;
    }

    public ArcUpdate estimateArcUpdate(Data data) {

        System.out.println("[ESTIMATOR CONSUMER]--> " + data.date + " " + data.prevStopTime +
                " " + data.lineId + " " + data.busId + " " + data.prevStopId + " " +
                data.latitude + " " + data.longitude);

        // -----------------------------
        // Case 1: We know previous stop
        // -----------------------------
        if (data.prevStopId != -1 && !data.prevStopTime.isEmpty()) {

            Vertex previousVertex =
                    graph.findVertexByStopId(String.valueOf(data.prevStopId));

            Vertex nextVertex =
                    graph.findVertexByStopId(graph.getNextStop(
                            String.valueOf(data.lineId),
                            previousVertex.getStopId()
                    ));

            double nextLat = nextVertex.getY();
            double nextLon = nextVertex.getX();

            double distance = distanceKm(data.latitude, data.longitude, nextLat, nextLon);

            int stopMatrixId1 = -1;
            int stopMatrixId2 = -1;
            double averageSpeed = -1.0;

            // Only update when bus is close to the next stop
            if (distance < 0.120) {  // 120 m

                LocalDateTime t2 = parseTime(data.date);
                LocalDateTime t1 = parseTime(data.prevStopTime);

                long seconds = Duration.between(t1, t2).getSeconds();

                // Protect from invalid time
                if (seconds <= 0) {

                    BusUpdate busUpdate = new BusUpdate(
                            data.orientation,
                            data.lineId,
                            Integer.parseInt(nextVertex.getStopId()),
                            data.busId,
                            data.date
                    );

                    return new ArcUpdate(-1, -1, -2.0, busUpdate);
                }

                double hours = seconds / 3600.0;

                if (hours <= 0) hours = 0.0001; // Avoid division by zero

                averageSpeed = distance / hours;

                stopMatrixId1 = graph.findStopIndexById(previousVertex.getStopId());
                stopMatrixId2 = graph.findStopIndexById(nextVertex.getStopId());
            }

            BusUpdate busUpdate = new BusUpdate(
                    data.orientation,
                    data.lineId,
                    Integer.parseInt(nextVertex.getStopId()),
                    data.busId,
                    data.date
            );

            return new ArcUpdate(
                    stopMatrixId1,
                    stopMatrixId2,
                    averageSpeed,
                    busUpdate
            );
        }

        // ---------------------------------------
        // Case 2: No previous stop → estimate nearest
        // ---------------------------------------
        double minDistance = 5000.0;
        Vertex nearest = null;

        for (Vertex v : graph.findEdgesByLineId(String.valueOf(data.lineId))) {
            double dist = distanceKm(v.getY(), v.getX(), data.latitude, data.longitude);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = v;
            }
        }

        int nearestStopId = -1;
        if (nearest != null && minDistance < 0.120) {
            nearestStopId = Integer.parseInt(nearest.getStopId());
        }

        BusUpdate busUpdate = new BusUpdate(
                data.orientation,
                data.lineId,
                nearestStopId,
                data.busId,
                data.date
        );

        return new ArcUpdate(-1, -1, -2.0, busUpdate);
    }

    public LocalDateTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }

    public double distanceKm(double lat1, double lon1, double lat2, double lon2){
        double R = 6371.0; //Earths radius in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;  // distance in kms
    }
}