package examples.mapreduce;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.MessageHandler;
import examples.mapreduce.model.EventType;
import examples.mapreduce.model.EventsPerIdMap;
import examples.mapreduce.model.Transformer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class MapperWorkerActor extends Actor {


    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onNewSourceFile)
                .build();
    }

    private void onNewSourceFile(String filePath, ActorRef sender) {

        final Path file = toResourcePath(filePath);

        try (Stream<String> lines = Files.lines(file)) {

            final Map<Integer, List<int[]>> eventsPerId = new HashMap<>();

            lines.map(this::toMapEntry).forEach(entry -> {
                eventsPerId.compute(entry.getKey(), (id, values) -> {
                    if (values == null) {
                        values = new ArrayList<>();
                    }
                    values.add(entry.getValue());
                    return values;
                });
            });

            sender.tell(new EventsPerIdMap(eventsPerId));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map.Entry<Integer, int[]> toMapEntry(String line) {
        final String[] fields = line.split(",");
        final EventType eventType = EventType.valueOf(fields[0].toUpperCase());
        final int[] values = Transformer.toEventTypeArray(eventType);
        final int id = Integer.parseInt(fields[1]);
        return new AbstractMap.SimpleEntry<>(id, values);
    }

    private Path toResourcePath(String fileName) {
        try {
            final URI fileUri = this.getClass().getClassLoader().getResource(fileName).toURI();
            return Paths.get(fileUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
