package examples.senderreceiver;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import com.perapoch.concurrency.core.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenderActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderActor.class);
    private static final int MAGIC_NUMBER = 43;

    private final ActorRef consumer;

    public SenderActor(ActorRef consumer) {
        this.consumer = consumer;
    }

    protected void onReceive(String msg, ActorRef sender) {
        LOGGER.info("SenderActor has received a {} message!", msg);
        if ("start".equals(msg)) {

            consumer.tell("hello from Sender", self());

        } else if ("hello from Receiver".equals(msg)) {

            consumer.tell(MAGIC_NUMBER, self());
        }
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onReceive)
                .build();
    }
}
