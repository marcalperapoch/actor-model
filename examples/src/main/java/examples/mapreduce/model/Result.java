package examples.mapreduce.model;

import java.util.Collections;
import java.util.Map;

public class Result {

    private final Map<Integer, Kpis> kpisPerId;

    public Result(Map<Integer, Kpis> kpisPerId) {
        this.kpisPerId = Collections.unmodifiableMap(kpisPerId);
    }

    @Override
    public String toString() {
        return "Result{\n" +
                kpisPerId +
                "\n}";
    }
}
