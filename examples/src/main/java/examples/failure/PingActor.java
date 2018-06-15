package examples.failure;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import com.perapoch.concurrency.core.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static examples.TestUtils.sleep;

public class PingActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingActor.class);
    private final ActorRef ponger;

    public PingActor(ActorRef ponger) {
        this.ponger = ponger;
    }

    private void onReceive(String msg, ActorRef sender) {
        if ("start".equals(msg)) {
            LOGGER.info("Got {} message. Starting to play...", msg);
        } else {
            LOGGER.info("Got {} from ponger", msg);
        }

        sleep(ThreadLocalRandom.current().nextInt(500, 2000), TimeUnit.MILLISECONDS);

        ponger.tell("ping", self());
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onReceive)
                .build();
    }
}
