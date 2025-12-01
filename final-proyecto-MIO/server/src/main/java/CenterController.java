import Demo.ArcUpdate;
import Demo.Data;
import Demo.Datagram;
import utils.BusIdDate;

public class CenterController extends Thread {

    private final QueueManager queueManager;
    private final Database database;
    private ConnectionImpl connection;
    private volatile boolean running = true;

    //--VARIABLES PARA DEBUGIN--

    private long pdTotalTimeNs;
    private long pdLastPrint = System.currentTimeMillis();
    private long auTotalTimeNs;
    private long auLastPrint = System.currentTimeMillis();


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
        long start = System.nanoTime(); //DEBUGIN

        if(!(datagram.lineId == -1 || datagram.lineId == 999)) {

            BusIdDate busIdDate  = database.getLastStop(datagram.busId);

            if(busIdDate == null) {
                Data data = new Data(
                        datagram.orientation,
                        datagram.lineId,
                        datagram.busId,
                        datagram.latitude,
                        datagram.longitude,
                        datagram.registerDate,
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
                        datagram.registerDate,
                        -1,
                        ""
                );
                queueManager.enqueueData(data);
            }
            else {
                queueManager.enqueueData(transformDatagram(datagram));
            }
        }

        //--DEBUGIN PROCESSDATA--

        long end = System.nanoTime();
        pdTotalTimeNs += (end - start);
        long now = System.currentTimeMillis();
        if (now - pdLastPrint >= 30000) { // 30 segundos
            System.out.println("[CenterController.produceData] ------- REPORT (30s) -------");
            System.out.println("[CenterController.produceData] Acumulated(ms): " + (pdTotalTimeNs / 1_000_000));
            System.out.println("[CenterController.produceData] ----------------------------");
            pdLastPrint = now;
        }

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
        if(busIdDate != null) {
            data.prevStopId = busIdDate.busId;
            data.prevStopTime = busIdDate.date;
        }

        return data;
    }


    //Consume Arc
    private void handleArcUpdate(ArcUpdate arcUpdate) {
        long start = System.nanoTime(); //DEBUGIN

        if (arcUpdate.averageSpeed != -1) {
            //System.out.println("[CenterController.handleArcUpdate] Processing ARC UPDATE");
            database.updateArc(arcUpdate.stopMatrixId1, arcUpdate.stopMatrixId2, arcUpdate.averageSpeed, arcUpdate.bus);
        }
        else {
            System.out.println("[CenterController.handleArcUpdate] Skip.");
        }

        //--DEBUGING ARCUPDATE--

        long end = System.nanoTime();
        auTotalTimeNs += (end - start);
        long now = System.currentTimeMillis();
        if (now - auLastPrint >= 30000) { // 30 segundos
            System.out.println("[CenterController.handleArcUpdate] ------- REPORT (30s) -------");
            System.out.println("[CenterController.handleArcUpdate] Accumulated(ms): " + (auTotalTimeNs / 1_000_000));
            System.out.println("[CenterController.handleArcUpdate] ----------------------------");
            auLastPrint = now;
        }

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
