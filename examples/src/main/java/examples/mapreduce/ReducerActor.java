package examples.mapreduce;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.MessageHandler;
import examples.mapreduce.model.EventsPerIdMap;
import examples.mapreduce.model.Kpis;
import examples.mapreduce.model.KpisPerId;
import examples.mapreduce.model.Result;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReducerActor extends Actor {

    private final int numReducers;

    private Queue<ActorRef> reducers;

    private Map<Integer, Kpis> kpisPerId;
    private int pendingReductions;
    private ActorRef orchestrator;

    public ReducerActor(int numReducers) {
        this.numReducers = numReducers;
        this.kpisPerId = new HashMap<>();
        this.pendingReductions = 0;
    }

    @Override
    protected void onActorRegistered() {
        this.reducers = IntStream.range(0, numReducers)
                .mapToObj(i -> self().newActor(ReducerWorkerActor.class, "ReducerWorker-"+i))
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(EventsPerIdMap.class, this::onEventsPerIdReceived)
                .withHandler(KpisPerId.class, this::onKpisPerIdReceived)
                .build();
    }

    private void onEventsPerIdReceived(EventsPerIdMap eventsPerIdMap, ActorRef sender) {
        this.orchestrator = sender;
        eventsPerIdMap.stream()
                .forEach(eventsPerId -> {
                    final ActorRef reducerWorker = reducers.poll();
                    ++pendingReductions;
                    reducerWorker.tell(eventsPerId, self());
                    reducers.offer(reducerWorker);
                });
    }

    private void onKpisPerIdReceived(KpisPerId newKpisPerId, ActorRef sender) {
        kpisPerId.compute(newKpisPerId.getId(), (id, oldKpis) -> {
            if (oldKpis == null) {
                return newKpisPerId.getKpis();
            } else {
                return oldKpis.sum(newKpisPerId.getKpis());
            }
        });

        --pendingReductions;
        if (pendingReductions == 0) {
            orchestrator.tell(new Result(kpisPerId));
        }
    }
}
