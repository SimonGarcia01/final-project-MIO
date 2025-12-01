import Demo.ArcUpdate;
import Demo.Data;
import Demo.Datagram;
import utils.StopIdDate;

public class CenterController extends Thread {

    private final QueueManager queueManager;
    private final Database database;
    private ConnectionImpl connection;
    private volatile boolean running = true;

    public CenterController(QueueManager queue, Database database) {
        this.queueManager = queue;
        this.database = database;
    }

    @Override
    public void run() {
        System.out.println("[CenterController] Thread iniciado.");

        while (running) {
            try {
                // Consumir ArcUpdates
                ArcUpdate arcUpdate = queueManager.dequeueArcUpdate();
                if (arcUpdate != null) {
                    handleArcUpdate(arcUpdate);
                }

                //Evita usar 100% CPU
                Thread.sleep(10);

            } catch (InterruptedException e) {
                System.out.println("[CenterController] Interrumpido.");
                running = false;
            }
        }

        System.out.println("[CenterController] Thread finalizado.");
    }

    //Detener hilo (no se usa por ahora)
    public void stopController() {
        running = false;
        this.interrupt();
    }

    public void produceData(Datagram datagram) {
        long start = System.nanoTime();
        //System.out.println("[CenterController] -->" + " - " + datagram.registerDate + " - "+datagram.datagramDate);
        if(!(datagram.lineId == -1 || datagram.lineId == 999)) {

            StopIdDate busIdDate  = database.getLastStop(datagram.busId);

            if(busIdDate == null) {
                Data data = new Data(
                        datagram.orientation,
                        datagram.lineId,
                        datagram.busId,
                        datagram.latitude,
                        datagram.longitude,
                        datagram.datagramDate,
                        -1,
                        ""
                );
                queueManager.enqueueData(data);
            }
            else if (busIdDate.lineId != datagram.lineId) {
                database.restartLocations(datagram.busId);

                Data data = new Data(
                        datagram.orientation,
                        datagram.lineId,
                        datagram.busId,
                        datagram.latitude,
                        datagram.longitude,
                        datagram.datagramDate,
                        -1,
                        ""
                );
                queueManager.enqueueData(data);
            }
            else {
                queueManager.enqueueData(transformDatagram(datagram));
            }
        }

        long end = System.nanoTime();
        //System.out.println("[CenterController.produceData] Processing Time: " + (end - start) / 1000000.0 + "ms");
    }

    private Data transformDatagram(Datagram datagram) {
        //Transform Datagram
        Data data = new Data();
        data.orientation = datagram.orientation;
        data.lineId = datagram.lineId;
        data.busId = datagram.busId;
        data.latitude = datagram.latitude;
        data.longitude = datagram.longitude;
        data.date = datagram.datagramDate;

        //Get the info of the stop before
        StopIdDate stopIdDate = database.getLastStop(datagram.busId);
        if(stopIdDate != null) {
            data.prevStopId = stopIdDate.stopId;
            data.prevStopTime = stopIdDate.date;
        }

        return data;
    }


    //Consume Arc
    private void handleArcUpdate(ArcUpdate arcUpdate) {
        long start = System.nanoTime();
        if (arcUpdate.averageSpeed != -1) {
            //System.out.println("[CenterController.handleArcUpdate] Processing ARC UPDATE");
            database.updateArc(arcUpdate.stopMatrixId1, arcUpdate.stopMatrixId2, arcUpdate.averageSpeed, arcUpdate.bus);
        }

        else {
            System.out.println("[CenterController.handleArcUpdate] Skip.");
        }
        long end = System.nanoTime();
        //System.out.println("[CenterController.handleArcUpdate] Processing Time: " + (end - start) / 1000000.0 + "ms");
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
