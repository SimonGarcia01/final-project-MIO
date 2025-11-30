import Demo.ArcUpdate;
import Demo.ConnectionPrx;
import Demo.Data;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import utils.GraphCreation;
import utils.GraphImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Estimator {

    private final ConnectionPrx serverConnection;
    private final ExecutorService workerPool;
    private final GraphImpl graph;

    //Defines an estimato with a worker/consumer pool
    public Estimator(ConnectionPrx serverConnection, int threads) {
        this.serverConnection = serverConnection;
        this.workerPool = Executors.newFixedThreadPool(threads);
        this.graph = GraphCreation.getGraph();
        System.out.println(graph.printMatrix());
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

            System.out.println("Connected to server!");

            //Quantity of threads based on available processors.
            int threads = Runtime.getRuntime().availableProcessors();

            System.out.println("Creating distance graphs.");
            //Creating estimator and starting estimation process
            Estimator estimator = new Estimator(serverConnection, threads);
            //estimator.start();
        }
    }

    public void start() {
        System.out.println("Estimator started. Waiting for Data...");

        while (true) {
            try {
                //Getting data from server
                Data data = serverConnection.getDequeueData();

                //If there is no data, retry
                if (data == null) {
                    System.out.println("No data received. Retrying...");
                    continue;
                }

                //Sending data to thread pool
                workerPool.submit(() -> processData(data));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processData(Data data) {
        try {
            //Creating consumers and start estimation
            EstimatorConsumer consumer = new EstimatorConsumer();
            ArcUpdate update = consumer.estimateArcUpdate(data);

            //Sending results to server
            serverConnection.receiveArcUpdate(update);

            System.out.println("ArcUpdate processed by thread " +
                    Thread.currentThread().getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GraphImpl getGraph() {
        return graph;
    }
}
