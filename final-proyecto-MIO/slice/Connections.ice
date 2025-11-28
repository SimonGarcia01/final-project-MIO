module Demo {

    //client side subscriber
    interface Observer {
        //Inform observer to get updated graph
        void notifyUpdate();
    }

    //server side publisher
    interface Connection {
        // Register a client observer
        void subscribe(Observer* observer);

        // The client requests an updated graph
        string getUpdateGraph();

    }

};