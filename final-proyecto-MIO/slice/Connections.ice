module Demo {

    //client side subscriber
    interface Observer {
        //Inform observer to get updated graph
        void notifyUpdate();
    }

    //Representation of a datagram
    struct Datagram {
        int eventType;
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

    //List of datagrams
    sequence<Datagram> DatagramSeq;

    //server side publisher
    interface Connection {
        // Register a client observer
        void subscribe(Observer* observer);

        // The client requests an updated graph
        string getUpdateGraph();

        //To receive the datagrams
        void sendDatagrams(DatagramSeq datagrams);

        //Send real time datagrams
        void receiveDatagram(Datagram datagram);
    }

};