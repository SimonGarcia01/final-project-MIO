import Demo.ArcUpdate;
import Demo.ConnectionPrx;
import Demo.Data;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import utils.GraphCreation;
import utils.GraphImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.atomic.AtomicLong; //DEBUGIN IMPORT

public class Estimator {

    private final ConnectionPrx serverConnection;
    private final ExecutorService workerPool;
    private final GraphImpl graph;
    private final ThreadLocal<EstimatorConsumer> consumer;

    //--DEBUGIN VARIABLES--
    private final AtomicLong totalProcessingTimeNs = new AtomicLong(0);
    private final AtomicLong processedCount = new AtomicLong(0);
    private boolean reportPrinted = false;
    private int emptyCounter = 0;
    private final int emptyThreshold = 50;

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
        System.out.println("[Estiamtor] Estimator started. Waiting for Data...");

        while (true) {
            try {
                //Getting data from server
                Data data = serverConnection.getDequeueData();

                //If there is no data, retry
                if (data == null) {
                    emptyCounter++; //DEBUGIN
                    if (emptyCounter >= emptyThreshold && !reportPrinted) { //DEBUGIN
                        printTemporaryReport(); //DEBUGIN
                        reportPrinted = true; //DEBUGIN
                    }
                    Thread.sleep(20);
                    System.out.println("[Estimator] No data received. Retrying...");
                    continue;
                }
                emptyCounter = 0;
                reportPrinted = false;

                //Sending data to thread pool
                workerPool.submit(() -> processData(data));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processData(Data data) {
        long start = System.nanoTime(); //DEBUGIN

        try {
            //Creating consumers and start estimation
            EstimatorConsumer c = consumer.get();
            ArcUpdate update = c.estimateArcUpdate(data);

            //Sending results to server
            serverConnection.receiveArcUpdate(update);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //DEBUGIN
    private void printTemporaryReport() {
        long count = processedCount.get();
        long totalMs = totalProcessingTimeNs.get() / 1_000_000;

        System.out.println("\n===== TEMPORARY REPORT =====");
        System.out.println("Processed Data so far: " + count);
        System.out.println("Accumulated Processing Time: " + totalMs + " ms");
        if (count > 0) {
            System.out.println("Average Time per Data: " + (totalMs / (double) count) + " ms");
        }
        System.out.println("============================\n");
    }
}
