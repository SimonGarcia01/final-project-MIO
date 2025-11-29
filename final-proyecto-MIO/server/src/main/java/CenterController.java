import Demo.ArcUpdate;
import Demo.Data;

public class CenterController {

    private final QueueManager queueManager;
    private final Database database;
    private ConnectionImpl connection;

    public CenterController(QueueManager queue, Database database) {
        this.queueManager = queue;
        this.database = database;
    }

    //TODO: Codigo y mensaje añadido por ChatGPT
    // El controller podría tener un loop de consumo:
    public void processArcUpdates() {
        ArcUpdate update = queueManager.dequeueArcUpdate();
        if (update != null) {
            // actualizar grafo aquí
            System.out.println("Processing update: " + update);
        }
    }

    public void produceData(Data data) {
        queueManager.enqueueData(data);
    }

    public void consumeData() {
        Data data = queueManager.dequeueData();
        if (data != null) {
            database.addStop(data.busId, data.prevStopId);
        }
    }

    public String getGraph() {
        return database.getGraph();
    }

    public void setGraph(String graph) {
        database.setGraph(graph);
    }

    public void setConnection(ConnectionImpl connection) {
        this.connection = connection;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public ConnectionImpl getConnection() {
        return connection;
    }
}
