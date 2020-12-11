public class ConcreteObserver extends Observer {

    public ConcreteObserver(Subject subject){
        this.subject = subject;
        this.subject.addObserver(this);
    }

    @Override
    public void update() {
        System.out.println(subject.getState());
    }
}
