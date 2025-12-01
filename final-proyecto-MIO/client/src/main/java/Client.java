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

            // Give UI time to generate graph
            Thread.sleep(5_000);

            // Start input thread (non-blocking) - it'll read line IDs
            Thread inputThread = new Thread(new LineInspector(ui.getGraph()));
            inputThread.start();

            // Update loop every 30 seconds
            while (true) {
                try {
                    // call server (no pre-request logging)
                    double[][] graphMatrix = serverConnection.getUpdatedGraph();

                    // Send new matrix to UI (UI prints single "received" message)
                    ui.updateMap(graphMatrix);

                    // sleep exactly 30 seconds between requests
                    Thread.sleep(30_000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception ex) {
                    // print error but do not spam
                    System.out.println("Error calling server: " + ex.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}