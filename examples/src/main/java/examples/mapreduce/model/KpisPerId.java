package examples.mapreduce.model;

public class KpisPerId {

    final int id;
    final Kpis kpis;

    public KpisPerId(int id, Kpis kpis) {
        this.id = id;
        this.kpis = kpis;
    }

    public int getId() {
        return id;
    }

    public Kpis getKpis() {
        return kpis;
    }

    @Override
    public String toString() {
        return "KpisPerId{" +
                "id=" + id +
                ", kpis=" + kpis +
                '}';
    }
}
