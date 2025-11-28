import Demo.Connection;
import Demo.Datagram;
import Demo.ObserverPrx;
import com.zeroc.Ice.Current;

import java.util.ArrayList;
import java.util.List;

public class ConnectionImpl implements Connection {

    private final List<ObserverPrx> observers = new ArrayList<>();

    private final CenterController centerController;

    public ConnectionImpl(CenterController centerController) {
        this.centerController = centerController;
    }

    @Override
    public void subscribe(ObserverPrx observer, Current current) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer subscribed: " + observer);
        }
    }

    @Override
    public String getUpdateGraph(Current current) {
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
        notifyObservers();
    }

    // This is to notify all the observers
    public void notifyObservers() {
        for (ObserverPrx prx : observers) {
            prx.notifyUpdate();
        }
    }
}
