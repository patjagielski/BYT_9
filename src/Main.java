import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Observer> tempList = new ArrayList<>();
        String URL = "http://www.pja.edu.pl/";
        Subject subject = new Subject(tempList, URL);
        ConcreteObserver newObserver = new ConcreteObserver(subject);
        subject.addObserver(newObserver);

        Thread t1 = new Thread(subject);
        t1.start();

    }
}
