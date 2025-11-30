import Demo.ArcUpdate;
import Demo.Data;

import java.util.concurrent.BlockingQueue; //Using BlockingQueue because is better for product-consumer
import java.util.concurrent.LinkedBlockingQueue; //It's used too because it doesn't have a set size, like LinkedList

public class QueueManager {
    private final BlockingQueue<Data> dataQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ArcUpdate> arcQueue = new LinkedBlockingQueue<>();
    private CenterController centerController;

    public void setCenterController(CenterController centerController) {
        this.centerController = centerController;
    }

    //Data
    public void enqueueData(Data data) {
        System.out.println("[QueueManager] Enqueue DATA bus=" + data.busId); //Debug Line
        dataQueue.add(data);
    }

    public Data dequeueData() {
        System.out.println("Que tiene esto: " + dataQueue.poll());
        return dataQueue.poll();
    }

    //ArcUpdate
    public void enqueueArcUpdate(ArcUpdate arc) {
        System.out.println("[QueueManager] Enqueue ARC UPDATE --> stopId1: " + arc.stopMatrixId1 + " stopId2: " + arc.stopMatrixId2);
        arcQueue.add(arc);
    }

    public ArcUpdate dequeueArcUpdate() {
        return arcQueue.poll();
    }

    public int size() {
        return dataQueue.size();
    }

    public boolean isEmpty() {
        return dataQueue.isEmpty();
    }
}
