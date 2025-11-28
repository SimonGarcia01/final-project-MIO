module Demo {

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

    //Server Connection Interface
    interface Connection {
        // The client requests an updated graph
        string getUpdatedGraph();

        //To receive the datagrams
        void sendDatagrams(DatagramSeq datagrams);

        //Send real time datagrams
        void receiveDatagram(Datagram datagram);
    }

};