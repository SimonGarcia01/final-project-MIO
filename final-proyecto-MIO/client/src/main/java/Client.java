import Demo.ObserverPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import Demo.ConnectionPrx;

public class Client {
    public static void main(String[] args) {
        try(Communicator communicator = Util.initialize(args)){
            //First I'll connect to the server connection
            ConnectionPrx serverConnection = ConnectionPrx.checkedCast(
                    communicator.stringToProxy("serverconnection:default -h localhost -p 1090")
            );

            if(serverConnection==null){
                throw new Error("No server connection!");
            }

            System.out.println("Connected to server!");

            //Now I'll make the adapter for the notification
            //This adapter doesn't need the endpoint because of the server connection
            //Was already established
            //Left the string empty so no additional config is needed
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(
                    "ObserverAdapter",
                    "default -h localhost -p 1091"
            );

            //Make the observer
            ObserverImpl observer = new ObserverImpl();
            //Make the proxy that can be called after
            ObserverPrx observerPrx = ObserverPrx.uncheckedCast(
                    adapter.addWithUUID(observer)
            );

            adapter.activate();

            System.out.println("Observer activated!");

            //Now subscribe the client
            serverConnection.subscribe(observerPrx);
            System.out.println("Subscribed to the server!");

            System.out.println("Asking for updated graph:");
            String graph = serverConnection.getUpdateGraph();
            System.out.println(graph);

            communicator.waitForShutdown();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}