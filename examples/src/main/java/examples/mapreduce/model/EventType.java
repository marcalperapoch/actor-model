package examples.mapreduce.model;

public enum  EventType {

    IMPRESSION(0),
    CLICK(1),
    BUY(2);

    int position;

    EventType(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
