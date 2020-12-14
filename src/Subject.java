import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Subject implements Runnable {
    private List<Observer> observers = new ArrayList<Observer>();
    private long state;
    private String URL;
    private Momento savedState;
    private Observer observer;
    private HashMap<Observer, String> observedWebpages;
    //Create dictionary of Observers and URL X
    //When assigning a Observer to URL first check if an Observer is already watching
    //  if so then use that Observer, else create a new Observer for that URL and add it to the dictionary
    //Write to file the URL and the Observer specific to that URL

    public Subject(List<Observer> observers, String URL){
        this.observers = observers;
        this.URL =  URL;
    }

    public HashMap<Observer, String> getObservedWebpages() {
        return observedWebpages;
    }

    public void setObservedWebpages(HashMap<Observer, String> observedWebpages) {
        this.observedWebpages = observedWebpages;
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

    public void addToDictionary(Observer observer, String url){
        this.observedWebpages.put(observer, url);
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
        writer.println("New change at: " + getURL() + " with new state of " + savedState.getState()  + " being watched by: ");
        writer.close();
    }
    public String getSavedState(){
        Path path = Paths.get("src/WebpageState/newStates");

        StringBuilder sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> sb.append(s).append("\n"));

        } catch (IOException ex) {
            // Handle exception
        }
        return sb.toString();
    }
    public void returnToPrevState(){
        this.rollbackState(this.savedState);
    }

    @Override
    public void run() {
        System.out.println("thread is running");
        while (true) {
            //check if this is new URL or old URL
            if(this.observedWebpages != null){
                if(this.observedWebpages.containsValue(getURL())) {
                    //if this URL does not exists then get that Observer instead of creating new one
                    addToDictionary(this.observer, this.URL);
                }
            }
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


    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }
}
