import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import Demo.ConnectionPrx;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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

            // Give UI time to generate graph (optional)
            Thread.sleep(5_000);

            // Start input thread (non-blocking) - it'll read line IDs
            Thread inputThread = new Thread(new LineInspector(ui.getGraph()));
            inputThread.setDaemon(true);
            inputThread.start();

            // Use a scheduled executor to poll every 30 seconds
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "graph-poller");
                t.setDaemon(true);
                return t;
            });

            final AtomicBoolean inFlight = new AtomicBoolean(false);
            final long initialDelay = 0L; // start immediately after scheduling
            final long period = 30L; // seconds

            Runnable pollTask = () -> {
                // Ensure only one call in flight at a time
                if (!inFlight.compareAndSet(false, true)) {
                    // previous call still running -> skip this round
                    System.out.println("[CLIENT] Previous update still running; skipping this poll.");
                    return;
                }

                try {
                    double[][] graphMatrix = serverConnection.getUpdatedGraph();
                    ui.updateMap(graphMatrix);
                } catch (Exception ex) {
                    // Print once per error but do not spam
                    System.out.println("[CLIENT] Error calling server: " + ex.getMessage());
                } finally {
                    inFlight.set(false);
                }
            };

            // scheduleAtFixedRate will attempt to run every 'period', but our inFlight prevents overlap
            scheduler.scheduleAtFixedRate(pollTask, initialDelay, period, TimeUnit.SECONDS);

            // Keep main alive while the communicator runs. Wait for shutdown (same behavior as before)
            communicator.waitForShutdown();

            // shutdown scheduler if communicator finishes
            scheduler.shutdownNow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}