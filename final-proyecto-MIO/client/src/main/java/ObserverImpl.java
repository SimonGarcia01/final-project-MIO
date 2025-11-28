import Demo.Observer;
import com.zeroc.Ice.Current;

public class ObserverImpl implements Observer {

    @Override
    public void notifyUpdate(Current current) {
        System.out.println("Observer received update!");
    }

}
