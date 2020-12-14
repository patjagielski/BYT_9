import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;


public class ServerTest {

    List<Observer> tempList;
    String URL;
    Subject subject;
    ConcreteObserver newObserver;
    FileReader reader;
    Momento savedState;

    @Before
    public void init() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        tempList = new ArrayList<>();
        URL = "http://www.pja.edu.pl/";
        subject = new Subject(tempList, URL);
        newObserver = new ConcreteObserver(subject);
        reader = new FileReader("src/WebpageState/newStates");

    }
    @Test
    public void checkObservers(){
        subject.addObserver(newObserver);

        Subject mock = Mockito.mock(Subject.class);
        when(mock.getObservers()).thenReturn(tempList);

        assertEquals(mock.getObservers(), subject.getObservers());
    }
    @Test
    public void checkSetState(){
        Subject mock = Mockito.mock(Subject.class);
        long temp = 100000;
        mock.setState(temp);
        when(mock.getState()).thenReturn(temp);
        assertEquals(mock.getState(), temp);
    }
    @Test
    public void checkSavedState(){
        try {
            Subject mock = Mockito.mock(Subject.class);
            mock.setSavedState();
            String result = "New change at: " + subject.getURL() + " with new state of " + mock.getState()  + " being watched by: ";
            when(mock.getSavedState()).thenReturn(result);
            assertEquals(mock.getSavedState(), "New change at: " + subject.getURL() + " with new state of " + mock.getState()  + " being watched by: " );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
