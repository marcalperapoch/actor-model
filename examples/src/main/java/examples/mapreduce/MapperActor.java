package examples.mapreduce;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.MessageHandler;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapperActor extends Actor {

    private final int numMappers;

    private Queue<ActorRef> mappers;

    public MapperActor(int numMappers) {
        this.numMappers = numMappers;
    }

    @Override
    protected void onActorRegistered() {
        this.mappers = IntStream.range(0, numMappers)
                .mapToObj(i -> self().newActor(MapperWorkerActor.class, "MapperWorker-"+i))
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onNewSourceFile)
                .build();
    }

    private void onNewSourceFile(String filePath, ActorRef sender) {
        final ActorRef worker = mappers.poll();
        worker.tell(filePath, sender);
        mappers.offer(worker);
    }
}
