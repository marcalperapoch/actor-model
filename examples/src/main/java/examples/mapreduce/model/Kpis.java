package examples.mapreduce.model;

public class Kpis {

    final int[] kpis;

    public Kpis(int[] kpis) {
        this.kpis = kpis;
    }

    private int getTotalImpressions() {
        return kpis[EventType.IMPRESSION.getPosition()];
    }

    private int getTotalClicks() {
        return kpis[EventType.CLICK.getPosition()];
    }

    private int getTotalBuys() {
        return kpis[EventType.BUY.getPosition()];
    }

    public Kpis sum(Kpis other) {
        int[] values = new int[kpis.length];
        for (int i = 0; i < this.kpis.length; ++i) {
            values[i] += kpis[i] + other.kpis[i];
        }
        return new Kpis(values);
    }

    @Override
    public String toString() {
        return "{" +
                "I=" + getTotalImpressions() +
                ", C=" + getTotalClicks() +
                ", B=" + getTotalBuys() +
                '}';
    }
}
