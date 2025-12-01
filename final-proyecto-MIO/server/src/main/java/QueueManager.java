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
        dataQueue.add(data);
    }

    public Data dequeueData() {
        try {
            return dataQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    //ArcUpdate
    public void enqueueArcUpdate(ArcUpdate arc) {
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
