package examples.senderreceiver;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import com.perapoch.concurrency.core.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverActor.class);

    protected void onStringReceived(String msg, ActorRef sender) {
        LOGGER.info("ReceiverActor has received a {} message!", msg);
        if ("hello from Sender".equals(msg)) {
            sender.tell("hello from Receiver");
        }
    }

    protected void onIntegerReceived(Integer magicNumber, ActorRef sender) {
        sender.tell(magicNumber + 14);
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onStringReceived)
                .withHandler(Integer.class, this::onIntegerReceived)
                .build();
    }
}
