package examples.mapreduce.model;

import java.util.HashMap;
import java.util.Map;

public class Transformer {

    private static final Map<EventType, int[]> EVENT_TYPE_MAP = new HashMap<>();

    static {
        int totalEventTypes = EventType.values().length;
        for (EventType eventType : EventType.values()) {
            int[] values = new int[totalEventTypes];
            values[eventType.getPosition()] = 1;
            EVENT_TYPE_MAP.put(eventType, values);
        }
    }

    public static int[] toEventTypeArray(EventType eventType) {
        return EVENT_TYPE_MAP.get(eventType);
    }
}
