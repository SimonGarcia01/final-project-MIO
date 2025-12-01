import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import Demo.ConnectionPrx;

public class Client {

    public static void main(String[] args) {

        try (Communicator communicator = Util.initialize(args, "client.config")) {

            ConnectionPrx serverConnection = ConnectionPrx.checkedCast(
                    communicator.propertyToProxy("serverconnection.Proxy")
            );

            if (serverConnection == null) {
                throw new Error("No server connection!");
            }

            System.out.println("Connected to server!");

            // Create UI (initial graph)
            System.out.println("Creating the Graph.");
            UI ui = new UI();

            // Start input thread (non-blocking)
            Thread inputThread = new Thread(new LineInspector(ui.getGraph()));
            inputThread.setDaemon(true); // won't prevent program from closing
            inputThread.start();

            // Give UI time to generate graph
            Thread.sleep(5_000);

            // Update loop every 30 sec
            while (true) {
                try {
                    double[][] graphMatrix = serverConnection.getUpdatedGraph();

                    // Send new matrix to UI
                    ui.updateMap(graphMatrix);

                    Thread.sleep(30_000); // wait 30 seconds

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception ex) {
                    System.out.println("Error calling server: " + ex.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
