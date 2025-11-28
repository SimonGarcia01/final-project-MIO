import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "server.config")){
            ObjectAdapter adapter = communicator.createObjectAdapter("ConnectionAdapter");
            //This was the original without config
//            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(
//                    "ConnectionAdapter",
//                    "default -h localhost -p 1090"
//            );

            ConnectionImpl serverConnection = new ConnectionImpl();

            adapter.add(serverConnection, Util.stringToIdentity("serverconnection"));

            System.out.println("serverconnection - port 1090");

            adapter.activate();

            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Press ENTER to notify observers...");

                while (true) {
                    scanner.nextLine();
                    System.out.println("Notifying observers...");
                    serverConnection.notifyObservers();
                }

            }).start();

            communicator.waitForShutdown();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}