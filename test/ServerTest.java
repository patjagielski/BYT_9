import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class ServerTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void simulateWebpage(){
        List<Observer> tempList = new ArrayList<>();
        String URL = "http://www.pja.edu.pl/";
        Subject subject = new Subject(tempList, URL);
        ConcreteObserver newObserver = new ConcreteObserver(subject);
        subject.addObserver(newObserver);

        Subject mock = Mockito.mock(Subject.class);
        when(mock.getObservers()).thenReturn(tempList);

        assertEquals(mock.getObservers(), subject.getObservers());

    }
}
