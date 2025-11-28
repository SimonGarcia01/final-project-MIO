import Demo.ConnectionPrx;
import Demo.Observer;
import com.zeroc.Ice.Current;

public class ObserverImpl implements Observer {

    private final ConnectionPrx serverConnection;
    private final UI ui;

    public ObserverImpl(ConnectionPrx serverConnection, UI ui) {
        this.serverConnection = serverConnection;
        this.ui = ui;
    }

    @Override
    public void notifyUpdate(Current current) {
        System.out.println("Observer received update!");

        // Now call the server to get the updated graph
        String graph = serverConnection.getUpdateGraph();

        //DOESN'T REACH HERE
        System.out.println("Graph arrived");

        //Now call the UI to print the graph
        ui.updateMapNotif(graph);
    }
}
