package examples.mapreduce.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EventsPerIdMap {

    private final Map<Integer, List<int[]>> eventsPerId;

    public EventsPerIdMap(Map<Integer, List<int[]>> eventsPerId) {
        this.eventsPerId = Collections.unmodifiableMap(eventsPerId);
    }

    public Stream<EventsPerId> stream() {
        return eventsPerId.entrySet().stream()
                .map(entry -> new EventsPerId(entry.getKey(), entry.getValue()));
    }
}
