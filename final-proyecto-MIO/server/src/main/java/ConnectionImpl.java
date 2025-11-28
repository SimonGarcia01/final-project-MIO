import Demo.Connection;
import Demo.ObserverPrx;
import com.zeroc.Ice.Current;

import java.util.ArrayList;
import java.util.List;

public class ConnectionImpl implements Connection {

    private final List<ObserverPrx> observers = new ArrayList<>();

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
        return "Graph string test";
    }

    // This is to notify all the observers
    public void notifyObservers() {
        for (ObserverPrx prx : observers) {
            prx.notifyUpdate();
        }
    }
}
