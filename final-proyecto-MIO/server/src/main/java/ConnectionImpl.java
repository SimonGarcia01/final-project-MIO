import Demo.ArcUpdate;
import Demo.Connection;
import Demo.Data;
import Demo.Datagram;
import com.zeroc.Ice.Current;

public class ConnectionImpl implements Connection {

    private final CenterController centerController;
    private final Database database;

    public ConnectionImpl(CenterController centerController, Database database) {
        this.centerController = centerController;
        this.database = database;
    }

    @Override
    public double[][] getUpdatedGraph(Current current) {
        System.out.println("Graph requested");
        return database.returnGraph();
    }

    @Override
    public void sendDatagrams(Datagram[] datagrams, Current current) {
        System.out.println("Recibidos " + datagrams.length + " datagramas:");

        for (Datagram d : datagrams) {
            System.out.println("Bus " + d.busId + " stop=" + d.stopId);

            Data data = transformDatagram(d);
            centerController.produceData(data);
        }
    }

    @Override
    public void receiveDatagram(Datagram datagram, Current current) {
        System.out.println("Bus " + datagram.busId + " stop=" + datagram.stopId);

        Data data = transformDatagram(datagram);
        centerController.produceData(data);
    }

    private Data transformDatagram(Datagram datagram) {
        //Transform Datagram
        Data data = new Data();
        data.orientation = datagram.orientation;
        data.lineId = datagram.lineId;
        data.busId = datagram.busId;
        data.latitude = datagram.latitude;
        data.longitude = datagram.longitude;
        data.date = datagram.datagramDate;
        data.prevStopId = datagram.stopId;
        data.prevStopTime = datagram.registerDate;

        return data;
    }

    @Override
    public Data getDequeueData(Current current) {
        return centerController.getQueueManager().dequeueData();
    }

    @Override
    public void receiveArcUpdate(ArcUpdate arcUpdate, Current current) {
        centerController.processArcUpdates();
    }
}
