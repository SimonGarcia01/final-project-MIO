import Demo.ConnectionPrx;
import Demo.Datagram;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import java.util.List;

public class MIO {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Uso: java -jar MIO.jar <ruta_del_csv>");
            return;
        }

        String csvPath = args[0];
        System.out.println("Leyendo archivo externo: " + csvPath);

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
                System.out.println("Enviando " + datagram.busId);
                serverconnection.receiveDatagram(datagram);
            }

            System.out.println("Datos enviados correctamente.");
        }
    }
}
