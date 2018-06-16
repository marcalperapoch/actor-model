package examples.mapreduce.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventsPerIdMap {

    private final Map<Integer, List<int[]>> eventsPerId;
    private String stringRepresentation;

    public EventsPerIdMap(Map<Integer, List<int[]>> eventsPerId) {
        this.eventsPerId = Collections.unmodifiableMap(eventsPerId);
    }

    @Override
    public String toString() {
        if (stringRepresentation == null) {
            stringRepresentation = stringify(eventsPerId);
        }
        return "EventsPerIdMap{" +
                "eventsPerId=" + stringRepresentation +
                '}';
    }

    private String stringify(final Map<Integer, List<int[]>> eventsPerId) {
        return eventsPerId.entrySet().stream()
                .map(entry ->
                        entry.getKey() + "=["
                                + entry.getValue().stream().map(Arrays::toString).collect(Collectors.joining(", "))
                                + "]"
                ).collect(Collectors.joining(",", "{", "}"));
    }

    public Stream<EventsPerId> stream() {
        return eventsPerId.entrySet().stream()
                .map(entry -> new EventsPerId(entry.getKey(), entry.getValue()));
    }
}
