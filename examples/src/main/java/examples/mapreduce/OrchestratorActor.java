package examples.mapreduce;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.MessageHandler;
import examples.TestUtils;
import examples.mapreduce.model.EventsPerIdMap;
import examples.mapreduce.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;

public class OrchestratorActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrchestratorActor.class);

    private static final List<String> FILES = asList("events_1.csv", "events_2.csv", "events_3.csv");
    private static final String DESTINATION_FILE = "statistics.csv";

    private final ActorRef mapper;
    private final ActorRef reducer;

    public OrchestratorActor(ActorRef mapper, ActorRef reducer) {
        this.mapper = mapper;
        this.reducer = reducer;
    }


    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onNewOrderReceived)
                .withHandler(EventsPerIdMap.class, this::onEventsPerIdReceived)
                .withHandler(Result.class, this::onResultReceived)
                .build();
    }

    private void onNewOrderReceived(String order, ActorRef actorRef) {
        if ("start".equals(order)) {
            FILES.forEach(fileName -> mapper.tell(fileName, self()));
        }
    }

    private void onEventsPerIdReceived(EventsPerIdMap eventsPerIdMap, ActorRef sender) {
        reducer.tell(eventsPerIdMap, self());
    }

    private void onResultReceived(Result result, ActorRef sender) {
        LOGGER.info("Orchestrator has received {}", result);

        final Path destinationPath = Paths.get(DESTINATION_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(destinationPath)) {

            writer.write(result.toCsv());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
