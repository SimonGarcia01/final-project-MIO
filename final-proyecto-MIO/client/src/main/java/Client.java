import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import Demo.ConnectionPrx;

public class Client {
    public static void main(String[] args) {
        try(Communicator communicator = Util.initialize(args, "client.config")){
            //Connecting to the server
            ConnectionPrx serverConnection = ConnectionPrx.checkedCast(
                    communicator.propertyToProxy("serverconnection.Proxy")
            );

            if(serverConnection==null){
                throw new Error("No server connection!");
            }

            System.out.println("Connected to server!");

            //Make the UI that will print the graph
            System.out.println("Creating the Graph.");
            UI ui = new UI();

            //Give the UI 10 seconds to make its graph
            Thread.sleep(10_000);

            //Now just get an update every 30 seconds
            while (true) {
                try {
                    // Ask the server for the graph
                    double[][] graph = serverConnection.getUpdatedGraph();

                    // Send to UI
                    ui.updateMap(graph);

                    // Wait 30 seconds
                    Thread.sleep(30_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception ex) {
                    System.out.println("Error calling server: " + ex.getMessage());
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}