import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Server {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args)){
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(
                    "ConnectionAdapter",
                    "default -h localhost -p 1090"
            );

            ConnectionImpl serverConnection = new ConnectionImpl();

            adapter.add(serverConnection, Util.stringToIdentity("serverconnection"));

            System.out.println("serverconnection - port 1090");

            adapter.activate();
            communicator.waitForShutdown();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}