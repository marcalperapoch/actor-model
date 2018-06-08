package examples.counter;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckerActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerActor.class);

    private final ActorRef counter;

    public CheckerActor(ActorRef counter) {
        this.counter = counter;
    }

    @Override
    protected void onReceive(Message msg) {
        LOGGER.info("CheckerActor has received a {} message!", msg.getValue());
        if ("ask".equals(msg.getValue())) {
            counter.tell(new Message("get"), self());
        }
    }

}
