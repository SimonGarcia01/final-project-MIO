import Demo.ConnectionPrx;
import Demo.Datagram;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import java.util.List;

public class MIO {
    public static void main(String[] args) {

        try (Communicator communicator = Util.initialize(args, "MIO.config")) {

            // Connection to the server
            ConnectionPrx serverconnection = ConnectionPrx.checkedCast(
                    communicator.propertyToProxy("serverconnection.Proxy")
            );

            if (serverconnection == null) {
                throw new Error("Proxy inválido");
            }

            // Read the files
            List<Datagram> datagrams = DatagramReader.readDatagrams("test.csv");

            // Change the list to the Ice Datagram List
            Datagram[] array = datagrams.toArray(new Datagram[0]);

            //Send the datagrams
            for(Datagram datagram : array){
                System.out.println("Enviando " + datagram.busId);
                serverconnection.receiveDatagram(datagram);
            }

            //serverconnection.sendDatagrams(array);

            System.out.println("Datos enviados correctamente.");
        }
    }
}
