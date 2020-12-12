public class ConcreteObserver extends Observer {
    private int id;
    public ConcreteObserver(Subject subject){
        this.subject = subject;
        this.subject.addObserver(this);
    }

    @Override
    public void update() {
        System.out.println(subject.getState());
    }
}
