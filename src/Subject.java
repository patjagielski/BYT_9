import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Subject implements Runnable {
    private List<Observer> observers = new ArrayList<Observer>();
    private long state;
    private String URL;
    private Momento savedState;

    public Subject(List<Observer> observers, String URL){
        this.observers = observers;
        this.URL =  URL;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public void addObserver(Observer observer){
        this.observers.add(observer);
    }

    public void notifyObservers(){
        for(Observer observer : this.observers){
            observer.update();
        }
    }
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Momento save(){
        return new Momento(getState());
    }
    public void rollbackState(Momento prevState){
        this.state = prevState.getState();
    }

    public void setSavedState() throws IOException {
        savedState = this.save();
        FileWriter file = new FileWriter("src/WebpageState/newStates");
        PrintWriter writer = new PrintWriter(file);
        Date date = Calendar.getInstance().getTime();
        writer.println("New change at: " + getURL() + " with new state of " + savedState.getState() + " at " + date.toString() );
        writer.close();
    }
    public void returnToPrevState(){
        this.rollbackState(this.savedState);
    }

    @Override
    public void run() {
        System.out.println("thread is running");
        while (true) {
            //Connect to URL
            try {
                URL address = new URL(getURL());
                //Watch for changes in URL
                URLConnection connection = address.openConnection();
                long temp = connection.getLastModified();
                Date date = Calendar.getInstance().getTime();
                //check if change is different
                if(getState() != temp){
                    System.out.println(getURL() + " last change was at " + date);
                    //notify about such change
                    setState(temp);
                    notifyObservers();
                    setSavedState();
                }else System.out.println("no new changes at " + getURL() + " yet");

                Thread.sleep(6000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


}
