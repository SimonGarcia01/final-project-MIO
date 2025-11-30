import Demo.ArcUpdate;
import Demo.Data;
import Demo.Datagram;
import utils.BusIdDate;

public class CenterController {

    private final QueueManager queueManager;
    private final Database database;
    private ConnectionImpl connection;

    public CenterController(QueueManager queue, Database database) {
        this.queueManager = queue;
        this.database = database;
    }

    public void produceData(Datagram datagram) {
        queueManager.enqueueData(transformDatagram(datagram));
    }

    private Data transformDatagram(Datagram datagram) {
        //Transform Datagram
        Data data = new Data();
        data.orientation = datagram.orientation;
        data.lineId = datagram.lineId;
        data.busId = datagram.busId;
        data.latitude = datagram.latitude;
        data.longitude = datagram.longitude;
        data.date = datagram.registerDate;

        //Get the info of the stop before
        BusIdDate busIdDate = database.getLastStop(datagram.busId);
        data.prevStopId = busIdDate.budId;
        data.prevStopTime = busIdDate.date;

        return data;
    }

//    public void consumeData() {
//        Data data = queueManager.dequeueData();
//        if (data != null) {
//            database.addStop(data.busId, data.prevStopId);
//        }
//    }

    public void setConnection(ConnectionImpl connection) {
        this.connection = connection;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public ConnectionImpl getConnection() {
        return connection;
    }

    public void handleArcUpdate() {
        ArcUpdate arcUpdate = queueManager.dequeueArcUpdate();
        if(arcUpdate.averageSpeed != -1){
            database.updateArc(arcUpdate.stopMatrixId1, arcUpdate.stopMatrixId1, arcUpdate.averageSpeed, arcUpdate.bus);
        }
    }
}
