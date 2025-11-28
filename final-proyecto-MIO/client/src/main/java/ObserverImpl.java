import Demo.ConnectionPrx;
import Demo.Observer;
import com.zeroc.Ice.Current;

public class ObserverImpl implements Observer {

    private final ConnectionPrx serverConnection;

    public ObserverImpl(ConnectionPrx serverConnection) {
        this.serverConnection = serverConnection;
    }

    @Override
    public void notifyUpdate(Current current) {
        System.out.println("Observer received update!");

        // Now call the server to get the updated graph
        String graph = serverConnection.getUpdateGraph();
        System.out.println(graph);
    }
}
