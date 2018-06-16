package examples.mapreduce;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.MessageHandler;
import examples.mapreduce.model.EventsPerId;
import examples.mapreduce.model.Kpis;
import examples.mapreduce.model.KpisPerId;

public class ReducerWorkerActor extends Actor {
    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(EventsPerId.class, this::onEventsPerIdReceived)
                .build();
    }

    private void onEventsPerIdReceived(EventsPerId eventsPerId, ActorRef sender) {
        final int[] totals = eventsPerId.getEvents().stream()
                .reduce(new int[]{0,0,0}, (values, nextValues) -> {
                    for (int i = 0; i < values.length; ++i) {
                        values[i] += nextValues[i];
                    }
                    return values;
                });

        sender.tell(new KpisPerId(eventsPerId.getId(), new Kpis(totals)));
    }
}
