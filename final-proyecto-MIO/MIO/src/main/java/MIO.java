import Demo.ConnectionPrx;
import Demo.Datagram;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import java.util.List;

public class MIO {
    public static void main(String[] args) {
        long start = System.nanoTime();

        if (args.length < 1) {
            System.err.println("[MIO] No file route added: java -jar MIO.jar <csv file route>");
            return;
        }

        String csvPath = args[0];
        System.out.println("[MIO] Reading External File: " + csvPath);

        try (Communicator communicator = Util.initialize(new String[]{}, "MIO.config")) {

            // Connection to the server
            ConnectionPrx serverconnection = ConnectionPrx.checkedCast(
                    communicator.propertyToProxy("serverconnection.Proxy")
            );

            if (serverconnection == null) {
                throw new Error("Proxy inválido");
            }

            // Read the files
            List<Datagram> datagrams = DatagramReader.readDatagrams(csvPath);

            // Change the list to the Ice Datagram List
            Datagram[] array = datagrams.toArray(new Datagram[0]);

            //Send the datagrams
            for(Datagram datagram : array){
                System.out.println("[MIO] Sending " + datagram.busId);
                serverconnection.receiveDatagram(datagram);
            }

            System.out.println("[MIO] Data successfully sent.");
        }
        long end = System.nanoTime();
        System.out.println("[MIO] Processing Time: " + (end - start) / 1000000.0 + "ms");
    }
}
