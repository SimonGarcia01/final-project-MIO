import Demo.ArcUpdate;
import Demo.ConnectionPrx;
import Demo.Data;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import utils.GraphCreation;
import utils.GraphImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import java.util.concurrent.atomic.LongAdder; //DEBUGIN IMPORT

public class Estimator {

    private final ConnectionPrx serverConnection;
    private final ExecutorService workerPool;
    private final GraphImpl graph;
    private final ThreadLocal<EstimatorConsumer> consumer;
    private long lastDataTime = System.currentTimeMillis();

    //--DEBUGIN VARIABLES--
    /*
    private final LongAdder totalProcessingTimeNs = new LongAdder();
    private final LongAdder processedCount = new LongAdder();
    private volatile long lastPrint = System.currentTimeMillis();
    */

    //Defines an estimato with a worker/consumer pool
    public Estimator(ConnectionPrx serverConnection, int threads) {
        this.serverConnection = serverConnection;
        this.workerPool = Executors.newFixedThreadPool(threads);
        this.graph = GraphCreation.getGraph();
        this.consumer = ThreadLocal.withInitial(() -> new EstimatorConsumer(graph));
        System.out.println("[Estimator] The graph was created successfully.");
    }

    //Main method with server connection and estimator creation
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "estimator.config")) {

            ConnectionPrx serverConnection = ConnectionPrx.checkedCast(
                    communicator.propertyToProxy("serverconnection.Proxy")
            );

            if (serverConnection == null) {
                throw new Error("No server connection!");
            }

            System.out.println("[Estimator] Connected to server!");

            //Quantity of threads based on available processors.
            int threads = Runtime.getRuntime().availableProcessors();

            System.out.println("[Estimator] Creating distance graphs.");
            //Creating estimator and starting estimation process
            Estimator estimator = new Estimator(serverConnection, threads);
            estimator.start();
        }
    }

    public void start() {
        System.out.println("[Estimator] Estimator started. Waiting for Data...");

        while (true) {
            try {
                //Getting data from server
                Data data = serverConnection.getDequeueData();


                if (data != null) {
                    lastDataTime = System.currentTimeMillis();
                    workerPool.submit(() -> processData(data));
                }

                //If there is no data, retry
                long now = System.currentTimeMillis();
                if (now - lastDataTime >= 10_000) { // 10s sin datos
                    System.out.println("[Estimator] No new data received for 10 seconds. Retrying");
                    lastDataTime = now; // reset para la próxima pausa
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processData(Data data) {
        //long start = System.nanoTime(); //DEBUGIN

        try {
            //Creating consumers and start estimation
            EstimatorConsumer c = consumer.get();
            ArcUpdate update = c.estimateArcUpdate(data);

            //Sending results to server
            serverConnection.receiveArcUpdate(update);

            //DEBUGIN
            /*
            long end = System.nanoTime();
            long duration = end - start;
            //System.out.println("[Estimator] processed by thread " + Thread.currentThread().getName());
            //System.out.println("[Estimator] processing time: " + (end - start) / 1000000.0 + "ms");
            totalProcessingTimeNs.add(duration);
            processedCount.increment();
            long now = System.currentTimeMillis();
            if (now - lastPrint >= 30_000) {
                long totalMs = totalProcessingTimeNs.sum() / 1_000_000;
                long count = processedCount.sum();
                System.out.println("[Estimator] ----- REPORT (30s) -----");
                System.out.println("[Estimator] Processed Data: " + count);
                System.out.println("[Estimator] Accumulated Time: " + totalMs + " ms");
                System.out.println("[Estimator]-------------------------");
                lastPrint = now;
            }
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
