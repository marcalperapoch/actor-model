package examples.mapreduce.model;

import java.util.List;

public class EventsPerId {

    private final int id;
    private final List<int[]> events;

    public EventsPerId(int id, List<int[]> events) {
        this.id = id;
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public List<int[]> getEvents() {
        return events;
    }
}
