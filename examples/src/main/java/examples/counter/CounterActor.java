package examples.counter;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import com.perapoch.concurrency.core.MessageHandler;

public class CounterActor extends Actor {

    private int counter;

    public CounterActor() {
        this.counter = 0;
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(String.class, this::onReceive)
                .build();
    }

    private void onReceive(String msg, ActorRef sender) {
        if ("inc".equals(msg)) {
            ++counter;
        } else {
            sender.tell(counter);
        }
    }
}
