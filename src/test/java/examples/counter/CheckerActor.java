package examples.counter;

import com.perapoch.concurrency.ActorAddress;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckerActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerActor.class);

    private final ActorAddress counter;

    public CheckerActor(ActorAddress counter) {
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
