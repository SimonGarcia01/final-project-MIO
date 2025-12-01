import Demo.ArcUpdate;
import Demo.Connection;
import Demo.Data;
import Demo.Datagram;
import com.zeroc.Ice.Current;

public class ConnectionImpl implements Connection {

    private final CenterController centerController;
    private final Database database;
    private final QueueManager queueManager;

    public ConnectionImpl(CenterController centerController, Database database, QueueManager queueManager) {
        this.centerController = centerController;
        this.database = database;
        this.queueManager = queueManager;
    }

    @Override
    public double[][] getUpdatedGraph(Current current) {
        System.out.println("Graph requested");
        System.out.println("Graph requested at " + System.currentTimeMillis());
        return database.returnGraph();
    }

    @Override
    public void sendDatagrams(Datagram[] datagrams, Current current) {
        System.out.println("Recibidos " + datagrams.length + " datagramas:");

        for (Datagram d : datagrams) {
            System.out.println("Bus " + d.busId + " stop=" + d.stopId);
            centerController.produceData(d);
        }
    }

    @Override
    public void receiveDatagram(Datagram datagram, Current current) {
        System.out.println("Bus " + datagram.busId + " stop=" + datagram.stopId);
        centerController.produceData(datagram);
    }

    @Override
    public Data getDequeueData(Current current) {
        return queueManager.dequeueData();
    }

    @Override
    public void receiveArcUpdate(ArcUpdate arcUpdate, Current current) {
        queueManager.enqueueArcUpdate(arcUpdate);
    }
}
