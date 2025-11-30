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
        Vertex previousVertex = graph.findVertexByStopId(String.valueOf(data.prevStopId));
        Vertex nextVertex = graph.findVertexByStopId(graph.getNextStop(String.valueOf(data.lineId), previousVertex.getStopId()));

        double prevLat = previousVertex.getY();
        double prevLon = previousVertex.getX();
        double nextLat = nextVertex.getY();
        double nextLon = nextVertex.getX();

        double distance = distanceKm(prevLat, prevLon, nextLat, nextLon);

        int stopMatrixId1 = -1;
        int stopMatrixId2 = -1;
        double averageSpeed = -1;

        //Check if the bus is less than 50 meters away from the next stop
        if(distance < 0.05){
            LocalDateTime time2 = parseTime(data.date);
            LocalDateTime time1 = parseTime(data.prevStopTime);

            // Time difference in seconds
            long seconds = Duration.between(time1, time2).getSeconds();

            // Convert seconds → hours
            double hours = seconds / 3600.0;

            // Now compute speed
            averageSpeed = distance / hours;

            stopMatrixId1 = graph.findStopIndexById(previousVertex.getStopId());
            stopMatrixId2 = graph.findStopIndexById(nextVertex.getStopId());
        }

        BusUpdate busUpdate = new BusUpdate(
                data.orientation,
                data.lineId,
                data.busId,
                data.date);

        return new ArcUpdate(
                stopMatrixId1,
                stopMatrixId2,
                averageSpeed,
                busUpdate
                );
    }

    public LocalDateTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
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