module Demo {

    //Representation of a datagram
    struct Datagram {
        int orientation;
        string registerDate;
        int stopId;
        int odometer;
        double latitude;
        double longitude;
        int taskId;
        int lineId;
        int tripId;
        long unknown1;
        string datagramDate;
        int busId;
    };

    struct Data {
        int orientation;
        int lineId;
        int busId;
        double latitude;
        double longitude;
        string date;
        int prevStopId;
        string prevStopTime;
    };

    struct BusUpdate {
        bool orientation;
        int lineId;
        int busId;
        string timestamp;
    };

    struct ArcUpdate {
        int stopMatrixId1;
        int stopMatrixId2;
        double averageSpeed;
        BusUpdate bus;
    };


    //List of datagrams
    sequence<Datagram> DatagramSeq;

    //Server Connection Interface
    interface Connection {
        // The client requests an updated graph
        string getUpdatedGraph();

        //To receive the datagrams
        void sendDatagrams(DatagramSeq datagrams);

        //Send real time datagrams
        void receiveDatagram(Datagram datagram);

        //For estimators to get data
        Data getDequeueData();

        //For estimators to send Arc updates
        void receiveArcUpdate(ArcUpdate arcUpdate);
    };
};