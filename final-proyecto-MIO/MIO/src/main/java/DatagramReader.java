import Demo.Datagram;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatagramReader {

    public static List<Datagram> readDatagrams(String resourceName) {
        List<Datagram> list = new ArrayList<>();

        try {
            // Cargar archivo desde resources/
            InputStream in = DatagramReader.class.getResourceAsStream("/" + resourceName);

            if (in == null) {
                throw new RuntimeException("No se encontró el archivo en resources: " + resourceName);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

                String line;
                while ((line = br.readLine()) != null) {

                    String[] data = line.split(",");

                    Datagram d = new Datagram();
                    d.orientation    = Integer.parseInt(data[0]);
                    d.registerDate = data[1];
                    d.stopId       = Integer.parseInt(data[2]);
                    d.odometer     = Integer.parseInt(data[3]);
                    d.latitude     = Double.parseDouble(data[4]) / 1_000_000d;
                    d.longitude    = Double.parseDouble(data[5]) / 1_000_000d;
                    d.taskId       = Integer.parseInt(data[6]);
                    d.lineId       = Integer.parseInt(data[7]);
                    d.tripId       = Integer.parseInt(data[8]);
                    d.unknown1     = Long.parseLong(data[9]);
                    d.datagramDate = data[10];
                    d.busId        = Integer.parseInt(data[11]);

                    list.add(d);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo CSV: " + e.getMessage(), e);
        }

        return list;
    }
}