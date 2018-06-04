package examples.failure;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
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

    @Override
    protected void onReceive(Message msg) {
        if ("start".equals(msg.getValue())) {
            LOGGER.info("Got {} message. Starting to play...", msg.getValue());
        } else {
            LOGGER.info("Got {} from ponger", msg.getValue());
        }

        sleep(ThreadLocalRandom.current().nextInt(500, 2000), TimeUnit.MILLISECONDS);

        ponger.tell(new Message("ping"), self());
    }
}
