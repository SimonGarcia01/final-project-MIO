import Demo.ArcUpdate;
import Demo.Data;
import utils.GraphImpl;
import utils.Vertex;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EstimatorConsumer {

    private final GraphImpl graph;

    public EstimatorConsumer(GraphImpl graph) {
        this.graph = graph;
    }

    public ArcUpdate estimateArcUpdate(Data data) {
        Vertex previousVertex = graph.findVertexByStopId(String.valueOf(data.prevStopId));
        return null;
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