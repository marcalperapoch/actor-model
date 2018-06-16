package examples.mapreduce.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class Result {

    private final Map<Integer, Kpis> kpisPerId;

    public Result(Map<Integer, Kpis> kpisPerId) {
        this.kpisPerId = Collections.unmodifiableMap(kpisPerId);
    }

    public String toCsv() {
        final StringBuilder sb = new StringBuilder("id;");

        final String headers = Arrays.stream(EventType.values())
                .map(eventType -> eventType.toString().toLowerCase())
                .collect(Collectors.joining(";"));

        sb.append(headers).append("\n");

        kpisPerId.entrySet().forEach(entry -> {
            sb.append(entry.getKey())
                    .append(";")
                    .append(entry.getValue().toCsv())
                    .append("\n");
        });

        return sb.toString();
    }
}
