package examples.counter;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import com.perapoch.concurrency.core.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckerActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerActor.class);

    private final ActorRef counter;

    public CheckerActor(ActorRef counter) {
        this.counter = counter;
    }

    private void onReceiveString(String msg, ActorRef sender) {
        LOGGER.info("CheckerActor has received a {} message!", msg);
        if ("ask".equals(msg)) {
            counter.tell("get", self());
        }
    }

    private void onReceiveInteger(Integer totalCount, ActorRef sender) {
        LOGGER.info("CheckerActor has received a {} message!", totalCount);
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onReceiveString)
                .withHandler(Integer.class, this::onReceiveInteger)
                .build();
    }
}
