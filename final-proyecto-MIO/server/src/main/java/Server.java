import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Server {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "server.config")) {

            ObjectAdapter adapter = communicator.createObjectAdapter("ConnectionAdapter");

            System.out.println("Starting up the database (Creating graph).");
            Database database = new Database();
            Thread.sleep(1_000);

            QueueManager queueManager = new QueueManager();

            CenterController centerController = new CenterController(queueManager, database);
            centerController.start();

            queueManager.setCenterController(centerController);

            ConnectionImpl serverConnection = new ConnectionImpl(centerController, database, queueManager);

            centerController.setConnection(serverConnection);

            adapter.add(serverConnection, Util.stringToIdentity("serverconnection"));

            System.out.println("serverconnection - port 1090");

            adapter.activate();

            communicator.waitForShutdown();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}