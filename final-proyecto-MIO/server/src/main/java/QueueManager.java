import Demo.ArcUpdate;
import Demo.Data;

import java.util.concurrent.BlockingQueue; //Using BlockingQueue because is better for product-consumer
import java.util.concurrent.LinkedBlockingQueue; //It's used too because it doesn't have a set size, like LinkedList

public class QueueManager {
    private final BlockingQueue<Data> dataQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ArcUpdate> arcQueue = new LinkedBlockingQueue<>();

    //Data
    public void enqueueData(Data data) {
        System.out.println("[QueueManager] Enqueue DATA bus=" + data.busId); //Debug Line
        dataQueue.offer(data);
    }

    public Data dequeueData() {
        return dataQueue.poll();
    }

    //ArcUpdate
    public void enqueueArcUpdate(ArcUpdate arc) {
        arcQueue.offer(arc);
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
