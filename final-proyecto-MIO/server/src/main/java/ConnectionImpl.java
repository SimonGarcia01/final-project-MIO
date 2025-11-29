import Demo.ArcUpdate;
import Demo.Connection;
import Demo.Data;
import Demo.Datagram;
import com.zeroc.Ice.Current;

public class ConnectionImpl implements Connection {

    private final CenterController centerController;

    public ConnectionImpl(CenterController centerController) {
        this.centerController = centerController;
    }

    @Override
    public String getUpdatedGraph(Current current) {
        System.out.println("Graph requested");
        return centerController.getGraph();
    }

    @Override
    public void sendDatagrams(Datagram[] datagrams, Current current) {
        System.out.println("Recibidos " + datagrams.length + " datagramas:");

        for (Datagram d : datagrams) {
            System.out.println("Bus " + d.busId + " stop=" + d.stopId);
        }
    }

    @Override
    public void receiveDatagram(Datagram datagram, Current current) {
        System.out.println("Bus " + datagram.busId + " stop=" + datagram.stopId);
        centerController.setGraph(datagram.registerDate);
    }

    @Override
    public Data getDequeueData(Current current) {
        return null;
    }

    @Override
    public void receiveArcUpdate(ArcUpdate arcUpdate, Current current) {

    }
}
